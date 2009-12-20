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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.tools.jdi.LinkedHashMap;

public class ClassUtilsTest {
    interface I {}
    class S {}
    class A extends S implements I {}
    class B extends S {}
    class X extends B {}
    
    private static <T> boolean isBefore(List<T> list, T element1, T element2) {
        boolean element1Found = false;
        for (T element : list) {
            if (element.equals(element1)) {
                element1Found = true;
            } else if (element.equals(element2)) {
                return element1Found;
            }
        }
        return false;
    }
    
    @Test
    public void testSortHierarchically() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(I.class);
        classes.add(S.class);
        classes.add(A.class);
        classes.add(B.class);
        classes.add(X.class);
        List<Class<?>> list = ClassUtils.sortHierarchically(classes);
        Assert.assertEquals(classes.size(), list.size());
        Assert.assertTrue(isBefore(list, S.class, A.class));
        Assert.assertTrue(isBefore(list, I.class, A.class));
        Assert.assertTrue(isBefore(list, S.class, B.class));
        Assert.assertTrue(isBefore(list, B.class, X.class));
    }
    
    @Test
    public void testIsSuperclass() {
        // Interface with interface
        Assert.assertTrue(ClassUtils.isSuperclass(Collection.class, List.class));
        Assert.assertFalse(ClassUtils.isSuperclass(List.class, Collection.class));
        Assert.assertTrue(ClassUtils.isSuperclass(Collection.class, SortedSet.class));
        // Class with interface
        Assert.assertTrue(ClassUtils.isSuperclass(Set.class, HashSet.class));
        Assert.assertFalse(ClassUtils.isSuperclass(HashSet.class, Set.class));
        Assert.assertTrue(ClassUtils.isSuperclass(Collection.class, HashSet.class));
        // Class with class
        Assert.assertTrue(ClassUtils.isSuperclass(Object.class, String.class));
        Assert.assertFalse(ClassUtils.isSuperclass(String.class, Object.class));
        Assert.assertTrue(ClassUtils.isSuperclass(AbstractMap.class, LinkedHashMap.class));
    }
}
