package org.nesty.example.httpserver.handler;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.HttpMethod;

@Controller
public class ServiceController {

    @RequestMapping(value = "/service/get")
    public ServiceResponse serviceGet() {
        System.out.println("ServiceController api serviceGet() called");
        return new ServiceResponse();
    }

    @RequestMapping(value = "/service/post", method = HttpMethod.POST)
    public ServiceResponse servicePost() {
        System.out.println("ServiceController api servicePost() called");
        return new ServiceResponse();
    }
}
