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
package com.google.code.ddom.core;

/**
 * Indicates that an attempt was made to insert a child node where it is not allowed.
 * 
 * @author Andreas Veithen
 */
public class ChildTypeNotAllowedException extends CoreModelException {
    private static final long serialVersionUID = -676602453248623826L;

    public ChildTypeNotAllowedException() {
    }

    public ChildTypeNotAllowedException(String message) {
        super(message);
    }
}
