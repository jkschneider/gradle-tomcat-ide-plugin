# Embedded Tomcat Runner

Provides an implementation of embedded Tomcat for the purpose of running a standalone server.

### Supported Tomcat versions

<table>
    <tr>
        <th>Major Version</th>
        <th>Main Class</th>
    </tr>
    <tr>
        <td>Tomcat 7</td>
        <td><code>nebula.tomcat.tomcat7x.TomcatRunner</code></td>
    </tr>
    <tr>
        <td>Tomcat 8</td>
        <td><code>nebula.tomcat.tomcat8x.TomcatRunner</code></td>
    </tr>
</table>

### Running the Tomcat instance

Executing Tomcat requires you to provide the main class and the runtime classpath. The runtime classpath requires
the appropriate Tomcat runner JAR files, the embedded Tomcat libraries and the runtime libraries required by your web application.

<table>
    <tr>
        <th>Module</th>
        <th>Description</th>
        <th>JAR file</th>
    </tr>
    <tr>
        <td>Embedded</td>
        <td>Shared logic for configuring embedded Tomcat</td>
        <td><code>embedded-&lt;version&gt;.jar</code></td>
    </tr>
    <tr>
        <td>Tomcat 7</td>
        <td>Logic for executing Tomcat 7</td>
        <td><code>tomcat7x-&lt;version&gt;.jar</code></td>
    </tr>
    <tr>
        <td>Tomcat 8</td>
        <td>Logic for executing Tomcat 8</td>
        <td><code>tomcat8x-&lt;version&gt;.jar</code></td>
    </tr>
</table>

The following example demonstrate the use of a Tomcat runner for version 7.0.59:

    java -cp embedded-0.1.jar;tomcat7x-0.1.jar;ecj-4.4.jar;tomcat-embed-core-7.0.59.jar;tomcat-embed-el-7.0.59.jar;tomcat-embed-jasper-7.0.59.jar;tomcat-embed-logging-juli-7.0.59.jar nebula.tomcat.tomcat7x.TomcatRunner

You should see the output of Tomcat in your console. The output should look similar to this:

    Apr 02, 2015 10:02:03 AM org.apache.coyote.AbstractProtocol init
    INFO: Initializing ProtocolHandler ["http-bio-8080"]
    Apr 02, 2015 10:02:03 AM org.apache.catalina.core.StandardService startInternal
    INFO: Starting service Tomcat
    Apr 02, 2015 10:02:03 AM org.apache.catalina.core.StandardEngine startInternal
    INFO: Starting Servlet Engine: Apache Tomcat/7.0.59
    Apr 02, 2015 10:02:03 AM org.apache.catalina.startup.ContextConfig getDefaultWebXmlFragment
    INFO: No global web.xml found
    Apr 02, 2015 10:02:03 AM org.apache.coyote.AbstractProtocol start
    INFO: Starting ProtocolHandler ["http-bio-8080"]

By default the Tomcat instance is configured to hot reload class files of your application. Simply recompile one or many
of the classes in your project. After a couple of seconds you should see that Tomcat refreshes the context.

    Apr 02, 2015 10:02:33 AM org.apache.catalina.core.StandardContext reload
    INFO: Reloading Context with name [/test] has started

### Configuration options

The Tomcat instance can be configured through system properties. The following table shows all options, their default
 values as well as the system property used for providing a custom value.

<table>
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Default Value</th>
        <th>System Property</th>
    </tr>
    <tr>
        <td>HTTP Port</td>
        <td>HTTP port used for server</td>
        <td><code>8080</code></td>
        <td><code>tomcatHttpPort</code></td>
    </tr>
    <tr>
        <td>Context Path</td>
        <td>Context path for application</td>
        <td><code>/</code></td>
        <td><code>tomcatContextPath</code></td>
    </tr>
    <tr>
        <td>Classes Directory</td>
        <td>Directory containing compiled class files</td>
        <td><code>/build/classes/main</code></td>
        <td><code>tomcatClassesDir</code></td>
    </tr>
    <tr>
        <td>Web application base directory</td>
        <td>Directory containing web assets like HTML files, JSPs etc.</td>
        <td><code>WebRoot</code></td>
        <td><code>tomcatWebAppBaseDir</code></td>
    </tr>
    <tr>
        <td>Server descriptor</td>
        <td>The <code>server.xml</code> used to describe server configuration</td>
        <td><code>null</code></td>
        <td><code>tomcatServerXml</code></td>
    </tr>
</table>

Let's say you want to run on a different port. Simply add the following system property to your command execution:
`-DtomcatHttpPort=9090`.

### Testing support

Integration testing and more specifically functional testing often requires a running Tomcat server instance. This project
also provides support for more complex testing scenarios.

#### JUnit 4 Runner

A JUnit Runner implementation is helpful if want to bring up a Tomcat container before any of the test methods of a
class should be run. The limitation of a JUnit Runner implementation is that you can only assign one per test class.

<table>
    <tr>
        <th>Major Version</th>
        <th>Main Class</th>
    </tr>
    <tr>
        <td>Tomcat 7</td>
        <td><code>nebula.tomcat.tomcat7x.junit4.Tomcat7xServerJUnit4ClassRunner</code></td>
    </tr>
    <tr>
        <td>Tomcat 8</td>
        <td><code>nebula.tomcat.tomcat8x.junit4.Tomcat8xServerJUnit4ClassRunner</code></td>
    </tr>
</table>

#### Usage Example

    import nebula.tomcat.tomcat7x.junit4.Tomcat7xServerJUnit4ClassRunner;
    import org.junit.Test;

    @RunWith(Tomcat7xServerJUnit4ClassRunner.class)
    public class MyIntegrationTest {
        ...
    }

#### JUnit 4 Test Rule

A JUnit Test Rule implementation is helpful if want to bring up a Tomcat container before any of the test methods of a
class should be run. You can add multiple JUnit Test Rules per test class. Keep in mind that you will not need to assign
a Runner if you decide to go for this option.

<table>
    <tr>
        <th>Major Version</th>
        <th>Main Class</th>
    </tr>
    <tr>
        <td>Tomcat 7</td>
        <td><code>nebula.tomcat.tomcat7x.junit4.Tomcat7xServerJUnit4TestRule</code></td>
    </tr>
    <tr>
        <td>Tomcat 8</td>
        <td><code>nebula.tomcat.tomcat8x.junit4.Tomcat8xServerJUnit4TestRule</code></td>
    </tr>
</table>

#### Usage Example

    import nebula.tomcat.tomcat7x.junit4.Tomcat7xServerJUnit4TestRule;
    import org.junit.Rule;
    import org.junit.rules.TestRule;

    public class MyIntegrationTest {
        @Rule
        public TestRule rule = new Tomcat7xServerJUnit4TestRule();

        ...
    }