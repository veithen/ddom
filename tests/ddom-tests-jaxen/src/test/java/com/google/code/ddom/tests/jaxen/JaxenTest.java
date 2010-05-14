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

import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.dom.DocumentNavigator;
import org.jaxen.test.XPathTestBase;

import com.google.code.ddom.DocumentHelperFactory;

public class JaxenTest extends XPathTestBase {
    public JaxenTest(String name) {
        super(name);
    }

    @Override
    protected Object getDocument(String url) throws Exception {
        // This method is never used in XPathTestBase
        throw new UnsupportedOperationException();
    }

    @Override
    protected Navigator getNavigator() {
        return new DocumentNavigator() {
            @Override
            public Object getDocument(String url) throws FunctionCallException {
                // TODO: we need to properly close the input stream/parser somewhere
                InputStream in = JaxenTest.class.getClassLoader().getResourceAsStream(url);
                return DocumentHelperFactory.INSTANCE.newInstance().parse("dom", in);
            }
        };
    }
}
