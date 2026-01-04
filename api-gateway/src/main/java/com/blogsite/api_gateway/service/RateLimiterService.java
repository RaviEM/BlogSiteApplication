package com.blogsite.api_gateway.service;

import com.blogsite.api_gateway.config.RateLimitConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Service
@Slf4j
public class RateLimiterService {

    private final RateLimitConfig config;
    private Bucket bucket;

    public RateLimiterService(RateLimitConfig config){
        this.config = config;
    }

    @PostConstruct
    public void init(){
        Bandwidth limit = Bandwidth.classic(
                config.getRequestsPerSecond(),
                Refill.greedy(config.getRequestsPerSecond(), Duration.ofSeconds(1))
        );
        bucket = Bucket4j.builder().addLimit(limit).build();

        log.info("Rate limiter initialized - Overall: {} requests/second", config.getRequestsPerSecond());
    }

    public boolean isAllowed(){
        if(!config.isEnabled()){
            return true;
        }

        boolean allowed = bucket.tryConsume(1);
        if(!allowed){
            log.warn("Rate limiter exceeded - {} requests/second limit reached", config.getRequestsPerSecond());
        }
        return allowed;
    }

    public long getRemainingTokens(){ return bucket.getAvailableTokens();}


    public int getRateLimit() { return config.getRequestsPerSecond();}
}
