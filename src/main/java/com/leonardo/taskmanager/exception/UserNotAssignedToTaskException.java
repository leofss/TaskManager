package com.leonardo.taskmanager.exception;

public class UserNotAssignedToTaskException extends RuntimeException{
    public UserNotAssignedToTaskException(String message){
        super(message);
    }
}
