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
package com.googlecode.ddom.util.namespace;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;

public final class ScopedNamespaceContext extends AbstractNamespaceContext {
    private String[] namespaceStack = new String[32];
    private int bindings;
    private int[] scopeStack = new int[8];
    private int scopes;

    public boolean isBound(String prefix, String namespaceURI) {
        if (prefix.equals(XMLConstants.XML_NS_PREFIX) && namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return true;
        } else {
            for (int i=(bindings-1)*2; i>=0; i-=2) {
                if (prefix.equals(namespaceStack[i])) {
                    return namespaceURI.equals(namespaceStack[i+1]);
                }
            }
            return prefix.length() == 0 && namespaceURI.length() == 0;
        }
    }
    
    public void setPrefix(String prefix, String namespaceURI) {
        if (bindings*2 == namespaceStack.length) {
            int len = namespaceStack.length;
            String[] newNamespaceStack = new String[len*2];
            System.arraycopy(namespaceStack, 0, newNamespaceStack, 0, len);
        }
        namespaceStack[bindings*2] = prefix;
        namespaceStack[bindings*2+1] = namespaceURI;
        bindings++;
    }
    
    public void startScope() {
        if (scopes == scopeStack.length) {
            int[] newScopeStack = new int[scopeStack.length*2];
            System.arraycopy(scopeStack, 0, newScopeStack, 0, scopeStack.length);
            scopeStack = newScopeStack;
        }
        scopeStack[scopes++] = bindings;
    }
    
    public void endScope() {
        bindings = scopeStack[--scopes];
    }
    
    /**
     * Get the number of namespace bindings defined in this context, in all scopes. This number
     * increases every time {@link #setPrefix(String, String)} is called.
     * 
     * @return the namespace binding count
     */
    public int getBindingsCount() {
        return bindings;
    }

    /**
     * Get the index of the first namespace binding defined in the current scope. Together with
     * {@link #getBindingsCount()} this method can be used to iterate over the namespace bindings
     * defined in the current scope.
     * 
     * @return the index of the first namespace binding defined in the current scope
     */
    public int getFirstBindingInCurrentScope() {
        return scopeStack[scopes-1];
    }
    
    public String getPrefix(int i) {
        return namespaceStack[i*2];
    }
    
    public String getNamespaceURI(int i) {
        return namespaceStack[i*2+1];
    }

    @Override
    protected String doGetPrefix(String namespaceURI) {
        outer: for (int i=(bindings-1)*2; i>=0; i-=2) {
            if (namespaceURI.equals(namespaceStack[i+1])) {
                String prefix = namespaceStack[i];
                // Now check that the prefix is not masked
                for (int j=i+2; j<bindings*2; j+=2) {
                    if (prefix.equals(namespaceStack[j])) {
                        continue outer;
                    }
                }
                return prefix;
            }
        }
        return null;
    }

    @Override
    protected Iterator<String> doGetPrefixes(final String namespaceURI) {
        return new Iterator<String>() {
            private int binding = bindings;
            private String next;

            public boolean hasNext() {
                if (next == null) {
                    outer: while (--binding >= 0) {
                        if (namespaceURI.equals(namespaceStack[binding*2+1])) {
                            String prefix = namespaceStack[binding*2];
                            // Now check that the prefix is not masked
                            for (int j=binding+1; j<bindings; j++) {
                                if (prefix.equals(namespaceStack[j*2])) {
                                    continue outer;
                                }
                            }
                            next = prefix;
                            break;
                        }
                    }
                }
                return next != null;
            }

            public String next() {
                if (hasNext()) {
                    String result = next;
                    next = null;
                    return result;
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    protected String doGetNamespaceURI(String prefix) {
        for (int i=(bindings-1)*2; i>=0; i-=2) {
            if (prefix.equals(namespaceStack[i])) {
                return namespaceStack[i+1];
            }
        }
        return XMLConstants.NULL_NS_URI;
    }
}
