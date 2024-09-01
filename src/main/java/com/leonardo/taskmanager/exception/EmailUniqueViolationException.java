package com.leonardo.taskmanager.exception;

public class EmailUniqueViolationException extends RuntimeException{
    public EmailUniqueViolationException(String message){
        super(message);
    }
}
