/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;

import com.googlecode.ddom.frontend.dom.intf.NormalizationConfig;

public class DOMConfigurationImpl implements DOMConfiguration, NormalizationConfig {
    private static final int PARAM_CANONICAL_FORM = 0;
    private static final int PARAM_CDATA_SECTIONS = 1;
    private static final int PARAM_CHECK_CHARACTER_NORMALIZATION = 2;
    private static final int PARAM_COMMENTS = 3;
    private static final int PARAM_DATATYPE_NORMALIZATION = 4;
    private static final int PARAM_ELEMENT_CONTENT_WHITESPACE = 5;
    private static final int PARAM_ENTITIES = 6;
    private static final int PARAM_ERROR_HANDLER = 7;
    private static final int PARAM_INFOSET = 8;
    private static final int PARAM_NAMESPACES = 9;
    private static final int PARAM_NAMESPACE_DECLARATIONS = 10;
    private static final int PARAM_NORMALIZE_CHARACTERS = 11;
    private static final int PARAM_SPLIT_CDATA_SECTIONS = 12;
    private static final int PARAM_VALIDATE = 13;
    private static final int PARAM_VALIDATE_IF_SCHEMA = 14;
    private static final int PARAM_WELL_FORMED = 15;
    
    private static final String[] parameterNames = {
        "canonical-form",
        "cdata-sections",
        "check-character-normalization",
        "comments",
        "datatype-normalization",
        "element-content-whitespace",
        "entities",
        "error-handler",
        "infoset",
        "namespaces",
        "namespace-declarations",
        "normalize-characters",
        "split-cdata-sections",
        "validate",
        "validate-if-schema",
        "well-formed" };
    
    // List of parameters forced to false when the infoset parameter is set to true 
    private static final int[] infoSetParamsFalse = { PARAM_VALIDATE_IF_SCHEMA, PARAM_ENTITIES,
            PARAM_DATATYPE_NORMALIZATION, PARAM_CDATA_SECTIONS };
    
    // List of parameters forced to true when the infoset parameter is set to true 
    private static final int[] infoSetParamsTrue = { PARAM_NAMESPACE_DECLARATIONS, PARAM_WELL_FORMED,
            PARAM_ELEMENT_CONTENT_WHITESPACE, PARAM_COMMENTS, PARAM_NAMESPACES }; 
    
    private static final DOMStringList parameterList =
            new DOMStringListImpl(Arrays.asList(parameterNames));
    
    private static final Map<String,Integer> parameterIds = new HashMap<String,Integer>();
    
    static {
        for (int i=0; i<parameterNames.length; i++) {
            parameterIds.put(parameterNames[i], i);
        }
    }
    
    private DOMErrorHandler errorHandler;
    private boolean cdataSections = true;
    private boolean comments = true;
    private boolean elementContentWhitespace = true;
    private boolean entities = true;
    private boolean namespaces = true;
    private boolean namespaceDeclarations = true;
    private boolean splitCDataSections = true;
    private boolean wellFormed = true;
    
    public DOMErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public boolean isKeepCDATASections() {
        return cdataSections;
    }
    
    public boolean isKeepComments() {
        return comments;
    }
    
    public boolean isNormalizeNamespaces() {
        return namespaces;
    }

    public boolean isNamespaceDeclarations() {
        return namespaceDeclarations;
    }

    public boolean isSplitCDataSections() {
        return splitCDataSections;
    }

    public boolean isCheckWellFormed() {
        return wellFormed;
    }

    private int getParameterId(String name) {
        Integer id = parameterIds.get(name.toLowerCase());
        return id == null ? -1 : id;
    }
    
    public boolean canSetParameter(String name, Object value) {
        switch (getParameterId(name)) {
            case PARAM_CDATA_SECTIONS:
            case PARAM_COMMENTS:
            case PARAM_ELEMENT_CONTENT_WHITESPACE:
            case PARAM_ENTITIES:
            case PARAM_INFOSET:
            case PARAM_NAMESPACES:
            case PARAM_NAMESPACE_DECLARATIONS:
            case PARAM_SPLIT_CDATA_SECTIONS:
            case PARAM_WELL_FORMED:
                return value instanceof Boolean;
            case PARAM_ERROR_HANDLER:
                return value == null || value instanceof DOMErrorHandler;
            case PARAM_CANONICAL_FORM:
            case PARAM_CHECK_CHARACTER_NORMALIZATION:
            case PARAM_DATATYPE_NORMALIZATION:
            case PARAM_NORMALIZE_CHARACTERS:
            case PARAM_VALIDATE:
            case PARAM_VALIDATE_IF_SCHEMA:
                return Boolean.FALSE.equals(value);
            default:
                return false;
        }
    }

    private Object getParameter(int parameterId) throws DOMException {
        switch (parameterId) {
            case PARAM_CDATA_SECTIONS:
                return cdataSections;
            case PARAM_COMMENTS:
                return comments;
            case PARAM_ELEMENT_CONTENT_WHITESPACE:
                return elementContentWhitespace;
            case PARAM_ENTITIES:
                return entities;
            case PARAM_ERROR_HANDLER:
                return errorHandler;
            case PARAM_INFOSET:
                for (int paramId : infoSetParamsFalse) {
                    if ((Boolean)getParameter(paramId)) {
                        return false;
                    }
                }
                for (int paramId : infoSetParamsTrue) {
                    if (!(Boolean)getParameter(paramId)) {
                        return false;
                    }
                }
                return true;
            case PARAM_NAMESPACES:
                return namespaces;
            case PARAM_NAMESPACE_DECLARATIONS:
                return namespaceDeclarations;
            case PARAM_SPLIT_CDATA_SECTIONS:
                return splitCDataSections;
            case PARAM_WELL_FORMED:
                return wellFormed;
            case PARAM_CANONICAL_FORM:
            case PARAM_CHECK_CHARACTER_NORMALIZATION:
            case PARAM_DATATYPE_NORMALIZATION:
            case PARAM_NORMALIZE_CHARACTERS:
            case PARAM_VALIDATE:
            case PARAM_VALIDATE_IF_SCHEMA:
                return Boolean.FALSE;
            default:
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
    }

    public Object getParameter(String name) throws DOMException {
        return getParameter(getParameterId(name));
    }
    
    private void setParameter(int parameterId, Object value) throws DOMException {
        switch (parameterId) {
            case PARAM_CDATA_SECTIONS:
                cdataSections = (Boolean)value;
                break;
            case PARAM_COMMENTS:
                comments = (Boolean)value;
                break;
            case PARAM_ELEMENT_CONTENT_WHITESPACE:
                elementContentWhitespace = (Boolean)value;
                break;
            case PARAM_ENTITIES:
                entities = (Boolean)value;
                break;
            case PARAM_ERROR_HANDLER:
                errorHandler = (DOMErrorHandler)value;
                break;
            case PARAM_INFOSET:
                if ((Boolean)value) {
                    for (int paramId : infoSetParamsFalse) {
                        setParameter(paramId, false);
                    }
                    for (int paramId : infoSetParamsTrue) {
                        setParameter(paramId, true);
                    }
                }
                break;
            case PARAM_NAMESPACES:
                namespaces = (Boolean)value;
                break;
            case PARAM_NAMESPACE_DECLARATIONS:
                namespaceDeclarations = (Boolean)value;
                break;
            case PARAM_SPLIT_CDATA_SECTIONS:
                splitCDataSections = (Boolean)value;
                break;
            case PARAM_WELL_FORMED:
                wellFormed = (Boolean)value;
                break;
            case PARAM_CANONICAL_FORM:
            case PARAM_CHECK_CHARACTER_NORMALIZATION:
            case PARAM_DATATYPE_NORMALIZATION:
            case PARAM_NORMALIZE_CHARACTERS:
            case PARAM_VALIDATE:
            case PARAM_VALIDATE_IF_SCHEMA:
                if (!Boolean.FALSE.equals(value)) {
                    throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
                }
                break;
            default:
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
    }

    public void setParameter(String name, Object value) throws DOMException {
        setParameter(getParameterId(name), value);
    }
    
    public DOMStringList getParameterNames() {
        return parameterList;
    }
}
