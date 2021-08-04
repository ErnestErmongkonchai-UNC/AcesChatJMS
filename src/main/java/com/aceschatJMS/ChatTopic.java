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

  private String topicName;
  private List<String> conversation;

  public ChatTopic(
      String topicName,
      javax.jms.Topic topic,
      javax.jms.MessageConsumer subscriber,
      javax.jms.MessageProducer publisher) {
    this.topicName = topicName;
    this.topic = topic;
    this.subscriber = subscriber;
    this.publisher = publisher;
    this.conversation = new ArrayList<>();
  }

  public MessageConsumer getSubscriber() {
    return subscriber;
  }

  public MessageProducer getPublisher() {
    return publisher;
  }

  public Topic getTopic() {
    return topic;
  }

  public String getTopicName() {
    return topicName;
  }

  public List<String> getConversation() {
    return conversation;
  }

  // TODO: add sender name (Ernest: ____)
  public void addMessage(String message) {
    System.out.println("sending(addMessage): " + message);
    conversation.add(message);
  }
}
