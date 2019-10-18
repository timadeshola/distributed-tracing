package com.example.gateway.demogateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by timad on 17/10/2019.
 */
@Component
@Slf4j
public class PostFilter extends ZuulFilter {

    @Value("${zuul.filter-type-post}")
    private String filterType;

    @Override
    public String filterType() {
        return filterType;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
        log.info("Post Filter response -> {} Post Filter response status -> {}", response, response.getStatus());
        return null;
    }
}
