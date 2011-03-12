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
package com.googlecode.ddom.frontend.dom;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class NodeTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Validated @Test
    public void testIsEqualNodeWithForeignNode() {
        Document document = domUtil.newDocument();
        Document foreignDocument = CrimsonDOMUtil.INSTANCE.newDocument();
        ProcessingInstruction pi = document.createProcessingInstruction("target", "data");
        ProcessingInstruction foreignPi = foreignDocument.createProcessingInstruction("target", "data");
        Assert.assertTrue(pi.isEqualNode(foreignPi));
    }
    
    /**
     * Check that {@link org.w3c.dom.Node#normalize()} doesn't merge adjacent CDATA sections.
     */
    @Validated @Test
    public void testNormalizeAdjacentCDATASections() {
        Document document = domUtil.newDocument();
        Element element = document.createElement("test");
        element.appendChild(document.createCDATASection("te"));
        element.appendChild(document.createCDATASection("st"));
        element.normalize();
        NodeList children = element.getChildNodes();
        Assert.assertEquals(2, children.getLength());
    }
}
