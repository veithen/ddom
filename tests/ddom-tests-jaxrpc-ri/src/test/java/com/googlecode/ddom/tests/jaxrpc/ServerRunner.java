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
package com.googlecode.ddom.tests.jaxrpc;

import java.net.URISyntaxException;

import javax.xml.rpc.Stub;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerRunner {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Server");

        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(9999);
        server.setConnectors(new Connector[] {connector});

        WebAppContext webappcontext = new WebAppContext();
        webappcontext.setContextPath("/");

        String warPath = null;
        warPath = ServerRunner.class.getResource("/webapp").toURI().getPath();
        
        webappcontext.setWar(warPath);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {webappcontext, new DefaultHandler()});

        server.setHandler(handlers);
        server.start();
        try {
            Echo echo = new TestService_Impl().getEchoPort();
            ((Stub)echo)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9999/jaxrpc/echo");
            System.out.println(echo.echo("Hi!"));
        } finally {    
            server.stop();
            server.destroy();
        }
    }
}
