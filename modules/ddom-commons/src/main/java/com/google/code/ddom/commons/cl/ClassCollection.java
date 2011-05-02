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
package com.google.code.ddom.commons.cl;

import java.util.Collection;

public interface ClassCollection {
    Collection<ClassRef> getClassRefs();
    
    // TODO: need to specify when exceptions may be thrown (during execution of this method and/or when requesting items from the collection)
    Collection<Class<?>> getClasses();
    
    /**
     * Check whether all classes in this collection belong to the given package.
     * 
     * @param pkg
     *            the package name
     * @return <code>true</code> if every class in the collection belongs to the given package or
     *         one of its subpackages, <code>false</code> otherwise
     */
    boolean isInPackage(String pkg);
    
    /**
     * Get the name of the root package for this class collection. The root package is defined by
     * the following two properties:
     * <ul>
     * <li>The root package contains all classes in the collection. More precisely, every class in
     * the collection belongs to the root package or one of its subpackages.
     * <li>There is no subpackage of the root package that contains all classes in the collection.
     * </ul>
     * 
     * @return the root package name, <code>null</code> if the collection is empty (in which case
     *         the root package is undefined), or the empty string if there are classes from at
     *         least two different top level packages in the collection
     */
    String getRootPackageName();
}
