/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.core;

public interface CoreDocumentFragment extends CoreParentNode {
    /**
     * Set a new owner document for this document fragment.
     * 
     * @param document
     *            the new owner document, or <code>null</code> if the document fragment will have
     *            it's own owner document (which may be created lazily later)
     */
    void coreSetOwnerDocument(CoreDocument document);
}
