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

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;

/**
 * {@link StreamException} wrapper for {@link CoreModelException} used by the {@link XmlInput}
 * returned by {@link CoreParentNode#coreGetInput(boolean)}.
 * 
 * @author Andreas Veithen
 */
public class CoreModelStreamException extends StreamException {
    private static final long serialVersionUID = -7324484352634407874L;

    public CoreModelStreamException(CoreModelException cause) {
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
