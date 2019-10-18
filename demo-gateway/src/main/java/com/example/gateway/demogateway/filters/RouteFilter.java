package com.example.gateway.demogateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by timad on 17/10/2019.
 */
@Component
@Slf4j
public class RouteFilter extends ZuulFilter {

    @Value("${zuul.filter-type-route}")
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
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        log.info("Route Filter request -> {} Route Filter request method -> {} Route Filter Url -> {}", request, request.getMethod(), request.getRequestURL());
        return null;
    }
}
