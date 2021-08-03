package com.aceschatJMS;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabase {
  private List<ChatTopic> chatTopicList;
  private ChatTopic activeTopic;

  public ChatDatabase() {
    this.chatTopicList = new ArrayList<>();
    this.activeTopic = null;
  }

  public void addTopic(ChatTopic topic) {
    chatTopicList.add(topic);
  }

  public void setActiveTopic(ChatTopic topic) {
    activeTopic = topic;
  }

  public List<ChatTopic> getChatTopicList() {
    return chatTopicList;
  }

  public ChatTopic getActiveTopic() {
    return activeTopic;
  }
}
