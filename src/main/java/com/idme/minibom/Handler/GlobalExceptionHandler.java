package com.idme.minibom.Handler;

import com.idme.minibom.Exception.BaseException;
import com.idme.minibom.Result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result ExceptionHandler(BaseException ex){
        return Result.error(ex.getMessage());
    }
}
