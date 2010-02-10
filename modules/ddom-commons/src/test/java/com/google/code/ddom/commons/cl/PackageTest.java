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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.google.code.ddom.commons.Constants;

public class PackageTest {
    @Test
    public void testForClassName() throws Exception {
        Package pkg = Package.forClassName(ClassLoaderUtilsTest.class.getClassLoader(), ClassLoaderUtilsTest.class.getName());
        Collection<Class<?>> classes = pkg.getClasses();
        Assert.assertTrue(classes.contains(ClassLoaderUtilsTest.class));
        Assert.assertTrue(classes.contains(DummyClass.class));
    }
    
    /**
     * Check that {@link Package#getClassNames()} lists classes in the specified package, but not in
     * subpackages.
     * 
     * @throws Exception
     */
    @Test
    public void testNonRecursive() throws Exception {
        ClassLoader cl = new URLClassLoader(new URL[] { Constants.ACTIVATION_JAR.toURL() });
        Module module = Module.forClassName(cl, "javax.activation.DataSource");
        Package pkg = module.getPackage("javax");
        Assert.assertTrue(pkg.getClassNames().isEmpty());
    }
}