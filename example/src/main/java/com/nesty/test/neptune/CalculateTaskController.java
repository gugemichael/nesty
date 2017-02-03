package com.nesty.test.neptune;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.example.httpserver.handler.model.ServiceResponse;

@Controller
@RequestMapping("/web/api")
public class CalculateTaskController {

    @RequestMapping(value = "/task/calculate.json", method = RequestMethod.POST)
    public ServiceResponse calculateTask(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "calculateType", required = false) String calculateType,
            @RequestParam(value = "sampleId", required = false) Long sampleId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("calculateTask %d %s %d %d %d", projectId, calculateType, sampleId, modelId, featureId));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/task/kill.json", method = RequestMethod.POST)
    public ServiceResponse killTask(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "calculateType", required = false) String calculateType,
            @RequestParam(value = "sampleId", required = false) Long sampleId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("calculateTask %d %s %d %d %d", projectId, calculateType, sampleId, modelId, featureId));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/task/poll.json", method = RequestMethod.GET)
    public ServiceResponse pollTask(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "calculateType", required = false) String calculateType,
            @RequestParam(value = "sampleId", required = false) Long sampleId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("calculateTask %d %s %d %d %d", projectId, calculateType, sampleId, modelId, featureId));
        return new ServiceResponse();
    }
}