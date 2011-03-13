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

/**
 * Represents an arbitrary sequence of information items. This type of node doesn't represent any
 * information item defined by the Logical Content Model. Instances of this interface are used for
 * two purposes:
 * <ol>
 * <li>As an intermediary receptacle when manipulating existing object model trees. A document
 * fragment can be used to move collections of nodes from one location to another one in the tree.
 * <li>As a representation of an XML fragment, i.e. a document that is not a well-formed XML
 * document, but that contains an arbitrary sequence of information items.
 * </ol>
 * 
 * @author Andreas Veithen
 */
public interface CoreDocumentFragment extends CoreParentNode {
    /**
     * Set a new owner document for this document fragment.
     * 
     * @param document
     *            the new owner document, or <code>null</code> if the document fragment will have
     *            it's own owner document (which may be created lazily later)
     */
    void coreSetOwnerDocument(CoreDocument document);
}
