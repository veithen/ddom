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

public abstract class XmlOutput extends XmlValve {
    @Override
    final void doAppend(XmlValve valve) {
        throw new IllegalStateException("Attempting to append to an XML pipeline that is already terminated by an XmlOutput");
    }

    @Override
    final XmlHandler doConnect(Stream stream) {
        return createXmlHandler();
    }
    
    protected abstract XmlHandler createXmlHandler();
}
