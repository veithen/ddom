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

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.intf.SAAJNode;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;

@Mixin(CoreChildNode.class)
public abstract class NodeSupport implements SAAJNode {
    public String getValue() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void setValue(String value) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setParentElement(SOAPElement parent) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement getParentElement() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void detachNode() {
        // TODO: need specific unit test here; current coverage is only provided by unit tests that don't validate the behavior of detachNode
        try {
            coreDetach();
        } catch (CoreModelException ex) {
            SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public void recycleNode() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
