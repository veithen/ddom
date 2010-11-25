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
package com.google.code.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMFactory;

import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomInformationItem;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;

@Mixin(CoreNode.class)
public abstract class InformationItemSupport implements AxiomInformationItem {
    private OMFactory omFactory;
    
    public final OMFactory getOMFactory() {
//        return (OMFactory)coreGetOwnerDocument(true);
        return omFactory != null ? omFactory : getDefaultOMFactory();
    }

    // This method may be overridden by other mixins
    public OMFactory getDefaultOMFactory() {
        return ((AxiomNodeFactory)coreGetNodeFactory()).getOMFactory();
    }

    public final void setOMFactory(OMFactory omFactory) {
        this.omFactory = omFactory;
    }
}
