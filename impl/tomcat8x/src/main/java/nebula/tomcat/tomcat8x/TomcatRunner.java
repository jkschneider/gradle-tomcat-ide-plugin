package nebula.tomcat.tomcat8x;

import nebula.tomcat.embedded.CustomClassloaderTomatRunner;
import nebula.tomcat.embedded.FilteringClassloaderTomatRunner;

public class TomcatRunner {
    public static void main(String[] args) {
        CustomClassloaderTomatRunner customClassloaderTomatRunner = new FilteringClassloaderTomatRunner();
        customClassloaderTomatRunner.start(Tomcat8xServer.class.getName());
    }
}