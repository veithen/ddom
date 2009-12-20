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
package com.google.code.ddom.dom.impl;

import java.util.List;

import org.w3c.dom.DOMStringList;

public class DOMStringListImpl implements DOMStringList {
    private final List<String> items;
    
    public DOMStringListImpl(List<String> items) {
        this.items = items;
    }

    public int getLength() {
        return items.size();
    }

    public String item(int index) {
        return index < 0 || index >= items.size() ? null : items.get(index);
    }

    public boolean contains(String str) {
        return items.contains(str);
    }
}
