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
package com.google.code.ddom.backend.testsuite;

import java.io.StringReader;

import org.junit.Assert;

import junit.framework.TestCase;

import com.google.code.ddom.Options;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.stream.spi.SimpleFragmentSource;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

public class BackendTestCase extends TestCase {
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
    
    protected final CoreDocument parse(String xml) {
        CoreDocument document = nodeFactory.createDocument();
        try {
            document.coreSetContent(new SimpleFragmentSource(streamFactory.getProducer(new StringReader(xml), new Options(), true)));
        } catch (StreamException ex) {
            Assert.fail(ex.getMessage());
            return null;
        }
        return document;
    }
    
    protected final CoreDocumentFragment parse(CoreDocument document, String xml) {
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document);
        try {
            fragment.coreSetContent(new SimpleFragmentSource(streamFactory.getProducer(new StringReader(xml), new Options(), true)));
        } catch (StreamException ex) {
            Assert.fail(ex.getMessage());
            return null;
        }
        return fragment;
    }
}
