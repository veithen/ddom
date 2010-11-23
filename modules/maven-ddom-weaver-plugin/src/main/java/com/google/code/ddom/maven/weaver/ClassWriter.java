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
package com.google.code.ddom.maven.weaver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.googlecode.ddom.weaver.ClassDefinitionProcessor;
import com.googlecode.ddom.weaver.ClassDefinitionProcessorException;

public class ClassWriter implements ClassDefinitionProcessor {
    private final File outputDirectory;

    public ClassWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void processClassDefinition(String name, byte[] definition) throws ClassDefinitionProcessorException {
        File path = outputDirectory;
        int currentComponent = 0;
        int nextDot;
        while ((nextDot = name.indexOf('.', currentComponent)) != -1) {
            path = new File(path, name.substring(currentComponent, nextDot));
            if (!path.exists() && !path.mkdir()) {
                throw new ClassDefinitionProcessorException("Unable to create directory " + path);
            }
            currentComponent = nextDot+1;
        }
        path = new File(path, name.substring(currentComponent) + ".class");
        try {
            OutputStream out = new FileOutputStream(path);
            try {
                out.write(definition);
            } finally {
                out.close();
            }
        } catch (IOException ex) {
            throw new ClassDefinitionProcessorException("Unable to write class " + name + " (file " + path + ")", ex);
        }
    }
}
