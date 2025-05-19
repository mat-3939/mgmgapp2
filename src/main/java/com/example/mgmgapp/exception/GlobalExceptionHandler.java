package com.example.mgmgapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400系例外（すべて404ページに誘導）
    @ExceptionHandler({
    	NoHandlerFoundException.class,
        HttpRequestMethodNotSupportedException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle400Series(Exception ex) {
        return "error/404"; // templates/error/404.html
    }

    // 500系例外（その他すべて）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500(Exception ex) {
        return "error/500"; // templates/error/500.html
    }
}
