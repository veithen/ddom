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
package com.googlecode.ddom.frontend.dom.intf;

import org.w3c.dom.DOMErrorHandler;

public interface NormalizationConfig {
    NormalizationConfig DEFAULT = new NormalizationConfig() {
        public DOMErrorHandler getErrorHandler() { return null; }
        public boolean isKeepCDATASections() { return true; }
        public boolean isKeepComments() { return true; }
        public boolean isNormalizeNamespaces() { return false; }
        public boolean isSplitCDataSections() { return false; }
        public boolean isNamespaceDeclarations() { return false; }
        public boolean isCheckWellFormed() { return false; }
    };
    
    DOMErrorHandler getErrorHandler();
    boolean isKeepCDATASections();
    boolean isKeepComments();
    boolean isNormalizeNamespaces();
    boolean isSplitCDataSections();
    boolean isNamespaceDeclarations();
    boolean isCheckWellFormed();
}
