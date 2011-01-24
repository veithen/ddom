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
package com.googlecode.ddom.core;

import java.util.Iterator;

/**
 * Extended iterator interface used by various methods in {@link CoreParentNode}. It defines an
 * additional method that allows to replace the child node returned by the last call to
 * {@link #next()}. Implementations of this interface MUST properly implement the {@link #remove()}
 * method, i.e. they are not allowed to throw {@link UnsupportedOperationException}.
 * 
 * @author Andreas Veithen
 */
public interface ChildIterator<T extends CoreChildNode> extends Iterator<T> {
    void replace(CoreChildNode newNode) throws CoreModelException;
}
