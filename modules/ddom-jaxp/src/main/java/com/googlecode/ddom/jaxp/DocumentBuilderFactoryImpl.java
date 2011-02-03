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
package com.googlecode.ddom.jaxp;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import com.google.code.ddom.Options;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.stream.options.NamespaceAwareness;

/**
 * Document builder factory implementation that uses the DOM front-end.
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    /**
     * Feature for attribute sorting. When set to <code>true</code>, attributes will be sorted such
     * that they are returned in the same order as by the Xerces implementation. When set to
     * <code>false</code> (the default), attributes are returned in the order in which they appear
     * in the original document. Note that this setting only applies to attribute nodes created by
     * the parser. Attributes that are added programmatically will always be returned in the order
     * in which they have been added.
     * <p>
     * The DOM specification doesn't define the order in which attributes are returned by
     * {@link org.w3c.dom.NamedNodeMap#item(int)}. DDOM internally considers the attributes of an
     * element as an ordered list and thus preserves the order in which they have been added or in
     * which they appear in the original document. This is also the order in which attributes are
     * returned by the {@link org.w3c.dom.NamedNodeMap} implementation of the DOM front-end. On the
     * other hand, Xerces treats the attributes of an element as a sorted set.
     * <p>
     * This feature can be used to (partially) emulate the Xerces behavior. It should be noted that
     * enabling the feature adds a non-negligible overhead.
     */
    public static final String FEATURE_SORT_ATTRIBUTES = "http://ddom.googlecode.com/features/sort-attributes";
    
    private static final int FEATUREID_SORT_ATTRIBUTES = 1;
    
    private static final ModelDefinition DOM = ModelDefinitionBuilder.buildModelDefinition("dom");
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(DocumentBuilderFactoryImpl.class.getClassLoader());
    
    private static final Map<String,Integer> featureMap = new HashMap<String,Integer>();
    
    static {
        featureMap.put(FEATURE_SORT_ATTRIBUTES, FEATUREID_SORT_ATTRIBUTES);
    }
    
    private boolean sortAttributes;
    private Schema schema;
    
    private int getFeatureId(String name) throws ParserConfigurationException {
        Integer id = featureMap.get(name);
        if (id == null) {
            throw new ParserConfigurationException("Feature '" + name + "' is not supported");
        } else {
            return id;
        }
    }
    
    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        switch (getFeatureId(name)) {
            case FEATUREID_SORT_ATTRIBUTES:
                return sortAttributes;
            default:
                return false;
        }
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        switch (getFeatureId(name)) {
            case FEATUREID_SORT_ATTRIBUTES:
                sortAttributes = value;
        }
    }

    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        Model model;
        try {
            model = modelRegistry.getModel(DOM);
        } catch (ModelLoaderException ex) {
            ParserConfigurationException pce = new ParserConfigurationException("Unable to load model");
            pce.initCause(ex);
            throw pce;
        }
        Options options = new Options();
        options.set(NamespaceAwareness.get(isNamespaceAware()));
// TODO       props.put(XMLInputFactory.IS_VALIDATING, isValidating());
// TODO       private boolean whitespace = false;
// TODO        props.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, isExpandEntityReferences());
// TODO        props.put(XMLInputFactory.IS_COALESCING, isCoalescing());
        return new DocumentBuilderImpl(model, options, isIgnoringComments(), sortAttributes, schema);
    }
}
