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
package com.google.code.ddom.core;

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
    
    // Temporarily marked as deprecated. In most cases coreGetTextContent should be used.
    // However, this method may later be changed to return objects (e.g. QNames).
    @Deprecated
    String coreGetValue() throws DeferredParsingException;
    
    /**
     * Set the content of this node to the given value. This will remove all children previously
     * owned by this element.
     * 
     * @param value
     *            the value to set
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing. This may happen because the method
     *             needs to discard all existing children of the node and this may require the
     *             parser to be advanced to the end of this node.
     */
    void coreSetValue(String value) throws DeferredParsingException;
    
    /**
     * Remove all children from this node.
     * 
     * @throws DeferredParsingException 
     */
    void coreClear() throws DeferredParsingException;
    
    String coreGetTextContent() throws DeferredParsingException;
    
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
     * @throws WrongDocumentException
     *             if <code>newChild</code> belongs to a different document
     */
    void coreAppendChild(CoreChildNode newChild) throws HierarchyException, NodeMigrationException, DeferredParsingException;
    
    /**
     * 
     * @param newChildren
     * @throws CoreModelException
     * @throws WrongDocumentException
     *             if <code>newChildren</code> belongs to a different document
     */
    void coreAppendChildren(CoreDocumentFragment newChildren) throws CoreModelException;
    
    CoreDocumentTypeDeclaration coreAppendDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreNSUnawareElement coreAppendElement(String tagName) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreNSAwareElement coreAppendElement(String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreNSAwareElement coreAppendElement(Class<?> extensionInterface, String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreProcessingInstruction coreAppendProcessingInstruction(String target, String data) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreText coreAppendText(String data) throws ChildNotAllowedException, DeferredParsingException;

    CoreComment coreAppendComment(String data) throws ChildNotAllowedException, DeferredParsingException;

    CoreCDATASection coreAppendCDATASection(String data) throws ChildNotAllowedException, DeferredParsingException;

    CoreEntityReference coreAppendEntityReference(String name) throws ChildNotAllowedException, DeferredParsingException;
    
    <T extends CoreChildNode> ChildIterator<T> coreGetChildrenByType(Axis axis, Class<T> type);
    ChildIterator<CoreNSAwareElement> coreGetElementsByName(Axis axis, String namespaceURI, String localName);
    ChildIterator<CoreNSAwareElement> coreGetElementsByNamespace(Axis axis, String namespaceURI);
    ChildIterator<CoreNSAwareElement> coreGetElementsByLocalName(Axis axis, String localName);
    
    <T extends CoreChildNode> T coreGetFirstChildByType(Class<T> type) throws DeferredParsingException;
}
