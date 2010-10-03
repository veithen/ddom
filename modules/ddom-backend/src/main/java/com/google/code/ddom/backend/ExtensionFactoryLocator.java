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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public final class ExtensionFactoryLocator {
    private ExtensionFactoryLocator() {}
    
    public static <T> T locate(Class<T> factoryInterface) {
        Class<?> implClass;
        try {
            implClass = factoryInterface.getClassLoader().loadClass(factoryInterface.getName() + "$$Impl");
        } catch (ClassNotFoundException ex) {
            return createNullExtensionFactory(factoryInterface);
        }
        Field instanceField;
        try {
            instanceField = implClass.getField("INSTANCE");
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException(implClass.getName() + " doesn't have a field called INSTANCE");
        }
        try {
            return factoryInterface.cast(instanceField.get(null));
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(instanceField + " is not accessible");
        }
    }
    
    private static <T> T createNullExtensionFactory(Class<T> factoryInterface) {
        try {
            Map<Method,Constructor<?>> constructorMap = new HashMap<Method,Constructor<?>>();
            for (Method method : factoryInterface.getMethods()) {
                Class<?> implementationClass = method.getReturnType();
                Class<?>[] factoryMethodParameterTypes = method.getParameterTypes();
                Class<?>[] constructorParameterTypes = new Class<?>[factoryMethodParameterTypes.length-1];
                System.arraycopy(factoryMethodParameterTypes, 1, constructorParameterTypes, 0, factoryMethodParameterTypes.length-1);
                constructorMap.put(method, implementationClass.getConstructor(constructorParameterTypes));
            }
            return factoryInterface.cast(Proxy.newProxyInstance(ExtensionFactoryLocator.class.getClassLoader(),
                    new Class<?>[] { factoryInterface }, new NullExtensionFactoryInvocationHandler(constructorMap)));
        } catch (NoSuchMethodException ex) {
            throw new NoSuchMethodError(ex.getMessage());
        }
    }
}
