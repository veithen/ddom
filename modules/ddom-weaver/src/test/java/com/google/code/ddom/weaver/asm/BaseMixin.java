package com.google.code.ddom.weaver.asm;

import java.util.LinkedList;
import java.util.List;

import com.google.code.ddom.spi.model.Mixin;

@Mixin(IBase.class)
public abstract class BaseMixin implements IBase, IBaseMixin {
    private final List<String> list = new LinkedList<String>();
    
    public void addItem(String item) {
        list.add(item);
    }
}
