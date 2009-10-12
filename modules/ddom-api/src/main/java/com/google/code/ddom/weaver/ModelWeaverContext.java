package com.google.code.ddom.weaver;

import java.util.Collections;
import java.util.List;

import org.aspectj.weaver.loadtime.DefaultWeavingContext;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.tools.WeavingAdaptor;

import com.google.code.ddom.spi.model.Model;

public class ModelWeaverContext extends DefaultWeavingContext {
    public ModelWeaverContext(ClassLoader loader) {
        super(loader);
    }

    @Override
    public List<Definition> getDefinitions(ClassLoader loader, WeavingAdaptor adaptor) {
        Model model = ((ModelWeaver)loader).getModel();
        Definition definition = new Definition();
        definition.getAspectClassNames().addAll(model.getAspectClasses());
        return Collections.singletonList(definition);
    }
}
