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

/**
 * Translates {@link CoreModelException} instances into {@link RuntimeException} instances.
 * 
 * @author Andreas Veithen
 */
public interface ExceptionTranslator {
    /**
     * Default exception translator that wraps all exceptions in {@link CoreModelRuntimeException}
     * instances.
     */
    ExceptionTranslator DEFAULT = new ExceptionTranslator() {
        public RuntimeException toUncheckedException(CoreModelException ex) {
            return new CoreModelRuntimeException(ex);
        }
    };
    
    /**
     * Translate the given exception to an unchecked exception.
     * 
     * @param ex the original (checked) exception
     * @return the corresponding unchecked exception
     */
    RuntimeException toUncheckedException(CoreModelException ex);
}
