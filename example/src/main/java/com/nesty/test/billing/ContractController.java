package com.nesty.test.billing;

import org.nesty.commons.annotations.*;
import org.nesty.commons.constant.http.RequestMethod;

import java.util.List;

/**
 * Created by zhaowei.hzw on 2015/10/16.
 * 订单相关的对外接口
 */
@Controller
public class ContractController {

    /**
     * 创建购买资源订单
     *
     * @param tenantId
     * @param baseId
     * @param resourceType 资源类型 0 RDS
     * @param specs
     * @return
     */
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

    /**
     * 创建升级资源的订单
     *
     * @param tenantId
     * @param resourceId
     * @param specs
     * @return
     */
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

    /**
     * 创建续期资源的订单
     *
     * @param tenantId
     * @param resourceId
     * @param specs
     * @return
     */
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

    /**
     * 取消订单
     *
     * @param contractId
     * @param tenantId
     * @return
     */
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

    /**
     * 获得一个订单的详细信息
     *
     * @param contractId
     * @param tenantId
     * @return
     */
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

    /**
     * 获得租户的订单列表（分页）
     *
     * @param tenantId
     * @param offset
     * @param pageSize
     * @param orderStatus
     * @return
     */
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

    /**
     * 获得租户的未付款订单列表
     *
     * @param tenantId
     * @return
     */
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

    /**
     * 获得租户资源订单的总个数
     *
     * @param tenantId
     * @return
     */
    @RequestMapping(value = "/contractCount/{tenantId}", method = RequestMethod.GET)
    public @ResponseBody
    BillingResult<List<Boolean>> getContractCount(
            @PathVariable(value = "tenantId") Long tenantId
    ) {
        System.out.println(String.format("getContractCount %d", tenantId));
        return new BillingResult<List<Boolean>>();
    }
}

