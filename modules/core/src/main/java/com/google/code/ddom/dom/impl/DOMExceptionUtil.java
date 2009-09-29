package com.google.code.ddom.dom.impl;

import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.w3c.dom.DOMException;

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
}
