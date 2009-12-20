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
package com.google.code.ddom.commons.cl;

import java.util.Collection;
import java.util.List;

import com.google.code.ddom.commons.dag.EdgeRelation;
import com.google.code.ddom.commons.dag.TopologicalSort;

public class ClassUtils {
    private static final EdgeRelation<Class<?>> inheritanceRelation = new EdgeRelation<Class<?>>() {
        public boolean isEdge(Class<?> from, Class<?> to) {
            if (to.isInterface()) {
                for (Class<?> iface : from.getInterfaces()) {
                    if (iface == to) {
                        return true;
                    }
                }
                return false;
            } else {
                return from.getSuperclass() == to;
            }
        }
    };
    
    private ClassUtils() {}
    
    /**
     * Sort a collection of classes using inheritance relationships. The implementation is based on
     * the fact that a class diagram is a directed acyclic graph (DAG) and that the reachability
     * relation in a DAG forms a partial order. This fact is then used to perform a topological
     * sort.
     * <p>
     * Note that inheritance relations with classes not present in the given collections are not
     * taken into account: if the collection contains A and C, and if there is a class B that
     * extends A and is the superclass of C, then the returned list may contain A and C in an
     * incorrect order.
     * 
     * @param classes
     *            the collection of classes to sort
     * @return a sorted list of classes, where every superclass comes before all of its subclasses
     */
    public static List<Class<?>> sortHierarchically(Collection<Class<?>> classes) {
        return TopologicalSort.sort(classes, inheritanceRelation);
    }
    
    /**
     * Determine if a class is a superclass of another one.
     * 
     * @param c1
     *            a class
     * @param c2
     *            a class
     * @return <code>true</code> if and only if <code>c1</code> is a superclass of <code>c2</code>
     */
    public static boolean isSuperclass(Class<?> c1, Class<?> c2) {
        if (c1.isInterface()) {
            for (Class<?> iface : c2.getInterfaces()) {
                if (iface == c1 || isSuperclass(c1, iface)) {
                    return true;
                }
            }
            return false;
        } else {
            // In interface can never extend a class
            if (c2.isInterface()) {
                return false;
            }
            Class<?> cls = c2;
            while (true) {
                cls = cls.getSuperclass();
                if (cls == null) {
                    return false;
                }
                if (c1 == cls) {
                    return true;
                }
            }
        }
    }
}
