package org.nesty.core.server.rest.response;

import org.nesty.core.server.acceptor.ResultCode;

public class ResponseResult {

    private ResultCode status;
    private Object body;

    public ResponseResult(ResultCode status) {
        this(status, null);
    }

    public ResponseResult(ResultCode status, Object content) {
        this.status = status;
        this.body = content;
    }

    public ResultCode getStatus() {
        return status;
    }

    public void setStatus(ResultCode status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
