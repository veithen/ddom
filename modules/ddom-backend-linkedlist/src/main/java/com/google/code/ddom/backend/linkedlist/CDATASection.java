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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.core.ChildTypeNotAllowedException;
import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.core.DeferredParsingException;

// @Implementation
public class CDATASection extends Container implements CoreCDATASection {
    public CDATASection(Document document, String data) {
        super(document, true);
        try {
            coreSetValue(data);
        } catch (DeferredParsingException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    public final void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild)
            throws ChildTypeNotAllowedException, DeferredParsingException {
        if (!(newChild instanceof CoreText)) {
            throw new ChildTypeNotAllowedException();
        }
    }

    public String coreGetData() {
        try {
            return coreGetTextContent();
        } catch (DeferredParsingException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    public void coreSetData(String data) {
        try {
            coreSetValue(data);
        } catch (DeferredParsingException e) {
            throw new RuntimeException(e); // TODO
        }
    }
}
