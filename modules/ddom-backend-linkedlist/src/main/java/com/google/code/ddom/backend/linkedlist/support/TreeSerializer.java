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
package com.google.code.ddom.backend.linkedlist.support;

import com.google.code.ddom.backend.linkedlist.LLParentNode;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlInput;

public class TreeSerializer extends XmlInput {
    private final LLParentNode root;
    private final boolean preserve;
    
    public TreeSerializer(LLParentNode root, boolean preserve) {
        this.root = root;
        this.preserve = preserve;
    }

    @Override
    public boolean proceed() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
