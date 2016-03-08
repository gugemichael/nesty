package org.nesty.core.httpserver.rest;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.nesty.commons.constant.http.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class URLResource {

    public static final char VARIABLE = '{';

    private URLResourceIdentifier identifier;

    private URLResource() {
    }

    public static URLResource fromHttp(String url, RequestMethod requestMethod) {
        URLResource resource = new URLResource();
        resource.identifier = URLResourceIdentifier.analyse(url);
        resource.identifier.requestMethod = requestMethod;
        return resource;
    }

    @Override
    public String toString() {
        return String.format("identifier{%s}", identifier.toString());
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return !(other == null || !(other instanceof URLResource)) && identifier.equals(((URLResource) other).identifier);
    }

    /**
     * URLResource uniqu identifier member. used for URLResource
     * equals and hashcode id
     */
    static class URLResourceIdentifier {
        protected List<String> fragments = new ArrayList<>(32);
        protected RequestMethod requestMethod;

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
            if (requestMethod != other.requestMethod)
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

        /**
         * TODO : more URL fileds to hash. inefficient hash searching !
         *
         * we calculater hash code without url rest terms array content,
         * it will same as even rest array is diffrent. this can cause hashcode
         * is equaled when http method and url terms array size are same.
         *
         * HashMap put(K, V) use hashcode() to determine which bucket is
         * used to hold the Entry. but get() use hashcode() and equals() to search
         * the Entry. so the correctness has no affected but search performance
         * on non-exist Entry searching.
         *
         */
        @Override
        public int hashCode() {
            return requestMethod.name().hashCode() + fragments.size();
        }

        @Override
        public String toString() {
            return String.format("fragments=%s,requestMethod=%s", Joiner.on("/").skipNulls().join(fragments), requestMethod.name());
        }
    }
}
