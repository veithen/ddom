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
package com.googlecode.ddom.frontend.axiom;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.ddom.frontend.axiom.bookshop.search.Query;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ADBTest {
    private static OMFactory factory;
    
    @BeforeClass
    public static void init() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom"));
        factory = ((OMMetaFactory)model.getNodeFactory()).getOMFactory();
    }
    
    @Test
    public void testQuery() throws Exception {
        Query query = new Query();
        query.setAuthor("Jules Vernes");
        OMElement element = query.getOMElement(Query.MY_QNAME, factory);
        assertEquals(Query.MY_QNAME, element.getQName());
        OMElement child = element.getFirstElement();
        assertEquals(new QName(Query.MY_QNAME.getNamespaceURI(), "author"), child.getQName());
    }
}
