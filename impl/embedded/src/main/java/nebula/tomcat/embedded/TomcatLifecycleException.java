package nebula.tomcat.embedded;

public class TomcatLifecycleException extends RuntimeException {
    public TomcatLifecycleException(String message) {
        super(message);
    }

    public TomcatLifecycleException(Throwable cause) {
        super(cause);
    }

    public TomcatLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }
}
