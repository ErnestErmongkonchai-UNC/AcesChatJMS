package com.aceschatJMS;

import javax.jms.JMSException;

public class ChatControlCommImpl implements ChatControlComm {
  private ChatCommunication serverConnection;
  private ChatDatabase chatDatabase;

  public ChatControlCommImpl(ChatDatabase chatDatabase, ChatCommunication chatCommunication) {
    this.serverConnection = chatCommunication;
    this.chatDatabase = chatDatabase;
  }

  @Override
  public void connectBroker(String broker, String username, String password) {
    chatDatabase.setBroker(broker);
    chatDatabase.setUsername(username);
    chatDatabase.setPassword(password);
    serverConnection.connectToBroker(broker, username, password);
  }

  @Override
  public void connectTopic(String topicName) {
    // TODO: return true or false error case
    if (chatDatabase.getActiveTopic() != null
        && topicName.equals(chatDatabase.getActiveTopic().getTopicName())) {
      return;
    } else {
      // Check if topicName exists
      if (chatDatabase.topicExists(topicName)) {
        // topicName exists, set active
        chatDatabase.setActiveTopic(topicName);
        serverConnection.printSelectedTopic();
      } else {
        // doesn't exist, create new topic and set active
        chatDatabase.addTopic(serverConnection.connectToTopic(topicName));
        chatDatabase.setActiveTopic(topicName);
        serverConnection.printSelectedTopic();
      }
    }
  }

  @Override
  public void sendMessage(String message) throws JMSException {
    String msg = chatDatabase.getUsername() + ": " + message + "\n\r";
    serverConnection.postMessage(chatDatabase.getActiveTopic(), msg);
  }

  @Override
  public boolean topicExists(String topic) {
    return chatDatabase.topicExists(topic);
  }

  @Override
  public String getActiveTopic() {
    return chatDatabase.getActiveTopic().getTopicName();
  }
}
