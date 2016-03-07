package org.nesty.core.httpserver.rest.handler;

import org.nesty.commons.annotations.Body;
import org.nesty.commons.annotations.PathVariable;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.core.httpserver.rest.URLContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * controller method descriptor include annotation params
 *
 * Author : Michael
 * Date : March 07, 2016
 */
public class ControllerMethodDescriptor {

    // method
    private Method method;
    // method param include param class type and param annotation
    private MethodParams[] params;

    public ControllerMethodDescriptor(Method method) {
        this.method = method;
        String key, value;

        Annotation[][] annotations = method.getParameterAnnotations();
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
    }

    public Object invoke(ControllerClassDescriptor clazz, URLContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object target = clazz.getClazz().newInstance();
        if (params != null)
            return method.invoke(target, resolveParams(context));
        else
            return method.invoke(target);
    }

    private Object[] resolveParams(URLContext context) {
        return null;
    }

    public Method getMethod() {
        return method;
    }

    class MethodParams {
        public Annotation annotation;
        public Class<?> clazz;

        public MethodParams(Annotation annotation, Class<?> clazz) {
            this.annotation = annotation;
            this.clazz = clazz;
        }
    }
}
