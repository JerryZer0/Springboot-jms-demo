package com.example.demo.broker;


import com.example.demo.broker.error.SolaceBrokerException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.jms.TextMessage;

/**
 * @author Howells
 * @ClassName ShipmentAssociationBroker.java
 * @Description TODO
 * @createTime 11/29/2018
 */

@Component
public class ShipmentAssociationBroker {

    @Resource
    private MessageSender messageSender;

    @JmsListener(destination = "queueName", containerFactory = "queueListenerFactory", concurrency = "10")
    public void processMessage(TextMessage originalMessage) throws SolaceBrokerException {
        try {
            if (originalMessage.getText() == null) {
                return;
            }
            String incomingMessageJson = originalMessage.getText();

            messageSender.sendMessageToTopic("topicName", incomingMessageJson);
        } catch (Exception e) {
            throw new SolaceBrokerException(e, originalMessage);
        }
    }
}
