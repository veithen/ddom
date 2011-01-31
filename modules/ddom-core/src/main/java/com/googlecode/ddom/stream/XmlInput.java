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
package com.googlecode.ddom.stream;

public abstract class XmlInput extends XmlValve {
    private final DelegatingXmlHandler handler = new DelegatingXmlHandler();
    private XmlValve nextValve;
    
    @Override
    final void doAppend(XmlValve valve) {
        if (nextValve == null) {
            nextValve = valve;
        } else {
            nextValve.append(valve);
        }
    }

    @Override
    final XmlHandler doConnect(Stream stream) {
        if (nextValve == null) {
            throw new IllegalStateException("Pipeline is not terminated by an XmlOutput");
        }
        handler.setDelegate(nextValve.connect(stream));
        return null;
    }
    
    public final void addFilter(XmlFilter filter) {
        append(filter);
    }
    
    /**
     * Get the {@link XmlHandler} object that the implementation must use to produce
     * events. This method completes successfully even if the {@link XmlInput} has
     * not yet been connected to an {@link XmlOutput}. The implementation also guarantees
     * that the same object is returned over the whole lifecycle of the {@link XmlInput}
     * object.
     * 
     * @return the {@link XmlHandler} object
     */
    protected final XmlHandler getHandler() {
        return handler;
    }
    
    /**
     * Instructs the implementation to produce more XML events. An invocation of this method must
     * result in one or more method calls to the {@link XmlHandler} instance returned by
     * {@link #getHandler()}.
     * <p>
     * If the implementation produced more than one event, then it should make sure that the last
     * event corresponds to the same information item as the first one. This is not a strict
     * requirement, but the pass-through logic in the current builder implementation assumes that
     * this method behaves like this.
     * 
     * @throws StreamException
     */
    protected abstract void proceed() throws StreamException;
    
    /**
     * 
     * @return <code>false</code> if there are more events to consume; <code>true</code> if the end
     *         of the document has been reached
     */
    public final boolean isComplete() {
        return handler.isComplete();
    }
    
    public abstract void dispose();
}
