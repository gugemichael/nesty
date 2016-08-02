package com.nesty.test.neptune;

public class ApiResult<T> {

    private int errorCode = 0;
    private String errorMsg = "success";
    private T data;
}
