package org.nesty.core.server.rest;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Similar javax.servlet and SpringMVC  implemention
 * parameter. can be used for migration code from SpringMVC
 *
 *
 * Author : Michael
 * Date : March 11, 2016
 */
public abstract class Session {

    // client request with unique id, fetch it from http header(Request-Id)
    // if exsit. or generate by UUID
    private long creationTime = System.currentTimeMillis();

    public Session() {

    }

    public String getId() {
        return getRequestId();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessedTime() {
        // TODO : this time will updated by long connection
        //
        return creationTime;
    }

    public int getMaxInactiveInterval() {
        throw new NotImplementedException();
    }

    public void setMaxInactiveInterval(int interval) {
        throw new NotImplementedException();
    }

    public Object getAttribute(String name) {
        Object value;
        if ((value = getParams().get(name)) != null)
            return value;
        if ((value = getAttributes().get(name)) != null)
            return value;
        return null;
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return new Enumeration<String>() {
            Iterator<String> firest = getParams().keySet().iterator();
            Iterator<String> second = getAttributes().keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return firest.hasNext() || second.hasNext();
            }

            @Override
            public String nextElement() {
                try {
                    return firest.next();
                } catch (NoSuchElementException e) {
                    return second.next();
                }
            }
        };
    }

    public String[] getValueNames() {
        String[] arr = new String[getParams().keySet().size() + getAttributes().keySet().size()];
        Enumeration<String> names = getAttributeNames();
        int index = 0;
        while (names.hasMoreElements())
            arr[index++] = names.nextElement();
        return arr;
    }

    public void setAttribute(String name, Object value) {
        getAttributes().put(name, value);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        getAttributes().remove(name);
        getParams().remove(name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        getAttributes().clear();
    }

    public boolean isNew() {
        return true;
    }

    public abstract String getRequestId();

    public abstract Map<String, Object> getAttributes();

    public abstract Map<String, String> getParams();
}
