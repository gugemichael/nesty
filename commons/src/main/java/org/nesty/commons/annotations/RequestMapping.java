package org.nesty.commons.annotations;

import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.http.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
    RequestMethod method() default RequestMethod.GET;
    String consumes() default HttpConstants.CONTENT_TYPE_JSON;
    String produces() default HttpConstants.CONTENT_TYPE_TEXT;
}