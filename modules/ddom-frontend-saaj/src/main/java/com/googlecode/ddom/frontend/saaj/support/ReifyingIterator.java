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

import java.util.Iterator;

import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;

/**
 * Iterator wrapper that silently replaces generic {@link org.w3c.dom.Element} instances by new
 * instances that implement a given extension interface.
 * 
 * @author Andreas Veithen
 */
public class ReifyingIterator implements Iterator<CoreChildNode> {
    private final ChildIterator<?> parent;
    private final Class<? extends SAAJSOAPElement> childType;

    public ReifyingIterator(ChildIterator<?> parent, Class<? extends SAAJSOAPElement> childType) {
        this.parent = parent;
        this.childType = childType;
    }

    public boolean hasNext() {
        return parent.hasNext();
    }

    public CoreChildNode next() {
        try {
            CoreChildNode child = (CoreChildNode)parent.next();
            if (child instanceof CoreNSAwareElement) {
                CoreNSAwareElement element = (CoreNSAwareElement)child;
                SAAJSOAPElement reifiedElement = SAAJUtil.reify(element, childType);
                if (reifiedElement != element) {
                    parent.replace(reifiedElement);
                }
                return reifiedElement;
            } else {
                return child;
            }
        } catch (CoreModelException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    public void remove() {
        // TODO: is remove allowed/required by SAAJ?
        parent.remove();
    }
}