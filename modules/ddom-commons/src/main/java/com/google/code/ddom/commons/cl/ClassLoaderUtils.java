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
package com.google.code.ddom.commons.cl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.code.ddom.commons.io.URLUtils;

public class ClassLoaderUtils {
    private ClassLoaderUtils() {}
    
    /**
     * Translate a class name into the resource name of the corresponding <tt>.class</tt> file. E.g.
     * <tt>java.lang.String</tt> would be translated into <tt>java/lang/String.class</tt>.
     * 
     * @param className the class name
     * @return the resource name of the class file
     */
    public static String getResourceNameForClassName(String className) {
        return className.replace('.', '/') + ".class";
    }

    public static byte[] getClassDefinition(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String resourceName = getResourceNameForClassName(className);
        InputStream in = classLoader.getResourceAsStream(resourceName);
        if (in == null) {
            throw new ClassNotFoundException(className);
        }
        try {
            try {
                return IOUtils.toByteArray(in);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            throw new ClassNotFoundException(className, ex);
        }
    }
    
    /**
     * Load all classes that are in the same package as a given class.
     * 
     * @param classLoader
     *            the class loader
     * @param className
     *            the name of the class
     * @return an array with the classes in the package
     */
    public static Class<?>[] getClassesInPackage(ClassLoader classLoader, String className) throws ClassNotFoundException {
        String name = getResourceNameForClassName(className);
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
                URL[] urlsInPackage;
                try {
                    urlsInPackage = URLUtils.listFolder(packageUrl);
                } catch (IOException ex) {
                    throw new ClassNotFoundException(className, ex);
                }
                List<Class<?>> classesInPackage = new ArrayList<Class<?>>(urlsInPackage.length);
                String pkg = className.substring(0, className.lastIndexOf('.'));
                for (URL urlInPackage : urlsInPackage) {
                    String s = urlInPackage.getFile();
                    s = s.substring(s.lastIndexOf('/')+1);
                    if (s.endsWith(".class")) {
                        classesInPackage.add(classLoader.loadClass(pkg + "." + s.substring(0, s.length()-6)));
                    }
                }
                return classesInPackage.toArray(new Class<?>[classesInPackage.size()]);
            } else {
                throw new ClassNotFoundException(className);
            }
        }
    }
}
