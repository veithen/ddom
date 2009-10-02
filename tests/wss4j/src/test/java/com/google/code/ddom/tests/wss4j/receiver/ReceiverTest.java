/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.google.code.ddom.stax.StAXSource;


public class ReceiverTest {
    @Test
    public void testUsernameToken() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Document doc = new DocumentImpl(new StAXSource(factory.createXMLStreamReader(ReceiverTest.class.getResourceAsStream("UsernameToken.xml"))));
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
