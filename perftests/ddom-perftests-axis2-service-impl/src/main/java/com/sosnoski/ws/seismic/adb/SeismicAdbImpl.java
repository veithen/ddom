/**
 * SeismicBindingImpl.java
 *
 * This file was originally auto-generated from WSDL by the Apache Axis
 * WSDL2Java emitter, them modified to work with the actual service code.
 */

package com.sosnoski.ws.seismic.adb;

public class SeismicAdbImpl extends com.sosnoski.ws.seismic.adb.SeismicAdbSkeleton {
    public com.sosnoski.ws.seismic.types.Results matchQuakes(
        com.sosnoski.ws.seismic.types.MatchQuakes param0) {
        return QuakeBase.getInstance().process(param0);
    }
}
