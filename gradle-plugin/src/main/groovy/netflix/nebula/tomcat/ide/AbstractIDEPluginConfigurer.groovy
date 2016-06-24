package netflix.nebula.tomcat.ide

import netflix.nebula.tomcat.NebulaTomcatRunnerExtension
import netflix.nebula.tomcat.NebulaTomcatRunnerPlugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

abstract class AbstractIDEPluginConfigurer implements IDEPluginConfigurer {
    private static Logger logger = Logging.getLogger(NebulaTomcatRunnerPlugin);

    protected String getVmParameters(Project project, String classesDir, String paramSeparator, String webappDir) {
        Set<String> vmParameters = ["-DtomcatClassesDir=${classesDir}", "-DtomcatWebAppBaseDir=$webappDir"] as Set

        File sysPropsFile = getExtension(project).systemProps
        if(sysPropsFile) {
            def sysProps = new Properties()
            try {
                sysProps.load(sysPropsFile.newInputStream())
                vmParameters.addAll(sysProps.collect { k, v -> "-D$k=$v" })
            } catch(IOException e) {
                logger.warn("unable to load system properties for use in generating tomcat launcher from $sysPropsFile.canonicalPath", e)
            }
        }

        // may override system properties defined in a properties file
        List<String> additionalJvmArgs = getExtension(project).jvmArgs
        if(additionalJvmArgs)
            vmParameters.addAll(additionalJvmArgs)

        vmParameters.join(paramSeparator)
    }

    protected String getRunConfigName(Project project) {
        NebulaTomcatRunnerExtension extension = getExtension(project)
        extension.runConfigName ?: extension.embeddedTomcat.runConfig.defaultName
    }

    protected String getRunConfigMainClassName(Project project) {
        getExtension(project).embeddedTomcat.runConfig.mainClassName
    }

    private NebulaTomcatRunnerExtension getExtension(Project project) {
        project.extensions.getByName(NebulaTomcatRunnerPlugin.EXTENSION_NAME)
    }
}
