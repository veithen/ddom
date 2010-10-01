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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        ModelDefinitionBuilder modelDefinitionBuilder = new ModelDefinitionBuilder();
        modelDefinitionBuilder.addFrontend("dom");
        modelDefinitionBuilder.addFrontend("axiom");
        Document document = (Document)DocumentHelperFactory.INSTANCE.newInstance().newDocument(modelDefinitionBuilder.buildModelDefinition());
        Element element = document.createElementNS("urn:test", "p:root");
        ((OMElement)element).addAttribute("attr", "test", null);
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(element), new StreamResult(System.out));
    }
}
