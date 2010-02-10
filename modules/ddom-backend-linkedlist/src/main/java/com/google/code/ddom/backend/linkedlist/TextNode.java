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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;

@Implementation
public abstract class TextNode extends CharacterData implements CoreTextNode {
    public TextNode(Document document, String data) {
        super(document, data);
    }

    @Override
    final CharSequence internalCollectTextContent(CharSequence appendTo) throws DeferredParsingException {
        String data = coreGetData();
        if (appendTo == null) {
            return data;
        } else {
            StringBuilder builder;
            if (appendTo instanceof String) {
                String existing = (String)appendTo;
                builder = new StringBuilder(existing.length() + data.length());
                builder.append(existing);
            } else {
                builder = (StringBuilder)appendTo;
            }
            builder.append(data);
            return builder;
        }
    }
}
