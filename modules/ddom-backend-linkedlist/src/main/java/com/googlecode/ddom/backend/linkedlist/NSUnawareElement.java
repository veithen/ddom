/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreNSUnawareElement;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.ElementNameMismatchException;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class NSUnawareElement extends Element implements CoreNSUnawareElement {
    private String tagName;

    public NSUnawareElement(Document document, String tagName, boolean complete) {
        super(document, complete);
        this.tagName = tagName;
    }

    void initName(String tagName) throws ElementNameMismatchException {
        if (internalGetFlag(Flags.LOCAL_NAME_SET)) {
            // Do nothing; keep the overridden name
        } else if (this.tagName != null) {
            if (!this.tagName.equals(tagName)) {
                throw new ElementNameMismatchException("Unexpected name");
            }
        } else {
            this.tagName = tagName;
        }
    }
    
    public final int coreGetNodeType() {
        return NS_UNAWARE_ELEMENT_NODE;
    }

    public final String coreGetName() throws DeferredBuildingException {
        if (tagName == null && internalGetState() == STATE_SOURCE_SET) {
            internalGetOrCreateInputContext();
        }
        return tagName;
    }

    @Override
    protected final String getImplicitNamespaceURI(String prefix) {
        return null;
    }

    @Override
    protected final String getImplicitPrefix(String namespaceURI) {
        return null;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        handler.startElement(tagName);
    }

    @Override
    final LLElement shallowCloneWithoutAttributes(ClonePolicy policy) {
        return new NSUnawareElement(null, tagName, true);
    }
}
