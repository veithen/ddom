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

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMDataSourceExt;

import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.stax.StAXInput;

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

    public XmlInput getInput() {
        try {
            // TODO: we could cheat here if the returned XMLStreamReader is actually a StAXPivot;
            //       alternatively we could emit a warning because this is an indication of the OM-inside-OMDataSource anti-pattern
            return new StAXInput(ds.getReader(), null);
        } catch (XMLStreamException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
    }
}
