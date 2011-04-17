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
package com.googlecode.ddom.stream.sax;

import javax.xml.transform.sax.SAXSource;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlReader;

public class SAXInput extends XmlInput {
    private final SAXSource source;

    public SAXInput(SAXSource source) {
        this.source = source;
    }

    @Override
    protected XmlReader createReader(XmlHandler handler) {
        return new SAXReader(handler, source);
    }

    public void dispose() {
        // TODO: should we do something with the InputSource here?
    }
}
