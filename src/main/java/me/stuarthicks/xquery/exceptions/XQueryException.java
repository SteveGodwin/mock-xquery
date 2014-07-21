package me.stuarthicks.xquery.exceptions;

public class XQueryException extends Exception {

    private static final long serialVersionUID = -4168496752595230482L;

    public XQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}