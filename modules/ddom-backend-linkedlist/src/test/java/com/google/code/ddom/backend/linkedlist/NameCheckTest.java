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
package com.google.code.ddom.backend.linkedlist;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.code.ddom.commons.cl.Package;

public class NameCheckTest {
    // TODO: fix this
    @Ignore @Test
    public void testImplClassImplementsInterfaceWithSameName() throws Exception {
        ClassLoader cl = NameCheckTest.class.getClassLoader();
        for (Class<?> cls : Package.forClassName(cl,Document.class.getName()).getClasses()) {
            String name = cls.getSimpleName();
            if (!name.equals("NodeFactoryImpl") && name.endsWith("Impl")) {
                Class<?> expectedIface = Class.forName("com.google.code.ddom.spi.model.Core" + name.substring(0, name.length()-4));
                boolean found = false;
                for (Class<?> iface : cls.getInterfaces()) {
                    if (iface == expectedIface) {
                        found = true;
                        break;
                    }
                }
                Assert.assertTrue(found);
            }
        }
    }
}
