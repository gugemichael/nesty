package org.nesty.core.server.rest.controller;

import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.core.server.acceptor.ResultCode;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.response.ResponseResult;
import org.nesty.core.server.utils.Hitcounter;

import java.lang.reflect.Method;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLController extends Hitcounter {

    // target class
    private ControllerClassDescriptor provider;
    // target method
    private ControllerMethodDescriptor procedure;
    // internal controller sign. only for root-path
    private boolean internal = false;

    private URLController() {

    }

    public static URLController fromProvider(String URI, Class<?> providerClass, Method procedure) {
        URLController handler = new URLController();
        handler.provider = new ControllerClassDescriptor(providerClass);
        handler.procedure = new ControllerMethodDescriptor(URI, handler.provider, procedure);
        return handler;
    }

    public URLController internal() {
        this.internal = true;
        return this;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public ResponseResult call(RequestContext context) {
        /**
         * make new controller class instance with every request. because
         * of we desire every request may has own context variables and status.
         *
         * TODO : This newInstance() need a empty param default constructor.
         *
         */

        try {
            Object result = procedure.invoke(context);
            if (result != null && !result.getClass().isPrimitive())
                return new ResponseResult(ResultCode.SUCCESS, result);
            else
                return new ResponseResult(ResultCode.RESPONSE_NOT_VALID);
        } catch (ControllerParamsNotMatchException e) {
            return new ResponseResult(ResultCode.PARAMS_NOT_MATCHED);
        } catch (ControllerParamsParsedException e) {
            return new ResponseResult(ResultCode.PARAMS_CONVERT_ERROR);
        }
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getClazz().getCanonicalName(), procedure.getMethod().getName());
    }
}
