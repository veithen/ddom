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
package com.google.code.ddom.spi.parser;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class ParserFactory {
    private static final Map<ClassLoader,ParserFactory> factories = Collections.synchronizedMap(new WeakHashMap<ClassLoader,ParserFactory>());
    
    private final Map<String,ParserProvider> providers = new LinkedHashMap<String,ParserProvider>();
    
    private ParserFactory() {}
    
    public static ParserFactory getInstance(ClassLoader classLoader) {
        ParserFactory factory = factories.get(classLoader);
        if (factory == null) {
            factory = new ParserFactory();
            
            // TODO: replace this by a service discovery algorithm
            try {
                factory.providers.put("stax", (ParserProvider)classLoader.loadClass("com.google.code.ddom.stax.StAXParserProvider").newInstance());
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            factories.put(classLoader, factory);
        }
        return factory;
    }
    
    
    public static ParserFactory getInstance() {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public Parser getParser(String providerName, Object source, Map<String,Object> properties) throws ParseException {
        ParserProvider provider = providers.get(providerName);
        return provider == null ? null : provider.getParser(source, properties);
    }
    
    public Parser getParser(Object source, Map<String,Object> properties) throws ParseException {
        for (ParserProvider provider : providers.values()) {
            Parser parser = provider.getParser(source, properties);
            if (parser != null) {
                return parser;
            }
        }
        return null;
    }
}
