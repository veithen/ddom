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
package com.google.code.ddom.stream.core;

import com.google.code.ddom.OptionsProcessor;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamProvider;

public class CoreStreamProvider implements StreamProvider {
    public Producer getProducer(Object source, OptionsProcessor options, boolean preserve) throws StreamException {
        // TODO build a Producer for ParentNode objects (from our own core object model)
        return null;
    }

    public Consumer getConsumer(Object destination, OptionsProcessor options) throws StreamException {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T getSerializer(Class<T> serializerType, Consumer consumer, OptionsProcessor options) {
        // We don't define any particular serializers
        return null;
    }
}
