package com.bosto.atmapi.exception;

public class InvalidPasswordExcpetion extends RuntimeException {
    public InvalidPasswordExcpetion(){
        super();
    }

    public InvalidPasswordExcpetion(String message){
        super(message);
    }
}
