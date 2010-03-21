package com.google.code.ddom.weaver.asm;

import java.util.LinkedList;
import java.util.List;

@Mixin(IBase.class)
public abstract class BaseMixin implements IBase, IBaseMixin {
    private final List<String> someList = new LinkedList<String>();
    
    public void mixin(String arg) {
        
    }
}
