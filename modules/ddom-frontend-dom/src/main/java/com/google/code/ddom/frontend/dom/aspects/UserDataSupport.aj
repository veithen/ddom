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
package com.google.code.ddom.frontend.dom.aspects;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.UserDataHandler;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMNode;
import com.google.code.ddom.frontend.dom.support.UserData;

public aspect UserDataSupport {
    private Map<DOMNode,Map<String,UserData>> DOMDocument.userDataMap;
    
    public final Map<String,UserData> DOMDocument.getUserDataMap(DOMNode node, boolean create) {
        if (userDataMap == null) {
            if (!create) {
                return null;
            }
            userDataMap = new HashMap<DOMNode,Map<String,UserData>>();
        }
        Map<String,UserData> mapForNode = userDataMap.get(node);
        if (mapForNode == null) {
            if (!create) {
                return null;
            }
            mapForNode = new HashMap<String,UserData>();
            userDataMap.put(node, mapForNode);
        }
        return mapForNode;
    }
    
    public final Object DOMNode.getUserData(String key) {
        Map<String,UserData> userDataMap = ((DOMDocument)getDocument()).getUserDataMap(this, false);
        if (userDataMap == null) {
            return null;
        } else {
            UserData userData = userDataMap.get(key);
            return userData == null ? null : userData.getData();
        }
    }

    public final Object DOMNode.setUserData(String key, Object data, UserDataHandler handler) {
        UserData userData;
        if (data == null) {
            Map<String,UserData> userDataMap = ((DOMDocument)getDocument()).getUserDataMap(this, false);
            if (userDataMap != null) {
                userData = userDataMap.remove(key);
            } else {
                userData = null;
            }
        } else {
            Map<String,UserData> userDataMap = ((DOMDocument)getDocument()).getUserDataMap(this, true);
            userData = userDataMap.put(key, new UserData(data, handler));
        }
        return userData == null ? null : userData.getData();
    }
}
