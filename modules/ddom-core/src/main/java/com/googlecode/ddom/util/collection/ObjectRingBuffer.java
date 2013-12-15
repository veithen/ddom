/*
 * Copyright 2013 Andreas Veithen
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
package com.googlecode.ddom.util.collection;

public abstract class ObjectRingBuffer<E> {
    /**
     * Stores the items in the ring buffer. Only the array items [0,{@link #size}[ are part of the
     * ring buffer. All other items are <code>null</code>. {@link E} instances are created once
     * (when the ring buffer size is increased) and then recycled.
     */
    private E[] elements;
    
    /**
     * The current size of the ring buffer.
     */
    private int size;
    
    /**
     * The head of the ring buffer.
     */
    private int head;
    
    /**
     * The tail of the ring buffer.
     */
    private int tail;
    
    /**
     * Indicates if the ring buffer is currently full. This attribute is <code>true</code> if and
     * only if the next {@link #allocate()} call will increase {@link #size}.
     */
    private boolean full = true;
    
    @SuppressWarnings("unchecked")
    public ObjectRingBuffer() {
        elements = (E[])new Object[16];
    }
    
    @SuppressWarnings("unchecked")
    public E allocate() {
        if (full) {
            if (size == elements.length) {
                Object[] newElements = new Object[elements.length*2];
                System.arraycopy(elements, 0, newElements, 0, head);
                System.arraycopy(elements, head, newElements, head+1, size-head);
                elements = (E[])newElements;
            } else {
                System.arraycopy(elements, head, elements, head+1, size-head);
            }
            E object = createObject();
            elements[head] = object;
            if (size > 0) {
                head++; // Head moves because we allocated a new object
                tail++; // Tail moves because the element has moved inside the array
            }
            size++;
            return object;
        } else {
            E object = elements[head++];
            if (head == size) {
                head = 0;
            }
            full = head == tail;
            return object;
        }
    }

    public void pop() {
        recycleObject(elements[tail]);
        tail++;
        if (tail == size) {
            tail = 0;
        }
        full = false;
    }
    
    public E peek() {
        return elements[tail];
    }
    
    public E peek(int offset) {
        int index = tail+offset;
        if (index >= size) {
            index -= size;
        }
        return elements[index];
    }
    
    public boolean isEmpty() {
        return head == tail && (!full || size == 0);
    }
    
    protected abstract E createObject();
    protected abstract void recycleObject(E object);
}
