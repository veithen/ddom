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
package com.google.code.ddom.spi;

public class ClassLoaderUtils {
    private ClassLoaderUtils() {}
    
    /**
     * Translate a class name into the resource name of the corresponding <tt>.class</tt> file. E.g.
     * <tt>java.lang.String</tt> would be translated into <tt>java/lang/String.class</tt>.
     * 
     * @param className the class name
     * @return the resource name of the class file
     */
    public static String getResourceNameForClassName(String className) {
        return className.replace('.', '/') + ".class";
    }
}
