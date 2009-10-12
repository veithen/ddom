package com.google.code.ddom.weaver;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.aspectj.weaver.loadtime.ClassPreProcessor;

import com.google.code.ddom.spi.model.Model;

public class ModelWeaver extends ClassLoader {
    private final Model model;
    private final ClassPreProcessor preProcessor;
    
    public ModelWeaver(ClassLoader parent, Model model) {
        super(parent);
        this.model = model;
        preProcessor = new Aj(new ModelWeaverContext(this));
    }
    
    public Model getModel() {
        return model;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith("com.google.code.ddom.core.model.") || name.startsWith("com.google.code.ddom.dom.impl.")) { // TODO: hardcoded aspects package!!!
            System.out.println("Weaving " + name + "...");
            String resourceName = name.replace('.', '/') + ".class";
            InputStream in = super.getResourceAsStream(resourceName);
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                try {
                    byte[] classDef = preProcessor.preProcess(name, IOUtils.toByteArray(in), this);
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
}
