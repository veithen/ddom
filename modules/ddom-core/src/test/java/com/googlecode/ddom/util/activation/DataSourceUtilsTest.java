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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import junit.framework.Assert;

import org.junit.Test;

public class DataSourceUtilsTest {
    @Test
    public void testGetSizeOnByteArrayDataSource() {
        DataSource ds = new ByteArrayDataSource(new byte[1234], "application/octet-stream");
        Assert.assertEquals(1234, DataSourceUtils.getSize(ds));
    }
    
    @Test
    public void testGetSizeOnObjectDataSource() {
        // DataSourceUtils#getSize should never make an attempt to read an
        // ObjectDataSource: this is extremely costly because it involves creation
        // of a thread.
        DataSource ds = new DataHandler("Test", "text/plain").getDataSource();
        Assert.assertEquals(-1, DataSourceUtils.getSize(ds));
    }
}
