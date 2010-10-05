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
import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

public class Transformer {
    public class Source {
        private final Input input;

        Source(Input input) {
            this.input = input;
        }
        
        private void to(Output output) throws StreamException {
            while (input.proceed(output)) {
                // Just loop
            }
        }
        
        public void to(Object destination, Options options) throws StreamException {
            to(streamFactory.getOutput(destination, options));
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
        return new Source(streamFactory.getInput(providerName, source, options, preserve));
    }
    
    public Source from(Object source, Options options, boolean preserve) throws StreamException {
        return new Source(streamFactory.getInput(source, options, preserve));
    }
    
    public Source from(Object source) throws StreamException {
        return from(source, null, true);
    }
}
