package nebula.tomcat.embedded;

public interface TomcatServer {
    void start(TomcatStartParameters startParameters);
    void stop();
}
