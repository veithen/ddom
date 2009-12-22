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
package com.google.code.ddom.spi.model;

import java.util.List;

// TODO: this API is a bit simplistic; we need at least to support the following features:
//        - a model configuration (so that aspects can be selected dynamically)
//        - we need to be able to mix models and to support dependencies (e.g. SAAJ -> DOM)
//        - mixing models must take into account that there may be overlap between them
//          (one model may define a method with the same signature and behavior as a method
//          in another model)
// TODO: this should be renamed "Frontend", while the term "model" should be reserved for plain XML and SOAP
public interface Model {
    List<String> getAspectClasses();
}
