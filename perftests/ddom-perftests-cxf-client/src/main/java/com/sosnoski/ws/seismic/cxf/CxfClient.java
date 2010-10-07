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

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Web service client for seismic server. This just provides Metro linkage code for the common test client.
 */
public class CxfClient extends TestClient
{
    protected static TimeZone s_utcZone = TimeZone.getTimeZone("UTC");
    protected DatatypeFactory m_factory;
    private final String id;
    
    private CxfClient(String id) {
        this.id = id;
    }
    
    protected Object configure(String path) {
        try {
            
            // create a factory to use for queries
            m_factory = DatatypeFactory.newInstance();
            
            // create the client stub
            ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("client.xml");
            SeismicInterface stub = (SeismicInterface)appContext.getBean(id);
            
            // set the actual endpoint address
            System.out.println("Connecting to " + path);
            BindingProvider provider = (BindingProvider)stub;
            provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, path);
            return stub;
            
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException("Error: " + ex.getMessage(), ex);
        }
    }
    
    protected Object runQuery(QueryData data, Object stub) {
        MatchQuakes query = new MatchQuakes();
        GregorianCalendar minc = new GregorianCalendar(s_utcZone);
        minc.setTimeInMillis(data.m_timeMin);
        query.setMinDate(m_factory.newXMLGregorianCalendar(minc));
        GregorianCalendar maxc = new GregorianCalendar(s_utcZone);
        maxc.setTimeInMillis(data.m_timeMax);
        query.setMaxDate(m_factory.newXMLGregorianCalendar(maxc));
        query.setMinLat(data.m_latMin);
        query.setMaxLat(data.m_latMax);
        query.setMinLong(data.m_longMin);
        query.setMaxLong(data.m_longMax);
        List<QuakeSet> response = null;
        try {
            response = ((SeismicInterface)stub).matchQuakes(query).getResultSet();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
        return response;
    }
    
    protected int summarize(boolean verbose, Object obj) {
        List<QuakeSet> resp = (List<QuakeSet>)obj;
        int count = 0;
        for (Iterator<QuakeSet> iter = resp.iterator(); iter.hasNext();) {
            QuakeSet set = iter.next();
            if (verbose) {
                System.out.println("Seismic region " +
                    set.getAreaName() + " has " +
                    set.getRegions().getRegion().size() + " regions and " +
                    set.getQuakes().getQuake().size() + " matching quakes");
            }
            count += set.getQuakes().getQuake().size();
        }
        return count;
    }
    
    public static void main(String[] args) throws Exception {
        
        // make sure required arguments are supplied
        if (args.length < 7) {
            System.out.println("Usage: java com.sosnoski.ws.seismic.cxf.CxfClient id protocol"
                + " host port path\n fraction loops [quiet] [threads]\nWhere "
                + "id is the id of the client (plain, username, sign or signencr)\n"
                + "      protocol is the protocol ('http' or 'https'),\n"
                + "      host is the host name or IP address,\n"
                + "      port is the port number on the host,\n"
                + "      path is the path to the service on the host,\n"
                + "      fraction is decimal range fraction,\n"
                + "      loops is the number of requests to time,\n"
                + "      'quiet' flags printing only summary information, and\n"
                + "      threads is the number of threads to run");
        } else {
            CxfClient client = new CxfClient(args[0]);
            String[] trimargs = new String[args.length-1];
            System.arraycopy(args, 1, trimargs, 0, trimargs.length);
            client.runTest(trimargs);
        }
    }
}