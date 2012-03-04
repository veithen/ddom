/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist.intf;

import java.util.ArrayList;

import com.googlecode.ddom.backend.linkedlist.Builder;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.stream.XmlInput;

public interface LLDocument extends LLParentNode, CoreDocument {
    ArrayList<Builder> getBuilders();
    InputContext internalCreateInputContext(XmlInput input, LLParentNode target, boolean unwrap) throws DeferredParsingException;
    InputContext internalGetInputContext(LLParentNode target);
}
