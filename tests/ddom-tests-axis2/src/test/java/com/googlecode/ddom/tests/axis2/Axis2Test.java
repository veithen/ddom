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
package com.googlecode.ddom.tests.axis2;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.ddom.tests.axis2.helloworld.HelloworldCallbackHandler;
import com.googlecode.ddom.tests.axis2.helloworld.HelloworldStub;

public class Axis2Test {
    private static final int PORT = 9999;
    
    private static Server server;
    private static ConfigurationContext clientConfigurationContext;
    
    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.setConnectors(new Connector[] {connector});

        WebAppContext webappcontext = new WebAppContext();
        webappcontext.setContextPath("/axis2");

        String warPath = null;
        warPath = Axis2Test.class.getResource("/webapp").toURI().getPath();
        
        webappcontext.setWar(warPath);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {webappcontext, new DefaultHandler()});

        server.setHandler(handlers);
        server.start();
    }
    
    @BeforeClass
    public static void createClientConfigurationContext() throws Exception {
        clientConfigurationContext = ConfigurationContextFactory.createConfigurationContextFromURIs(Axis2Test.class.getResource("axis2_client.xml"), null);
    }
    
    @Test
    public void testHelloworld() throws Exception {
        HelloworldStub stub = new HelloworldStub(clientConfigurationContext, "http://localhost:" + PORT + "/axis2/services/helloworld");
        try {
            assertEquals("Hello world!", stub.sayHello("world"));
        } finally {
            stub.cleanup();
        }
    }
    
    @Test
    public void testHelloworldWithWSA() throws Exception {
        HelloworldStub stub = new HelloworldStub(clientConfigurationContext, "http://localhost:" + PORT + "/axis2/services/helloworld");
        try {
            ServiceClient serviceClient = stub._getServiceClient();
            serviceClient.engageModule("addressing");
            serviceClient.getOptions().setUseSeparateListener(true);
            final String[] resultHolder = new String[1];
            final CountDownLatch latch = new CountDownLatch(1);
            stub.startsayHello("world", new HelloworldCallbackHandler() {
                @Override
                public void receiveResultsayHello(String result) {
                    resultHolder[0] = result;
                    latch.countDown();
                }
            });
            latch.await();
            assertEquals("Hello world!", resultHolder[0]);
        } finally {
            stub.cleanup();
        }
    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }
}