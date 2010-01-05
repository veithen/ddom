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
package com.google.code.ddom.stream.options;

/**
 * Specifies how comments will be handled by a parser.
 * 
 * @author Andreas Veithen
 */
public enum CommentPolicy {
    /**
     * Comments will be removed by the parser.
     */
    REMOVE,
    
    /**
     * Comments will be preserved by the parser. This should be the default if the option is not
     * specified.
     */
    PRESERVE
}
