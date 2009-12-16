package com.google.code.ddom.spi;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public abstract class TransformingClassLoader extends ClassLoader {
    public TransformingClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (needsTransformation(name)) {
            String resourceName = name.replace('.', '/') + ".class";
            InputStream in = super.getResourceAsStream(resourceName);
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                try {
                    byte[] classDef = transformClass(name, IOUtils.toByteArray(in));
                    return defineClass(name, classDef, 0, classDef.length);
                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                throw new ClassNotFoundException(name, ex);
            }
        } else {
            return super.loadClass(name);
        }
    }
    
    protected abstract boolean needsTransformation(String className);
    protected abstract byte[] transformClass(String className, byte[] classDef);
}
