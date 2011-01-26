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
package com.google.code.ddom.frontend.dom.mixin;

import java.util.Map;

import org.w3c.dom.DOMImplementation;

import com.google.code.ddom.frontend.dom.intf.DOMCoreNode;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.support.UserData;
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(CoreNode.class)
public abstract class CoreNodeSupport implements DOMCoreNode {
    public final Map<String,UserData> getUserDataMap(boolean create) {
        return ((DOMDocument)coreGetOwnerDocument(true)).getUserDataMap(this, create);
    }
    
    public final DOMImplementation getDOMImplementation() {
        return ((DOMDocument)coreGetOwnerDocument(true)).getImplementation();
    }
}
