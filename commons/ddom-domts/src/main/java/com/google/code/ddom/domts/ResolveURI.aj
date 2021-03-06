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

import java.net.URL;

import org.w3c.domts.DOMTest;

public privileged aspect ResolveURI {
    URL around(DOMTest test, String baseURI): execution(URL DOMTest.resolveURI(String)) && this(test) && args(baseURI) {
        String docURI = test.factory.addExtension(baseURI);
        Class<?> testClass = test.getClass();
        return testClass.getResource("/" + testClass.getPackage().getName().substring(14).replace('.', '/') + "/" + docURI);
    }
}
