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
package com.googlecode.ddom.frontend.axiom.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.CoreParentNode;

/**
 * Base class for iterators that iterate over the namespaces in scope for a given element.
 */
public abstract class AbstractNamespaceIterator<T> implements Iterator<T> {
    private final Set<String> seenPrefixes = new HashSet<String>();
    private CoreElement element;
    private boolean hasNextCalled;
    private CoreNamespaceDeclaration nsDeclaration;

    public AbstractNamespaceIterator(CoreElement element) {
        this.element = element;
    }

    public final boolean hasNext() {
        if (!hasNextCalled) {
            try {
                CoreAttribute attribute = nsDeclaration;
                while (true) {
                    if (attribute == null) {
                        attribute = element.coreGetFirstAttribute();
                    } else {
                        attribute = attribute.coreGetNextAttribute();
                    }
                    if (attribute == null) {
                        CoreParentNode parent = element.coreGetParent();
                        if (parent instanceof CoreElement) {
                            element = (CoreElement)parent;
                        } else {
                            nsDeclaration = null;
                            break;
                        }
                    } else if (attribute instanceof CoreNamespaceDeclaration) {
                        CoreNamespaceDeclaration candidate = (CoreNamespaceDeclaration)attribute;
                        // We only return a namespace declaration if it has not been overridden (i.e. if
                        // we have not seen another declaration with the same prefix yet) and if the namespace
                        // URI is not empty. The second part of the condition covers the case of namespace
                        // declarations such as xmlns="" (for which no OMNamespace object is returned, as
                        // described in the Javadoc of the getNamespacesInScope method) as well as undeclared
                        // prefixes (XML 1.1 only).
                        if (seenPrefixes.add(candidate.coreGetDeclaredPrefix())
                                && candidate.coreGetDeclaredNamespaceURI().length() > 0
                                && matches(candidate)) {
                            nsDeclaration = candidate;
                            break;
                        }
                    }
                }
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
            hasNextCalled = true;
        }
        return nsDeclaration != null;
    }

    protected abstract boolean matches(CoreNamespaceDeclaration nsDeclaration);
    
    public final T next() {
        if (hasNext()) {
            hasNextCalled = false;
            return getValue(nsDeclaration);
        } else {
            throw new NoSuchElementException();
        }
    }
    
    protected abstract T getValue(CoreNamespaceDeclaration nsDeclaration);

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}