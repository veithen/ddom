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
package com.googlecode.ddom.backend;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field for injection by the weaver. The injection mechanism is used to inject front-end
 * specific resources into the back-end classes. The weaver generates code that initializes fields
 * having this annotation. Currently, only {@link com.googlecode.ddom.core.ext.ModelExtension}
 * instances can be injected. Note that injection only works in weavable classes.
 * 
 * @author Andreas Veithen
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Inject {
}
