package org.nesty.core.httpserver.rest;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.http.HttpMethod;
import org.nesty.core.httpserver.utils.HttpUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class NettyHttpRequestVisitor implements HttpRequestVisitor {

    private final Channel channel;
    private final FullHttpRequest request;

    public NettyHttpRequestVisitor(Channel channel, FullHttpRequest request) {
        this.channel = channel;
        this.request = request;
    }

    @Override
    public String accessRemoteAddress() {
        for (Map.Entry<String, String> entry : request.headers()) {
            if (entry.getKey().equals(HttpConstants.HEADER_X_FORWARDED_FOR))
                return entry.getValue();
        }
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public HttpMethod accessHttpMethod() {
        return HttpUtils.convertHttpMethodFromNetty(request);
    }

    @Override
    public String accessHttpBody() {
        return request.content().toString(CharsetUtil.UTF_8);
    }

    @Override
    public Map<String, String> accessHttpParams() {
        Map<String, String> params = new HashMap<>(32);

        // from URL
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri(), Charset.forName("UTF-8"));
        for (Map.Entry<String, List<String>> item : decoder.parameters().entrySet())
            params.put(item.getKey(), item.getValue().get(0));

        // query string and body
        if (accessHttpMethod() != HttpMethod.GET) {
            // from content body key-value
            QueryStringDecoder kvDecoder = new QueryStringDecoder(accessHttpBody(), Charset.forName("UTF-8"), false);
            for (Map.Entry<String, List<String>> item : kvDecoder.parameters().entrySet())
                params.put(item.getKey(), item.getValue().get(0));
        }

        return params;
    }

    @Override
    public Map<String, String> accessHttpHeaders() {
        Map<String, String> headers = new HashMap<>(32);
        Iterator<Map.Entry<String, String>> itr = request.headers().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            headers.put(entry.getKey(), entry.getValue());
        }
        return null;
    }
}
