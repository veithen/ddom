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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.Mapper;

public class AttributesByTypeIterator<T extends CoreAttribute,S> extends AbstractAttributeIterator<T,S> {
    public AttributesByTypeIterator(CoreElement element, Class<T> type, Mapper<T,S> mapper) {
        super(element, type, mapper);
    }

    @Override
    protected boolean matches(T attribute) {
        return true;
    }
}
