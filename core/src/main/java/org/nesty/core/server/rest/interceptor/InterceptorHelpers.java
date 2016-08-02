package org.nesty.core.server.rest.interceptor;

import org.nesty.core.server.rest.interceptor.internal.AccessLog;

public enum InterceptorHelpers {
    ACCESS_LOG(new AccessLog());

    InterceptorHelpers(Interceptor instance) {
        this.instance = instance;
    }

    public Interceptor instance;
}
