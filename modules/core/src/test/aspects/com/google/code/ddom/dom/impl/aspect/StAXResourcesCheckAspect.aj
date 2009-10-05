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
package com.google.code.ddom.dom.impl.aspect;

import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;

import org.junit.internal.runners.MethodRoadie;
import org.w3c.domts.JUnitTestCaseAdapter;

public aspect StAXResourcesCheckAspect {
    private final Set<XMLStreamReader> openReaders = new HashSet<XMLStreamReader>();
    private int level;

    void around(): execution(void MethodRoadie.run()) || execution(void JUnitTestCaseAdapter.runTest()) {
        level++;
        proceed();
        level--;
        if (level == 0 && !openReaders.isEmpty()) {
            System.out.println("Unclosed stream readers detected!");
            openReaders.clear();
        }
    }
    
    after() returning(XMLStreamReader reader): call(XMLStreamReader XMLInputFactory.*(..)) {
        if (level == 0) {
            System.out.println("Stream reader created outside of a test case?!?");
        } else {
            openReaders.add(reader);
        }
    }
    
    after(XMLStreamReader reader): call(void XMLStreamReader.close()) && target(reader) {
        openReaders.remove(reader);
    }
}
