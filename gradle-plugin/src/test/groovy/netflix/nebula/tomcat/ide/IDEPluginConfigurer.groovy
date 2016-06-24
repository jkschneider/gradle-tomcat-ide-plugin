package netflix.nebula.tomcat.ide

import org.gradle.api.Project

interface IDEPluginConfigurer {
    void configure(Project project)
}