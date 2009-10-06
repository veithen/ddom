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
package com.google.code.ddom.utils.dom.iterator;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;

public class DescendantsIteratorTest {
    @Test
    public void testAllNodes() throws Exception {
        Document doc = DOMTestUtil.toDOM("<root><!-- comment --><element/></root>");
        Iterator<Node> it = new DescendantsIterator<Node>(Node.class, doc);
        
        Assert.assertTrue(it.hasNext());
        Node node = it.next();
        Assert.assertTrue(node instanceof Element);
        Assert.assertEquals("root", node.getLocalName());
        
        Assert.assertTrue(it.hasNext());
        node = it.next();
        Assert.assertTrue(node instanceof Comment);
        
        Assert.assertTrue(it.hasNext());
        node = it.next();
        Assert.assertTrue(node instanceof Element);
        Assert.assertEquals("element", node.getLocalName());
        
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testElements() throws Exception {
        Document doc = DOMTestUtil.toDOM("<root><!-- comment --><element/></root>");
        Iterator<Element> it = new DescendantsIterator<Element>(Element.class, doc);
        
        Assert.assertTrue(it.hasNext());
        Element element = it.next();
        Assert.assertEquals("root", element.getLocalName());
        
        Assert.assertTrue(it.hasNext());
        element = it.next();
        Assert.assertEquals("element", element.getLocalName());
        
        Assert.assertFalse(it.hasNext());
    }
}
