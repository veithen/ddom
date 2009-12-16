package com.google.code.ddom.spi;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ClassLoaderLocalTest {
    @Test @Ignore
    public void test() throws Exception {
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
