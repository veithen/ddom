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
package com.googlecode.ddom.stream.options;

/**
 * Specifies how empty CDATA sections will be handled by a parser.
 * 
 * @author Andreas Veithen
 */
public enum EmptyCDATASectionPolicy {
    /**
     * Empty CDATA sections will be removed by the parser.
     */
    REMOVE,
    
    /**
     * Empty CDATA sections will be preserved by the parser.
     */
    PRESERVE
}
