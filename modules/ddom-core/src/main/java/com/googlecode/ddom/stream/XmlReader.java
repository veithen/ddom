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

public interface XmlReader {
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
     * @param flush
     *            The value of this parameter is set to <code>true</code> if the invocation is
     *            triggered by {@link Stream#flush()}. In this case, the implementation MAY choose
     *            to push all remaining events to the {@link XmlHandler} instead of producing
     *            individual events. The parameter is set to <code>false</code> if the invocation is
     *            triggered by {@link Stream#proceed()}. In this case, the implementation SHOULD
     *            attempt to limit the number of events produced.
     *            <p>
     *            The information provided by this parameter is only a hint and the implementation
     *            MAY choose to completely ignore it. It is useful if the implementation needs to
     *            integrate with a library that define different APIs for pull and push mode
     *            streaming and there is a potential performance benefit when using the push API.
     * 
     * @throws StreamException
     */
    void proceed(boolean flush) throws StreamException;
}
