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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;

public aspect ProcessingInstructionSupport {
    public final String DOMProcessingInstruction.getData() {
        return coreGetData();
    }

    public final void DOMProcessingInstruction.setData(String data) throws DOMException {
        coreSetData(data);
    }

    public final String DOMProcessingInstruction.getTarget() {
        return coreGetTarget();
    }
}
