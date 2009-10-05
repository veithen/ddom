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
package com.google.code.ddom.spi.stream;

import java.util.Map;

public interface StreamProvider {
    /**
     * Create a producer for a given source object.
     * 
     * @param source
     * @param properties
     * @param preserve <code>true</code> if the producer should preserve the data in the original
     *                 source object; <code>false</code> if the producer is allowed to consume
     *                 the source object in a destructive way
     * @return
     * @throws StreamException
     */
    Producer getProducer(Object source, Map<String,Object> properties, boolean preserve) throws StreamException;
    
    Consumer getConsumer(Object destination, Map<String,Object> properties) throws StreamException;
    
    <T> T getSerializer(Class<T> serializerType, Consumer consumer, Map<String,Object> properties);
}
