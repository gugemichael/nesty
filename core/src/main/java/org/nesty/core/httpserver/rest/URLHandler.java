package org.nesty.core.httpserver.rest;

import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.core.httpserver.impl.async.HttpResultStatus;
import org.nesty.core.httpserver.rest.response.HttpResponse;

import java.lang.reflect.Method;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLHandler {

    // target class
    private ControllerClassDescriptor provider;
    // target method
    private ControllerMethodDescriptor procedure;

    private URLHandler() {
    }

    public static URLHandler fromProvider(String URI, Class<?> provider, Method procedure) {
        URLHandler handler = new URLHandler();
        handler.provider = new ControllerClassDescriptor(provider);
        handler.procedure = new ControllerMethodDescriptor(URI, procedure);
        return handler;
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
            if (!result.getClass().isPrimitive())
                return new HttpResponse(HttpResultStatus.SUCCESS, result);
            else
                return new HttpResponse(HttpResultStatus.RESPONSE_NOT_VALID);
        } catch (ControllerParamsNotMatchException e) {
            return new HttpResponse(HttpResultStatus.PARAMS_NOT_MATCHED);
        } catch (ControllerParamsParsedException e) {
            return new HttpResponse(HttpResultStatus.PARAMS_CONVERT_ERROR);
        }
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getClazz().getCanonicalName(), procedure.getMethod().getName());
    }
}
