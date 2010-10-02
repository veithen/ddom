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
package com.google.code.ddom.frontend.axiom.soap;

import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.junit.Test;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;

public class FrontendTest {
    @Test
    public void test() {
        DocumentHelper helper = DocumentHelperFactory.INSTANCE.newInstance();
        OMDocument document = (OMDocument)helper.parse("axiom-soap", FrontendTest.class.getResourceAsStream("message.xml"));
        OMElement element = document.getOMDocumentElement();
        SOAPEnvelope env = (SOAPEnvelope)element;
        helper.disposeDocument(document);
    }
}
