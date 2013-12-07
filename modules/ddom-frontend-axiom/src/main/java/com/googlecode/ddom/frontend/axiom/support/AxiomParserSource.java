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

import org.apache.axiom.om.util.StAXParserConfiguration;
import org.xml.sax.InputSource;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.filter.CDATASectionFilter;
import com.googlecode.ddom.stream.parser.ParserSource;

public class AxiomParserSource implements XmlSource {
    private final ParserSource delegate;
    private final StAXParserConfiguration configuration;
    
    public AxiomParserSource(InputSource is, StAXParserConfiguration configuration) {
        delegate = new ParserSource(is);
        this.configuration = configuration;
    }

    public XmlInput getInput(Hints hints) throws StreamException {
        XmlInput input = delegate.getInput(hints);
        // TODO: doesn't cover all cases yet + dirty hack
        if (configuration != StAXParserConfiguration.PRESERVE_CDATA_SECTIONS && !configuration.toString().equals("TEST")) {
            input.addFilter(new CDATASectionFilter());
        }
        return input;
    }

    public boolean isDestructive() {
        return delegate.isDestructive();
    }
}
