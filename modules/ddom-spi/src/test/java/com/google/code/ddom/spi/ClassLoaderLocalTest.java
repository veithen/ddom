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

import org.junit.Assert;
import org.junit.Test;

public class ClassLoaderLocalTest {
    /**
     * Test that {@link ClassLoaderLocal} works correctly with the system class loader, more
     * precisely with the class loader that loaded the {@link ClassLoaderLocal} class.
     */
    @Test
    public void testGetSetWithSystemClassLoader() {
        ClassLoaderLocal<String> local1 = new ClassLoaderLocal<String>();
        ClassLoaderLocal<String> local2 = new ClassLoaderLocal<String>();
        ClassLoader cl = ClassLoaderLocal.class.getClassLoader();
        local1.put(cl, "test1");
        local2.put(cl, "test2");
        Assert.assertEquals("test1", local1.get(cl));
        Assert.assertEquals("test2", local2.get(cl));
    }
    
    /**
     * Use two sibling class loaders to check that values stored by {@link ClassLoaderLocal} are
     * indeed local to the class loader.
     */
    @Test
    public void testGetSetWithSiblingClassLoaders() {
        ClassLoader cl1 = new ClassLoader(ClassLoaderLocal.class.getClassLoader()) {};
        ClassLoader cl2 = new ClassLoader(ClassLoaderLocal.class.getClassLoader()) {};
        ClassLoaderLocal<String> local = new ClassLoaderLocal<String>();
        local.put(cl1, "test1");
        local.put(cl2, "test2");
        Assert.assertEquals("test1", local.get(cl1));
        Assert.assertEquals("test2", local.get(cl2));
    }
    
    /**
     * Use two class loaders in parent-child relationship to check that values stored by
     * {@link ClassLoaderLocal} are indeed local to the class loader.
     */
    @Test
    public void testGetSetWithHierarchicalClassLoaders() {
        ClassLoader cl1 = new ClassLoader(ClassLoaderLocal.class.getClassLoader()) {};
        ClassLoader cl2 = new ClassLoader(cl1) {};
        ClassLoaderLocal<String> local = new ClassLoaderLocal<String>();
        local.put(cl1, "test1");
        local.put(cl2, "test2");
        Assert.assertEquals("test1", local.get(cl1));
        Assert.assertEquals("test2", local.get(cl2));
    }
    
    /**
     * Test that {@link ClassLoaderLocal} releases objects if and only if the class loader is no
     * longer used. This test fails with a naive implementation of {@link ClassLoaderLocal} based
     * on {@link java.util.WeakHashMap}.
     * 
     * @throws Exception
     */
    @Test
    public void testGarbageCollection() throws Exception {
        final Class<?> dummyClass = DummyClass.class;
        ClassLoaderLocal<Object> local = new ClassLoaderLocal<Object>();
        ClassLoader cl0 = null;
        for (int i=0; i<1024; i++) {
            ClassLoader cl = new TransformingClassLoader(dummyClass.getClassLoader()) {
                @Override
                protected boolean needsTransformation(String className) {
                    return className.equals(dummyClass.getName());
                }
                
                @Override
                protected byte[] transformClass(String className, byte[] classDef) {
                    return classDef;
                }
            };
            Class<?> dummyClass2 = cl.loadClass(dummyClass.getName());
            Assert.assertNotSame(dummyClass, dummyClass2);
            // If entries are not released properly, this will eventually cause an OutOfMemoryError.
            // (For the reason see "Implementation note" in the WeakHashMap Javadoc)
            local.put(cl, dummyClass2.newInstance());
            // Keep the reference to at least one ClassLoader
            if (cl0 == null) {
                cl0 = cl;
            }
        }
        // The value linked to the one 
        Assert.assertNotNull(local.get(cl0));
    }
}
