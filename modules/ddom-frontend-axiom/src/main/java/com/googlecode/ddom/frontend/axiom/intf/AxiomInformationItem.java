/*
 * Copyright 2009,2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.intf;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMInformationItem;

import com.googlecode.ddom.core.CoreNode;

public interface AxiomInformationItem extends CoreNode, OMInformationItem {
    /**
     * Get the default {@link OMFactory} for this information item. This instance will be returned
     * by {@link #getOMFactory()} if none has been set explicitly using
     * {@link #setOMFactory(OMFactory)}. This occurs if the node has been created using an API other
     * than Axiom.
     * 
     * @return the default factory
     */
    OMFactory getDefaultOMFactory();
    
    /**
     * Set the {@link OMFactory} instance for this information item. In Axiom, two nodes belonging
     * to the same tree don't necessarily have the same factory. E.g. it is possible to add a node
     * created by a pure XML factory to a node created by a SOAP factory. Therefore, the factory is
     * really a property of the individual node.
     * 
     * @param omFactory
     */
    void setOMFactory(OMFactory omFactory);
}
