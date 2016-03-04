package org.nesty.example.httpserver.handler;

import org.nesty.commons.annotations.Method;
import org.nesty.commons.annotations.Path;
import org.nesty.commons.constant.http.HttpMethod;

public class ServiceController {

    @Path("/a")
    @Method(HttpMethod.GET)
    public ServiceResponse service() {
//        System.out.println("ServiceController api service() called");
        return new ServiceResponse();
    }
}
