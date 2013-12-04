/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import org.apache.axiom.om.OMNode;

import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomComment;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(CoreComment.class)
public abstract class CommentSupport implements AxiomComment {
    public final int getType() {
        return OMNode.COMMENT_NODE;
    }

    public final String getValue() {
        try {
            return coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setValue(String text) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
