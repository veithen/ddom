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
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.stream.spi.SimpleFragmentSource;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

public class BackendTestCase extends TestCase {
    private final StreamFactory streamFactory = StreamFactory.getInstance(BackendTestCase.class.getClassLoader());
    
    protected final NodeFactory nodeFactory;
    
    public BackendTestCase(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        setName(getClass().getName());
    }
    
    public BackendTestCase(NodeFactory nodeFactory, String nameQualifier) {
        this.nodeFactory = nodeFactory;
        setName(getClass().getName() + " [" + nameQualifier + "]");
    }
    
    protected CoreDocument parse(String xml) {
        CoreDocument document =  nodeFactory.createDocument();
        try {
            document.coreSetContent(new SimpleFragmentSource(streamFactory.getProducer(new StringReader(xml), new Options(), true)));
        } catch (StreamException ex) {
            Assert.fail(ex.getMessage());
            return null;
        }
        return document;
    }
}
