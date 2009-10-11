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
package com.google.code.ddom.dom.impl;

/**
 * Aspect implementing {@link org.w3c.dom.Node#normalize()} and
 * {@link org.w3c.dom.Document#normalizeDocument()}.
 * 
 * @author Andreas Veithen
 */
public aspect Normalization {
    public final void NodeImpl.normalize() {
        // TODO Auto-generated method stub
    }
    
    public final void DocumentImpl.normalizeDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
