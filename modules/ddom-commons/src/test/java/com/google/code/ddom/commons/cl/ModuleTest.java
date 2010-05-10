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

import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.Assert;

import org.junit.Test;

import com.google.code.ddom.commons.Constants;

public class ModuleTest {
    @Test
    public void testGetPackageFromDirModule() {
        // Test classes are always loaded from a directory
        Module module = Module.forClass(ModuleTest.class);
        Package pkg = module.getPackage(ModuleTest.class.getPackage().getName());
        Assert.assertTrue(pkg.getClasses().contains(ModuleTest.class));
    }
    
    @Test
    public void testGetPackageFromJarModule() throws Exception {
        ClassLoader cl = new URLClassLoader(new URL[] { Constants.ACTIVATION_JAR.toURL() });
        Module module = Module.forClassName(cl, "javax.activation.DataSource");
        Package pkg = module.getPackage("javax.activation");
        boolean found = false;
        for (ClassRef classRef : pkg.getClassRefs()) {
            if (classRef.getClassName().equals("javax.activation.DataHandler")) {
                found = true;
            }
        }
        Assert.assertTrue(found);
    }
    
    @Test
    public void testClassFromBootstrapClassLoader() {
        Assert.assertNotNull(Module.forClass(String.class));
    }
}
