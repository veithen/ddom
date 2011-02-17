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
package com.google.code.ddom.tests.jaxen;

import java.io.InputStream;

import org.apache.axiom.test.jaxen.JaxenXPathTestBase;
import org.jaxen.Navigator;
import org.jaxen.dom.DocumentNavigator;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.stream.Options;
import com.googlecode.ddom.stream.StreamFactory;

public class JaxenTest extends JaxenXPathTestBase {
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(JaxenTest.class.getClassLoader());
    private static final StreamFactory streamFactory = StreamFactory.getInstance(JaxenTest.class.getClassLoader());
    
    public JaxenTest(String name) {
        super(name);
    }

    @Override
    protected Navigator createNavigator() {
        return new DocumentNavigator();
    }

    @Override
    protected Object loadDocument(InputStream in) throws Exception {
        DOMDocument document = (DOMDocument)modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("dom")).getNodeFactory().createDocument();
        document.coreSetContent(streamFactory.getSource(in, new Options(), false));
        return document;
    }

    @Override
    protected void releaseDocument(Object document) {
        // TODO
    }
}
