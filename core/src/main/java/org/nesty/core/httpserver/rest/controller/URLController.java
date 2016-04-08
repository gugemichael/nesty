package org.nesty.core.httpserver.rest.controller;

import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.core.httpserver.impl.async.HttpResultStatus;
import org.nesty.core.httpserver.rest.HttpContext;
import org.nesty.core.httpserver.rest.response.HttpResponse;
import org.nesty.core.httpserver.utils.Countable;

import java.lang.reflect.Method;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLController extends Countable {

    // target class
    private ControllerClassDescriptor provider;
    // target method
    private ControllerMethodDescriptor procedure;

    private boolean internal = false;

    private URLController() {
    }

    public static URLController fromProvider(String URI, Class<?> provider, Method procedure) {
        URLController handler = new URLController();
        handler.provider = new ControllerClassDescriptor(provider);
        handler.procedure = new ControllerMethodDescriptor(URI, procedure);
        return handler;
    }

    public URLController internal() {
        this.internal = true;
        return this;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public HttpResponse call(HttpContext context) {
        /**
         * make new controller class instance with every http request. because
         * of we desire every request may has own context variables and status.
         *
         * TODO : This newInstance() need a empty param default constructor.
         *
         */

        try {
            Object result = procedure.invoke(provider, context);
            if (result != null && !result.getClass().isPrimitive())
                return new HttpResponse(HttpResultStatus.SUCCESS, result);
            else
                return new HttpResponse(HttpResultStatus.RESPONSE_NOT_VALID);
        } catch (ControllerParamsNotMatchException e) {
            e.printStackTrace();
            return new HttpResponse(HttpResultStatus.PARAMS_NOT_MATCHED);
        } catch (ControllerParamsParsedException e) {
            e.printStackTrace();
            return new HttpResponse(HttpResultStatus.PARAMS_CONVERT_ERROR);
        }
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getClazz().getCanonicalName(), procedure.getMethod().getName());
    }
}
