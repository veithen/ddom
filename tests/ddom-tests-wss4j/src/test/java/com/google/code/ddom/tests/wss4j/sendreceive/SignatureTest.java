package com.google.code.ddom.tests.wss4j.sendreceive;
import java.util.Vector;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSEncryptionPart;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.code.ddom.DocumentHelperFactory;

/*
 * Copyright 2009-2010 Andreas Veithen
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
public class SignatureTest {
    private final Crypto crypto = CryptoFactory.getInstance();
    
    private void testSignature(String file, Vector<WSEncryptionPart> parts) throws Exception {
        WSSecSignature sign = new WSSecSignature();
        sign.setUserInfo("key1", "password");
        sign.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
        sign.setParts(parts);

        Document doc = (Document)DocumentHelperFactory.INSTANCE.newInstance().parse("dom", SignatureTest.class.getResourceAsStream(file));
        
        WSSecHeader secHeader = new WSSecHeader();
        secHeader.insertSecurityHeader(doc);
        
        Document signedDoc = sign.build(doc, crypto, secHeader);
        
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(signedDoc), new StreamResult(System.out));

        WSSecurityEngine secEngine = new WSSecurityEngine();
        Assert.assertEquals(2, secEngine.processSecurityHeader(signedDoc, null, null, crypto).size());
    }
    
    @Test
    public void testSignHeaderAndBody() throws Exception {
        Vector<WSEncryptionPart> parts = new Vector<WSEncryptionPart>();
        parts.add(new WSEncryptionPart("header", "urn:ns1", ""));
        parts.add(new WSEncryptionPart("Body", "http://schemas.xmlsoap.org/soap/envelope/", ""));
        testSignature("envelope1.xml", parts);
    }
    
    @Test
    public void testSignPartById() throws Exception {
        Vector<WSEncryptionPart> parts = new Vector<WSEncryptionPart>();
        parts.add(new WSEncryptionPart("my-id"));
        testSignature("envelope2.xml", parts);
    }
}
