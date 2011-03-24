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
package com.googlecode.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMDataSource;

import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomSourcedElement;
import com.googlecode.ddom.frontend.axiom.support.OMDataSourceAdapter;

@Mixin(AxiomSourcedElement.class)
public abstract class SourcedElementSupport implements AxiomSourcedElement {
    private OMDataSource dataSource;

    public OMDataSource getDataSource() {
        return dataSource;
    }

    public OMDataSource setDataSource(OMDataSource dataSource) {
        this.dataSource = dataSource;
        coreSetSource(new OMDataSourceAdapter(dataSource));
        return null; // TODO
    }

    public boolean isExpanded() {
        // TODO Auto-generated method stub
        return false;
    }
}
