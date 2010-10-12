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

import java.util.Iterator;

import com.google.code.ddom.core.ChildIterator;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;

/**
 * Iterator wrapper that silently replaces generic {@link org.w3c.dom.Element} instances by new
 * instances that implement a given extension interface.
 * 
 * @author Andreas Veithen
 */
public class ReifyingIterator<T> implements Iterator<T> {
    private final ChildIterator<CoreNSAwareElement> parent;
    private final Class<?> extensionInterface;
    private final Class<? extends T> type;

    public ReifyingIterator(ChildIterator<CoreNSAwareElement> parent, Class<?> extensionInterface, Class<? extends T> type) {
        this.parent = parent;
        this.extensionInterface = extensionInterface;
        this.type = type;
    }

    public boolean hasNext() {
        return parent.hasNext();
    }

    public T next() {
        CoreNSAwareElement element = parent.next();
        if (type.isInstance(element)) {
            return type.cast(element);
        } else {
            try {
                SAAJSOAPElement newElement = SAAJUtil.reify(element, extensionInterface);
                parent.replace(newElement);
                return type.cast(newElement);
            } catch (CoreModelException ex) {
                throw new RuntimeException(ex); // TODO
            }
       }
    }

    public void remove() {
        // TODO: is remove allowed/required by SAAJ?
        parent.remove();
    }
}