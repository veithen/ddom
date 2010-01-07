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
package com.google.code.ddom.frontend.saaj.aspects;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.saaj.intf.SAAJNode;

public aspect NodeSupport {
    public String SAAJNode.getValue() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void SAAJNode.setValue(String value) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void SAAJNode.setParentElement(SOAPElement parent) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJNode.getParentElement() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void SAAJNode.detachNode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void SAAJNode.recycleNode() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
