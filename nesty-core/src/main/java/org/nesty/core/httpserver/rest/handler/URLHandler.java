package org.nesty.core.httpserver.rest.handler;

import org.nesty.core.httpserver.utils.SerializeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLHandler {

    private Class<?> provider;
    private Method procedure;

    private URLHandler() {
    }

    public static URLHandler fromProvider(Class<?> provider, Method procedure) {
        URLHandler handler = new URLHandler();
        handler.provider = provider;
        handler.procedure = procedure;
        return handler;
    }

    public Class<?> getProvider() {
        return provider;
    }

    public Method getProcedure() {
        return procedure;
    }

    public byte[] invoke() {

        /**
         * make new controller class instance with every http request. because
         * of we desire every request may has own context variables and status.
         *
         * TODO : This newInstance() need a empty param default constructor.
         *
         */

        try {
            Object target = provider.newInstance();
            Object result = procedure.invoke(target);
            if (!result.getClass().isPrimitive())
                return SerializeUtils.format(result);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getCanonicalName(), procedure.getName());
    }
}
