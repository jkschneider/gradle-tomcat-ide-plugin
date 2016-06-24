package nebula.tomcat.embedded;

public interface CustomClassloaderTomatRunner {
    void start(String tomcatServerClassName);
    void stop();
}
