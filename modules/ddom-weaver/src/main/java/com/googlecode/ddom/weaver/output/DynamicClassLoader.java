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
package com.googlecode.ddom.weaver.output;

/**
 * Class loader that accepts class definitions and loads them. This class simply exposes the
 * protected {@link ClassLoader#defineClass(String, byte[])} through the
 * {@link ClassDefinitionProcessor} interface. Note that this only works because the contract of
 * {@link ClassDefinitionProcessor} specifies that superclasses and implemented interfaces must be
 * submitted first.
 */
public class DynamicClassLoader extends ClassLoader implements ClassDefinitionProcessor {
    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void processClassDefinition(String name, byte[] definition) {
        defineClass(name, definition, 0, definition.length);
    }
}
