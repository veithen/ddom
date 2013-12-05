/*
 * Copyright 2013 Andreas Veithen
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
package com.googlecode.ddom.backend.testsuite;

public enum Event {
    START_DOCUMENT,
    START_FRAGMENT,
    XML_DECLARATION,
    START_DOCUMENT_TYPE_DECLARATION,
    END_DOCUMENT_TYPE_DECLARATION,
    START_NS_UNAWARE_ELEMENT,
    START_NS_AWARE_ELEMENT,
    END_ELEMENT,
    START_NS_UNAWARE_ATTRIBUTE,
    START_NS_AWARE_ATTRIBUTE,
    START_NAMESPACE_DECLARATION,
    END_ATTRIBUTE,
    ATTRIBUTES_COMPLETED,
    CHARACTER_DATA,
    START_PROCESSING_INSTRUCTION,
    END_PROCESSING_INSTRUCTION,
    START_COMMENT,
    END_COMMENT,
    START_CDATA_SECTION,
    END_CDATA_SECTION,
    ENTITY_REFERENCE,
    COMPLETED
}
