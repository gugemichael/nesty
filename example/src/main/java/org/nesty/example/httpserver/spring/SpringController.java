package org.nesty.example.httpserver.spring;

import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.example.httpserver.spring.ExampleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.nesty.commons.annotations.Controller;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by adol on 17-2-24.
 */
@Component
@Controller
@RequestMapping("/hello")
public class SpringController {
    @Autowired
    ExampleBean exampleBean;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, Object> hello() {
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("msg", exampleBean.msg);

         return map;
    }
}
