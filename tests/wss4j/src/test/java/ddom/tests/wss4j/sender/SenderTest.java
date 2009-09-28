package ddom.tests.wss4j.sender;

import javax.xml.stream.XMLInputFactory;

import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.junit.Test;
import org.w3c.dom.Document;

import ddom.DocumentImpl;
import ddom.tests.wss4j.receiver.ReceiverTest;

public class SenderTest {
    @Test
    public void testUsernameToken() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Document doc = new DocumentImpl(factory.createXMLStreamReader(ReceiverTest.class.getResourceAsStream("UsernameToken.xml")));
        WSSecUsernameToken builder = new WSSecUsernameToken();
        builder.setUserInfo("user", "password");
        WSSecHeader secHeader = new WSSecHeader();
        secHeader.insertSecurityHeader(doc);
        Document signedDoc = builder.build(doc, secHeader);
        System.out.println(signedDoc);
    }
}
