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
package com.google.code.ddom.domts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestSink;
import org.w3c.domts.DOMTestSuite;

public class FilteredDOMTestSuite extends DOMTestSuite {
    private final DOMTestSuite parent;
    private final Set<Class> excludes = new HashSet<Class>();
    private final List<Pattern> excludePatterns = new ArrayList<Pattern>();
    
    public FilteredDOMTestSuite(DOMTestDocumentBuilderFactory factory, DOMTestSuite parent) {
        super(factory);
        this.parent = parent;
    }

    public void addExclude(Class exclude) {
        excludes.add(exclude);
    }
    
    public void addExclude(String pattern) {
        excludePatterns.add(Pattern.compile(pattern));
    }

    @Override
    public String getTargetURI() {
        return parent.getTargetURI();
    }

    @Override
    public void build(DOMTestSink sink) {
        parent.build(new FilteredDOMTestSink(sink, excludes, excludePatterns));
    }
}
