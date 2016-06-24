package nebula.tomcat.tomcat8x.junit4;

import nebula.tomcat.embedded.junit4.TomcatServerRunner;
import nebula.tomcat.tomcat8x.Tomcat8xServer;
import org.junit.runners.model.InitializationError;

public class Tomcat8XServerRunner extends TomcatServerRunner {
    public Tomcat8XServerRunner(Class<?> klass) throws InitializationError {
        super(klass, Tomcat8xServer.class);
    }
}
