package org.nesty.core.server.acceptor.http;

import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.nesty.commons.constant.http.HttpConstants;
import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.protocol.NestyProtocol;
import org.nesty.core.server.rest.request.RequestVisitor;
import org.nesty.core.server.utils.HttpUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public class HttpRequestVisitor implements RequestVisitor {

    private final Channel channel;
    private final FullHttpRequest request;

    public HttpRequestVisitor(Channel channel, FullHttpRequest request) {
        this.channel = channel;
        this.request = request;
    }

    @Override
    public String remoteAddress() {
        for (Map.Entry<String, String> entry : request.headers()) {
            if (entry.getKey().equals(HttpConstants.HEADER_X_FORWARDED_FOR))
                return entry.getValue();
        }
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public RequestMethod method() {
        return HttpUtils.convertHttpMethodFromNetty(request);
    }

    @Override
    public String body() {
        return request.content().toString(CharsetUtil.UTF_8);
    }

    @Override
    public Map<String, String> params() {
        Map<String, String> params = new HashMap<>(32);

        // from URL
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri(), Charset.forName("UTF-8"));
        for (Map.Entry<String, List<String>> item : decoder.parameters().entrySet())
            params.put(item.getKey(), item.getValue().get(0));

        // query string and body
        if (method() != RequestMethod.GET) {
            // from content body key-value
            QueryStringDecoder kvDecoder = new QueryStringDecoder(body(), Charset.forName("UTF-8"), false);
            for (Map.Entry<String, List<String>> item : kvDecoder.parameters().entrySet())
                params.put(item.getKey(), item.getValue().get(0));
        }

        return params;
    }

    @Override
    public Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>(32);
        for (Map.Entry<String, String> entry : request.headers())
            headers.put(entry.getKey(), entry.getValue());
        return headers;
    }

    @Override
    public String uri() {
        return request.getUri();
    }

    @Override
    public String[] terms() {
        String termsUrl = HttpUtils.truncateUrl(request.getUri());
        return FluentIterable.from(Splitter.on('/').omitEmptyStrings().trimResults().split(termsUrl)).toArray(String.class);
    }

    @Override
    public NestyProtocol protocol() {
        return (request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)) ? NestyProtocol.HTTP_1_0 : NestyProtocol.HTTP;
    }

}
