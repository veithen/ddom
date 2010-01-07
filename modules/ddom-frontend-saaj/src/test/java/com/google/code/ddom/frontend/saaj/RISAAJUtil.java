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
package com.google.code.ddom.frontend.saaj;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.junit.Assert;

import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl;

public class RISAAJUtil extends SAAJUtil {
    public static final RISAAJUtil INSTANCE = new RISAAJUtil();
    
    private RISAAJUtil() {}
    
    private final SOAPFactory factory = new SOAPFactory1_1Impl();
    
    @Override
    public SOAPElement createSOAPElement(String namespaceURI, String localName, String prefix) {
        try {
            return factory.createElement(localName, prefix, namespaceURI);
        } catch (SOAPException ex) {
            Assert.fail(ex.getMessage());
            return null;
        }
    }
}
