package com.example.gateway.demogateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Created by timad on 17/10/2019.
 */
@Component
@Slf4j
public class SessionFilter extends ZuulFilter {

    @Value("${zuul.filter-type-session}")
    private String filterType;

    @Override
    public String filterType() {
        return filterType;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpSession httpSession = context.getRequest().getSession();
        context.addZuulRequestHeader("Cookie", "SESSION=" + httpSession.getId());
        log.info("Session -> {} Filter Session ID -> {} Route Filter Session context -> {}", httpSession, httpSession.getId(), httpSession.getSessionContext());
        return null;
    }
}
