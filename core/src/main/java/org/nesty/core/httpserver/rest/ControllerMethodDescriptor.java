package org.nesty.core.httpserver.rest;

import org.nesty.commons.annotations.Body;
import org.nesty.commons.annotations.Header;
import org.nesty.commons.annotations.PathVariable;
import org.nesty.commons.annotations.RequestParam;
import org.nesty.commons.exception.ControllerParamsNotMatchException;
import org.nesty.commons.exception.ControllerParamsParsedException;
import org.nesty.commons.exception.SerializeException;
import org.nesty.commons.utils.SerializeUtils;
import org.nesty.core.httpserver.rest.ControllerMethodDescriptor.MethodParams.AnnotationType;

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

    public ControllerMethodDescriptor(String URI, Method method) {
        this.method = method;

        String key, value;
        Annotation[][] annotations = method.getParameterAnnotations();
        Class<?>[] paramsTypes = method.getParameterTypes();
        int total = paramsTypes.length;
        this.params = new MethodParams[total];


        // TODO : ugly code here !! messy
        //
        for (int i = 0; i != total; i++) {
            params[i] = new MethodParams(annotations[i][0], paramsTypes[i]);
            if (params[i].annotation instanceof Header) {
                params[i].annotationType = AnnotationType.HEADER;
            } else if (params[i].annotation instanceof RequestParam) {
                params[i].annotationType = AnnotationType.REQUEST_PARAM;
            } else if (params[i].annotation instanceof Body) {
                params[i].annotationType = AnnotationType.BODY;
            } else if (params[i].annotation instanceof PathVariable) {
                params[i].annotationType = AnnotationType.PATH_VARIABLE;
                String name = ((PathVariable) params[i].annotation).value();
                // findout the index of correspond variable
                String[] pathVariable = URI.split("/");
                int index = 0;
                for (String path : pathVariable) {
                    if (path == null || path.isEmpty())
                        continue;
                    if (path.charAt(0) == URLResource.VARIABLE && path.length() > 2) {
                        String varName = path.substring(1, path.length() - 1);
                        if (varName.equals(name))
                            params[i].urlPathIndex = index;
                    }
                    index++;
                }
                if (params[i].urlPathIndex == -1)
                    throw new IllegalArgumentException(String.format("%s[%s] is not found around %s()", PathVariable.class.getSimpleName(), name, method.getName()));
            } else {
                // TODO : throw runtime Exception ?
                throw new IllegalArgumentException("unknown annotation " + params[i].annotation.annotationType().getName());
            }
        }
    }

    public Object invoke(ControllerClassDescriptor clazz, HttpContext context) throws ControllerParamsNotMatchException, ControllerParamsParsedException {
        try {
            Object target = clazz.getClazz().newInstance();
            if (params != null)
                return method.invoke(target, resolveParams(context));
            else
                return method.invoke(target);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {

            /**
             * TODO : we suppose there is no exception on newInstance() and invoke()
             *
             */
            e.printStackTrace();

            throw new ControllerParamsNotMatchException(String.format("invoke %s.%s throw exception %s", clazz.getClass().getName(), method.getName(), e.getMessage()));
        }
    }

    private Object[] resolveParams(HttpContext context) throws ControllerParamsNotMatchException, ControllerParamsParsedException {
        Object[] paramList = new Object[params.length];

        String value = null;
        boolean serialize = false;
        // iterate whole method params
        for (int i = 0; i != paramList.length; i++) {
            boolean required = true;
            switch (params[i].annotationType) {
            case REQUEST_PARAM:
                RequestParam reqParam = (RequestParam) params[i].annotation;
                value = context.getHttpParams().get(reqParam.value());
                // only if required is fase
                if (value == null && !reqParam.required())
                    required = false;
                break;
            case PATH_VARIABLE:
                PathVariable pathParam = (PathVariable) params[i].annotation;
                if (params[i].urlPathIndex < context.getTerms().length)
                    value = context.getTerms()[params[i].urlPathIndex];
                break;
            case HEADER:
                Header header = (Header) params[i].annotation;
                value = context.getHttpHeaders().get(header.value());
                // only if required is fase
                if (value == null && !header.required())
                    required = false;
                break;
            case BODY:
                value = context.getHttpBody();
                serialize = true;
                break;
            }

            if (value == null && required)
                throw new ControllerParamsNotMatchException(String.format("resolve %s failed", params[i].annotation.annotationType().getName()));

            try {
                paramList[i] = parseParam(params[i].clazz, value, serialize);
            } catch (NumberFormatException | SerializeException e) {
                throw new ControllerParamsParsedException(String.format("parse param exception %s", e.getMessage()));
            }
        }

        return paramList;
    }

    private Object parseParam(Class<?> clazz, String value, boolean serialize) throws SerializeException {
        // need body serialize parsed
        if (serialize) {
            return value != null ? SerializeUtils.decode(value, clazz) : null;
        }

        // enum
        if (clazz.isEnum()) {
            // traversal all enum constants. UNNECESSARY test value is null
            for (Object member : clazz.getEnumConstants())
                if (member.toString().equalsIgnoreCase(value))
                    return member;
            return null;
        }

        // default value
        //      String          null
        //      int/short/long  0
        //      float/double    0
        //      boolean         false
        //      Integer/Long/Short null
        //      Boolean         null
        //
        if (clazz == String.class) {
            return value;
        } else if (clazz == int.class || clazz == Integer.class) {
            return value != null ? Integer.parseInt(value) : 0;
        } else if (clazz == short.class || clazz == Short.class) {
            return value != null ? Short.parseShort(value) : (short) 0;
        } else if (clazz == long.class || clazz == Long.class) {
            return value != null ? Long.parseLong(value) : 0L;
        } else if (clazz == float.class || clazz == Float.class) {
            return value != null ? Float.parseFloat(value) : 0.0f;
        } else if (clazz == double.class || clazz == Double.class) {
            return value != null ? Double.parseDouble(value) : 0.0d;
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            // without test null
            return Boolean.parseBoolean(value);
        }

        return value;
    }

    public Method getMethod() {
        return method;
    }

    static class MethodParams {

        // annotation instance
        public Annotation annotation;
        // annotation type. this is used for less <istanceof> operation
        public AnnotationType annotationType;
        // param class type
        public Class<?> clazz;
        // used for PathVariable type param. record its variable index
        public int urlPathIndex = -1;

        public MethodParams(Annotation annotation, Class<?> clazz) {
            this.annotation = annotation;
            this.clazz = clazz;
        }

        enum AnnotationType {
            REQUEST_PARAM, PATH_VARIABLE, BODY, HEADER
        }
    }
}
