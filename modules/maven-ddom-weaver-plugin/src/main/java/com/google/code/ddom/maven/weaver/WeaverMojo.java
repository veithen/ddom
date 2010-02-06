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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.model.Backend;
import com.google.code.ddom.weaver.ModelWeaver;

/**
 * @goal weave
 * @phase compile
 */
public class WeaverMojo extends AbstractMojo {
    /**
     * The backend to use. Note that the corresponding JARs must be added to the project
     * dependencies.
     * 
     * @parameter expression="${backend}"
     */
    private String backend;
    
    /**
     * The list of frontends to use. Note that the corresponding JARs must be added to the project
     * dependencies.
     * 
     * @parameter expression="${frontends}"
     * @required
     */
    private String[] frontends;

    public void execute() throws MojoExecutionException, MojoFailureException {
        
        // http://maven.apache.org/guides/mini/guide-maven-classloading.html
        // http://old.nabble.com/Problem-with-classloader-in-maven-plugin-td17339940.html
        
        // ProviderFinder.find(classLoader, Backend.class);
        
        // new ModelWeaver(classLoader, processor, backend);
        
    }
}
