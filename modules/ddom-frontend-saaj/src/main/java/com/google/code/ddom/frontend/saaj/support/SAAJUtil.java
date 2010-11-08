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
package com.google.code.ddom.frontend.saaj.support;

import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;

public final class SAAJUtil {
    private SAAJUtil() {}
    
    public static SAAJSOAPElement reify(CoreNSAwareElement element, Class<?> extensionInterface) throws CoreModelException {
        if (extensionInterface == null || extensionInterface.isInstance(element)) {
            return (SAAJSOAPElement)element;
        } else {
            CoreNSAwareElement newElement = element.coreGetNodeFactory().createElement(
                    element.coreGetOwnerDocument(true), extensionInterface, element.coreGetNamespaceURI(),
                    element.coreGetLocalName(), element.coreGetPrefix());
            // TODO: maybe there is a more efficient way to do this
            CoreChildNode child;
            while ((child = element.coreGetFirstChild()) != null) {
                newElement.coreAppendChild(child);
            }
            // TODO: copy over the attributes of the original element
//            CoreAttribute attr;
//            while ((attr = element.coreGetFirstAttribute()) != null) {
//                newElement.core
//            }
            return (SAAJSOAPElement)newElement;
        }
    }
}
