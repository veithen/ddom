/*
 * Copyright 2009-2010 Andreas Veithen
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

public abstract class ObjectStack<E> {
    private E[] elements;
    private int size;
    
    @SuppressWarnings("unchecked")
    public ObjectStack() {
        elements = (E[])new Object[16];
    }
    
    @SuppressWarnings("unchecked")
    private void increaseCapacity() {
        Object[] newElements = new Object[elements.length*2];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = (E[])newElements;
    }
    
    public E allocate() {
        if (size == elements.length) {
            increaseCapacity();
        }
        E element = elements[size];
        if (element == null) {
            element = createObject();
            elements[size] = element;
        }
        size++;
        return element;
    }
    
    public void pop() {
        recycleObject(elements[--size]);
    }
    
    public E peek() {
        return elements[size-1];
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public E get(int index) {
        return elements[index];
    }
    
    protected abstract E createObject();
    protected abstract void recycleObject(E object);
}
