package nebula.tomcat.tomcat8x;

import nebula.tomcat.embedded.TomcatLifecycleException;
import nebula.tomcat.embedded.TomcatServer;
import nebula.tomcat.embedded.TomcatStartInfo;
import nebula.tomcat.embedded.TomcatStartParameters;
import org.apache.catalina.*;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Catalina;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;

public class Tomcat8xServer implements TomcatServer {
    private ExtendedTomcat tomcat;

    @Override
    public void start(TomcatStartParameters startParameters) {
        try {
            tomcat = createEmbeddedServer(startParameters);

            StandardContext ctx = createContext(tomcat, startParameters);
            addClassesToContext(ctx, startParameters);

            CountDownLatch startupBarrier = new CountDownLatch(1);
            TomcatStartInfo tomcatStartInfo = new TomcatStartInfo(tomcat.getPort(), ctx.getPath());
            tomcat.getServer().addLifecycleListener(new AfterStartEventLifecycleListener(startupBarrier, startParameters.isBackground(), tomcatStartInfo));

            tomcat.start();
            startupBarrier.await();
        }
        catch(Throwable e) {
            throw new TomcatLifecycleException(e);
        }
    }

    private ExtendedTomcat createEmbeddedServer(TomcatStartParameters startParameters) throws IOException, SAXException {
        ExtendedTomcat tomcat = new ExtendedTomcat();
        tomcat.getServer().setParentClassLoader(Thread.currentThread().getContextClassLoader());

        File serverXml = startParameters.getServerXml();

        if(serverXml != null) {
            if(!serverXml.exists()) {
                throw new TomcatLifecycleException("Provided server descriptor does not exist: " + serverXml.getCanonicalPath());
            }

            Digester digester = new ExtendedCatalina().createStartDigester();
            digester.push(tomcat);
            digester.parse(serverXml);
        }
        else {
            tomcat.setPort(startParameters.getPort());
            tomcat.setBaseDir(new File(new File(System.getProperty("user.home")), "embeddedTomcat8x").getCanonicalPath());
        }

        return tomcat;
    }

    private StandardContext createContext(Tomcat tomcat, TomcatStartParameters startParameters) throws IOException, ServletException {
        return (StandardContext)tomcat.addWebapp(startParameters.getContextPath(), startParameters.getWebAppBaseDir().getCanonicalPath());
    }

    private void addClassesToContext(StandardContext context, TomcatStartParameters startParameters) {
        StandardRoot resources = new StandardRoot(context);

        try {
            resources.createWebResourceSet(WebResourceRoot.ResourceSetType.CLASSES_JAR, "/WEB-INF/classes",
                    startParameters.getClassesDir().toURI().toURL(), null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e); // unrecoverable
        }

        context.setResources(resources);
    }

    @Override
    public void stop() {
        if(tomcat != null) {
            try {
                tomcat.stop();
                tomcat.destroy();
            }
            catch(LifecycleException e) {
                throw new TomcatLifecycleException("Failed to stop Tomcat server", e);
            }
        }
    }

    public static class ExtendedTomcat extends Tomcat {
        /**
         * Required using Digester parsing server.xml in order to push object on to Tomcat model.
         *
         * @param server Server
         */
        public void setServer(Server server) {
            this.server = server;
        }

        public int getPort() {
            return port;
        }
    }

    private static class ExtendedCatalina extends Catalina {
        @Override
        public Digester createStartDigester() {
            return super.createStartDigester();
        }
    }

    private class AfterStartEventLifecycleListener implements LifecycleListener {
        private final CountDownLatch startupBarrier;
        private final boolean background;
        private final TomcatStartInfo tomcatStartInfo;

        public AfterStartEventLifecycleListener(CountDownLatch startupBarrier, boolean background, TomcatStartInfo tomcatStartInfo) {
            this.startupBarrier = startupBarrier;
            this.background = background;
            this.tomcatStartInfo = tomcatStartInfo;
        }

        @Override
        public void lifecycleEvent(LifecycleEvent event) {
            if(event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
                System.out.println(tomcatStartInfo.getStartMessage());

                if(background) {
                    startupBarrier.countDown();
                }
            }
        }
    }
}
