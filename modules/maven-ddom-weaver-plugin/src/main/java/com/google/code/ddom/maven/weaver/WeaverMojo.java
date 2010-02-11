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
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.model.Backend;
import com.google.code.ddom.spi.model.Frontend;
import com.google.code.ddom.weaver.ModelWeaver;
import com.google.code.ddom.weaver.ModelWeaverException;

/**
 * @goal weave
 * @phase compile
 */
public class WeaverMojo extends AbstractMojo {
    /**
     * The backend to use. Note that the corresponding JARs must be added to the project
     * dependencies.
     * 
     * @parameter expression="linkedlist"
     * @required
     */
    private String backend;
    
    /**
     * The list of frontends to use. Note that the corresponding JARs must be added to the project
     * dependencies.
     * 
     * @parameter
     * @required
     */
    private String[] frontends;

    /**
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;
    
    /**
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;
    
    private ClassLoader createClassLoader(ClassLoader parent, List<String> classpathElements) throws IOException {
        URL urls[] = new URL[classpathElements.size()];
        for (int i = 0; i < classpathElements.size(); i++) {
            urls[i] = new File(classpathElements.get(i)).toURL();
        }
        return new URLClassLoader(urls, parent);
    }
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassLoader cl;
        try {
            cl = createClassLoader(getClass().getClassLoader(), classpathElements);
        } catch (IOException ex) {
            throw new MojoExecutionException("Failed to create class loader", ex);
        }
        
        Backend backendImpl = ProviderFinder.find(cl, Backend.class).get(backend);
        if (backendImpl == null) {
            throw new MojoFailureException("Backend '" + backend + "' not found");
        }
        
        Map<String,Frontend> frontendMap = ProviderFinder.find(cl, Frontend.class);
        Map<String,Frontend> frontendImpls = new LinkedHashMap<String,Frontend>();
        for (String frontend : frontends) {
            Frontend frontendImpl = frontendMap.get(frontend);
            if (frontendImpl == null) {
                throw new MojoFailureException("Frontend '" + frontend + "' not found");
            }
            frontendImpls.put(frontend, frontendImpl);
        }
        
        if (!outputDirectory.mkdirs()) {
            throw new MojoFailureException("Unable to create directory " + outputDirectory);
        }
        
        try {
            ModelWeaver weaver = new ModelWeaver(cl, new ClassWriter(outputDirectory), backendImpl);
            weaver.weave(frontendImpls);
        } catch (ModelWeaverException ex) {
            throw new MojoExecutionException("Weaving failed", ex);
        }
    }
}
