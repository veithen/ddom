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
package com.google.code.ddom.core;

import com.google.code.ddom.core.ext.ModelExtension;

/**
 * Node factory. The frontend code MUST use this interface to create new nodes. To do so, it MUST
 * obtain an instance using {@link CoreDocument#getDocumentFactory()}. On the other hand, the frontend
 * MUST NOT assume that all nodes are created using this factory. The backend internally MAY create
 * nodes without using this factory, in particular during deferred parsing. Thus, frontend aspects
 * SHOULD NOT apply advices to this interface or its implementing classes.
 * <p>
 * Parameters passed to methods defined by this interface are not required to be canonicalized. It
 * is the responsibility of the implementation to canonicalize names, namespace URIs and prefixes as
 * necessary.
 * 
 * @author Andreas Veithen
 */
public interface DocumentFactory {
    CoreDocument createDocument();
    CoreDocument createDocument(ModelExtension modelExtension);
}
