/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMDataSourceExt;
import org.apache.axiom.om.ds.AbstractPullOMDataSource;
import org.apache.axiom.om.ds.AbstractPushOMDataSource;
import org.apache.axiom.util.stax.wrapper.XMLStreamWriterWrapper;

import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.stax.StAXPullInput;
import com.googlecode.ddom.stream.stax.StAXPushInput;

public class OMDataSourceAdapter implements XmlSource {
    private final OMDataSource ds;

    public OMDataSourceAdapter(OMDataSource ds) {
        this.ds = ds;
    }

    public boolean isDestructive() {
        if (ds instanceof OMDataSourceExt) {
            return ((OMDataSourceExt)ds).isDestructiveRead();
        } else {
            return true;
        }
    }

    public XmlInput getInput(Hints hints) {
        boolean push;
        if (ds instanceof AbstractPushOMDataSource) {
            push = true;
        } else if (ds instanceof AbstractPullOMDataSource) {
            push = false;
        } else {
            push = hints.isPreferPush();
        }
        if (push) {
            return new StAXPushInput() {
                @Override
                protected void serialize(XMLStreamWriter out) throws XMLStreamException {
                    ds.serialize(new XMLStreamWriterWrapper(out) {
                        @Override
                        public void writeStartDocument() throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void writeStartDocument(String encoding, String version) throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void writeStartDocument(String version) throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void writeEndDocument() throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void writeStartElement(String localName) throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void close() throws XMLStreamException {
                            throw new UnsupportedOperationException();
                        }
                    });
                }
                
                @Override
                public void dispose() {
                    // TODO
                }
            };
        } else {
            try {
                // TODO: we could cheat here if the returned XMLStreamReader is actually a StAXPivot;
                //       alternatively we could emit a warning because this is an indication of the OM-inside-OMDataSource anti-pattern
                return new StAXPullInput(ds.getReader());
            } catch (XMLStreamException ex) {
                // TODO
                throw new RuntimeException(ex);
            }
        }
    }
}
