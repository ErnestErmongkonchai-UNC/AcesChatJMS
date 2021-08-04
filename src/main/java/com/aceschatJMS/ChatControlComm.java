package com.aceschatJMS;

import javax.jms.JMSException;
import java.util.List;

public interface ChatControlComm {
    void connectBroker(String broker, String username, String password);

    void connectTopic(String topic);

    void sendMessage(String message) throws JMSException;

    boolean topicExists(String topic);

    String getActiveTopic();
}
