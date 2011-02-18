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
package com.sun.xml.messaging.saaj.soap;

import javax.xml.soap.SOAPException;

public class SOAPVersionMismatchException extends SOAPException {
    private static final long serialVersionUID = 8876145950655260039L;

    public SOAPVersionMismatchException() {
    }

    public SOAPVersionMismatchException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public SOAPVersionMismatchException(String reason) {
        super(reason);
    }

    public SOAPVersionMismatchException(Throwable cause) {
        super(cause);
    }
}
