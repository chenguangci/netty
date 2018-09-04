package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.Iterator;

/**
 * 接收/处理/响应客户端webSocket请求的核心处理类
 */
public class MyWebSocketHandle extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private static final String WEB_SOCKET_URL = "ws://localhost:8888/webSocket";

    /*
     * 服务端处理客户端webSocket请求的核心方法
     */




    /*
     * 客户端与服务器端创建链接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务器端链接开启......");
    }

    /*
     * 客户端与服务器端断开链接的时候调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务器端链接关闭......");
    }

    /*
     * 服务端接收客户端发来的请求结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /*
     * 工程出现异常时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        cause.printStackTrace();
        //关闭Channel
        ctx.close();
    }


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //处理http请求
        if (o instanceof FullHttpRequest) {
            handHttpRequest(channelHandlerContext, (FullHttpRequest)o);
        } else if (o instanceof WebSocketFrame) {  //处理webSocket链接
            handWebSocketFrame(channelHandlerContext, (WebSocketFrame) o);
        }
    }

    /**
     * 处理客户端和服务端webSocket的链接业务
     * @param context
     * @param frame
     */
    private void handWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame) {
        //是否为关闭指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(context.channel(), (CloseWebSocketFrame) frame.retain());
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            context.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //判断是否是二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            System.out.println("不支持二进制消息");
            throw new RuntimeException("不支持二进制消息");
        }
        //返回应答
        String request = ((TextWebSocketFrame)frame).text();
        System.out.println("服务端收到消息 ===>>> " + request);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(new Date().toString() + context.channel().id() + " ===>>> " + request);
        //群发，服务端向每个客户端发送数据
//        NettyConfig.group.writeAndFlush(textWebSocketFrame);
        //只对相应的客户端发送数据
        NettyConfig.group.find(context.channel().id()).writeAndFlush(textWebSocketFrame);
    }

    /**
     * 处理客户端向服务端发起http握手请求的业务
     * @param context
     * @param request
     */
    private void handHttpRequest(ChannelHandlerContext context, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || !("websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(context, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
        handshaker = factory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
        } else {
            handshaker.handshake(context.channel(), request);
        }
    }

    /**
     * 服务端向客户端响应消息
     * @param context
     * @param request
     * @param response
     */
    private void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, DefaultFullHttpResponse response) {
        if (response.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        //服务端向客户端发送数据
        ChannelFuture future = context.channel().writeAndFlush(response);
        if (response.status().code() != 200)
            future.addListener(ChannelFutureListener.CLOSE);
    }
}
