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

import com.google.code.ddom.OptionsTracker;

// TODO: refer to the @Provider annotation
public interface StreamProvider {
    /**
     * Create a producer for a given source object.
     * 
     * @param source
     * @param options
     * @param preserve <code>true</code> if the producer should preserve the data in the original
     *                 source object; <code>false</code> if the producer is allowed to consume
     *                 the source object in a destructive way
     * @return
     * @throws StreamException
     */
    XmlInput getInput(Object source, OptionsTracker options, boolean preserve) throws StreamException;
    
    XmlOutput getOutput(Object destination, OptionsTracker options) throws StreamException;
    
    <T> T getSerializer(Class<T> serializerType, XmlOutput output, OptionsTracker options);
}
