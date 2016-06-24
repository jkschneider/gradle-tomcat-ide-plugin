package netflix.nebula.tomcat.ide.eclipse

import netflix.nebula.tomcat.NebulaTomcatRunnerPlugin
import netflix.nebula.tomcat.ide.AbstractIDEPluginConfigurer
import org.gradle.api.Project
import org.gradle.util.GFileUtils

class EclipsePluginConfigurer extends AbstractIDEPluginConfigurer {
    @Override
    void configure(Project project) {
        project.eclipse.project.file.whenMerged {
            writeLaunchFile(project)
        }

        project.eclipse.classpath {
            plusConfigurations += [
                project.configurations.findByName(NebulaTomcatRunnerPlugin.EMBEDDED_TOMCAT_RUNNER_CONFIG),
                project.configurations.findByName(NebulaTomcatRunnerPlugin.EMBEDDED_TOMCAT_LIBS_CONFIG)
            ]

            noExportConfigurations += [
                project.configurations.runtime,
                project.configurations.testRuntime,
                project.configurations.compile,
                project.configurations.testCompile
            ]
        }

        project.tasks.create('eclipseTomcatLauncher') {
            doLast {
                writeLaunchFile(project)
            }
        }
    }

    protected File writeLaunchFile(Project project) {
        String runConfigName = getRunConfigName(project)
        String runConfigMainClassName = getRunConfigMainClassName(project)
        File launchFile = new File(project.projectDir, "${runConfigName}.launch")

        def projectName = project.name == project.rootProject.name ? "${project.rootProject.name}-${project.name}" :
                project.name

        GFileUtils.writeStringToFile(launchFile, """\
            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            <launchConfiguration type="org.eclipse.jdt.launching.localJavaApplication">
            <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
            <listEntry value="/${projectName}"/>
            </listAttribute>
            <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
            <listEntry value="4"/>
            </listAttribute>
            <booleanAttribute key="org.eclipse.jdt.launching.ATTR_USE_START_ON_FIRST_THREAD" value="true"/>
            <listAttribute key="org.eclipse.debug.ui.favoriteGroups">
            <listEntry value="org.eclipse.debug.ui.launchGroup.debug"/>
            <listEntry value="org.eclipse.debug.ui.launchGroup.run"/>
            </listAttribute>
            <stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="${runConfigMainClassName}"/>
            <stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="${projectName}"/>
            <stringAttribute key="org.eclipse.jdt.launching.VM_ARGUMENTS" value="${getVmParameters(project, 'bin', '&#10;', project.webAppDirName)}"/>
            </launchConfiguration>\
        """.stripIndent())

        launchFile
    }
}
