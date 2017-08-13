# ActiveMQ Broker and Admin Console as a WAR file

This project is a simple maven overlay over the standard ActiveMQ Web
Console WAR.  The modifications are minimal and are limited to
configuration.

This modifies `WEB-INF/webconsole-embedded.xml` in the WAR and removes
`WEB-INF/activemq.xml` from the WAR.

`webconsole-embedded.xml` is modified so that the `activemq.xml` config file
is found in a directory specified with the `activemq.conf` system property
(which is the same system property the binary ActiveMQ distribution uses).

`webconsole-embedded.xml` is made to look more like what you see in the
binary assembly distribution of ActiveMQ.  If you're curious, you can see
this version in the ActiveMQ source code in
`assembly/src/release/webapps/admin/WEB-INF/webconsole-embedded.xml`.

You can deploy this WAR file in your application server and launch the JVM
with `-Dactivemq.conf` to specify the directory location containing your
`activemq.xml` and `credentials.properties` configuration files.  This gives
you both an ActiveMQ Broker and the Web Console running within your
application server.

When you deploy the WAR, you'll probably want to rename the WAR to something
like `activemq-admin.war` so that you can access the web console with a
simple URL like http://localhost/activemq-admin.

If you need a starting point for your configuration files, use the
`activemq.xml` and `credentials.properties` files from the binary ActiveMQ
distribution in the `conf` directory.  If you use the stock `activemq.xml`,
you'll need to set the `activemq.data` system property as well.

I also had to make the following modifications to the stock activemq.xml:
* Delete the logQuery bean
* Comment out transports that weren't in use.  I only configured the
openwire transport.
* Delete the shutdownHooks section
* Delete the import of jetty.xml

## Building the WAR

Run:
```
./gradlew war
```

The WAR ends up in the `builds/lib` directory.

## App Servers like Tomcat without JSTL

Tomcat doesn't ship with JSTL dependencies (and possibly other application
servers), so if you're deploying the WAR to an application server without
JSTL, use the WAR file from `with-jstl-overlay/builds/lib`.
