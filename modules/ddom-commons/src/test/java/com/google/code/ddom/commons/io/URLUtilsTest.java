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
package com.google.code.ddom.commons.io;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Assert;
import org.junit.Test;

public class URLUtilsTest {
    private static final File testjar = new File("target/testjar").getAbsoluteFile();
    
    @Test
    public void testListFolderWithFileProtocol() throws Exception {
        URL[] urls = URLUtils.listFolder(testjar.toURL());
        Assert.assertEquals(1, urls.length);
        Assert.assertTrue(urls[0].toExternalForm().endsWith("activation.jar"));
    }
    
    @Test
    public void testListFolderWithJarProtocol() throws Exception {
        ClassLoader cl = new URLClassLoader(new URL[] { new File(testjar, "activation.jar").toURL() });
        URL url = cl.getResource("javax/activation/DataSource.class");
        String file = url.getFile();
        URL folderUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), file.substring(0, file.length()-"DataSource.class".length()));
        URL[] urls = URLUtils.listFolder(folderUrl);
        Assert.assertTrue(urls.length > 1);
        boolean containsOriginalUrl = false;
        for (URL u : urls) {
            // Check that the URL is valid and that the resource can be read
            InputStream in = url.openStream();
            try {
                in.read();
            } finally {
                in.close();
            }
            if (u.toExternalForm().equals(url.toExternalForm())) {
                containsOriginalUrl = true;
            }
        }
        Assert.assertTrue(containsOriginalUrl);
    }
}
