package com.interswitch.exceptions;

public class UserAccountDisabledException extends Exception{
    public UserAccountDisabledException(String message) {
        super(message);
    }
}
