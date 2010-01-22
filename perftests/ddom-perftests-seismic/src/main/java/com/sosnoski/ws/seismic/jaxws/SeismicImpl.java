package com.sosnoski.ws.seismic.jaxws;

import javax.jws.WebService;

import com.sosnoski.ws.seismic.types.MatchQuakes;
import com.sosnoski.ws.seismic.types.Results;
import com.sosnoski.ws.seismic.wsdl.SeismicInterface;

@WebService(endpointInterface="com.sosnoski.ws.seismic.wsdl.SeismicInterface",
            serviceName="Seismic",
            portName="SeismicPort",
            targetNamespace = "http://ws.sosnoski.com/seismic/wsdl",
            wsdlLocation = "META-INF/wsdl/SeismicService.wsdl")
public class SeismicImpl implements SeismicInterface {
    public Results matchQuakes(MatchQuakes parameters) {
        return QuakeBase.process(parameters);
    }
}
