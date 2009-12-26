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
package com.google.code.ddom.frontend.axiom.aspects;

import javax.xml.namespace.QName;

import com.google.code.ddom.backend.CoreNSAwareNamedNode;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.frontend.axiom.intf.CoreNSAwareNamedNodeWithQNameCaching;

// TODO: move this to some common aspect module (since it is not strictly related to Axiom)
public aspect QNameCaching {
    declare parents: @Implementation CoreNSAwareNamedNode+ implements CoreNSAwareNamedNodeWithQNameCaching;
    
    private QName CoreNSAwareNamedNodeWithQNameCaching.qname;
    
    QName around(CoreNSAwareNamedNodeWithQNameCaching attr): execution(QName CoreNSAwareNamedNodeWithQNameCaching.coreGetQName()) && this(attr) {
        if (attr.qname == null) {
            attr.qname = proceed(attr);
        }
        return attr.qname;
    }
    
    // TODO: need to reset cached QName when namespace, local name or prefix changes
}
