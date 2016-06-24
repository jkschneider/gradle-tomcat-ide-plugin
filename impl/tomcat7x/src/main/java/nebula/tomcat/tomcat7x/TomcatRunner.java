package nebula.tomcat.tomcat7x;

import nebula.tomcat.embedded.CustomClassloaderTomatRunner;
import nebula.tomcat.embedded.FilteringClassloaderTomatRunner;

public class TomcatRunner {
    public static void main(String[] args) {
        CustomClassloaderTomatRunner customClassloaderTomatRunner = new FilteringClassloaderTomatRunner();
        customClassloaderTomatRunner.start(Tomcat7xServer.class.getName());
    }
}
