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
package com.google.code.ddom;

/**
 * Tracks with options in an {@link Options} object have been processed. Instances of this interface
 * are produced by {@link Options#createTracker()} and are used to check that all options flagged as
 * "must understand" have been processed.
 * 
 * @author Andreas Veithen
 */
public interface OptionsTracker {
    <T> T get(Class<T> key);
    void markProcessed(Class<?> key);
    <T> T getAndMarkAsProcessed(Class<T> key);
    void finish() throws UnprocessedOptionException;
}
