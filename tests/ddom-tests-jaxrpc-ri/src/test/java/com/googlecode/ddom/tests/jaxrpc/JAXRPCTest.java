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

import static org.junit.Assert.assertEquals;

import javax.activation.DataHandler;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.Stub;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.ddom.tests.jaxrpc.attachments.Attachments;
import com.googlecode.ddom.tests.jaxrpc.attachments.AttachmentsService;
import com.googlecode.ddom.tests.jaxrpc.calculator.Calculator;
import com.googlecode.ddom.tests.jaxrpc.calculator.CalculatorService;
import com.googlecode.ddom.tests.jaxrpc.echo.Echo;
import com.googlecode.ddom.tests.jaxrpc.echo.EchoService;

public class JAXRPCTest {
    private static final int PORT = 9999;
    
    private static Server server;
    
    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.setConnectors(new Connector[] {connector});

        WebAppContext webappcontext = new WebAppContext();
        webappcontext.setContextPath("/");

        String warPath = null;
        warPath = JAXRPCTest.class.getResource("/webapp").toURI().getPath();
        
        webappcontext.setWar(warPath);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {webappcontext, new DefaultHandler()});

        server.setHandler(handlers);
        server.start();
    }
    
    @Test
    public void testWithoutHandlers() throws Exception {
        Echo echo = ((EchoService)ServiceFactory.newInstance().loadService(EchoService.class)).getEchoPort();
        ((Stub)echo)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:" + PORT + "/jaxrpc/echo");
        assertEquals("Hi!", echo.echo("Hi!"));
    }
    
    @Test @Ignore
    public void testWithHandlers() throws Exception {
        Calculator calculator = ((CalculatorService)ServiceFactory.newInstance().loadService(CalculatorService.class)).getCalculatorPort();
        ((Stub)calculator)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:" + PORT + "/jaxrpc/calculator");
        assertEquals(5, calculator.add(2, 3));
    }
    
    @Test @Ignore
    public void testSwA() throws Exception {
        Attachments attachments = ((AttachmentsService)ServiceFactory.newInstance().loadService(AttachmentsService.class)).getAttachmentsPort();
        ((Stub)attachments)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:" + PORT + "/jaxrpc/attachments");
        assertEquals("OK", attachments.addAttachment("12345", new DataHandler("This is a test", "text/plain")));
    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }
}
