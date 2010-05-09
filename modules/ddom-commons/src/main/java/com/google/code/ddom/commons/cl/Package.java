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
package com.google.code.ddom.commons.cl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.code.ddom.commons.io.URLUtils;

public class Package extends ClassCollection {
    private final ClassLoader classLoader;
    private final URL url;
    private final String name;
    
    Package(ClassLoader classLoader, URL url, String name) {
        this.classLoader = classLoader;
        this.url = url;
        this.name = name;
    }
    
    @Override
    public Collection<ClassRef> getClassRefs() {
        URL[] urlsInPackage;
        try {
            urlsInPackage = URLUtils.listFolder(url);
        } catch (IOException ex) {
            throw new ClassCollectionException(ex);
        }
        List<ClassRef> classesInPackage = new ArrayList<ClassRef>(urlsInPackage.length);
        for (URL urlInPackage : urlsInPackage) {
            String s = urlInPackage.getFile();
            s = s.substring(s.lastIndexOf('/')+1);
            if (s.endsWith(".class")) {
                classesInPackage.add(new ClassRef(classLoader, name + "." + s.substring(0, s.length()-6)));
            }
        }
        return classesInPackage;
    }

    public static Package forClassName(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String name = ClassLoaderUtils.getResourceNameForClassName(className);
        URL url = classLoader.getResource(name);
        if (url == null) {
            throw new ClassNotFoundException(className);
        } else {
            String localName = name.substring(name.lastIndexOf('/')+1);
            String file = url.getFile();
            if (file.endsWith(localName)) {
                URL packageUrl;
                try {
                    packageUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(),
                            file.substring(0, file.length()-localName.length()));
                } catch (MalformedURLException ex) {
                    throw new ClassNotFoundException(className, ex);
                }
                return new Package(classLoader, packageUrl, className.substring(0, className.lastIndexOf('.')));
            } else {
                throw new ClassNotFoundException(className);
            }
        }
    }

    public static Package forClass(Class<?> clazz) {
        try {
            return forClassName(clazz.getClassLoader(), clazz.getName());
        } catch (ClassNotFoundException ex) {
            // If we start from a Class object, we should never get a ClassNotFoundException,
            // except if something goes badly wrong. Thus throw an unchecked exception.
            throw new ClassCollectionException(ex);
        }
    }
}
