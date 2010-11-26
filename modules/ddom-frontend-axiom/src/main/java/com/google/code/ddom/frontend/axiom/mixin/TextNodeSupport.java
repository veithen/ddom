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
package com.google.code.ddom.frontend.axiom.mixin;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.util.stax.XMLStreamWriterUtils;

import com.google.code.ddom.core.CoreTextNode;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomTextNode;

@Mixin(CoreTextNode.class)
public abstract class TextNodeSupport implements AxiomTextNode {
    public String getText() {
        return coreGetData();
    }
    
    public char[] getTextCharacters() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean isCharacters() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName getTextAsQName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace getNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public DataHandler getDataHandler() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean isOptimized() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void setOptimize(boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean isBinary() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void setBinary(boolean binary) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String getContentID() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void setContentID(String contentID) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void internalSerialize(XMLStreamWriter writer, boolean cache) throws XMLStreamException {
        // TODO
        writer.writeCharacters(coreGetData());
    }
}
