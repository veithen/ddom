/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.backend.linkedlist.intf;

import com.google.code.ddom.stream.spi.XmlHandler;

public interface LLBuilder {
    /**
     * Enables pass-through mode for this builder. In this mode, events for the current node (and
     * its descendants) are passed directly to the specified handler instead of building nodes for
     * them. Pass-through mode is automatically disabled again after the builder receives the final
     * event for the current node.
     * 
     * @param handler
     *            the handler to send events to
     */
    void setPassThroughHandler(XmlHandler handler);
}
