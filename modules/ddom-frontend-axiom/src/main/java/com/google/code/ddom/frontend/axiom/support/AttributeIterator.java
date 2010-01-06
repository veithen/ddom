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
package com.google.code.ddom.frontend.axiom.support;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;

// TODO: replace this by something from the core model
public class AttributeIterator implements Iterator {
    private final AxiomElement element;
    private AxiomAttribute current;
    
    public AttributeIterator(AxiomElement element) {
        this.element = element;
    }

    public boolean hasNext() {
        return current == null && element.coreGetFirstAttribute() != null
                || current != null && current.coreGetNextAttribute() != null;
    }

    public Object next() {
        if (current == null) {
            current = (AxiomAttribute)element.coreGetFirstAttribute();
        } else {
            current = (AxiomAttribute)current.coreGetNextAttribute();
        }
        if (current == null) {
            throw new NoSuchElementException();
        } else {
            return current;
        }
    }

    public void remove() {
        // TODO: check if this is supported by Axiom
    }
}
