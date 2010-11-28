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
package com.google.code.ddom.stream.spi;

public abstract class XmlInput {
    final DelegatingXmlHandler handler = new DelegatingXmlHandler();
    
    /**
     * Get the {@link XmlHandler} object that the implementation must use to produce
     * events. This method completes successfully even if the {@link XmlInput} has
     * not yet been connected to an {@link XmlOutput}. The implementation also guarantees
     * that the same object is returned over the whole lifecycle of the {@link XmlInput}
     * object.
     * 
     * @return the {@link XmlHandler} object
     */
    protected final XmlHandler getHandler() {
        return handler;
    }
    
    public abstract boolean proceed() throws StreamException;
    
    public abstract void dispose();
}
