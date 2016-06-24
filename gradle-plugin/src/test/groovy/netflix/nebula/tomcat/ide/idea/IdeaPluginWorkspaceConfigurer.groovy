package netflix.nebula.tomcat.ide.idea

import netflix.nebula.tomcat.ide.AbstractIDEPluginConfigurer
import org.gradle.api.Project
import org.gradle.api.XmlProvider

class IdeaPluginWorkspaceConfigurer extends AbstractIDEPluginConfigurer {
    private static final String RUN_MANAGER_CONFIG_TYPE = 'Application'

    @Override
    void configure(Project project) {
        Project topLevelRootProject = project.project(':')

        topLevelRootProject.idea.workspace.iws.withXml { XmlProvider provider ->
            String runConfigName = getRunConfigName(project)
            String runConfigMainClassName = getRunConfigMainClassName(project)

            Node node = provider.asNode()
            Node runManager = node.component.find { it.'@name' == 'RunManager' }

            def embeddedTomcatConfig = runManager.configuration.find {
                it.@type == RUN_MANAGER_CONFIG_TYPE && it.@name == runConfigName
            }

            if (!embeddedTomcatConfig) {
                NodeBuilder builder = new NodeBuilder()
                String webAppDir = "${project.projectDir.absolutePath}/${project.webAppDirName}" - "${project.rootDir.absolutePath}/"

                embeddedTomcatConfig = builder.configuration(default: 'false', factoryName: RUN_MANAGER_CONFIG_TYPE,
                        type: RUN_MANAGER_CONFIG_TYPE, name: runConfigName) {
                    option(name: 'MAIN_CLASS_NAME', value: runConfigMainClassName)
                    option(name: 'VM_PARAMETERS', value: getVmParameters(project, "out/production/$project.name", ' ', webAppDir))
                    option(name: 'WORKING_DIRECTORY', value: 'file://$PROJECT_DIR$')
                    module(name: project.name)
                }

                runManager.append(embeddedTomcatConfig)
                runManager.'@selected' = "Application.${runConfigName}"
            }
        }
    }
}
