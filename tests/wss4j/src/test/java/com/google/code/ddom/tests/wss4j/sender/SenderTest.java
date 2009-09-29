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
package com.google.code.ddom.tests.wss4j.sender;

import javax.xml.stream.XMLInputFactory;

import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.code.ddom.dom.impl.DocumentImpl;
import com.google.code.ddom.tests.wss4j.receiver.ReceiverTest;


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
