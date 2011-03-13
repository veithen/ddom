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
package com.googlecode.ddom.core;

/**
 * Represents a namespace aware element information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreNSAwareElement extends CoreElement, CoreNSAwareNamedNode {
    /**
     * Get (or optionally create) an child element according to a given sequence.
     * 
     * @param sequence
     *            the sequence
     * @param index
     *            the index in the sequence
     * @param create
     *            if set to <code>true</code> and no matching child element is found, then a new one
     *            is created
     * @return the child element matching the specified item in the sequence, or <code>null</code>
     *         if <code>create</code> is <code>false</code> and no matching element was found
     * @throws CoreModelException
     */
    CoreNSAwareElement coreGetElementFromSequence(Sequence sequence, int index, boolean create) throws CoreModelException;
    
    CoreNSAwareElement coreCreateElementInSequence(Sequence sequence, int index) throws CoreModelException;

    void coreInsertElementInSequence(Sequence sequence, int index, CoreNSAwareElement element) throws CoreModelException;
}
