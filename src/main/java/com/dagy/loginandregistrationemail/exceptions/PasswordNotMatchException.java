package com.dagy.loginandregistrationemail.exceptions;

public class PasswordNotMatchException extends  RuntimeException{
    public PasswordNotMatchException(String message){
        super(message);
    }
    public PasswordNotMatchException(String message, Throwable cause){
        super(message, cause);
    }

}
