package com.bosto.atmapi.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@Slf4j
public class CustomerExceptionHandler {
    @ExceptionHandler({JWTDecodeException.class,InitAlgorithmTokenException.class,
            InvalidPasswordExcpetion.class,TokenExpiredException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Error error(RuntimeException e) {
        log.error(e.getMessage());
        return new Error("403", e.getMessage());
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error error(BaseException e) {
        log.error(e.getMessage());
        return BaseException.toError(e);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error error(Exception e) {
        log.error(e.getMessage());
        return new Error ("500", e.getMessage());
    }


}
