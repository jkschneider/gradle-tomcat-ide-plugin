package nebula.tomcat.embedded;

public enum TomcatStartParameterSystemProperty {
    HTTP_PORT("tomcatHttpPort"),
    CONTEXT_PATH("tomcatContextPath"),
    CLASSES_DIR("tomcatClassesDir"),
    WEB_APP_BASE_DIR("tomcatWebAppBaseDir"),
    SERVER_XML("tomcatServerXml"),
    BACKGROUND("tomcatBackground");

    private final String key;

    private TomcatStartParameterSystemProperty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getProperty() {
        return System.getProperty(key);
    }
}
