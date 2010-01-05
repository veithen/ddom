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
package com.google.code.ddom.stream.spi;

public interface Producer {
    /**
     * Get the symbol table used by this producer. More precisely, get the symbol table that the
     * producer uses to canonicalize the return values of {@link Event#getName()},
     * {@link Event#getNamespaceURI()} and {@link Event#getPrefix()}.
     * 
     * @return the symbol table; may not be <code>null</code>
     */
    Symbols getSymbols();
    
    /**
     * 
     * 
     * Must result in one or more calls to the {@link Consumer}.
     * 
     * @param consumer
     * @return <code>true</code> if there are more events to consume; <code>false</code> if the end
     *         of the document has been reached
     * @throws StreamException
     */
    boolean proceed(Consumer consumer) throws StreamException;
    
    void dispose();
}
