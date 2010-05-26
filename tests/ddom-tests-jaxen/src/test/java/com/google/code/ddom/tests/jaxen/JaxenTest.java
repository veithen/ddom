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
package com.google.code.ddom.tests.jaxen;

import java.io.InputStream;

import org.apache.axiom.test.jaxen.JaxenXPathTestBase;
import org.jaxen.Navigator;
import org.jaxen.dom.DocumentNavigator;

import com.google.code.ddom.DocumentHelperFactory;

public class JaxenTest extends JaxenXPathTestBase {
    public JaxenTest(String name) {
        super(name);
    }

    @Override
    protected Navigator createNavigator() {
        return new DocumentNavigator();
    }

    @Override
    protected Object loadDocument(InputStream in) throws Exception {
        return DocumentHelperFactory.INSTANCE.newInstance().parse("dom", in);
    }

    @Override
    protected void releaseDocument(Object document) {
        DocumentHelperFactory.INSTANCE.newInstance().disposeDocument(document);
    }
}
