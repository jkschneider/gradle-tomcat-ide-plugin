package nebula.tomcat.tomcat7x

import nebula.test.IntegrationSpec
import nebula.tomcat.embedded.TomcatStartParameterSystemProperty

class Tomcat7xServerIntegrationTest extends IntegrationSpec {
    WebComponentFixture webComponentFixture = new WebComponentFixture()

    def "can start server"() {
        setup:
        webComponentFixture.createServlet2xWebApp(projectDir)

        when:
        buildFile << """
            apply plugin: 'java'

            repositories {
                mavenCentral()
            }

            configurations {
                tomcat
                groovy
            }

            dependencies {
                //compile 'javax.servlet:servlet-api:2.5'
                def tomcatVersion = '7.0.61'
                tomcat "org.apache.tomcat.embed:tomcat-embed-core:\${tomcatVersion}",
                       "org.apache.tomcat.embed:tomcat-embed-logging-juli:\${tomcatVersion}",
                       "org.apache.tomcat.embed:tomcat-embed-jasper:\${tomcatVersion}"
            }

            task downloadTomcatJars(type: Copy) {
                from configurations.tomcat
                into "\$buildDir/libs/tomcat"
            }
        """

        def classesDir = new File(projectDir, 'build/classes/main')
        def tomcatLibsDir = new File(projectDir, 'build/libs/tomcat')

        then:
        runTasksSuccessfully('compileJava', 'downloadTomcatJars')
        classesDir.exists()
        tomcatLibsDir.exists()

        when:
        File projectRootDir = new File(projectDir, '../../../../')
        AntBuilder ant = new AntBuilder()
        ant.java(classname: 'nebula.tomcat.tomcat7x.TomcatRunner', fork: true, timeout: 5000) {
            //jvmarg(value: '-XX:+TraceClassLoading')
            classpath {
                pathelement(path: new File(projectRootDir, 'embedded/build/classes/main'))
                pathelement(path: new File(projectRootDir, 'tomcat7x/build/classes/main'))
                pathelement(path: classesDir)

                tomcatLibsDir.listFiles().each { lib ->
                    pathelement(location: lib)
                }
            }
            sysproperty(key: TomcatStartParameterSystemProperty.CLASSES_DIR.key, value: new File(projectDir, 'build/classes/main').canonicalPath)
            sysproperty(key: TomcatStartParameterSystemProperty.WEB_APP_BASE_DIR.key, value: new File(projectDir, 'src/main/webapp').canonicalPath)
        }

        then:
        noExceptionThrown()
    }
}
