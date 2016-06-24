package netflix.nebula.tomcat.ide.idea

import netflix.nebula.tomcat.NebulaTomcatRunnerPlugin
import netflix.nebula.tomcat.ide.IDEPluginConfigurer
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

class IdeaPluginModuleConfigurer implements IDEPluginConfigurer {
    @Override
    void configure(Project project) {
        project.idea.module {
            Configuration embeddedTomcatRunnerConfig = project.configurations.findByName(NebulaTomcatRunnerPlugin.EMBEDDED_TOMCAT_RUNNER_CONFIG)
            Configuration embeddedTomcatLibsConfig = project.configurations.findByName(NebulaTomcatRunnerPlugin.EMBEDDED_TOMCAT_RUNNER_CONFIG)

            if (embeddedTomcatRunnerConfig && embeddedTomcatLibsConfig) {
                scopes.RUNTIME.plus += [embeddedTomcatRunnerConfig, embeddedTomcatLibsConfig]
            }
        }
    }
}
