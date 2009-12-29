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
package com.google.code.ddom.xsltts;

import java.net.URL;

import com.google.code.ddom.collections.Identifiable;

public class XSLTConformanceTest implements Identifiable<String> {
    private final String id;
    private final boolean errorScenario;
    private final boolean inDoubt;
    private final URL input;
    private final URL stylesheet;
    private final URL output;
    private final String compare; // TODO: doesn't seem to be reliable
    
    XSLTConformanceTest(String id, boolean errorScenario, boolean inDoubt, URL input, URL stylesheet, URL output, String compare) {
        this.id = id;
        this.errorScenario = errorScenario;
        this.inDoubt = inDoubt;
        this.input = input;
        this.stylesheet = stylesheet;
        this.output = output;
        this.compare = compare;
    }
    
    public String getId() {
        return id;
    }

    public boolean isErrorScenario() {
        return errorScenario;
    }

    public boolean isInDoubt() {
        return inDoubt;
    }

    public URL getInput() {
        return input;
    }

    public URL getStylesheet() {
        return stylesheet;
    }

    public URL getOutput() {
        return output;
    }
}
