package dev.madcat.m3dc3t.util;

public class NOI extends RuntimeException {

    public NOI(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "Fuck off nigga!";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}