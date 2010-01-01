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
package com.google.code.ddom.xmlts;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLConformanceTestUtils {
    private XMLConformanceTestUtils() {}
    
    /**
     * Remove <tt>xml:base</tt> attributes from the given node and all of its
     * descendants. According to section 1.3.4 of the DOM Level 3 Core
     * specification, when expanding an external entity reference, DOM parser
     * will add <tt>xml:base</tt> attributes to all elements appearing in the
     * external entity. These attributes don't appear in the reference output
     * files provided in the test suite (see
     * {@link XMLConformanceTest#getOutput()}). This method can be used to
     * remove <tt>xml:base</tt> attributes in DOM documents.
     * 
     * @param node
     *            the node to process
     */
    public static void removeXmlBaseAttributes(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element)node;
            element.removeAttribute("xml:base");
        }
        Node child = node.getFirstChild();
        while (child != null) {
            removeXmlBaseAttributes(child);
            child = child.getNextSibling();
        }
    }
}
