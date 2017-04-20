package com.github.ignaciotcrespo.junitwithparams;

/**
 * Created by crespo on 4/15/17.
 */
public class WithParamsException extends Exception {
    public WithParamsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WithParamsException(final String message) {
        super(message);
    }
}
