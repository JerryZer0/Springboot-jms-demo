package com.example.demo.broker.error;

import com.example.demo.broker.MessageSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author CHENJI4
 * @Description solace broker error handler
 * @createTime 11/29/2018
 */

@Component
public class SolaceErrorHandler implements ErrorHandler {

    private Logger logger = LoggerFactory.getLogger(SolaceErrorHandler.class);

    @Value("${solace.broker.autoRetry.max:3}")
    private int maxAutoRetry;

    @Value("${solace.broker.autoRetry.interval:3000}")
    private int retryInterval;

    @Resource
    private MessageSender messageSender;

    public static List<String> excludedHeaders = Arrays
            .asList("JMSDestination", "JMSReplyTo", "JMSMessageID", "JMSTimestamp", "JMSExpiration",
                    "JMSType");

    @Override
    public void handleError(Throwable throwable) {
        try {
            SolaceBrokerException error = SolaceBrokerException.getInstance(throwable);
            if (error != null) {
                TextMessage originMessage = error.getOriginMessage();
                String destination = getDestination(originMessage);
                int delayMillis = getDeliveryDelay(originMessage);
                messageSender
                        .sendMessageToQueueWithMillisDelay(destination, new ErrorMessageCreator(originMessage, throwable, maxAutoRetry),
                                delayMillis);
                logger.warn("retry message time: " + getAutoRetryCount(originMessage) + "/" + maxAutoRetry + " after " + delayMillis + "ms;");
            }
        } catch (Exception e) {
            logger.error("[Solace-JMS-Error] Meet Exception during exception handling", e);
        }

    }

    private int getDeliveryDelay(TextMessage originMessage) {
        int autoRetryCount = getAutoRetryCount(originMessage);
        if (autoRetryCount < maxAutoRetry) {
            return retryInterval;
        }
        return 0;
    }

    private String getDestination(TextMessage originMessage) throws JMSException {
        int autoRetryCount = getAutoRetryCount(originMessage);
        if (autoRetryCount < maxAutoRetry) {
            return originMessage.getJMSDestination().toString();
        }
        return originMessage.getJMSDestination().toString() + "/EXCEPTION";
    }

    public static int getAutoRetryCount(TextMessage message) {
        int autoRetryCount;
        try {
            autoRetryCount = message.getIntProperty("CS_AutoRetryCount");
        } catch (Exception e) {
            autoRetryCount = -1;
        }
        return autoRetryCount;
    }
}


class ErrorMessageCreator implements MessageCreator {

    private TextMessage originMessage;
    private Throwable error;
    private int maxAutoRetry;

    private Logger logger = LoggerFactory.getLogger(ErrorMessageCreator.class);

    public ErrorMessageCreator(TextMessage solaceMessage, Throwable throwable, int maxAutoRetry) {
        this.originMessage = solaceMessage;
        this.error = throwable;
        this.maxAutoRetry = maxAutoRetry;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        try {
            TextMessage message = session.createTextMessage(originMessage.getText());
            copyHeaders(message, originMessage);
            message.setStringProperty("CS_ExceptionDetail", ExceptionUtils.getMessage(error));
            message.setStringProperty("CS_SenderDestName", originMessage.getJMSDestination().toString());
            message.setStringProperty("CS_RetryDestName", originMessage.getJMSDestination().toString());
            String originMessageId =
                    StringUtils.defaultIfEmpty(originMessage.getStringProperty("CS_OriginalMessageID"),
                            originMessage.getJMSMessageID());
            String originTimestamp =
                    StringUtils.defaultIfEmpty(originMessage.getStringProperty("CS_OriginalTimestamp"),
                            Long.toString(originMessage.getJMSTimestamp()));
            message.setStringProperty("CS_OriginalMessageID", originMessageId);
            message.setStringProperty("CS_OriginalTimestamp", originTimestamp);
            message.setIntProperty("CS_AutoRetryCount", increaseAutoRetryCount());

            return message;
        } catch (Exception e) {
            logger.error("[Solace-JMS-Error] [ErrorMessageCreator] Failed to create JMS TextMessage", e);
        }
        return null;
    }

    void copyHeaders(TextMessage message, TextMessage originMessage) {
        try {
            Enumeration propertiesNames = originMessage.getPropertyNames();
            while (propertiesNames.hasMoreElements()) {
                String propertyName = (String) propertiesNames.nextElement();
                if (!SolaceErrorHandler.excludedHeaders.contains(propertyName)) {
                    message.setObjectProperty(propertyName, originMessage.getObjectProperty(propertyName));
                }
            }
        } catch (JMSException e) {
            logger.error("[Solace-JMS-Error] [ErrorMessageCreator] Failed to copy message headers", e);
        }
    }

    int increaseAutoRetryCount() {
        int autoRetryCount = SolaceErrorHandler.getAutoRetryCount(originMessage);
        if (autoRetryCount == -1) {
            return 1;
        }
        if (autoRetryCount == maxAutoRetry) {
            return autoRetryCount;
        }
        return autoRetryCount + 1;
    }

}
