/*
 * Copyright 2009-2011 Andreas Veithen
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
/**
 * Contains the core model API.
 * 
 * 
 * <h3>Namespace conventions</h3>
 * <ul>
 * <li>By convention, the core model uses the empty string (instead of <code>null</code>) as the namespace URI for
 * named nodes that have no namespace. If also uses the empty string as the prefix for nodes
 * that have no namespace or are in the default namespace. These two conventions apply to the following methods
 * (non exhaustive list):
 * <ul>
 * <li>{@link com.googlecode.ddom.core.NodeFactory#createElement(CoreDocument, String, String, String)}
 * <li>{@link com.googlecode.ddom.core.NodeFactory#createAttribute(CoreDocument, String, String, String, String, String)}
 * <li>{@link com.googlecode.ddom.core.NodeFactory#createNamespaceDeclaration(CoreDocument, String, String)}
 * <li>{@link com.googlecode.ddom.core.CoreNSAwareNamedNode#coreGetNamespaceURI()}
 * <li>{@link com.googlecode.ddom.core.CoreNSAwareNamedNode#coreGetPrefix()}
 * <li>{@link com.googlecode.ddom.core.CoreNSAwareNamedNode#coreSetNamespaceURI(String)}
 * <li>{@link com.googlecode.ddom.core.CoreNSAwareNamedNode#coreSetPrefix(String)}
 * <li>{@link com.googlecode.ddom.core.CoreNamespaceDeclaration#coreGetDeclaredNamespaceURI()}
 * <li>{@link com.googlecode.ddom.core.CoreNamespaceDeclaration#coreGetDeclaredPrefix()}
 * </ul>
 * For methods that take a prefix or namespace URI as a parameter, the behavior is undefined if
 * <code>null</code> is used instead of the empty string. It is the responsibility of the frontend
 * to conform to these conventions. The rationale for this choice is that some frontend APIs use
 * different conventions (allowing or requiring empty strings in the cases described above),
 * in which case the frontend must translate prefixes and namespace URIs. By doing so, it naturally
 * enforces the conventions of the core model. Requiring the backend to validate namespace prefixes and
 * URIs would then add an unnecessary overhead.
 */
package com.googlecode.ddom.core;