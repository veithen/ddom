/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.jaxp;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.ddom.frontend.dom.intf.DOMNodeFactory;

/**
 * Document builder factory implementation that is constructed as an adapter to a given
 * {@link DOMNodeFactory}.
 * 
 * @author Andreas Veithen
 */
public class DocumentBuilderFactoryAdapter extends AbstractDocumentBuilderFactory {
    private final DOMNodeFactory nodeFactory;

    public DocumentBuilderFactoryAdapter(DOMNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    @Override
    protected DOMNodeFactory getNodeFactory() throws ParserConfigurationException {
        return nodeFactory;
    }
}
