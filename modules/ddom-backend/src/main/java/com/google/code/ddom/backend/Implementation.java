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
package com.google.code.ddom.backend;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a back-end class as the implementation of a particular Core Model interface.
 * This annotation must be applied to one and only one implementation of the following
 * interfaces:
 * <ul>
 * <li>{@link CoreCDATASection}
 * <li>{@link CoreComment}
 * <li>{@link CoreDocumentTypeDeclaration}
 * <li>{@link CoreEntityReference}
 * <li>{@link CoreNamespaceDeclaration}
 * <li>{@link CoreNSAwareAttribute}
 * <li>{@link CoreNSAwareElement}
 * <li>{@link CoreNSUnawareAttribute}
 * <li>{@link CoreNSUnawareElement}
 * <li>{@link CoreProcessingInstruction}
 * <li>{@link CoreText}
 * </ul>
 * The weaver uses this information to generate the model extension classes.
 * 
 * @author Andreas Veithen
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Implementation {

}
