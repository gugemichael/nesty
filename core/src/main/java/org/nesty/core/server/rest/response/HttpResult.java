package org.nesty.core.server.rest.response;

import org.nesty.core.server.acceptor.ResultCode;

public class HttpResult {

    private ResultCode httpStatus;
    private Object httpContent;

    public HttpResult(ResultCode status) {
        this(status, null);
    }

    public HttpResult(ResultCode status, Object content) {
        this.httpStatus = status;
        this.httpContent = content;
    }

    public ResultCode getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(ResultCode httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Object getHttpContent() {
        return httpContent;
    }

    public void setHttpContent(Object httpContent) {
        this.httpContent = httpContent;
    }
}
