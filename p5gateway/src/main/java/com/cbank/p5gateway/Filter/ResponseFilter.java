package com.cbank.p5gateway.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ResponseFilter extends ZuulFilter {
    public String filterType() {
        return "post";
    }

    public int filterOrder() {
        return 999;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse  response = ctx.getResponse();
        try {
            InputStream stream = ctx.getResponseDataStream();
            byte[] responseEntityBytes = StreamUtils.copyToByteArray(stream);
            {
                //todo 在这里返回响应报文
            }
            ctx.setResponseBody(new String(responseEntityBytes));
        }
        catch (IOException e) {
            // 设置返回信息
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("修改返回体出错");
            // 用来给后面的 Filter 标识，是否继续执行
            // 对该请求禁止路由，禁止访问下游服务
            ctx.setSendZuulResponse(false);
            //打印日志
            System.out.println(String.format("【修改返回报文出错了】：%s", ExceptionUtils.getStackFrames(e)));
        }
        return null;
    }
}