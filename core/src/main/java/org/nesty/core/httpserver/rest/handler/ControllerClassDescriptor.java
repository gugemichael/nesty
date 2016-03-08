package org.nesty.core.httpserver.rest.handler;

/**
 * controller class
 *
 * Author : Michael
 * Date : March 07, 2016
 */
public class ControllerClassDescriptor {

    private Class<?> clazz;

    public ControllerClassDescriptor(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
