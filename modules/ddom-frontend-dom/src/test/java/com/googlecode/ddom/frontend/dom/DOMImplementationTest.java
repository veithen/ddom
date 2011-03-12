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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

/**
 * @author Andreas Veithen
 */
@RunWith(ValidatedTestRunner.class)
public class DOMImplementationTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;

    @Validated @Test
    public void testCreateDocumentFromDocumentType() {
        DOMImplementation domImpl = domUtil.getDOMImplementation();
        DocumentType doctype = domImpl.createDocumentType("root", null, null);
        Document doc = domImpl.createDocument(null, "root", doctype);
        Assert.assertSame(doc, doctype.getOwnerDocument());
        Assert.assertSame(doc, doctype.getParentNode());
    }
}
