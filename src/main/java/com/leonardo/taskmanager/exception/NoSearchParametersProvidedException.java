package com.leonardo.taskmanager.exception;

public class NoSearchParametersProvidedException extends RuntimeException{
    public NoSearchParametersProvidedException(String message){
        super(message);
    }

}