package netflix.nebula.tomcat

class NebulaTomcatRunnerExtension {
    /**
     * The major version of Tomcat. Defaults to "7".
     */
    String majorVersion = EmbeddedTomcat.VERSION_7.majorVersion

    /**
     * The name of the run configuration. Defaults to null.
     */
    String runConfigName

    /**
     * Adds JVM arguments when launching the Tomcat process. Defaults to an empty List.
     */
    List<String> jvmArgs = []

    File systemProps

    EmbeddedTomcat getEmbeddedTomcat() {
        EmbeddedTomcat.getEmbeddedTomcatForVersion(majorVersion)
    }
}
