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
package com.google.code.ddom.backend;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

class NullExtensionFactoryInvocationHandler implements InvocationHandler {
    private final Map<Method,Constructor<?>> constructorMap;

    public NullExtensionFactoryInvocationHandler(Map<Method,Constructor<?>> constructorMap) {
        this.constructorMap = constructorMap;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args[0] != null) {
            throw new IllegalArgumentException("No extensions defined; expected null");
        } else {
            Object[] constructorArgs = new Object[args.length-1];
            System.arraycopy(args, 1, constructorArgs, 0, args.length-1);
            return constructorMap.get(method).newInstance(constructorArgs);
        }
    }
}
