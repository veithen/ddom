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
package com.googlecode.ddom.weaver.asm;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.ddom.frontend.Mixin;

@Mixin(IBase.class)
public abstract class BaseMixin implements IBase, IBaseMixin {
    private final List<String> list = new LinkedList<String>();
    
    public void addItem(String item) {
        list.add(item);
    }
}
