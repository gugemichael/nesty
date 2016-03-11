package com.nesty.test.neptune;


import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestBody;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.example.httpserver.handler.model.ServiceResponse;

import java.util.List;

@RequestMapping("/web/api")
@Controller
public class CommonController {

    @RequestMapping(value = "/openTabs.json", method = RequestMethod.GET)
    public ServiceResponse getTabRecord(@RequestParam(value = "projectId", required = false) Long projectId,
                                        @RequestBody List<Integer> tabContent) {
        System.out.println(String.format("calculateTask %s", tabContent.get(1)));
        return new ServiceResponse();
    }

}