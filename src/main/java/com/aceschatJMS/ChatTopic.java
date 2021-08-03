package com.aceschatJMS;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Topic;
import java.util.ArrayList;
import java.util.List;

public class ChatTopic {
    private javax.jms.MessageConsumer subscriber;
    private javax.jms.MessageProducer publisher;
    private javax.jms.Topic topic;

    private List<String> conversation;

    public ChatTopic(javax.jms.Topic topic, javax.jms.MessageConsumer subscriber, javax.jms.MessageProducer publisher) {
        this.topic = topic;
        this.subscriber = subscriber;
        this.publisher = publisher;
        this.conversation = new ArrayList<>();
    }

    public MessageConsumer getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(MessageConsumer subscriber) {
        this.subscriber = subscriber;
    }

    public MessageProducer getPublisher() {
        return publisher;
    }

    public void setPublisher(MessageProducer publisher) {
        this.publisher = publisher;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    //TODO: add sender name (Ernest: ____)
    public void addMessage(String message) {
        conversation.add(message);
    }
}
