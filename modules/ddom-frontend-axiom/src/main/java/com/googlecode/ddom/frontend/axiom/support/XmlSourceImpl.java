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
package com.googlecode.ddom.frontend.axiom.support;

import com.googlecode.ddom.frontend.axiom.intf.AxiomContainer;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public class XmlSourceImpl implements XmlSource {
    private final AxiomContainer container;
    private final boolean cache;
    
    public XmlSourceImpl(AxiomContainer container, boolean cache) {
        this.container = container;
        this.cache = cache;
    }

    public XmlInput getInput(Hints hints) {
        // TODO: need to add a filter that removes DOCTYPE declarations
        return container.coreGetInput(cache);
    }

    public boolean isDestructive() {
        return !cache;
    }
}
