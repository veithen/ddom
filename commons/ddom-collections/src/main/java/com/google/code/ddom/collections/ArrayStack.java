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
package com.google.code.ddom.collections;

import java.util.ArrayList;

public class ArrayStack<E> extends ArrayList<E> implements Stack<E> {
    public void push(E o) {
        add(o);
    }
    
    public E peek() {
        return get(size()-1);
    }

    public E pop() {
        return remove(size()-1);
    }

    public boolean replace(E oldItem, E newItem) {
        boolean replaced = false;
        for (int i=0; i<size(); i++) {
            if (get(i).equals(oldItem)) {
                set(i, newItem);
                replaced = true;
            }
        }
        return true;
    }
}
