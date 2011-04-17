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
package com.googlecode.ddom.stream;

import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

final class IncludeXmlOutputHandler extends XmlHandlerWrapper {
    IncludeXmlOutputHandler(XmlHandler parent) {
        super(parent);
    }

    @Override
    public void startEntity(boolean fragment, String inputEncoding) {
        // Do nothing.
    }

    @Override
    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // Do nothing.
    }

    @Override
    public void completed() throws StreamException {
        // Do nothing.
    }
}
