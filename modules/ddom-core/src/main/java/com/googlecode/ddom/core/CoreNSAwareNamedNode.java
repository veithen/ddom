/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import javax.xml.namespace.QName;

/**
 * Represents a namespace aware named information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreNSAwareNamedNode extends CoreNode {
    /**
     * Get the namespace URI of this node. If the node is an element whose namespace URI was left
     * unspecified during construction, then the namespace URI will be determined by processing the
     * source object set with
     * {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * 
     * @return The namespace URI of the node, or the empty string if the node has no namespace.
     * @throws DeferredBuildingException
     *             If an error occurs while processing the source object of this node set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * @throws IllegalStateException
     *             If the namespace URI of the node has not been specified during construction and
     *             no source object has been set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     */
    String coreGetNamespaceURI() throws DeferredBuildingException;
    
    void coreSetNamespaceURI(String namespaceURI);
    
    /**
     * Get the namespace prefix of this node. If the node is an element whose prefix was left
     * unspecified during construction, then the prefix will be determined by processing the source
     * object set with {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * 
     * @return The namespace prefix, or the empty string if the node has no prefix, i.e. if the node
     *         has no namespace or is in the default namespace.
     * @throws DeferredBuildingException
     *             If an error occurs while processing the source object of this node set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * @throws IllegalStateException
     *             If the prefix of the node has not been specified during construction and no
     *             source object has been set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     */
    String coreGetPrefix() throws DeferredBuildingException;
    
    void coreSetPrefix(String prefix);

    /**
     * Get the local part of the node name. If the node is an element whose local name was left
     * unspecified during construction, then the local name will be determined by processing the
     * source object set with
     * {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * 
     * @return The local part of the node name. The return value is never <code>null</code>.
     * @throws DeferredBuildingException
     *             If an error occurs while processing the source object of this node set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * @throws IllegalStateException
     *             If the local part of the node name has not been specified during construction and
     *             no source object has been set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     */
    String coreGetLocalName() throws DeferredBuildingException;
    
    void coreSetLocalName(String localName);
    
    QName coreGetQName() throws DeferredBuildingException;
}
