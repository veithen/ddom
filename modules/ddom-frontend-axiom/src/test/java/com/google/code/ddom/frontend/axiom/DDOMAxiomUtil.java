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
package com.google.code.ddom.frontend.axiom;

import java.io.StringReader;

import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.Options;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public class DDOMAxiomUtil implements AxiomUtil {
    public static final DDOMAxiomUtil INSTANCE = new DDOMAxiomUtil();
    
    private final ModelDefinition modelDefinition = ModelDefinitionBuilder.buildModelDefinition("axiom");
    private final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance();

    private DDOMAxiomUtil() {}
    
    public OMMetaFactory getMetaFactory() {
        return documentHelper.getAPIObject(modelDefinition, OMMetaFactory.class);
    }
    
    public OMFactory getOMFactory() {
        return getMetaFactory().getOMFactory();
    }
    
    public OMDocument createDocument() {
        return (OMDocument)documentHelper.newDocument(modelDefinition);
    }

    public OMDocument parse(String xml) {
        return (OMDocument)documentHelper.parse(modelDefinition, new StringReader(xml), new Options());
    }
}
