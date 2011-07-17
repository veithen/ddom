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
package com.googlecode.ddom.stream.filter;

import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;

import org.xml.sax.ErrorHandler;

import com.googlecode.ddom.stream.XmlFilter;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.sax.ContentHandlerAdapter;
import com.googlecode.ddom.stream.sax.XmlHandlerAdapter;

public class ValidationFilter extends XmlFilter {
    private final Schema schema;
    private ErrorHandler errorHandler;
    
    public ValidationFilter(Schema schema) {
        this.schema = schema;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    protected XmlHandler createXmlHandler(XmlHandler target) {
        ValidatorHandler vh = schema.newValidatorHandler();
        // TODO: set error handler
        vh.setContentHandler(new ContentHandlerAdapter(target));
        if (errorHandler != null) {
            vh.setErrorHandler(errorHandler);
        }
        return new XmlHandlerAdapter(vh, null);
    }
}
