package com.google.code.ddom.dom.impl;

import java.util.Map;

import org.w3c.dom.UserDataHandler;

public aspect UserDataSupport {
    private Map<String,Object> NodeImpl.userData;
    
    public final Object NodeImpl.getUserData(String key) {
        return userData == null ? null : userData.get(key);
    }

    public final Object NodeImpl.setUserData(String key, Object data, UserDataHandler handler) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
}
