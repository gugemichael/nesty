package org.nesty.core.server.acceptor.spdy;

import org.nesty.core.server.rest.ExecutorTask;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.controller.URLController;
import org.nesty.core.server.rest.interceptor.Interceptor;
import org.nesty.core.server.rest.response.ResponseResult;

import java.util.List;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
class SpdyExecutableTask extends ExecutorTask<Object> {

    SpdyExecutableTask(RequestContext requestContext, List<Interceptor> interceptor, URLController handler) {
        super(requestContext, interceptor, handler);
    }

    @Override
    protected Object filterReject() {
        return null;
    }

    @Override
    protected Object process(ResponseResult result) {
        return null;
    }

}
