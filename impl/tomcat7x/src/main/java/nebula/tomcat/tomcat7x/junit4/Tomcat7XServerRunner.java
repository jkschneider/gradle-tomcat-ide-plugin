package nebula.tomcat.tomcat7x.junit4;

import nebula.tomcat.embedded.junit4.TomcatServerRunner;
import nebula.tomcat.tomcat7x.Tomcat7xServer;
import org.junit.runners.model.InitializationError;

public class Tomcat7XServerRunner extends TomcatServerRunner {
    public Tomcat7XServerRunner(Class<?> klass) throws InitializationError {
        super(klass, Tomcat7xServer.class);
    }
}
