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
package com.googlecode.ddom.stream.filter;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

final class CommentFilterHandler extends XmlHandlerWrapper {
    private boolean skip;

    CommentFilterHandler(XmlHandler parent) {
        super(parent);
    }

    @Override
    public void startComment() throws StreamException {
        skip = true;
    }

    @Override
    public void endComment() throws StreamException {
        skip = false;
    }

    @Override
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (!skip) {
            super.processCharacterData(data, ignorable);
        }
    }
}
