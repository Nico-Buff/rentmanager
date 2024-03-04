package com.epf.rentmanager.Exception;

public class DaoException extends Exception{

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
