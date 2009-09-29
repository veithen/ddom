package com.google.code.ddom.tests.wss4j.receiver;

import java.util.Vector;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.xml.stream.XMLInputFactory;

import junit.framework.Assert;

import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.code.ddom.dom.impl.DocumentImpl;


public class ReceiverTest {
    @Test
    public void testUsernameToken() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Document doc = new DocumentImpl(factory.createXMLStreamReader(ReceiverTest.class.getResourceAsStream("UsernameToken.xml")));
        WSSecurityEngine engine = WSSecurityEngine.getInstance();
        CallbackHandler cb = new CallbackHandler() {
            public void handle(Callback[] callbacks) {
                // Any password is good
            }
        };
        Vector results = engine.processSecurityHeader(doc, null, cb, null);
        Assert.assertEquals(1, results.size());
        WSSecurityEngineResult result = (WSSecurityEngineResult)results.get(0);
        Assert.assertEquals("user", ((WSUsernameTokenPrincipal)result.get(WSSecurityEngineResult.TAG_PRINCIPAL)).getName());
    }
}
