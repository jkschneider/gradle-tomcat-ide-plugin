package netflix.nebula.tomcat

enum EmbeddedTomcat {
    VERSION_7('7',
              new RunConfig('Tomcat 7', 'nebula.tomcat.tomcat7x.TomcatRunner'),
              new Dependencies([new Dependencies.Dependency('com.netflix.nebula.embedded-tomcat-runner', 'embedded', '0.2'),
                                new Dependencies.Dependency('com.netflix.nebula.embedded-tomcat-runner', 'tomcat7x', '0.2')],
                               [new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-core', '7.0.59'),
                                new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-logging-juli', '7.0.59'),
                                new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-jasper', '7.0.59')])),
    VERSION_8('8',
              new RunConfig('Tomcat 8', 'nebula.tomcat.tomcat8x.TomcatRunner'),
              new Dependencies([new Dependencies.Dependency('com.netflix.nebula.embedded-tomcat-runner', 'embedded', '0.2'),
                                new Dependencies.Dependency('com.netflix.nebula.embedded-tomcat-runner', 'tomcat8x', '0.2')],
                               [new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-core', '8.0.21'),
                                new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-logging-juli', '8.0.21'),
                                new Dependencies.Dependency('org.apache.tomcat.embed', 'tomcat-embed-jasper', '8.0.21')]))

    private final String majorVersion
    private final RunConfig runConfig
    private final Dependencies dependencies

    private EmbeddedTomcat(String majorVersion, RunConfig runConfig, Dependencies dependencies) {
        this.majorVersion = majorVersion
        this.runConfig = runConfig
        this.dependencies = dependencies
    }

    String getMajorVersion() {
        majorVersion
    }

    RunConfig getRunConfig() {
        runConfig
    }

    Dependencies getDependencies() {
        dependencies
    }

    static EmbeddedTomcat getEmbeddedTomcatForVersion(String majorVersion) {
        switch(majorVersion) {
            case VERSION_7.majorVersion:
                return VERSION_7
            case VERSION_8.majorVersion:
                return VERSION_8
            default:
                throw new IllegalArgumentException("Unsupported Tomcat version '$majorVersion'. Supported versions are ['$VERSION_7.majorVersion', '$VERSION_8.majorVersion'].")
        }
    }

    static class RunConfig {
        private final String defaultName
        private final String mainClassName

        RunConfig(String defaultName, String mainClassName) {
            this.defaultName = defaultName
            this.mainClassName = mainClassName
        }

        String getDefaultName() {
            defaultName
        }

        String getMainClassName() {
            mainClassName
        }
    }

    static class Dependencies {
        private final List<Dependency> runnerLibs
        private final List<Dependency> embeddedLibs

        Dependencies(List<Dependency> runnerLibs, List<Dependency> embeddedLibs) {
            this.runnerLibs = runnerLibs
            this.embeddedLibs = embeddedLibs
        }

        List<Dependency> getRunnerLibs() {
            runnerLibs
        }

        List<Dependency> getEmbeddedLibs() {
            embeddedLibs
        }

        static class Dependency {
            private final String group
            private final String name
            private final String version

            Dependency(String group, String name, String version) {
                this.group = group
                this.name = name
                this.version = version
            }

            String getGroup() {
                group
            }

            String getName() {
                name
            }

            String getVersion() {
                version
            }


            @Override
            String toString() {
                "$group:$name:$version"
            }
        }
    }
}
