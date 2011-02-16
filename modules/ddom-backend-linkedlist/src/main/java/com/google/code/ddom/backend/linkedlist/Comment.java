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

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class Comment extends CharacterDataContainer implements CoreComment {
    public Comment(Document document, boolean complete) {
        super(document, complete);
    }

    public Comment(Document document, Object content) {
        super(document, content);
    }

    public final int coreGetNodeType() {
        return COMMENT_NODE;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        handler.startComment();
    }

    public final void internalGenerateEndEvent(XmlHandler handler) throws StreamException {
        handler.endComment();
    }
}
