package nebula.tomcat.embedded.junit4;

import com.google.common.base.Preconditions;
import nebula.tomcat.embedded.CustomClassloaderTomatRunner;
import nebula.tomcat.embedded.FilteringClassloaderTomatRunner;
import nebula.tomcat.embedded.TomcatServer;
import nebula.tomcat.embedded.TomcatStartParameterSystemProperty;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TomcatServerRule implements TestRule {
    private final Class<? extends nebula.tomcat.embedded.TomcatServer> tomcatServerClass;
    private Integer httpPort = 8080;
    private File serverXml = null;

//    HTTP_PORT("tomcatHttpPort"),
//    CONTEXT_PATH("tomcatContextPath"),
//    CLASSES_DIR("tomcatClassesDir"),
//    WEB_APP_BASE_DIR("tomcatWebAppBaseDir"),
//    SERVER_XML("tomcatServerXml"),
//    BACKGROUND("tomcatBackground");

    public TomcatServerRule(Class<? extends nebula.tomcat.embedded.TomcatServer> tomcatServerClass) {
        this.tomcatServerClass = tomcatServerClass;
    }

    public static class Builder {
        private Class<? extends TomcatServer> tomcatServerClass;
        private int port = 8080;
        private File serverXml = null;

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withServerXml(File serverXml) {
            this.serverXml = serverXml;
            return this;
        }

        public Builder withServerXml(String serverXml) {
            withServerXml(new File(serverXml));
            return this;
        }

        public Builder withSystemPropertiesFile(File sysProps) {
            Preconditions.checkArgument(sysProps.exists(), sysProps.getAbsolutePath() + " does not exist");
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(sysProps));
                for (Map.Entry<Object, Object> prop : props.entrySet())
                    System.setProperty(prop.getKey().toString(), prop.getValue().toString());
            } catch (IOException e) {
                // do nothing
            }
            return this;
        }

        public Builder withSystemPropertiesFile(String sysProps) {
            withSystemPropertiesFile(new File(sysProps));
            return this;
        }

        public Builder withSystemProperties(String... sysProps) {
            Preconditions.checkArgument(sysProps.length % 2 == 0, "sysProps must be an array of key, value tuples");
            for(int i = 0; i < sysProps.length; i+=2)
                System.setProperty(sysProps[i], sysProps[i+1]);
        }

        public TomcatServerRule build() {
            Preconditions.checkState(tomcatServerClass != null,
                    "A tomcat server implementation class (Tomcat7xServer or Tomcat8xServer) must be specified");
            TomcatServerRule rule = new TomcatServerRule(tomcatServerClass);
            rule.httpPort = port;
            rule.serverXml = serverXml;
            return rule;
        }
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new TomcatStatement(base);
    }

    private class TomcatStatement extends Statement {
        private final Statement base;

        public TomcatStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            System.setProperty(TomcatStartParameterSystemProperty.BACKGROUND.getKey(), "true");
            System.setProperty(TomcatStartParameterSystemProperty.HTTP_PORT.getKey(), httpPort.toString());
            System.setProperty(TomcatStartParameterSystemProperty.CLASSES_DIR.getKey(), )

            CustomClassloaderTomatRunner tomcatRunner = new FilteringClassloaderTomatRunner();

            try {
                tomcatRunner.start(tomcatServerClass.getName());
                base.evaluate();
            }
            finally {
                System.clearProperty(TomcatStartParameterSystemProperty.BACKGROUND.getKey());
                tomcatRunner.stop();
            }
        }
    }
}
