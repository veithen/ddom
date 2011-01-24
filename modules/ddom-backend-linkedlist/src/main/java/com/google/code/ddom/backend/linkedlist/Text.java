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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.googlecode.ddom.core.CoreText;

// @Implementation
public class Text extends TextNode implements CoreText {
    public Text(Document document, String data, boolean ignorable) {
        super(document, data);
        internalSetFlag(Flags.IGNORABLE, ignorable);
    }

    public final void internalGenerateEvents(XmlHandler handler) throws StreamException {
        handler.processText(coreGetData(), coreIsIgnorable());
    }

    public final boolean coreIsIgnorable() {
        return internalGetFlag(Flags.IGNORABLE);
    }
}
