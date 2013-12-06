/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.backend.testsuite;

import java.io.StringReader;

import junit.framework.TestCase;

import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.parser.Parser;

public abstract class BackendTestCase extends TestCase {
    // We define constants for this so that we can easily locate tests that depend on the builder type
    public static final int BUILDER_TYPE_1 = 1;
    public static final int BUILDER_TYPE_2 = 2;
    public static final int BUILDER_TYPE_3 = 3;
    
    private final StreamFactory streamFactory = StreamFactory.getInstance(BackendTestCase.class.getClassLoader());
    
    protected final NodeFactory nodeFactory;
    protected final int builderType;
    
    public BackendTestCase(BackendTestSuiteConfig config) {
        nodeFactory = config.getNodeFactory();
        builderType = config.getBuilderType();
        setName(getClass().getName());
    }
    
    public BackendTestCase(BackendTestSuiteConfig config, String nameQualifier) {
        this(config);
        setName(getClass().getName() + " [" + nameQualifier + "]");
    }
    
    protected final XmlSource toXmlSource(final String xml, final boolean namespaceAware, boolean destructive) {
        if (destructive) {
            return new SimpleXmlSource(new Parser(new StringReader(xml), namespaceAware));
        } else {
            return new XmlSource() {
                public boolean isDestructive() {
                    return false;
                }
                
                public XmlInput getInput(Hints hints) throws StreamException {
                    return new Parser(new StringReader(xml), namespaceAware);
                }
            };
        }
    }
    
    protected final CoreDocument parse(String xml, boolean namespaceAware) {
        CoreDocument document = nodeFactory.createDocument();
        document.coreSetContent(toXmlSource(xml, namespaceAware, true));
        return document;
    }
    
    protected final CoreDocumentFragment parse(CoreDocument document, String xml, boolean namespaceAware) {
        CoreDocumentFragment fragment = document.coreGetNodeFactory().createDocumentFragment(document);
        fragment.coreSetContent(toXmlSource(xml, namespaceAware, true));
        return fragment;
    }

    @Override
    protected abstract void runTest() throws Throwable;
}
