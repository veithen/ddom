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
package com.googlecode.ddom.saaj;

import javax.xml.soap.SOAPMessage;

import com.googlecode.ddom.saaj.compat.CompatibilityPolicy;

/**
 * Default {@link CompatibilityPolicy} implementation that strictly adheres to what is required by
 * the SAAJ specification.
 * 
 * @author Andreas Veithen
 */
public final class DefaultCompatibilityPolicy implements CompatibilityPolicy {
    public static CompatibilityPolicy INSTANCE = new DefaultCompatibilityPolicy();
    
    private DefaultCompatibilityPolicy() {}
    
    public SOAPMessage wrapMessage(SOAPMessage message) {
        return message;
    }
}
