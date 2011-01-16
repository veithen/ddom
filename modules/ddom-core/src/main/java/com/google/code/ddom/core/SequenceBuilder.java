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
package com.google.code.ddom.core;

import java.util.ArrayList;
import java.util.List;

public class SequenceBuilder {
    private final List<SequenceItem> items = new ArrayList<SequenceItem>();
    private boolean matchByInterface;
    
    public SequenceBuilder addItem(String namespaceURI, String localName) {
        items.add(new SequenceItem(namespaceURI, localName, null, false));
        return this;
    }
    
    public SequenceBuilder addItem(Class<? extends CoreNSAwareElement> extensionInterface, String namespaceURI, String localName) {
        items.add(new SequenceItem(namespaceURI, localName, extensionInterface, true));
        return this;
    }
    
    public SequenceBuilder enableMatchByInterface() {
        matchByInterface = true;
        return this;
    }
    
    public Sequence build() {
        // TODO: if matchByInterface is true, we should validate that all items have an extensionInterface set
        return new Sequence(items.toArray(new SequenceItem[items.size()]), matchByInterface);
    }
}
