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
package com.googlecode.ddom.backend.testsuite.cdata;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.TextCollectorPolicy;

public class TestCoreClone extends BackendTestCase {
    public TestCoreClone(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreCDATASection cdataSection = nodeFactory.createCDATASection(null, "test content");
        CoreNode clone = cdataSection.coreClone(new ClonePolicy() {
            public boolean cloneAttributes() {
                return false;
            }
            
            public boolean cloneChildren(int nodeType) {
                return nodeType == CoreNode.CDATA_SECTION_NODE;
            }
        });
        assertTrue(clone instanceof CoreCDATASection);
        assertEquals("test content", ((CoreCDATASection)clone).coreGetTextContent(TextCollectorPolicy.DEFAULT));
    }
}
