/*
 * Copyright 2009-2010 Andreas Veithen
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
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import com.googlecode.ddom.axiom.OMMetaFactoryImpl;

public class ServiceClientRunner {
    private static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.apache.org/axis2/xsd", "ns1");
        OMElement method = fac.createOMElement("echo", omNs);
        OMElement value = fac.createOMElement("value", omNs);
        value.setText("Hello , my first service utilization");
        method.addChild(value);
        return method;
    }

    public static void main(String[] args) throws Exception {
        ConfigurationContext configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        // TODO: we are cheating here because the ConfigurationContextFactory already uses Axiom
        OMAbstractFactory.setMetaFactory(new OMMetaFactoryImpl());
        ServiceClient client = new ServiceClient(configContext, null);
        Options options = new Options();
        options.setTo(new EndpointReference("http://127.0.0.1:8080/axis2/services/MyService"));
        options.setAction("urn:echo");
        client.setOptions(options);
        OMElement res = client.sendReceive(createPayLoad());
    }
}
