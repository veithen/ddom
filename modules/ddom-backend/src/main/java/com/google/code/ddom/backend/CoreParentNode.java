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

public interface CoreParentNode extends CoreNode {
    void internalSetFirstChild(CoreChildNode child);
    void notifyChildrenModified(int delta);
    CoreChildNode coreGetFirstChild();
    CoreChildNode coreGetLastChild();
    int coreGetChildCount();
    
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
     * 
     * @param newChild
     * @param refChild
     * @throws SelfRelationshipException if <code>newChild</code> and <code>refChild</code> are the same
     */
    void coreInsertChildAfter(CoreNode newChild, CoreChildNode refChild) throws CoreModelException;
    
    /**
     * 
     * @param newChild
     * @param refChild
     * @throws SelfRelationshipException if <code>newChild</code> and <code>refChild</code> are the same
     */
    void coreInsertChildBefore(CoreNode newChild, CoreChildNode refChild) throws CoreModelException;
    
    void coreRemoveChild(CoreChildNode child) throws CoreModelException;
    void coreReplaceChild(CoreNode newChild, CoreChildNode oldChild) throws CoreModelException;
    
    Iterator<CoreNSAwareElement> coreGetElementsByName(Axis axis, String namespaceURI, String localName);
}
