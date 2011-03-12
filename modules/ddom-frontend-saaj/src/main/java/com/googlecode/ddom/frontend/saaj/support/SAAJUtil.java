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
package com.googlecode.ddom.frontend.saaj.support;

import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.dom.support.Policies;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;

public final class SAAJUtil {
    private SAAJUtil() {}
    
    public static <T extends SAAJSOAPElement> T reify(CoreNSAwareElement element, Class<T> childType) throws CoreModelException {
        if (childType.isInstance(element)) {
            return childType.cast(element);
        } else {
            T newElement = element.coreGetNodeFactory().createElement(
                    element.coreGetOwnerDocument(true), childType, element.coreGetNamespaceURI(),
                    element.coreGetLocalName(), element.coreGetPrefix());
            // TODO: maybe there is a more efficient way to do this
            CoreChildNode child;
            while ((child = element.coreGetFirstChild()) != null) {
                newElement.coreAppendChild(child, Policies.NODE_MIGRATION_POLICY);
            }
            // TODO: copy over the attributes of the original element
//            CoreAttribute attr;
//            while ((attr = element.coreGetFirstAttribute()) != null) {
//                newElement.core
//            }
            return newElement;
        }
    }
}
