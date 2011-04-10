/*
 * Copyright 2009 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sosnoski.ws.seismic.adb;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import java.io.IOException;

/**
 * Simple password callback handler. This just checks if the password for the private key
 * is being requested, and if so sets that value.
 */
public class PWCBHandler implements CallbackHandler
{
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
            String id = pwcb.getIdentifier();
            switch (pwcb.getUsage()) {
                case WSPasswordCallback.USERNAME_TOKEN_UNKNOWN:
                    
                    // used when plaintext password in message
                    if (!"earthinfo".equals(id) || !"quakes".equals(pwcb.getPassword())) {
                        throw new UnsupportedCallbackException(callbacks[i], "check failed");
                    }
                    break;
                    
                case WSPasswordCallback.USERNAME_TOKEN:
                    
                    // used when hashed password in message
                    if ("earthinfo".equals(id)) {
                        pwcb.setPassword("quakes");
                    }
                    break;
                    
                case WSPasswordCallback.DECRYPT:
                case WSPasswordCallback.SIGNATURE:
                    
                    // used to retrieve password for private key
                    if ("serverkey".equals(id)) {
                        pwcb.setPassword("serverpass");
                    }
                    break;
            }
        }
    }
}