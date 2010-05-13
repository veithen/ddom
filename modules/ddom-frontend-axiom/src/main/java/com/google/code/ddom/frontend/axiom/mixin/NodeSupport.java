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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMFactory;

import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(CoreNode.class)
public abstract class NodeSupport implements AxiomNode {
    public OMFactory getOMFactory() {
        return (OMFactory)coreGetDocument();
    }

    public void close(boolean build) {
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

    public void serialize(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, true);
    }

    public void serializeAndConsume(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, false);
    }
    
    public void serialize(XMLStreamWriter xmlWriter, boolean cache) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
