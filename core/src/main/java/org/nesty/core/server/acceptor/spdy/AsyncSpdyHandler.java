package org.nesty.core.server.acceptor.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.nesty.commons.constant.RequestMethod;
import org.nesty.core.server.NestyServerMonitor;
import org.nesty.core.server.acceptor.AsyncRequestDispatcher;
import org.nesty.core.server.rest.RequestContext;
import org.nesty.core.server.rest.URLResource;
import org.nesty.core.server.rest.controller.URLController;

/**
 * nesty
 * <p>
 * Author Michael on 03/03/2016.
 */
public class AsyncSpdyHandler extends AsyncRequestDispatcher<FullHttpRequest> {

    // Http context
    private volatile RequestContext requestContext;
    // channel context
    private ChannelHandlerContext context;

    static AsyncSpdyHandler build() {
        return new AsyncSpdyHandler();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest httpRequest) throws Exception {
        // build context
        this.context = ctx;
        this.requestContext = RequestContext.build(new SpdyRequestVisitor(context.channel()));

        super.channelRead0(ctx, httpRequest);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void handleTimeout() {

    }

    @Override
    public boolean checkup() {
        // check http method
        if (requestContext.getRequestMethod() == RequestMethod.UNKOWN) {
            return false;
        }

        return true;
    }

    @Override
    public URLController findController() {
        // build URLResource from incoming http request
        URLResource resource = URLResource.fromUri(requestContext.getUri(), requestContext.getRequestMethod());
        URLController controller;
        if ((controller = controllerRouter.findURLController(resource)) == null) {
            NestyServerMonitor.incrRequestMiss();
            return null;
        }

        NestyServerMonitor.incrRequestHit();
        controller.hit();

        return controller;
    }

    @Override
    public void execute(URLController controller) {
    }
}
