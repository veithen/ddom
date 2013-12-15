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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.core.ext.ModelExtensionMapper;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlOutput;

// TODO: also allow for deferred building of attributes
public class Builder extends XmlOutput {
    private final XmlInput input;
    final ModelExtensionMapper modelExtensionMapper;
    private final BuilderHandlerDelegate rootDelegate;
    final Document document;
    private StreamException streamException;
    
    boolean expand;

    public Builder(XmlInput input, ModelExtension modelExtension, Document document, LLParentNode target, boolean unwrap) {
        this.input = input;
        modelExtensionMapper = modelExtension == null ? null : modelExtension.newMapper();
        this.document = document;
        rootDelegate = unwrap ? new UnwrappingBuilderHandlerDelegate(this, (LLElement)target) : new Context(this, null, target);
    }

    @Override
    protected XmlHandler createXmlHandler() {
        return new BuilderHandler(rootDelegate);
    }

    private Context internalGetRootContext() {
        return rootDelegate instanceof Context ? (Context)rootDelegate : rootDelegate.getNestedContext();
    }
    
    public final InputContext getInputContext(LLParentNode target) {
        for (Context context = internalGetRootContext(); context != null; context = context.getNestedContext()) {
            if (context.getTargetNode() == target) {
                return context;
            }
        }
        return null;
    }
    
    /**
     * Determine if this instance is a builder for any of the nodes in the object model tree
     * specified by a given root node.
     * 
     * @param root
     *            the root node of the tree
     * @return <code>true</code> if there is at least one node in the tree for which this instance
     *         is a builder, <code>false</code> otherwise
     */
    public final boolean isBuilderForTree(LLParentNode root) {
        // TODO: we can optimize this because in most cases the target of a context is a child node
        //       of the target of the previous context
        for (Context context = internalGetRootContext(); context != null; context = context.getNestedContext()) {
            LLParentNode node = context.getTargetNode();
            while (true) {
                if (node == root) {
                    return true;
                }
                if (node instanceof LLChildNode) {
                    node = ((LLChildNode)node).internalGetParent();
                } else {
                    break;
                }
            }
        }
        return false;
    }
    
    public final InputContext getRootInputContext() throws DeferredBuildingException {
        if (rootDelegate instanceof UnwrappingBuilderHandlerDelegate) {
            UnwrappingBuilderHandlerDelegate unwrappingDelegate = (UnwrappingBuilderHandlerDelegate)rootDelegate;
            while (!unwrappingDelegate.isElementFound()) {
                next(false);
            }
            return unwrappingDelegate.getNestedContext();
        } else {
            return (Context)rootDelegate;
        }
    }
    
    final void next(boolean expand) throws DeferredBuildingException {
        if (streamException == null) {
            this.expand = expand;
            try {
                // TODO: review the node appended stuff
//                nodeAppended = false; 
//                do {
                    getStream().proceed();
//                } while (context != null && !nodeAppended);
            } catch (StreamException ex) {
                streamException = ex;
            }
            this.expand = false;
        }
        if (streamException != null) {
            if (streamException instanceof CoreModelStreamException) {
                CoreModelException cause = ((CoreModelStreamException)streamException).getCoreModelException();
                if (cause instanceof DeferredBuildingException) {
                    // Typically we get here if an ElementNameMismatchException is thrown
                    throw (DeferredBuildingException)cause;
                }
            }
            throw new DeferredParsingException(streamException);
        }
    }

    public final void dispose() {
        input.dispose();
    }
}
