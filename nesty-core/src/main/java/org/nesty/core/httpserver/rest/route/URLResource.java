package org.nesty.core.httpserver.rest.route;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.nesty.commons.constant.http.HttpMethod;
import org.nesty.core.httpserver.rest.HttpRequestVisitor;
import org.nesty.core.httpserver.rest.URLContext;

import java.util.ArrayList;
import java.util.List;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLResource {

    private static final char VARIABLE = '$';

    private URLResourceIdentifier identifier;

    /**
     * http context
     */
    private URLContext context;

    private URLResource() {
    }

    public static URLResource fromHttp(String url, HttpMethod httpMethod) {
        URLResource resource = new URLResource();
        resource.identifier = URLResourceIdentifier.analyse(url);
        resource.identifier.httpMethod = httpMethod;
        return resource;
    }

    public URLResource parse(HttpRequestVisitor visitor) {
        context = new URLContext();
        context.remoteAddress = visitor.accessRemoteAddress();
        context.httpMethod = visitor.accessHttpMethod();
        context.httpHeaders = visitor.accessHttpHeaders();
        context.httpParams = visitor.accessHttpParams();

        if (context.httpMethod != HttpMethod.GET)
            context.httpBody = visitor.accessHttpBody();

        return this;
    }

    public URLContext getURLContext() {
        return context;
    }

    @Override
    public String toString() {
        return String.format("identifier=%s", identifier.toString());
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof URLResource))
            return false;
        return identifier.equals(((URLResource) other).identifier);
    }

    /**
     * URLResource uniqu identifier member. used for URLResource
     * equals and hashcode id
     */
    static class URLResourceIdentifier {
        protected List<String> fragments = new ArrayList<>(32);
        protected HttpMethod httpMethod;

        public static URLResourceIdentifier analyse(String url) {
            URLResourceIdentifier identifier = new URLResourceIdentifier();
            for (String s : Splitter.on('/').trimResults().omitEmptyStrings().split(url))
                identifier.fragments.add(s);
            return identifier;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || !(object instanceof URLResourceIdentifier))
                return false;
            URLResourceIdentifier other = (URLResourceIdentifier) object;
            if (httpMethod != other.httpMethod)
                return false;
            // rest array is not nessary
            if (fragments.size() != other.fragments.size())
                return false;
            for (int i = 0; i != fragments.size(); i++) {
                if (!(fragments.get(i).charAt(0) == VARIABLE || other.fragments.get(i).charAt(0) == VARIABLE || fragments.get(i).equals(other.fragments.get(i))))
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return Joiner.on('.').skipNulls().join(fragments).hashCode() + httpMethod.name().hashCode();
        }

        @Override
        public String toString() {
            return String.format("fragments=%s,httpMethod=%s", Joiner.on("/").skipNulls().join(fragments), httpMethod.name());
        }
    }
}
