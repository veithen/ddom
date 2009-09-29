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
package com.google.code.ddom.utils.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class that counts the number of invocations on one or more objects. It also counts the
 * number of exceptions thrown by these invocations. The class uses dynamic proxies to implement
 * this feature.
 * <p>
 * This class is thread safe.
 * 
 * @author Andreas Veithen
 */
public class InvocationCounter {
    private class InvocationHandlerImpl implements InvocationHandler {
        private final Object target;

        public InvocationHandlerImpl(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            invocationCount.incrementAndGet();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException ex) {
                exceptionCount.incrementAndGet();
                throw ex.getCause();
            }
        }
    }
    
    final AtomicInteger invocationCount = new AtomicInteger();
    final AtomicInteger exceptionCount = new AtomicInteger();
    
    /**
     * Create a new proxy. Every invocation of a method on this proxy increases the invocation count
     * by one unit. If the invocation results in an exception, then the exception counter is also
     * incremented.
     * 
     * @param <T> the interface type
     * @param iface the interface type
     * @param target the target instance
     * @return the proxy instance
     */
    public <T> T createProxy(Class<T> iface, T target) {
        // TODO: probably the proxy should implement ALL interfaces of the target object, not only the one the caller is interested in
        // TODO: add a note about this to the Javadoc
        return iface.cast(Proxy.newProxyInstance(InvocationCounter.class.getClassLoader(),
                new Class<?>[] { iface }, new InvocationHandlerImpl(target)));
    }

    /**
     * Get the number of invocations counted by this instance.
     * 
     * @return the number of invocations
     */
    public int getInvocationCount() {
        return invocationCount.get();
    }

    /**
     * Get the number of exceptions counted by this instance.
     * 
     * @return the number of exceptions
     */
    public int getExceptionCount() {
        return exceptionCount.get();
    }
    
    /**
     * Reset all counters to zero.
     */
    public void reset() {
        invocationCount.set(0);
        exceptionCount.set(0);
    }
}
