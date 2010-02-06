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
package com.google.code.ddom.backend;

import java.util.Iterator;

import com.google.code.ddom.stream.spi.FragmentSource;

public interface CoreParentNode extends CoreNode {
    /**
     * Determine if this parent node is complete.
     * 
     * @return <code>true</code> if the node is complete, <code>false</code> otherwise
     */
    boolean coreIsComplete();
    
    void coreBuild() throws DeferredParsingException;
    
    /**
     * 
     * either a String or a ChildNode
     * 
     * @return
     */
    Object coreGetContent();
    
    void coreSetContent(FragmentSource source);
    
    String coreGetValue() throws DeferredParsingException;
    
    /**
     * Set the content of this node to the given value. This will remove all children previously
     * owned by this element.
     * 
     * @param value
     *            the value to set
     */
    void coreSetValue(String value);
    
    // TODO: specify behavior if the element neither has children nor a value
    boolean coreIsExpanded();
    
    CoreChildNode coreGetFirstChild() throws DeferredParsingException;
    CoreChildNode coreGetLastChild() throws DeferredParsingException;
    int coreGetChildCount() throws DeferredParsingException;
    
    /**
     * Append a new child to the list of children of this parent node. If the node to be added
     * already has a parent node, it is first removed from this parent.
     * 
     * @param newChild
     *            the new child; this may either be a {@link CoreChildNode} or a
     *            {@link CoreDocumentFragment}
     * @throws CoreModelException
     *             TODO: specify the exceptions
     */
    void coreAppendChild(CoreNode newChild) throws CoreModelException;
    
    /**
     * @deprecated Use {@link CoreChildNode#coreInsertSiblingAfter(CoreChildNode)}
     */
    void coreInsertChildAfter(CoreNode newChild, CoreChildNode refChild) throws CoreModelException;
    
    /**
     * @deprecated Use {@link CoreChildNode#coreInsertSiblingBefore(CoreChildNode)}
     */
    void coreInsertChildBefore(CoreNode newChild, CoreChildNode refChild) throws CoreModelException;
    
    // TODO: need a more object oriented replacement for this
    void coreReplaceChild(CoreNode newChild, CoreChildNode oldChild) throws CoreModelException;

    <T extends CoreChildNode> Iterator<T> coreGetChildrenByType(Axis axis, Class<T> type);
    Iterator<CoreNSAwareElement> coreGetElementsByName(Axis axis, String namespaceURI, String localName);
    Iterator<CoreNSAwareElement> coreGetElementsByNamespace(Axis axis, String namespaceURI);
    Iterator<CoreNSAwareElement> coreGetElementsByLocalName(Axis axis, String localName);
}
