/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.core;

import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

/**
 * A node that acts as a container for {@link CoreChildNode} objects. With the exception of
 * {@link CoreDocumentFragment}, an instance of this interface represents a parent information item
 * in the Logical Content Model.
 * 
 * @author Andreas Veithen
 */
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
    
    void coreSetContent(XmlSource source);
    
    /**
     * Determine if the content of this node is represented in compact form. This is the case if
     * both of the following conditions are met:
     * <ol>
     * <li>The node is currently not expanded, i.e. its child nodes have not been created yet.
     * <li>After expansion, the node would have a single child of type {@link CoreCharacterData}.
     * </ol>
     * Note that this method will always return <code>false</code> if {@link #coreIsEmpty()} returns
     * <code>true</code>.
     * 
     * @return <code>true</code> if the node has a value; <code>false</code> otherwise
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing. This may happen because the method
     *             needs to check if in the underlying stream, the node has a single child
     *             information item of type text.
     */
    boolean coreHasValue() throws DeferredParsingException;
    
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
     * Remove all children from this node. Child nodes that have not been materialized yet will be
     * discarded. This may occur during the execution of the method or at a later time. Note that
     * this has no visible consequences because there is no way to navigate to these nodes after the
     * invocation of this method.
     * 
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing. Note that if the implementation uses
     *             a builder of type 2, no events will be requested from the underlying parser and
     *             no exception of this type will ever be thrown.
     */
    void coreClear() throws DeferredParsingException;
    
    /**
     * Collect the text content from this node. Since different APIs define different rules to get
     * the text content of a parent node, a {@link TextCollectorPolicy} object is used to define
     * these rules.
     * 
     * @param policy
     *            the policy to use when collecting the text content; must not be <code>null</code>
     * @return the text content of the node; if the node has no text content, then an empty string
     *         is returned
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing.
     */
    String coreGetTextContent(TextCollectorPolicy policy) throws DeferredParsingException;
    
    // TODO: specify behavior if the element neither has children nor a value
    // TODO: maybe this is no longer required since we have coreHasValue
    boolean coreIsExpanded();
    
    CoreChildNode coreGetFirstChild() throws DeferredParsingException;
    CoreChildNode coreGetLastChild() throws DeferredParsingException;
    
    /**
     * Determine if this node has any children. In contrast to {@link #coreGetChildCount()}, this
     * method will not necessarily build the entire node.
     * 
     * @return <code>true</code> if this node doesn't have any children; <code>false</code> if this
     *         node has at least one child or if it is unexpanded and a value is set
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing.
     */
    boolean coreIsEmpty() throws DeferredParsingException;
    
    // TODO: specify behavior if node is unexpanded
    int coreGetChildCount() throws DeferredParsingException;

    /**
     * Get an {@link XmlInput} object for this node. This object can be used to serialize the
     * content of the node.
     * <p>
     * Structurally modifying the tree while the {@link XmlInput} object is in use will lead to
     * unpredictable results and should be avoided.
     * <p>
     * A single invocation of the {@link XmlInput#proceed()} method on the returned {@link XmlInput}
     * object will in general produce a limited number of events (typically only events
     * corresponding to information contained in a single node). However, this is not a strict
     * requirement and there are at least two exceptions to this rule:
     * <ul>
     * <li>If <code>preserve</code> is set to <code>false</code>, then at some point the returned
     * {@link XmlInput} implementation may start to simply pass through events from another
     * {@link XmlInput} implementation. In that case, the number of events produced by a single
     * invocation of {@link XmlInput#proceed()} is determined by the original {@link XmlInput}
     * instance.
     * <li>If one of the descendants of this node is unexpanded and has a {@link XmlSource}
     * that supports non destructive serialization, then instead of expanding that node and
     * synthesizing events from the expanded node, the returned {@link XmlInput} implementation will
     * request an {@link XmlInput} instance from the {@link XmlSource} and pass through events
     * generated by that instance. This occurs even if <code>preserve</code> is set to
     * <code>true</code>. Indeed, since the {@link XmlSource} is non destructive, it is still
     * possible to expand the node afterwards. On the other hand, if the {@link XmlSource} is
     * destructive, then this will only occur if <code>preserve</code> is <code>false</code> (but
     * this case is already covered by the first item).
     * </ul>
     * <p>
     * The event sequence produced by the returned {@link XmlInput} implementation is a literal
     * transformation of the information items in the subtree starting from the node on which this
     * method is invoked. In particular this means that the event sequence is not necessarily well
     * formed with respect to namespaces. Therefore, depending on the use case, it may be necessary
     * to transform this event sequence using a namespace repairing filter.
     * 
     * @param preserve
     *            Determines whether the content (descendants) of this node should be preserved. The
     *            value of this flag is only relevant if this node or one of its descendants is
     *            incomplete. If the flag is set to <code>true</code>, then consuming the
     *            {@link XmlInput} will build the remaining nodes as necessary. Otherwise, the nodes
     *            will not be built and an attempt to access these parts of the tree afterwards will
     *            result in an error.
     * @return the {@link XmlInput} object
     */
    XmlInput coreGetInput(boolean preserve);
    
    /**
     * Append a new child to the list of children of this parent node. If the node to be added
     * already has a parent node, it is first removed from this parent.
     * 
     * @param newChild
     *            the new child
     * @param policy
     *            the policy to apply if the new child already has a parent or belongs to a
     *            different document
     * @throws CoreModelException
     *             TODO: specify the exceptions
     * @throws WrongDocumentException
     *             if <code>newChild</code> belongs to a different document
     */
    void coreAppendChild(CoreChildNode newChild, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException;
    
    /**
     * 
     * @param newChildren
     * @throws CoreModelException
     * @throws WrongDocumentException
     *             if <code>newChildren</code> belongs to a different document
     */
    void coreAppendChildren(CoreDocumentFragment newChildren) throws CoreModelException;
    
    CoreDocumentTypeDeclaration coreAppendDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws ChildNotAllowedException, DeferredParsingException;
    
    /**
     * Create a namespace unaware element and append it to this parent node. For the precise
     * semantics of the parameters, please refer to the corresponding method in {@link NodeFactory}.
     * 
     * @param tagName
     *            the name of the element
     * @return the newly created node
     * @throws ChildNotAllowedException
     *             If an attempt is made to insert a child node where it is not allowed.
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing.
     * 
     * @see NodeFactory#createElement(CoreDocument, String)
     * @see #coreAppendChild(CoreChildNode, NodeMigrationPolicy)
     */
    CoreNSUnawareElement coreAppendElement(String tagName) throws ChildNotAllowedException, DeferredParsingException;

    /**
     * Create a namespace aware element and append it to this parent node. For the precise semantics
     * of the parameters, please refer to the corresponding method in {@link NodeFactory}.
     * 
     * @param namespaceURI
     *            the namespace URI of the element
     * @param localName
     *            the local part of the element's name
     * @param prefix
     *            the prefix of the element
     * @return the newly created node
     * @throws ChildNotAllowedException
     *             If an attempt is made to insert a child node where it is not allowed.
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing.
     * 
     * @see NodeFactory#createElement(CoreDocument, String, String, String)
     * @see #coreAppendChild(CoreChildNode, NodeMigrationPolicy)
     */
    CoreNSAwareElement coreAppendElement(String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException;
    
    <T extends CoreNSAwareElement> T coreAppendElement(Class<T> extensionInterface, String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreProcessingInstruction coreAppendProcessingInstruction(String target, String data) throws ChildNotAllowedException, DeferredParsingException;
    
    CoreCharacterData coreAppendCharacterData(String data) throws ChildNotAllowedException, DeferredParsingException;

    CoreComment coreAppendComment(String data) throws ChildNotAllowedException, DeferredParsingException;

    CoreCDATASection coreAppendCDATASection() throws ChildNotAllowedException, DeferredParsingException;

    CoreEntityReference coreAppendEntityReference(String name) throws ChildNotAllowedException, DeferredParsingException;
    
    <T extends CoreChildNode> ChildIterator<T> coreGetChildrenByType(Axis axis, Class<T> type);
    
    <T extends CoreElement> ChildIterator<T> coreGetElements(Axis axis, Class<T> type, ElementMatcher<? super T> matcher, String namespaceURI, String name);
    
    <T extends CoreChildNode> T coreGetFirstChildByType(Class<T> type) throws DeferredParsingException;
}
