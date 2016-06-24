package nebula.tomcat.embedded;

public class TomcatStartInfo {
    private final int port;
    private final String contextPath;

    public TomcatStartInfo(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

    public String getStartMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Started server on port ");
        message.append(port);
        message.append(" and context path '");
        message.append(contextPath);
        message.append("'");
        return message.toString();
    }
}