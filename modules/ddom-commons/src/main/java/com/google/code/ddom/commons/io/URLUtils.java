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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class URLUtils {
    private URLUtils() {}
    
    /**
     * List the content of a folder specified by URL. The following protocols are supported:
     * <tt>file</tt>, <tt>jar</tt>, <tt>wsjar</tt> (used by WebSphere) and <tt>zip</tt>.
     * 
     * @param folderUrl
     *            the URL of the folder
     * @return the URLs of the resources in the folder
     * @throws IOException
     *             if the content folder could not be listed
     */
    public static URL[] listFolder(URL folderUrl) throws IOException {
        String proto = folderUrl.getProtocol();
        String file = folderUrl.getFile(); 
        if (proto.equals("file")) {
            File[] files = new File(file).listFiles();
            // TODO: this will cause a NullPointerException if the directory doesn't exist;
            //       need to specify if in that case we return an empty array or null
            URL[] urls = new URL[files.length];
            for (int i=0; i<files.length; i++) {
                urls[i] = files[i].toURL();
            }
            return urls;
        } else if (proto.equals("jar") || proto.equals("wsjar") || proto.equals("zip")) {
            int idx = file.indexOf("!");
            String archive = file.substring(0, idx);
            URL archiveUrl = new URL(archive);
            String folder = file.substring(idx+2); // Also strip the '/'
            List<URL> urls = new ArrayList<URL>();
            InputStream in = archiveUrl.openStream();
            try {
                ZipInputStream zip = new ZipInputStream(in);
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        if (entryName.startsWith(folder) && entryName.indexOf('/', folder.length()) == -1) {
                            urls.add(new URL(proto, null, archive + "!/" + entryName));
                        }
                    }
                }
            } finally {
                in.close();
            }
            return urls.toArray(new URL[urls.size()]);
        } else {
            throw new IOException("Unknown protocol: " + proto);
        }
    }
}
