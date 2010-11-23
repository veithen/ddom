package com.googlecode.ddom.weaver.asm;

import java.util.LinkedList;
import java.util.List;

import com.google.code.ddom.frontend.Mixin;

@Mixin(IBase.class)
public abstract class BaseMixin implements IBase, IBaseMixin {
    private final List<String> list = new LinkedList<String>();
    
    public void addItem(String item) {
        list.add(item);
    }
}
