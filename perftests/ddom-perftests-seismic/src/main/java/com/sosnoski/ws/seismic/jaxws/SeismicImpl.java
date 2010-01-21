package com.sosnoski.ws.seismic.jaxws;

import com.sosnoski.ws.seismic.types.MatchQuakes;
import com.sosnoski.ws.seismic.types.Results;
import com.sosnoski.ws.seismic.wsdl.SeismicInterface;

public class SeismicImpl implements SeismicInterface {
    public Results matchQuakes(MatchQuakes parameters) {
        return QuakeBase.process(parameters);
    }
}
