package com.sosnoski.ws.seismic.adb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * Base client test program. This base program is used for testing all variations of the seismic service. It takes a set
 * of basic parameters for controlling the test run, first the various components of the path, then a decimal fraction
 * giving the portion of the total space and total time ranges specified for each query, then the number of queries to
 * execute, and optionally a number of threads to use and a quiet output flag.
 */
public abstract class TestClient
{
    protected static long s_minDate;
    
    protected static long s_maxDate;
    
    protected static float s_minLong = -180.0f;
    
    protected static float s_maxLong = 180.0f;
    
    protected static float s_minLat = -90.0f;
    
    protected static float s_maxLat = 90.0f;
    
    protected abstract Object configure(String path);
    
    protected abstract Object runQuery(QueryData query, Object stub);
    
    protected abstract int summarize(boolean verbose, Object obj);
    
    protected void runTest(String[] args) throws Exception {
        
        // make sure required arguments are supplied
        if (args.length < 6) {
            System.out.println("Usage: java TestClient protocol host port path "
                + "fraction loops [quiet] [threads]\nWhere protocol is the "
                + "protocol ('http' or 'https'),\n"
                + "      host is the host name or IP address,\n"
                + "      port is the port number on the host,\n"
                + "      path is the path to the service on the host,\n"
                + "      fraction is decimal range fraction,\n"
                + "      loops is the number of requests to time,\n"
                + "      'quiet' flags printing only summary information, and\n"
                + "      threads is the number of threads to run");
        } else {
            
            // initialize everything for test loop
            String target = args[0] + "://" + args[1] + ":" + args[2] + args[3];
            double fraction = Double.parseDouble(args[4]);
            int loops = Integer.parseInt(args[5]);
            int index = 6;
            boolean verbose = true;
            int numthread = 1;
            if (args.length > index) {
                if (args[index].equals("quiet")) {
                    verbose = false;
                    index++;
                }
                if (args.length > index) {
                    numthread = Integer.parseInt(args[index]);
                }
            }
            
            // run a single query to initialize all code
            System.out.println("Running initialization request to prepare for timed test");
            Thread init = new Thread(new TestRunnable("Init: ", .1, target, false, 1));
            init.start();
            synchronized (init) {
                while (init.isAlive()) {
                    init.wait();
                }
            }
            
            // wait a while for the JVM to settle
            System.out.println("Completed initialization request, pausing for JVM to settle");
            System.gc();
            synchronized (this) {
                try {
                    this.wait(2000);
                } catch (InterruptedException e) { /* nothing to be done */ }
            }
            
            // create the real test threads
            Thread[] threads = new Thread[numthread];
            TestRunnable[] runnables = new TestRunnable[numthread];
            for (int i = 0; i < threads.length; i++) {
                String lead = "";
                if (numthread > 1) {
                    lead = "Thread " + i + ": ";
                }
                runnables[i] = new TestRunnable(lead, fraction, target, verbose, loops);
                threads[i] = new Thread(runnables[i]);
            }
            
            // run the tests and wait for completion
            System.out.println("Beginning actual timed test run (" + loops + " requests at " +
                fraction + " density)");
            long start = System.currentTimeMillis();
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
            int total = 0;
            for (int i = 0; i < threads.length; i++) {
                Thread thread = threads[i];
                synchronized (thread) {
                    while (thread.isAlive()) {
                        thread.wait();
                    }
                }
                total += runnables[i].m_resultCount;
            }
            if (numthread > 1) {
                System.out.println("Total elapsed time for test run " + (System.currentTimeMillis() - start)
                    + " ms. with " + total + " results");
            }
        }
    }
    
    protected class TestRunnable implements Runnable
    {
        private final String m_lead;
        
        private final String m_path;
        
        private final boolean m_verbose;
        
        private final int m_loops;
        
        private final Random m_random;
        
        private final TimeZone m_utcZone;
        
        private final DateFormat m_format;
        
        private double m_halfRangeFraction;
        
        private double m_halfRangeFractionRoot;
        
        private double m_complementFraction;
        
        protected int m_resultCount;
        
        public TestRunnable(String lead, double fraction, String path, boolean verbose, int loops) {
            m_lead = lead;
            m_path = path;
            m_verbose = verbose;
            m_loops = loops;
            m_random = new Random(5);
            m_halfRangeFraction = fraction * 0.5;
            m_halfRangeFractionRoot = Math.sqrt(fraction) * 0.5;
            m_utcZone = TimeZone.getTimeZone("UTC");
            Calendar calendar = new GregorianCalendar(m_utcZone);
            m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            m_format.setCalendar(calendar);
            try {
                s_minDate = m_format.parse("2000-01-01 01:19:26").getTime();
                s_maxDate = m_format.parse("2003-08-31 23:07:59").getTime();
            } catch (ParseException e) {
                throw new IllegalStateException("Error initializing");
            }
        }
        
        protected QueryData nextQuery() {
            
            // generate date range for query
            long ddiff = s_maxDate - s_minDate;
            long drange = (long)((m_random.nextDouble() * m_halfRangeFraction + m_halfRangeFraction) * ddiff);
            long dbase = s_minDate + (long)(m_random.nextDouble() * (ddiff - drange));
            
            // generate longitude range for query
            double ldiff = s_maxLong - s_minLong;
            float lngrng = (float)((m_random.nextDouble() * m_halfRangeFractionRoot + m_halfRangeFractionRoot) * ldiff);
            float lngbase = s_minLong + (float)(m_random.nextDouble() * (ldiff - lngrng));
            
            // generate latitude range for query
            ldiff = s_maxLat - s_minLat;
            float latrng = (float)((m_random.nextDouble() * m_halfRangeFractionRoot + m_halfRangeFractionRoot) * ldiff);
            float latbase = s_minLat + (float)(m_random.nextDouble() * (ldiff - latrng));
            
            // return generated query information
            return new QueryData(dbase, dbase + drange, lngbase, lngbase + lngrng, latbase, latbase + latrng);
        }
        
        public void run() {
            
            // run timed test
            long start = System.currentTimeMillis();
            Object stub = configure(m_path);
            for (int i = 0; i < m_loops; i++) {
                
                // generate and report query being sent
                QueryData query = nextQuery();
                if (m_verbose) {
                    System.out.println(m_lead + "Running query for date range from "
                        + m_format.format(new Date(query.m_timeMin)) + " to "
                        + m_format.format(new Date(query.m_timeMax)) + ",");
                    System.out.println("  longitude range from " + query.m_longMin + " to " + query.m_longMax + ",");
                    System.out.println("  latitude range from " + query.m_latMin + " to " + query.m_latMax + ",");
                }
                
                // time the actual call to the server
                long base = System.currentTimeMillis();
                Object resp = runQuery(query, stub);
                long time = System.currentTimeMillis() - base;
                
                // process returned results
                if (m_verbose) {
                    System.out.println(m_lead + "Results from query:");
                }
                int count = summarize(m_verbose, resp);
                m_resultCount += count;
                if (m_verbose) {
                    System.out.println(m_lead + "Result match count " + count + " in " + time + " ms.");
                    System.out.println();
                }
            }
            System.out.println(m_lead + "Total elapsed time for test " + (System.currentTimeMillis() - start)
                + " ms. with " + m_resultCount + " results");
        }
    }
    
    protected static class QueryData
    {
        protected final long m_timeMin;
        
        protected final long m_timeMax;
        
        protected final float m_longMin;
        
        protected final float m_longMax;
        
        protected final float m_latMin;
        
        protected final float m_latMax;
        
        protected QueryData(long tmin, long tmax, float lngmin, float lngmax, float latmin, float latmax) {
            m_timeMin = tmin;
            m_timeMax = tmax;
            m_longMin = lngmin;
            m_longMax = lngmax;
            m_latMin = latmin;
            m_latMax = latmax;
        }
    }
}