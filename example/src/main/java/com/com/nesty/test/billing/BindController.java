package com.nesty.test.billing;

import org.nesty.commons.annotations.Controller;
import org.nesty.commons.annotations.RequestMapping;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.commons.annotations.ResponseBody;
import org.nesty.commons.constant.http.RequestMethod;

@Controller
public class BindController {

    @RequestMapping(value = "/rds/bind", method = RequestMethod.POST)
    public @ResponseBody
    BillingResult<Void> bind(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("instanceId") String instanceId,
            @RequestParam("projectId") Long projectId) {
        System.out.println(String.format("bind %d %s %d ", projectId, instanceId, tenantId));
        return new BillingResult();
    }

    @RequestMapping(value = "/rds/unbind", method = RequestMethod.POST)
    public @ResponseBody
    BillingResult<Void> unbind(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("instanceId") String instanceId) {
        System.out.println(String.format("unbind %s %d ", instanceId, tenantId));
        return new BillingResult();
    }

    @RequestMapping(value = "/rds/project", method = RequestMethod.GET)
    public @ResponseBody
    BillingResult<Boolean> getBindProject(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("instanceId") String instanceId) {
        System.out.println(String.format("getBindProject %s %d ", instanceId, tenantId));
        return new BillingResult<Boolean>();
    }
}