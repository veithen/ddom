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

import java.lang.reflect.Field;

public final class ExtensionFactoryLocator {
    private ExtensionFactoryLocator() {}
    
    public static <T> T locate(Class<T> factoryInterface) {
        Class<?> implClass;
        try {
            implClass = factoryInterface.getClassLoader().loadClass(factoryInterface.getName() + "$$Impl");
        } catch (ClassNotFoundException ex) {
            // TODO: here we should fall back to a dynamic proxy
            return null;
//            throw new IllegalArgumentException();
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
}
