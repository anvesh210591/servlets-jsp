# Java Web application

The directory structure for Tomcat Web application would look like this.

-Tomcat
  -webapps
    -musicStore
      -admin
      -cart
      -catalog
      -META-INF
      -WEB-INF
        -classes
          -music
            -business
            -controller
            -data
            -util
        -lib

- WEB-INF: web.xml file is stored here. This file is used for configuration. This directory is not directly accessible from the web.
- WEB-INF/classes: This and its subdirectories include servlets.
- WEB-INF/lib: This contains JAR files that contain Java class libraries.
- META-INF: It contains context.xml. This file is used to configure application context.

To make classes within JAR file available to more than one web app, you can put JAR file in Tomcat's lib directory.
