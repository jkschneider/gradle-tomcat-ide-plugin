package nebula.tomcat.tomcat7x;

import nebula.tomcat.embedded.TomcatStartParameterSystemProperty;

import java.lang.reflect.InvocationTargetException;

public class TomcatRunnerIntegrationTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        System.setProperty(TomcatStartParameterSystemProperty.WEB_APP_BASE_DIR.getKey(), "tomcat7x/src/test/resources/WebRoot");
        TomcatRunner.main(args);
    }
}
