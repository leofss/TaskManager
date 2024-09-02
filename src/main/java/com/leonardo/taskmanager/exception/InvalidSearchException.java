package com.leonardo.taskmanager.exception;

public class InvalidSearchException extends RuntimeException{
    public InvalidSearchException(String message){
        super(message);
    }
}