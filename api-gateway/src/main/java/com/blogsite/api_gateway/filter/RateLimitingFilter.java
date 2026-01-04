package com.blogsite.api_gateway.filter;

import com.blogsite.api_gateway.config.RateLimitConfig;
import com.blogsite.api_gateway.service.RateLimiterService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class RateLimitingFilter extends ZuulFilter{

    private final RateLimiterService rateLimiterService;
    private final RateLimitConfig config;

    public RateLimitingFilter(RateLimiterService rateLimiterService, RateLimitConfig config){
        this.rateLimiterService = rateLimiterService;
        this.config = config;
    }

    @Override
    public String filterType(){ return "pre";}

    @Override
    public int filterOrder(){ return 1;}

    @Override
    public boolean shouldFilter(){ return config.isEnabled();}

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.debug("Rate limit check - URI: {}", request.getRequestURI());

        if(!rateLimiterService.isAllowed()){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            ctx.setResponseBody(buildRateLimitResponse());
            ctx.getResponse().setContentType("application/json");

            addRateLimitHeaders(ctx);
        } else {
            addRateLimitHeaders(ctx);
        }

        return null;
    }

    private void addRateLimitHeaders(RequestContext ctx){
        ctx.addZuulResponseHeader("X-RateLimit-Limit", String.valueOf(rateLimiterService.getRateLimit()));
        ctx.addZuulResponseHeader("X-RateLimit-Remaining", String.valueOf(rateLimiterService.getRemainingTokens()));
        ctx.addZuulResponseHeader("X-RateLimit-Reset", "1");
    }

    private String buildRateLimitResponse() {
        return "{\"success\":false,\"message\":\"Rate limit exceeded.Please try again later.\",\"error\":{\"code\";429,\"details\":\"Too many requests. Maximum "+ rateLimiterService.getRateLimit() + " requests per second allowed.\"}}";
    }
}
