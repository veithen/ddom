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
package com.googlecode.ddom.core;

import com.googlecode.ddom.stream.XmlSource;

/**
 * Indicates that there is a mismatch between the element name specified at construction time and
 * the actual name returned by the {@link XmlSource} set with
 * {@link CoreElement#coreSetSource(XmlSource)}.
 * 
 * @author Andreas Veithen
 */
public class ElementNameMismatchException extends DeferredBuildingException {
    private static final long serialVersionUID = 2806120524411278142L;

    public ElementNameMismatchException(String message) {
        super(message);
    }
}
