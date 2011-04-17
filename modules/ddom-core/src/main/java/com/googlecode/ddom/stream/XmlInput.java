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

import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

public abstract class XmlInput extends XmlValve {
    private XmlValve nextValve;
    private XmlReader reader;
    boolean complete;
    
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
        reader = createReader(new XmlHandlerWrapper(nextValve.connect(stream)) {
            @Override
            public void completed() throws StreamException {
                super.completed();
                complete = true;
            }
        });
        return null;
    }
    
    public final void addFilter(XmlFilter filter) {
        append(filter);
    }
    
    protected abstract XmlReader createReader(XmlHandler handler);
    
    void proceed(boolean flush) throws StreamException {
        reader.proceed(flush);
    }

    /**
     * 
     * @return <code>false</code> if there are more events to consume; <code>true</code> if the end
     *         of the document has been reached
     */
    public final boolean isComplete() {
        return complete;
    }
    
    public abstract void dispose();
}
