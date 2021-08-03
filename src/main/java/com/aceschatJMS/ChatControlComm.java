package com.aceschatJMS;

import javax.jms.JMSException;

public interface ChatControlComm {
    void connectBroker(String broker, String username, String password);

    void connectTopic(String topic);

    void sendMessage(String message) throws JMSException;

    void setActiveTopic(String topic);
}
