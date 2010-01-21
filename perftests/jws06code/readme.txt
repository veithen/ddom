The build setup used for this performance test code is similar to that used for
the earlier security examples but does includes a few more options in the
build.properties file. One of these is the path to the truststore used for the
SSL certificate presented by the server when making an HTTPS connection. If you
use a self-signed certificate for the server you'll have to import that
certificate into a truststore and make it available to the client code (or just
point the client code at the same file used for the server's keystore, if you're
running on the same system).

Most of the security configuration use the approach of merging the server-side
policy information into the generated services.xml file by using the mergetool I
included in an earlier article. The one exception is the WS-SecureConversation
configuration, which was too complex to easily handle in this way. The
WS-SecureConversation test instead uses a secureconverstation-services.xml file
which has been manually modified to include the WS-SecureConversation policy. If
you change the service WSDL or the WS-SecureConversation policy configuration
you'll need to update this file yourself.
