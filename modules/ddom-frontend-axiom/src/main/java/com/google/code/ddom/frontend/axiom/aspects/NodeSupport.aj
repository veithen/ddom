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
package com.google.code.ddom.frontend.axiom.aspects;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMFactory;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

public aspect NodeSupport {
    public OMFactory AxiomNode.getOMFactory() {
        return (OMFactory)coreGetDocument();
    }

    public void AxiomNode.close(boolean build) {
        CoreDocument document = coreGetDocument();
        if (build) {
            try {
                // TODO: not sure if the document or only the node should be built
                document.coreBuild();
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
        // TODO
//        document.dispose();
    }

    public void AxiomNode.serialize(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, true);
    }

    public void AxiomNode.serializeAndConsume(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, false);
    }
    
    public void AxiomNode.serialize(XMLStreamWriter xmlWriter, boolean cache) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
