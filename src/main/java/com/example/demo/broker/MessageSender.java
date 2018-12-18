package com.example.demo.broker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Howells
 * @ClassName MessageSender.java
 * @Description TODO
 * @createTime 11/29/2018
 */

@Component
public class MessageSender {
    private Logger log = LoggerFactory.getLogger("MessageSender");

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public void sendMessageToTopic(String destination, String message) {
        log.info("Sending event to topic: " + destination);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend(destination, message);
    }

    public void sendMessageToQueueWithMillisDelay(String destination, MessageCreator messageCreator, int delayMillis) {
        log.info("Sending event to queue: " + destination);
        jmsTemplate.setPubSubDomain(false);
        Date startTime = new Date(System.currentTimeMillis() + delayMillis);
        threadPoolTaskScheduler
                .schedule(new DelaySenderRunner(jmsTemplate, destination, messageCreator), startTime);
    }
}

class DelaySenderRunner implements Runnable {

    private JmsTemplate jmsTemplate;
    private String destination;
    private MessageCreator messageCreator;

    DelaySenderRunner(JmsTemplate jmsTemplate, String destination, MessageCreator messageCreator) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.messageCreator = messageCreator;
    }

    @Override
    public void run() {
        jmsTemplate.send(destination, messageCreator);
    }
}
