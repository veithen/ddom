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

import com.google.code.ddom.DeferredParsingException;

public interface ParserWrapper {
    /**
     * 
     * 
     * Must result in one or more calls to the {@link Consumer}.
     * 
     * @param listener
     * @throws DeferredParsingException
     */
    void proceed(ParserListener listener) throws DeferredParsingException;
}
