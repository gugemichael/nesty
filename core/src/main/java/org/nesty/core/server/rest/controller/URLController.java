package org.nesty.core.server.rest.controller;

import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.core.server.acceptor.ResultCode;
import org.nesty.core.server.rest.HttpContext;
import org.nesty.core.server.rest.response.HttpResult;
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

    public HttpResult call(HttpContext context) {
        /**
         * make new controller class instance with every http request. because
         * of we desire every request may has own context variables and status.
         *
         * TODO : This newInstance() need a empty param default constructor.
         *
         */

        try {
            Object result = procedure.invoke(context);
            if (result != null && !result.getClass().isPrimitive())
                return new HttpResult(ResultCode.SUCCESS, result);
            else
                return new HttpResult(ResultCode.RESPONSE_NOT_VALID);
        } catch (ControllerParamsNotMatchException e) {
            return new HttpResult(ResultCode.PARAMS_NOT_MATCHED);
        } catch (ControllerParamsParsedException e) {
            return new HttpResult(ResultCode.PARAMS_CONVERT_ERROR);
        }
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getClazz().getCanonicalName(), procedure.getMethod().getName());
    }
}
