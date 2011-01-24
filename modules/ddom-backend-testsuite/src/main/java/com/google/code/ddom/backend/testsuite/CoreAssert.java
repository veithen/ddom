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

import org.junit.Assert;

import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.DeferredParsingException;

public class CoreAssert {
    private CoreAssert() {}
    
    public static void assertOrphan(CoreChildNode node) throws DeferredParsingException {
        Assert.assertNull(node.coreGetParent());
        Assert.assertNull(node.coreGetPreviousSibling());
        Assert.assertNull(node.coreGetNextSibling());
    }
    
    public static void assertSiblings(CoreChildNode node1, CoreChildNode node2) throws DeferredParsingException {
        Assert.assertSame(node2, node1.coreGetNextSibling());
        Assert.assertSame(node1, node2.coreGetPreviousSibling());
    }
}
