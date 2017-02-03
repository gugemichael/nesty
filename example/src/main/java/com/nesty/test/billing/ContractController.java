package com.nesty.test.billing;

import org.nesty.commons.annotations.*;
import org.nesty.commons.constant.http.RequestMethod;

import java.util.List;

@Controller
public class ContractController {

    @RequestMapping(value = "/contract/new", method = RequestMethod.POST)
    public
    @ResponseBody
    BillingResult<Boolean> createNewContract(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("baseId") String baseId,
            @RequestParam("resourceType") Integer resourceType,
            @RequestParam("specs") String specs
    ) {
        System.out.println(String.format("createNewContract %d %s %d ", tenantId, baseId, resourceType));
        return new BillingResult<Boolean>();
    }

    @RequestMapping(value = "/contract/upgrade", method = RequestMethod.POST)
    public
    @ResponseBody
    BillingResult<Boolean> createUpgradeContract(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("baseId") String baseId,
            @RequestParam("resourceId") Long resourceId,
            @RequestParam("specs") String specs) {
        System.out.println(String.format("createUpgradeContract %d %s %d ", tenantId, baseId, resourceId));
        return new BillingResult<Boolean>();
    }

    @RequestMapping(value = "/contract/renew", method = RequestMethod.POST)
    public
    @ResponseBody
    BillingResult<Boolean> createRenewContract(
            @RequestParam("tenantId") Long tenantId,
            @RequestParam("baseId") String baseId,
            @RequestParam("resourceId") Long resourceId,
            @RequestParam("specs") String specs) {

        System.out.println(String.format("createRenewContract %d %s %d ", tenantId, baseId, resourceId));
        return new BillingResult<Boolean>();
    }

    @RequestMapping(value = "/contract/{tenantId}/{contractId}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    BillingResult<String> cancelContract(
            @PathVariable(value = "contractId") Long contractId,
            @PathVariable(value = "tenantId") Long tenantId,
            @RequestParam(value = "baseId") String baseId
    ) {
        System.out.println(String.format("cancelContract %d %s %d ", tenantId, baseId, contractId));
        return new BillingResult<String>();
    }

    @RequestMapping(value = "/contract/{tenantId}/{contractId}", method = RequestMethod.GET)
    public
    @ResponseBody
    BillingResult<Boolean> getContract(
            @PathVariable(value = "contractId") Long contractId,
            @PathVariable(value = "tenantId") Long tenantId
    ) {
        System.out.println(String.format("getContract %d %d ", tenantId, contractId));
        return new BillingResult<Boolean>();
    }

    @RequestMapping(value = "/contract/{tenantId}", method = RequestMethod.GET)
    public
    @ResponseBody BillingResult<Boolean> listContract(
            @PathVariable(value = "tenantId") Long tenantId,
            @RequestParam(value = "offset") Long offset,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "orderStatus", required = false) Integer orderStatus
    ) {

        System.out.println(String.format("listContract %d %d %d ", tenantId, offset, orderStatus));
        return new BillingResult<Boolean>();
    }

    //
    // TODO : this is duplicated !!!!  @RequestMapping(value = "/contract/unpay/{tenantId}", method = RequestMethod.GET)
    //
    @RequestMapping(value = "/contractUnpay/{tenantId}", method = RequestMethod.GET)
    public
    @ResponseBody BillingResult<List<Boolean>> listUnpayContract(
            @PathVariable(value = "tenantId") Long tenantId
    ) {
        System.out.println(String.format("listUnpayContract %d", tenantId));
        return new BillingResult<List<Boolean>>();
    }

    @RequestMapping(value = "/contractCount/{tenantId}", method = RequestMethod.GET)
    public @ResponseBody
    BillingResult<List<Boolean>> getContractCount(
            @PathVariable(value = "tenantId") Long tenantId
    ) {
        System.out.println(String.format("getContractCount %d", tenantId));
        return new BillingResult<List<Boolean>>();
    }
}

