package org.nesty.core.httpserver.rest.handler;

import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.commons.utils.Tuple;
import org.nesty.core.httpserver.impl.async.HttpResultStatus;
import org.nesty.core.httpserver.rest.URLContext;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

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

    public Tuple<HttpResultStatus, byte[]> call(URLContext context) {
        List<Object> paramsList = new LinkedList<>();


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
                return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.SUCCESS, SerializeUtils.encode(result));
            else
                return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.RESPONSE_NOT_VALID);
        } catch (ControllerParamsNotMatchException e) {
            return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.PARAMS_NOT_MATCHED);
        } catch(ControllerParamsParsedException e) {
            return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.PARAMS_CONVERT_ERROR);
        }
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getClazz().getCanonicalName(), procedure.getMethod().getName());
    }
}
