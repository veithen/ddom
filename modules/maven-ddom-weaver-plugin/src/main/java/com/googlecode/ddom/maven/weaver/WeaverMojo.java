/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.maven.weaver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.googlecode.ddom.backend.Backend;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.spi.StaticModel;
import com.googlecode.ddom.spi.Finder;
import com.googlecode.ddom.weaver.ModelWeaver;
import com.googlecode.ddom.weaver.ModelWeaverException;

/**
 * @goal weave
 * @phase compile
 * @requiresDependencyResolution compile
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
     * The Java package name for the classes produced by the weaver.
     * 
     * @parameter
     * @required
     */
    private String outputPackage;
    
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
        ModelWeaver weaver = new ModelWeaver();
        
        ClassLoader cl;
        try {
            cl = createClassLoader(getClass().getClassLoader(), classpathElements);
        } catch (IOException ex) {
            throw new MojoExecutionException("Failed to create class loader", ex);
        }
        weaver.setClassLoader(cl);
        
        Backend backendImpl = Finder.findNamedProviders(cl, Backend.class).get(backend);
        if (backendImpl == null) {
            throw new MojoFailureException("Backend '" + backend + "' not found");
        }
        weaver.setBackend(backendImpl);
        
        Map<String,Frontend> frontendMap = Finder.findNamedProviders(cl, Frontend.class);
        Map<String,Frontend> frontendImpls = new LinkedHashMap<String,Frontend>();
        for (String frontend : frontends) {
            Frontend frontendImpl = frontendMap.get(frontend);
            if (frontendImpl == null) {
                throw new MojoFailureException("Frontend '" + frontend + "' not found");
            }
            frontendImpls.put(frontend, frontendImpl);
        }
        weaver.setFrontends(frontendImpls);
        
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            throw new MojoFailureException("Unable to create directory " + outputDirectory);
        }
        weaver.setProcessor(new ClassWriter(outputDirectory));
        
        weaver.setOutputPackage(outputPackage);
        
        try {
            weaver.weave();
        } catch (ModelWeaverException ex) {
            throw new MojoExecutionException("Weaving failed", ex);
        }
        
        try {
            // Attempt to load and initialize the node factory to detect issues early
            Class.forName(weaver.getNodeFactoryClassName(), true, cl);
        } catch (ClassNotFoundException ex) {
            throw new MojoExecutionException("Failed to load the node factory class after weaving", ex);
        }
        
        ModelDefinitionBuilder modelDefinitionBuilder = new ModelDefinitionBuilder();
        modelDefinitionBuilder.setBackend(backend);
        for (String frontend : frontends) {
            modelDefinitionBuilder.addFrontend(frontend);
        }
        StaticModel model = new StaticModel(modelDefinitionBuilder.buildModelDefinition(), weaver.getNodeFactoryClassName());
        
        File servicesDir = new File(outputDirectory, "META-INF/services");
        if (!servicesDir.exists() && !servicesDir.mkdirs()) {
            throw new MojoFailureException("Unable to create directory " + servicesDir);
        }
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(servicesDir, StaticModel.class.getName() + ".ser")));
            try {
                out.writeObject(model);
            } finally {
                out.close();
            }
        } catch (IOException ex) {
            throw new MojoExecutionException("Unable to serialize model", ex);
        }
    }
}
