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

public abstract class XmlValve {
    private Stream stream;
    
    abstract void doAppend(XmlValve valve);
    abstract XmlHandler doConnect(Stream stream);
    
    final void append(XmlValve valve) {
        if (stream != null) {
            throw new IllegalStateException("Already connected");
        }
        doAppend(valve);
    }
    
    final XmlHandler connect(Stream stream) {
        if (this.stream != null) {
            throw new IllegalStateException("Already connected");
        }
        this.stream = stream;
        return doConnect(stream);
    }

    public final Stream getStream() {
        if (stream == null) {
            throw new IllegalStateException("Not connected");
        }
        return stream;
    }
}
