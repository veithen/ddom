/*
 * Copyright 2014 Andreas Veithen
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
package com.github.veithen.ddom;

import javax.xml.soap.SAAJMetaFactory;

import org.apache.axiom.testutils.suite.MatrixTestSuiteBuilder;

import com.github.veithen.rbeans.RBeanFactory;
import com.github.veithen.rbeans.RBeanFactoryException;

public class SAAJTestSuiteBuilder extends MatrixTestSuiteBuilder {
    private static final RBeanFactory rbeanFactory;
    
    static {
        try {
            rbeanFactory = new RBeanFactory(SAAJMetaFactoryRBean.class);
        } catch (RBeanFactoryException ex) {
            throw new Error(ex);
        }
    }
    
    private final SAAJMetaFactoryRBean metaFactory;

    public SAAJTestSuiteBuilder(SAAJMetaFactory metaFactory) {
        this.metaFactory = rbeanFactory.createRBean(SAAJMetaFactoryRBean.class, metaFactory);
    }

    @Override
    protected void addTests() {
        // TODO
    }
}
