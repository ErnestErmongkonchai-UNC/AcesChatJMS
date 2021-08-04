package com.aceschatJMS;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabase {
  private List<ChatTopic> chatTopicList;
  private ChatTopic activeTopic;

  private String broker;
  private String username;
  private String password;

  public ChatDatabase() {
    this.chatTopicList = new ArrayList<>();
    this.activeTopic = null;
  }

  public void addTopic(ChatTopic topic) {
    System.out.println("$ new topic in topiclist: " + topic.getTopicName());
    chatTopicList.add(topic);
  }

  public void setActiveTopic(String topicName) {
    for(ChatTopic topic : chatTopicList) {
      if(topic.getTopicName().equals(topicName)) {
        System.out.println("$ New Active Topic: " + topicName);
        activeTopic = topic;
      }
    }
  }

  public List<ChatTopic> getChatTopicList() {
    return chatTopicList;
  }

  public ChatTopic getActiveTopic() {
    return activeTopic;
  }

  public boolean topicExists(String topicName) {
    for(ChatTopic topic : chatTopicList) {
      if(topic.getTopicName().equals(topicName)) return true;
    }
    return false;
  }

  public String getBroker() {
    return broker;
  }

  public void setBroker(String broker) {
    this.broker = broker;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
