package com.blogsite.blog_common.exception;

public class BlogServiceException extends RuntimeException
{
    public BlogServiceException(String message)
    {
        super(message);
    }
    public BlogServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
