package com.nesty.test.billing;

public class BillingResult<T> {
    private int errorCode = 0;
    private String errorMsg = "success";
    private T data;
}
