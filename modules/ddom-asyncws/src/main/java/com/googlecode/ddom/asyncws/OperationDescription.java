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
package com.googlecode.ddom.asyncws;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class OperationDescription {
    private final Class<?> requestClass;
    private final Class<?> responseClass;
    
    public OperationDescription(Method method) {
        // TODO: implement various checks
        Type[] parameters = method.getGenericParameterTypes();
        requestClass = (Class<?>)parameters[0];
        responseClass = (Class<?>)((ParameterizedType)parameters[1]).getActualTypeArguments()[0];
    }

    public Class<?> getRequestClass() {
        return requestClass;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }
}
