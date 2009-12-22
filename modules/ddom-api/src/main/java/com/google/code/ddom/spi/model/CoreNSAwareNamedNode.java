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
package com.google.code.ddom.spi.model;

import javax.xml.namespace.QName;

public interface CoreNSAwareNamedNode extends CoreNode {
    /**
     * Get the namespace URI of this node.
     * 
     * @return the namespace URI, or <code>null</code> if the node has no namespace
     */
    String coreGetNamespaceURI();
    
    /**
     * Get the namespace prefix of this node.
     * 
     * @return the namespace prefix, or <code>null</code> if the node has no prefix
     */
    String coreGetPrefix();
    
    void coreSetPrefix(String prefix);
    
    String coreGetLocalName();
    
    void coreSetLocalName(String localName);
    
    QName coreGetQName();
}
