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
package com.googlecode.ddom.frontend.dom.domts;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestIncompatibleException;
import org.w3c.domts.DOMTestLoadException;
import org.w3c.domts.DocumentBuilderSetting;

import com.googlecode.ddom.frontend.dom.intf.DOMDocument;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.stream.Options;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.options.CoalescingFeature;
import com.googlecode.ddom.stream.options.EntityReferencePolicy;
import com.googlecode.ddom.stream.options.NamespaceAwareness;
import com.googlecode.ddom.stream.options.ValidationPolicy;

public class DOMTestDocumentBuilderFactoryImpl extends DOMTestDocumentBuilderFactory {
    private interface Strategy {
        void applySetting(Options options, boolean value);
    }
    
    private static class SimpleStrategy implements Strategy {
        private final Object enabledOption;
        private final Object disabledOption;
        
        public SimpleStrategy(Object enabledOption, Object disabledOption) {
            this.enabledOption = enabledOption;
            this.disabledOption = disabledOption;
        }

        public void applySetting(Options options, boolean value) {
            options.set(value ? enabledOption : disabledOption);
        }
    }
    
    private static final Map<String,Strategy> strategies =
        new HashMap<String,Strategy>();
    
    static {
        strategies.put("coalescing", new SimpleStrategy(CoalescingFeature.ENABLE, CoalescingFeature.DISABLE));
        strategies.put("expandEntityReferences", new SimpleStrategy(EntityReferencePolicy.EXPAND, EntityReferencePolicy.DONT_EXPAND));
        strategies.put("namespaceAware", new SimpleStrategy(NamespaceAwareness.ENABLE, NamespaceAwareness.DISABLE));
        strategies.put("validating", new SimpleStrategy(ValidationPolicy.ENABLE, ValidationPolicy.DISABLE));
    }
    
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(DOMTestDocumentBuilderFactoryImpl.class.getClassLoader());
    private static final StreamFactory streamFactory = StreamFactory.getInstance(DOMTestDocumentBuilderFactoryImpl.class.getClassLoader());
    
    private final Model model;
    private final Options options;
    
    public DOMTestDocumentBuilderFactoryImpl(DocumentBuilderSetting[] settings) throws DOMTestIncompatibleException {
        super(settings);
        try {
            model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("dom"));
        } catch (ModelLoaderException ex) {
            throw new Error(ex);
        }
        options = new Options();
        for (DocumentBuilderSetting setting : settings) {
            Strategy strategy = strategies.get(setting.getProperty());
            if (strategy == null) {
                throw new DOMTestIncompatibleException(null, setting);
            } else {
                strategy.applySetting(options, setting.getValue());
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
        DOMDocument document = (DOMDocument)model.getNodeFactory().createDocument();
        try {
            document.coreSetContent(streamFactory.getSource(url, options, false));
        } catch (StreamException ex) {
            throw new DOMTestLoadException(ex);
        }
        return document;
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        return (DOMImplementation)model.getAPIObjectFactory().getAPIObject(DOMImplementation.class);
    }

    @Override
    public boolean hasFeature(String feature, String version) {
        return getDOMImplementation().hasFeature(feature, version);
    }

    @Override
    public boolean isCoalescing() {
        return options.get(CoalescingFeature.class) == CoalescingFeature.ENABLE;
    }

    @Override
    public boolean isExpandEntityReferences() {
        return options.get(EntityReferencePolicy.class) != EntityReferencePolicy.DONT_EXPAND;
    }

    @Override
    public boolean isIgnoringElementContentWhitespace() {
        return false;
    }

    @Override
    public boolean isNamespaceAware() {
        return options.get(NamespaceAwareness.class) != NamespaceAwareness.DISABLE;
    }

    @Override
    public boolean isValidating() {
        return options.get(ValidationPolicy.class) == ValidationPolicy.ENABLE;
    }
}
