package com.u.park.uparkbackend.handler;

import javassist.tools.web.BadHttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(BadHttpRequest.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomError handleBadHttpRequestException(BadHttpRequest e) {
        CustomError customError = new CustomError();
        customError.setErrorMessage(e.getMessage());
        customError.setCode(HttpStatus.BAD_REQUEST.value());
        return customError;
    }
}
