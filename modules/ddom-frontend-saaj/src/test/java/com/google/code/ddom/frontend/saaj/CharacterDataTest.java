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
package com.google.code.ddom.frontend.saaj;

import static org.junit.Assert.assertEquals;

import javax.xml.soap.SOAPConstants;
import javax.xml.soap.Text;

import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

public abstract class CharacterDataTest extends AbstractTestCase {
    public CharacterDataTest() {
        super(SOAPConstants.URI_NS_SOAP_ENVELOPE);
    }
    
    protected abstract Text createNode(String content);
    
    @Validated @Test
    public final void testGetValue() {
        assertEquals("test", createNode("test").getValue());
    }
    
    @Validated @Test
    public final void testSetValue() {
        Text node = createNode("foo");
        node.setValue("bar");
        assertEquals("bar", node.getValue());
    }
}
