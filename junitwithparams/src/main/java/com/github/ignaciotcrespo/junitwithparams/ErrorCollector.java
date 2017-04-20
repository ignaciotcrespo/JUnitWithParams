package com.github.ignaciotcrespo.junitwithparams;

/**
 * Created by crespo on 4/15/17.
 */
class ErrorCollector extends org.junit.rules.ErrorCollector {
    private int errors;

    @Override
    public void verify() throws Throwable {
        super.verify();
    }

    @Override
    public void addError(final Throwable error) {
        super.addError(error);
        errors++;
    }

    public int getErrors() {
        return errors;
    }
}
