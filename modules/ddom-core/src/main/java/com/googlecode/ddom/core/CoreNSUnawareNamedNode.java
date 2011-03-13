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
 * Represents a namespace unaware named information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreNSUnawareNamedNode extends CoreNode {
    /**
     * Get the name of this node. If the node is an element whose name was left unspecified during
     * construction, then the name will be determined by processing the source object set with
     * {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * 
     * @return the name of the node
     * @throws DeferredParsingException
     *             If an error occurs while processing the source object of this node set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     * @throws IllegalStateException
     *             If the name of the node has not been specified during construction and no source
     *             object has been set with
     *             {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)}.
     */
    String coreGetName() throws DeferredParsingException;
}
