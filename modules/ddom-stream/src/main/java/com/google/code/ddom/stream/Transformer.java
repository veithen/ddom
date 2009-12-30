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
package com.google.code.ddom.stream;

import com.google.code.ddom.Options;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

public class Transformer {
    public class Source {
        private final Producer producer;

        Source(Producer producer) {
            this.producer = producer;
        }
        
        private void to(Consumer consumer) throws StreamException {
            while (producer.proceed(consumer)) {
                // Just loop
            }
        }
        
        public void to(Object destination, Options options) throws StreamException {
            to(streamFactory.getConsumer(destination, options));
        }
        
        public void to(Object destination) throws StreamException {
            to(destination, null);
        }
    }
    
    final StreamFactory streamFactory;

    private Transformer(StreamFactory streamFactory) {
        this.streamFactory = streamFactory;
    }

    public static Transformer getInstance() {
        return new Transformer(StreamFactory.getInstance());
    }
    
    public static Transformer getInstance(ClassLoader classLoader) {
        return new Transformer(StreamFactory.getInstance(classLoader));
    }
    
    public Source from(String providerName, Object source, Options options, boolean preserve) throws StreamException {
        return new Source(streamFactory.getProducer(providerName, source, options, preserve));
    }
    
    public Source from(Object source, Options options, boolean preserve) throws StreamException {
        return new Source(streamFactory.getProducer(source, options, preserve));
    }
    
    public Source from(Object source) throws StreamException {
        return from(source, null, true);
    }
}
