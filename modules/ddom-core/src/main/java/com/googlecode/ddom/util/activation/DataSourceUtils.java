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
package com.googlecode.ddom.util.activation;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.googlecode.ddom.util.lang.ClassUtils;

public final class DataSourceUtils {
    private DataSourceUtils() {}

    /**
     * Try to determine the size of the data represented by a {@link DataSource} object without
     * actually reading its content. This method supports some well known data source
     * implementations for which it is possible to get the size of the data without reading it.
     * 
     * @param ds
     *            the data source
     * @return the size of the data or <code>-1</code> if the size is unknown
     */
    public static long getSize(DataSource ds) {
        if (ds instanceof FileDataSource) {
            // If the data source is a FileDataSource, we can easily get the size without
            // reading the content. Note that we don't need reflection here because
            // FileDataSource is always visible to the class loader that loaded DataSourceUtils.
            // The reason is that FileDataSource is in the same package as DataSource, and
            // DataSource must be visible because it is used as argument type.
            return ((FileDataSource)ds).getFile().length();
        } else {
            // We need to use reflection here because ByteArrayDataSource is in a
            // different package (and even a different JAR), which means that the class is not
            // necessarily visible to the class loader that loaded DataSourceUtils.
            Class<?> clazz = ds.getClass();
            if (ClassUtils.isSubclass(clazz, "javax.mail.util.ByteArrayDataSource")) {
                // We know that JavaMail's ByteArrayDataSource#getInputStream() always returns a
                // ByteArrayInputStream. We also know that ByteArrayInputStream#available()
                // directly returns the size of the byte array.
                try {
                    Method method = clazz.getMethod("getInputStream");
                    return ((ByteArrayInputStream)method.invoke(ds)).available();
                    // Since it's a ByteArrayInputStream, no need to close it explicitly
                } catch (Exception ex) {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }
}
