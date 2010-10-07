/*
 * Copyright (c) 2009-2010, Dennis M. Sosnoski. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of
 * JiBX nor the names of its contributors may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sosnoski.ws.seismic.cxf;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import java.io.IOException;

/**
 * Simple password callback handler. This just handles two cases: matching the user name
 * and password, and providing the password used for access to the private key.
 */
public class ServerCallback implements CallbackHandler
{
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
            String id = pwcb.getIdentifier();
            switch (pwcb.getUsage()) {
                case WSPasswordCallback.USERNAME_TOKEN_UNKNOWN:
                    
                    // used when plaintext password in message
                    if (!"libuser".equals(id) || !"books".equals(pwcb.getPassword())) {
                        throw new UnsupportedCallbackException(callbacks[i], "check failed");
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