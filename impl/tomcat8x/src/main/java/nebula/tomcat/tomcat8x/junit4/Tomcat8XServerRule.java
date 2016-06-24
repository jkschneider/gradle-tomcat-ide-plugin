package nebula.tomcat.tomcat8x.junit4;

import nebula.tomcat.embedded.junit4.TomcatServerRule;
import nebula.tomcat.tomcat8x.Tomcat8xServer;

public class Tomcat8XServerRule extends TomcatServerRule {
    public Tomcat8XServerRule() {
        super(Tomcat8xServer.class);
    }
}
