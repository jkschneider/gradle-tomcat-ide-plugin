## Purpose

`netflix.tomcat-runner` generates Eclipse and IntelliJ run configurations to test your web application locally on Tomcat without having to configure a Tomcat server in your IDE.  Since it runs as a regular Java main method it also avoids much of the complexity associated with the publish/sync/run/stop/debug state machine familiar to those that have worked with Eclipse WTP or IntelliJ's Tomcat plugin.

The embedded Tomcat runner does NOT require you to have Tomcat installed on your machine.  It runs tomcat via a jar dependency that it downloads to an isolated configuration.  There is no relationship between the embedded Tomcat runner and any Tomcat servers configured in your IDE.

## Configuration

In your web project's build.gradle, add:

```groovy
apply plugin: 'netflix.tomcat-runner'
// these are all optional settings
tomcat {
   majorVersion = '7' // OPTIONAL - or '8', defaults to '7'
   runConfigName = 'myname' // OPTIONAL - default is 'Tomcat 7' or 'Tomcat 8' depending on which majorVersion you have selected
   jvmArgs = ['-Dfoo.bar=true', '-Xmx8G'] // OPTIONAL - only if you have a need to set JVM arguments
   systemProps = file('tomcat.properties') // OPTIONAL - will be converted to -D command line arguments and added to the launcher configuration
}
```

### The web root folder

The embedded tomcat server will look for web resources in the project's webAppDirName (which defaults to src/main/webapp).  In Gradle, it is possible to combine multiple independent folders into the root of the web archive by using

```groovy
web { // PLEASE DON'T DO THIS!!!
   from 'web' // this is a Gradle fileset, so could have multiple paths
}
```

Since Tomcat embedded itself can only be bootstrapped with one web directory, and we want to avoid copy-merging filesets outside of the project's source, we only support the webAppDirName based configuration of the web folder.

Instead, set:

```groovy
webAppDirName = 'web'
```

### Changing the port

If you need to change the default port (8080), add an element to jvmArgs like so:

```groovy
tomcat {
   jvmArgs = ['-DtomcatHttpPort=9090']
}
```

### Changing the context path
If you do not want your application to run under the default context path ('/'), add an element to jvmArgs like so:

```groovy
tomcat {
   jvmArgs = ['-DtomcatContextPath=yourpath']
}
```

### Run configuration name

This directly affects the name of the launch configuration in your IDE.  If you commonly include multiple Tomcat-based services in one Eclipse workspace, it is beneficial to specify a runConfigName yourself, as each service would generate a launch config with the default name and they would be difficult to disambiguate in the IDE dropdown.

```groovy
tomcat {
   runConfigName = 'myrunconfig'
}
```

Note: if you regenerate the Eclipse launch configuration (which is an actual file) after changing the runConfigName, you will have to manually delete the old .launch file.

### System properties

```groovy
tomcat {
   systemProps = file('tomcat.properties') // relative to the project directory
   jvmArgs = ['-Dfoo=bar'] // will override a foo=baz line in tomcat.properties
}
```

### Custom server.xml
You can provide a custom server.xml file to the embedded tomcat instance by setting a jvmArg:

```groovy
jvmArgs = ['-DtomcatServerXml=server.xml']
```

### Eclipse Users
The plugin makes available a gradle task that will generate a 'Tomcat 7.launch' (or 'Tomcat 8.launch') file in your web project directory that Eclipse will automatically pick up on and add to your available run configurations.  If you have set `tomcat.runConfigName` to something other than the default, the .launch file will be named accordingly.

To create this configuration, run:

```
./gradlew eclipseTomcatLauncher
```

### IntelliJ Users

The plugin creates a run configuration by adding to the configuration of the idea.workspace.iws property consumed by the Gradle Idea plugin.  

To create this configuration:

1. If you have already imported your project into IntelliJ, close it and delete the .idea folder at the root of your project, and all .iml files in the project root and all subproject directories.  To quickly delete all .iml files, you can run `./gradlew cleanIdea`, but this task will NOT delete the .idea folder.
2.  `./gradlew idea`
3.  In IntelliJ, File -> Open and select the project root.
4.  IntelliJ will present you a prompt to link the Gradle project; click the "Import Gradle Project" link.
5.  Select the .ipr project format, and import.