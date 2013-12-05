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
package com.googlecode.ddom.core;

import java.util.Iterator;

/**
 * Unchecked exception wrapper for {@link CoreModelException}. It is used in contexts where checked
 * exception cannot be thrown, e.g. by the {@link Iterator#hasNext()}, {@link Iterator#next()} and
 * {@link Iterator#remove()} methods {@link ChildIterator} implementations.
 * 
 * @author Andreas Veithen
 */
public class CoreModelRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6782641155164610837L;

    public CoreModelRuntimeException(CoreModelException cause) {
        super(cause);
    }

    @Override
    public Throwable initCause(Throwable cause) {
        if (!(cause instanceof CoreModelException)) {
            throw new IllegalArgumentException();
        }
        return super.initCause(cause);
    }
    
    /**
     * Get the {@link CoreModelException} wrapped by this exception.
     * 
     * @return the wrapped exception
     */
    public CoreModelException getCoreModelException() {
        return (CoreModelException)getCause();
    }
}
