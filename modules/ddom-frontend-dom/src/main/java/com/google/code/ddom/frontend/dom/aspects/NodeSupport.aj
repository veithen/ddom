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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect NodeSupport {
    public final boolean DOMNode.isSupported(String feature, String version) {
        return ((DOMDocument)getDocument()).getImplementation().hasFeature(feature, version);
    }

    public final Object DOMNode.getFeature(String feature, String version) {
        return this;
    }

    public final boolean DOMNode.isSameNode(Node other) {
        return other == this;
    }

    public final boolean DOMNode.isEqualNode(Node arg) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DOMNode.getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short DOMNode.compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
