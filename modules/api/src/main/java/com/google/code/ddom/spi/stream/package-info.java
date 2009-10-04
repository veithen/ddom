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
/**
 * Defines an abstract contract between a producer of XML data and its consumer.
 * The API defined in this package tries to be sufficiently general to take into account
 * the differences that exist between various parsers and between various types of consumers:
 * <ul>
 *   <li>Some parsers have a pull style API, while others use a push (callback) approach.
 *   <li>Some parsers produce attributes as individual events, while others produce them
 *   together with the owner element. In the same way, some consumers may be able
 *   to process attributes individually, while others require the whole set of attributes for
 *   a given element.
 *   <li>The data (mainly character data and attribute information) produced by the parser
 *   may be transient, i.e. only be available during the processing of the current event, or
 *   may have a longer lifespan, i.e. stored by the consumer and accessed at a later time.
 * </ul>
 * The API gives the parser and consumer some freedom to negotiate the best way to exchange
 * data, so that the incurred overhead is minimal.
 */
package com.google.code.ddom.spi.stream;
