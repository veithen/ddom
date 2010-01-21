
package com.sosnoski.ws.seismic.adb;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;

import com.sosnoski.ws.seismic.adb.SeismicAdbStub;
import com.sosnoski.ws.seismic.types.QuakeSet;
import com.sosnoski.ws.seismic.types.MatchQuakes;;

public class Axis2LitClient extends TestClient
{
    protected static TimeZone s_utcZone = TimeZone.getTimeZone("UTC");
    
    /**
     * Load policy file from classpath.
     */
    private static Policy loadPolicy(String name) throws XMLStreamException {
        ClassLoader loader = Axis2LitClient.class.getClassLoader();
        InputStream resource = loader.getResourceAsStream(name);
        if (resource == null) {
            return null;
        } else {
            StAXOMBuilder builder = new StAXOMBuilder(resource);
            return PolicyEngine.getPolicy(builder.getDocumentElement());
        }
    }
    
    protected Object configure(String path) {
        try {
            
            // create the actual client stub
            System.out.println("Connecting to service at " + path);
            SeismicAdbStub stub = new SeismicAdbStub(path);   
            
            // configure and engage Rampart
            ServiceClient client = stub._getServiceClient();
            Policy policy = loadPolicy("policy.xml");
            if (policy == null) {
                System.out.println("No policy found - rampart not engaged");
            } else {
                Options options = client.getOptions();
                options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, policy);
                options.setUserName("earthinfo");
                options.setPassword("quakes");
                client.engageModule("addressing");
                client.engageModule("rampart");
                client.engageModule("rahas");
                System.out.println("Rampart engaged for supplied policy");
            }
            
            // set extended client timeout
            client.getOptions().setTimeOutInMilliSeconds(120000);
            return stub;
            
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException("Error: " + ex.getMessage(), ex);
        }
    }
    
    protected Object runQuery(QueryData data, Object stub) {
        MatchQuakes query = new MatchQuakes();
        Calendar minc = new GregorianCalendar(s_utcZone);
        minc.setTimeInMillis(data.m_timeMin);
        query.setMinDate(minc);
        Calendar maxc = new GregorianCalendar(s_utcZone);
        maxc.setTimeInMillis(data.m_timeMax);
        query.setMaxDate(maxc);
        query.setMinLat(data.m_latMin);
        query.setMaxLat(data.m_latMax);
        query.setMinLong(data.m_longMin);
        query.setMaxLong(data.m_longMax);
        QuakeSet[] response = null;
        try {
            response = ((SeismicAdbStub)stub).matchQuakes(query).getResultSet();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        return response;
    }
    
    protected int summarize(boolean verbose, Object obj) {
        
        // note that as of Axis2 1.3, ADB returns a null instead of an empty array for no matches
        QuakeSet[] resp = (QuakeSet[])obj;
        int count = 0;
        if (resp != null) {
            for (int j = 0; j < resp.length; j++) {
                QuakeSet set = resp[j];
                if (verbose) {
                    System.out.println("Seismic region " +
                        set.getAreaName() + " has " +
                        set.getRegions().getRegion().length + " regions and " +
                        set.getQuakes().getQuake().length + " matching quakes");
                }
                count += set.getQuakes().getQuake().length;
            }
        }
        return count;
    }
    
    public static void main(String[] args) throws Exception {
        Axis2LitClient client = new Axis2LitClient();
        client.runTest(args);
    }
}