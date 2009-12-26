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
package com.google.code.ddom.frontend.dom.domts;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestIncompatibleException;
import org.w3c.domts.DOMTestLoadException;
import org.w3c.domts.DocumentBuilderSetting;

import com.google.code.ddom.DeferredDocumentFactory;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.frontend.dom.support.DOMImplementationImpl;
import com.google.code.ddom.spi.model.FrontendRegistry;

public class DOMTestDocumentBuilderFactoryImpl extends DOMTestDocumentBuilderFactory {
    private interface Strategy {
        void applySetting(XMLInputFactory factory, boolean value);
    }
    
    private static class SimpleStrategy implements Strategy {
        private final String property;
        
        public SimpleStrategy(String property) {
            this.property = property;
        }

        public void applySetting(XMLInputFactory factory, boolean value) {
            factory.setProperty(property, value);
        }
    }
    
    private static final Map<String,Strategy> strategies =
        new HashMap<String,Strategy>();
    
    static {
        strategies.put("coalescing", new SimpleStrategy(XMLInputFactory.IS_COALESCING));
        strategies.put("expandEntityReferences", new SimpleStrategy(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES));
        strategies.put("namespaceAware", new SimpleStrategy(XMLInputFactory.IS_NAMESPACE_AWARE));
        strategies.put("validating", new SimpleStrategy(XMLInputFactory.IS_VALIDATING));
    }
    
    private final XMLInputFactory factory;
    
    public DOMTestDocumentBuilderFactoryImpl(DocumentBuilderSetting[] settings) throws DOMTestIncompatibleException {
        super(settings);
        factory = XMLInputFactory.newInstance();
        for (DocumentBuilderSetting setting : settings) {
            Strategy strategy = strategies.get(setting.getProperty());
            if (strategy == null) {
                throw new DOMTestIncompatibleException(null, setting);
            } else {
                strategy.applySetting(factory, setting.getValue());
            }
        }
    }
    
    @Override
    public DOMTestDocumentBuilderFactory newInstance(DocumentBuilderSetting[] newSettings)
            throws DOMTestIncompatibleException {
        if (newSettings == null) {
            return this;
        } else {
            return new DOMTestDocumentBuilderFactoryImpl(mergeSettings(newSettings));
        }
    }

    @Override
    public Document load(URL url) throws DOMTestLoadException {
        // TODO: need to cleanup somehow
        try {
            return (Document)DeferredDocumentFactory.newInstance().parse("dom", factory.createXMLStreamReader(new StreamSource(url.toExternalForm())));
        } catch (XMLStreamException ex) {
            throw new DOMTestLoadException(ex);
        }
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        // TODO: check this
        return new DOMImplementationImpl((NodeFactory)FrontendRegistry.getInstance().getDocumentFactory("dom"));
    }

    @Override
    public boolean hasFeature(String feature, String version) {
        return getDOMImplementation().hasFeature(feature, version);
    }

    @Override
    public boolean isCoalescing() {
        return (Boolean)factory.getProperty(XMLInputFactory.IS_COALESCING);
    }

    @Override
    public boolean isExpandEntityReferences() {
        return (Boolean)factory.getProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES);
    }

    @Override
    public boolean isIgnoringElementContentWhitespace() {
        return false;
    }

    @Override
    public boolean isNamespaceAware() {
        return (Boolean)factory.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
    }

    @Override
    public boolean isValidating() {
        return (Boolean)factory.getProperty(XMLInputFactory.IS_VALIDATING);
    }
}
