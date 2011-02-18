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
package com.googlecode.ddom.tests.jaxrpc.calculator;

public class PerfDataCollector {
    private static Timestamps lastPerfData;
    
    public synchronized static void clearPerfData() {
        lastPerfData = null;
    }
    
    public synchronized static void reportPerfData(Timestamps timestamps) {
        lastPerfData = timestamps;
    }
    
    public synchronized static Timestamps getLastPerfData() {
        return lastPerfData;
    }
}
