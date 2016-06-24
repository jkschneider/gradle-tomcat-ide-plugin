package netflix.nebula.tomcat

import netflix.nebula.tomcat.ide.eclipse.EclipsePluginConfigurer
import netflix.nebula.tomcat.ide.idea.IdeaPluginModuleConfigurer
import netflix.nebula.tomcat.ide.idea.IdeaPluginWorkspaceConfigurer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.WarPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

class NebulaTomcatRunnerPlugin implements Plugin<Project> {
    public static final String EMBEDDED_TOMCAT_RUNNER_CONFIG = 'embeddedTomcatRunner'
    public static final String EMBEDDED_TOMCAT_LIBS_CONFIG = 'embeddedTomcatLibs'
    public static final String EXTENSION_NAME = 'tomcat'

    @Override
    void apply(Project project) {
        project.plugins.apply(WarPlugin)

        Configuration embeddedTomcatRunnerConfig = createConfiguration(project, EMBEDDED_TOMCAT_RUNNER_CONFIG, 'The embedded Tomcat runner libraries.')
        Configuration embeddedTomcatLibsConfig = createConfiguration(project, EMBEDDED_TOMCAT_LIBS_CONFIG, 'The embedded Tomcat libraries provided by Apache.')

        NebulaTomcatRunnerExtension extension = project.extensions.create(EXTENSION_NAME, NebulaTomcatRunnerExtension)

        project.afterEvaluate {
            EmbeddedTomcat embeddedTomcat = extension.embeddedTomcat
            declareEmbeddedTomcatRunnerDependencies(project, embeddedTomcatRunnerConfig, embeddedTomcat.dependencies)
            declareEmbeddedTomcatLibsDependencies(project, embeddedTomcatLibsConfig, embeddedTomcat.dependencies)
            configureIdePlugins(project)
        }
    }

    private Configuration createConfiguration(Project project, String name, String description) {
        project.configurations.create(name)
                .setVisible(false)
                .setTransitive(true)
                .setDescription(description)
    }

    private void declareEmbeddedTomcatRunnerDependencies(Project project, Configuration embeddedTomcatRunnerConfig, EmbeddedTomcat.Dependencies dependencies) {
        dependencies.runnerLibs.each { EmbeddedTomcat.Dependencies.Dependency runnerLib ->
            addDependency(project, embeddedTomcatRunnerConfig, runnerLib.toString())
        }
    }

    private void declareEmbeddedTomcatLibsDependencies(Project project, Configuration embeddedTomcatLibsConfig, EmbeddedTomcat.Dependencies dependencies) {
        dependencies.embeddedLibs.each { EmbeddedTomcat.Dependencies.Dependency embeddedLib ->
            addDependency(project, embeddedTomcatLibsConfig, embeddedLib.toString())
        }
    }

    private void addDependency(Project project, Configuration config, String coordinates) {
        Dependency dependency = project.dependencies.create(coordinates)
        config.dependencies.add(dependency)
    }

    private void configureIdePlugins(Project project) {
        project.with {
            plugins.withType(IdeaPlugin) {
                new IdeaPluginWorkspaceConfigurer().configure(project)
                new IdeaPluginModuleConfigurer().configure(project)
            }

            plugins.withType(EclipsePlugin) {
                new EclipsePluginConfigurer().configure(project)
            }
        }
    }
}
