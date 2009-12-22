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
package com.google.code.ddom.frontend.dom.support;

import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.w3c.dom.DOMException;

import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.HierarchyException;
import com.google.code.ddom.spi.model.NodeNotFoundException;
import com.google.code.ddom.spi.model.WrongDocumentException;

public class DOMExceptionUtil {
    private static final ResourceBundle messages =
            PropertyResourceBundle.getBundle(DOMExceptionUtil.class.getName());
    
    private static final String[] codeStrings = {
        "INDEX_SIZE_ERR",
        "DOMSTRING_SIZE_ERR",
        "HIERARCHY_REQUEST_ERR",
        "WRONG_DOCUMENT_ERR",
        "INVALID_CHARACTER_ERR",
        "NO_DATA_ALLOWED_ERR",
        "NO_MODIFICATION_ALLOWED_ERR",
        "NOT_FOUND_ERR",
        "NOT_SUPPORTED_ERR",
        "INUSE_ATTRIBUTE_ERR",
        "INVALID_STATE_ERR",
        "SYNTAX_ERR",
        "INVALID_MODIFICATION_ERR",
        "NAMESPACE_ERR",
        "INVALID_ACCESS_ERR",
        "VALIDATION_ERR",
        "TYPE_MISMATCH_ERR"
    };
    
    private DOMExceptionUtil() {}
    
    public static DOMException newDOMException(short code) {
        String key = codeStrings[code-1];
        String message;
        try {
            message = messages.getString(key);
        } catch (MissingResourceException ex) {
            message = null;
        }
        if (message == null) {
            return new DOMException(code, key);
        } else {
            return new DOMException(code, key + ": " + message);
        }
    }
    
    public static DOMException translate(CoreModelException ex) {
        if (ex instanceof NodeNotFoundException) {
            return newDOMException(DOMException.NOT_FOUND_ERR);
        } else if (ex instanceof HierarchyException) {
            return newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
        } else if (ex instanceof WrongDocumentException) {
            return newDOMException(DOMException.WRONG_DOCUMENT_ERR);
        } else {
            throw new IllegalArgumentException("Don't know how to translate " + ex.getClass().getName());
        }
    }
}
