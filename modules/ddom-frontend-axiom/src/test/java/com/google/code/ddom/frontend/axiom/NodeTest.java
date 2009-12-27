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
package com.google.code.ddom.frontend.axiom;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;

@RunWith(AxiomTestRunner.class)
public class NodeTest {
    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingAfterOnOrphan() {
        OMFactory factory = AxiomUtil.createDocument().getOMFactory();
        OMText text = factory.createOMText("test");
        OMElement element = factory.createOMElement("test", null);
        text.insertSiblingAfter(element);
    }

    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingBeforeOnOrphan() {
        OMFactory factory = AxiomUtil.createDocument().getOMFactory();
        OMText text = factory.createOMText("test");
        OMElement element = factory.createOMElement("test", null);
        text.insertSiblingBefore(element);
    }
}
