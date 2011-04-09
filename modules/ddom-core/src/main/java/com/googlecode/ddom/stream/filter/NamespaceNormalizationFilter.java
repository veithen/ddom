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
package com.googlecode.ddom.stream.filter;

import com.googlecode.ddom.stream.SimpleXmlFilter;
import com.googlecode.ddom.stream.StreamException;

/**
 * Filter that replaces namespace unaware events into namespace aware events.
 */
// TODO: check for prefixed names and support elements
public class NamespaceNormalizationFilter extends SimpleXmlFilter {
    @Override
    protected void startAttribute(String name, String type) throws StreamException {
        super.startAttribute("", name, "", type);
    }
}
