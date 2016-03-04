package org.nesty.core.httpserver.rest.handler;

import org.nesty.commons.annotations.Body;
import org.nesty.commons.annotations.PathVariable;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.commons.utils.Tuple;
import org.nesty.core.httpserver.impl.async.HttpResultStatus;
import org.nesty.core.httpserver.rest.URLContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

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

    public Tuple<HttpResultStatus, byte[]> invoke(URLContext context) {
        List<Object> paramsList = new LinkedList<>();
        String key, value;

        Annotation[][] annotations = procedure.getParameterAnnotations();
        for (Annotation[] paramsAnnotation : annotations) {
            Annotation annotation = paramsAnnotation[0];
            if (annotation instanceof RequestParam) {
                key = ((RequestParam) annotation).value();
//                value = context.httpParams.get(key);
//                if (value == null) {
//                    return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.PARAMS_NOT_MATCHED, null);
//                } else
//                    paramsList.add();
            } else if (annotation instanceof PathVariable) {

            } else if (annotation instanceof Body) {

            }
        }

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
                return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.SUCCESS, SerializeUtils.format(result));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return new Tuple<HttpResultStatus, byte[]>(HttpResultStatus.INTERNAL_ERROR, null);
    }

    @Override
    public String toString() {
        return String.format("provider=%s, procedure=%s", provider.getCanonicalName(), procedure.getName());
    }
}
