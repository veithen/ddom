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
package com.google.code.ddom.spi.parser;

public interface Data {
    // TODO: probably a better way is to introduce the concept of an InvalidationListener and have the parser fire an event when the data will be no longer available 
    public enum Scope {
        /**
         * The data is only available during the invocation of the consumer. The consumer
         * MUST NOT store the {@link ParserDataSource} instance for later use.
         */
        CONSUMER_INVOCATION,
        
        /**
         * The data is only available until the next invocation of
         * {@link Producer#proceed(Consumer)}. The consumer MAY store the data for
         * later use if it can guarantee that the data is consumed before the consumer
         * is invoked again. Note that this is only possible if the work of the consumer and the
         * consumer is coordinated by some third party that controls both of them.
         */
        PARSER_INVOCATION,
        
        /**
         * The data remains available forever. In this case, the {@link ParserDataSource}
         * instance may be considered as immutable and the consumer MAY store a reference
         * to it for later use.
         */
        ETERNAL
    }
    
    /**
     * Determine the scope in which the data represented by this object will be available.
     * Method defined by implementations of this interface will have undefined results when
     * called outside of this scope.
     * 
     * @return one of the values defined by the {@link Scope} enumeration
     */
    Scope getScope();
}
