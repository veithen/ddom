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
package com.googlecode.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMComment;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;

@Mixin(CoreComment.class)
// TODO: there is probably some code here that can be combined with the mixins for CoreProcessingInstruction and CoreCDATASection
public abstract class CommentSupport implements DOMComment {
    public final Node cloneNode(boolean deep) {
        return (Node)coreGetNodeFactory().createComment(coreGetOwnerDocument(true), getData());
    }

    public final String getData() {
        try {
            return coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }

    public final void setData(String data) throws DOMException {
        try {
            coreSetValue(data);
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }
    
    public final short getNodeType() {
        return Node.COMMENT_NODE;
    }

    public final String getNodeName() {
        return "#comment";
    }
}
