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
package com.googlecode.ddom.weaver;

/**
 * Processes class definitions. An implementation of this interface typically loads the class
 * definition into a class loader or writes it into a class file. If multiple classes are submitted
 * to the processor, then the superclass and the implemented interfaces of a class must be submitted
 * before the class itself.
 */
public interface ClassDefinitionProcessor {
    /**
     * Process a class definition.
     * 
     * @param name
     *            the fully qualified class name
     * @param definition
     *            the class definition, i.e. the content of the class file
     * @throws ClassDefinitionProcessorException
     *             if an error occurs while processing the class definition
     */
    void processClassDefinition(String name, byte[] definition) throws ClassDefinitionProcessorException;
}
