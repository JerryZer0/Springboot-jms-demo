package com.example.demo.broker.error;


import javax.jms.TextMessage;


public class SolaceBrokerException extends Throwable {

    private TextMessage originMessage;

    public SolaceBrokerException(Throwable t, TextMessage originMessage) {
        super(t);
        this.originMessage = originMessage;
    }

    public TextMessage getOriginMessage() {
        return originMessage;
    }

    public static SolaceBrokerException getInstance(Throwable t) {
        if (t == null) {
            return null;
        }

        if (t instanceof SolaceBrokerException) {
            return (SolaceBrokerException) t;
        }

        if (t.getCause() != null && t.getCause() instanceof SolaceBrokerException) {
            return (SolaceBrokerException) t.getCause();
        }

        return getInstance(t.getCause());
    }
}
