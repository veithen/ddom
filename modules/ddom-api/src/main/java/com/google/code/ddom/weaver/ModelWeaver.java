package com.google.code.ddom.weaver;

import org.aspectj.weaver.loadtime.Aj;
import org.aspectj.weaver.loadtime.ClassPreProcessor;

import com.google.code.ddom.commons.cl.TransformingClassLoader;
import com.google.code.ddom.spi.model.Model;

public class ModelWeaver extends TransformingClassLoader {
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
    protected boolean needsTransformation(String className) {
        return className.startsWith("com.google.code.ddom.core.model.") || className.startsWith("com.google.code.ddom.dom.impl."); // TODO: hardcoded aspects package!!!
    }

    @Override
    protected byte[] transformClass(String className, byte[] classDef) {
        return preProcessor.preProcess(className, classDef, this);
    }
}
