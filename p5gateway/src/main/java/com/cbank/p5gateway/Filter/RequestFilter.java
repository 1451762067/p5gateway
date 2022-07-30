package com.cbank.p5gateway.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Component
public class RequestFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return  1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request =  ctx.getRequest();
        try {
            InputStream  in = request.getInputStream();
            byte[] requestEntityBytes = StreamUtils.copyToByteArray(in);
            {
                //todo 在这里修改请求报文
            }
            ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
                //修改完请求信息需要重新封装
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(requestEntityBytes);
                }
                @Override
                public int getContentLength() {
                    return requestEntityBytes.length;
                }
                @Override
                public long getContentLengthLong() {
                    return requestEntityBytes.length;
                }
            });
        } catch (Exception e) {
            // 设置返回信息
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("修改请求体出错");
            // 用来给后面的 Filter 标识，是否继续执行
            // 对该请求禁止路由，禁止访问下游服务
            ctx.setSendZuulResponse(false);
            System.out.println(String.format("【修改请求报文出错了】：%s", ExceptionUtils.getStackFrames(e)));
        }
        return null;
    }
}