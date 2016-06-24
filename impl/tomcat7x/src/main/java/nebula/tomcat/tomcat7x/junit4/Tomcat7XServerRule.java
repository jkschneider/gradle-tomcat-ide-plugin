package nebula.tomcat.tomcat7x.junit4;

import nebula.tomcat.embedded.junit4.TomcatServerRule;
import nebula.tomcat.tomcat7x.Tomcat7xServer;

public class Tomcat7XServerRule extends TomcatServerRule {
    public Tomcat7XServerRule() {
        super(Tomcat7xServer.class);
    }
}
