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
package com.google.code.ddom.frontend.saaj.mixin;

import com.google.code.ddom.core.CoreNSAwareAttribute;
import com.google.code.ddom.frontend.saaj.intf.SAAJNSAwareAttribute;
import com.google.code.ddom.spi.model.Mixin;

/**
 * Mixin that implements the {@link Name} interface on namespace aware attributes. This is used to
 * optimize the implementation of {@link javax.xml.soap.SOAPElement#getAllAttributes()}. Note that
 * {@link Name#getPrefix()} and {@link Name#getLocalName()} are already implemented by the DOM
 * frontend.
 * 
 * @author Andreas Veithen
 */
@Mixin(CoreNSAwareAttribute.class)
public abstract class NSAwareAttributeSupport implements SAAJNSAwareAttribute {
    public String getQualifiedName() {
        String prefix = coreGetPrefix();
        String localName = coreGetLocalName();
        return prefix == null ? localName : prefix + ":" + localName;
    }
    
    public String getURI() {
        return coreGetNamespaceURI();
    }
}
