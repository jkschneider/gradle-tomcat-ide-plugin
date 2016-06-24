package nebula.tomcat.embedded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class FilteringClassloaderTomatRunner implements CustomClassloaderTomatRunner {
    public static final List<String> FILTERED_URLS_PATHS;
    private Object tomcatServer;

    static {
        FILTERED_URLS_PATHS = new ArrayList<String>();
        FILTERED_URLS_PATHS.add("servlet-api");
        FILTERED_URLS_PATHS.add("jetty-servlet");
    }

    @Override
    public void start(String tomcatServerClassName) {
        URLClassLoader servletApiExcludingClassLoader = createServletApiExcludingClassLoader();
        Thread.currentThread().setContextClassLoader(servletApiExcludingClassLoader);

        try {
            Object startParameters = createStartParameters(servletApiExcludingClassLoader);
            tomcatServer = createTomcatServer(servletApiExcludingClassLoader, tomcatServerClassName);
            startTomcatServer(servletApiExcludingClassLoader, tomcatServer, startParameters);
        }
        catch(Exception e) {
            throw new TomcatLifecycleException("Failed to start Tomcat server", e);
        }
    }

    @Override
    public void stop() {
        try {
            Method m = tomcatServer.getClass().getDeclaredMethod("stop", new Class[]{});
            m.invoke(tomcatServer);
        }
        catch(Exception e) {
            throw new TomcatLifecycleException("Failed to stop Tomcat server", e);
        }
    }

    private Object createStartParameters(URLClassLoader servletApiExcludingClassLoader) throws ClassNotFoundException,
                                         InstantiationException, IllegalAccessException, NoSuchMethodException,
                                         InvocationTargetException {
        Class tomcatStartParametersClass = servletApiExcludingClassLoader.loadClass("nebula.tomcat.embedded.TomcatStartParametersBuilder");
        Object tomcatStartParametersBuilder = tomcatStartParametersClass.newInstance();
        Method buildMethod = tomcatStartParametersClass.getDeclaredMethod("build", new Class[]{});
        return buildMethod.invoke(tomcatStartParametersBuilder);
    }

    private Object createTomcatServer(URLClassLoader servletApiExcludingClassLoader, String tomcatServerClassName)
                                        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class serverClass = servletApiExcludingClassLoader.loadClass(tomcatServerClassName);
        return serverClass.newInstance();
    }

    private void startTomcatServer(URLClassLoader servletApiExcludingClassLoader, Object tomcatServer,
                                   Object startParameters) throws ClassNotFoundException, InstantiationException,
                                   IllegalAccessException,  NoSuchMethodException, InvocationTargetException {
        Class[] param = new Class[1];
        param[0] = servletApiExcludingClassLoader.loadClass("nebula.tomcat.embedded.TomcatStartParameters");
        Method m = tomcatServer.getClass().getDeclaredMethod("start", param);
        m.invoke(tomcatServer, startParameters);
    }

    private URLClassLoader createServletApiExcludingClassLoader() {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();
        final List<URL> filteredURLs = new ArrayList<URL>();

        for(URL url : urls) {
            if(!matchesFilter(url.getPath())) {
                filteredURLs.add(url);
            }
        }

        return new URLClassLoader(filteredURLs.toArray(new URL[filteredURLs.size()]), sysClassLoader.getParent());
    }

    private boolean matchesFilter(String urlPath) {
        for(String filteredUrlsPath : FILTERED_URLS_PATHS) {
            if(urlPath.contains(filteredUrlsPath)) {
                return true;
            }
        }

        return false;
    }
}
