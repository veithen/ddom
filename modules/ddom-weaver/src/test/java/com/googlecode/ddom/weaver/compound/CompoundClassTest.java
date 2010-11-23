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
package com.googlecode.ddom.weaver.compound;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.weaver.DynamicClassLoader;

public class CompoundClassTest {
    @Test
    public void test() throws Exception {
        DynamicClassLoader cl = new DynamicClassLoader(CompoundClassTest.class.getClassLoader());
        String className = "generated.Compound";
        CompoundClass compoundClass = new CompoundClass(className, new ClassRef(Hello.class),
                new String[] { Hello1.class.getName(), Hello2.class.getName() });
        // TODO: why do we need a reactor argument here
        cl.processClassDefinition(className, compoundClass.getClassDefinition(null));
        Hello compound = (Hello)cl.loadClass(className).newInstance();
        List<String> output = new ArrayList<String>();
        compound.sayHello(output);
        Assert.assertEquals(2, output.size());
    }
}
