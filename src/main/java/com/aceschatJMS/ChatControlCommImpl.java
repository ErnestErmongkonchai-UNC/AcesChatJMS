package com.aceschatJMS;

import org.apache.commons.io.FileUtils;

import javax.jms.JMSException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;

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

    // TODO: Make new file if username not taken
    File root = new File("C:\\Users\\eermo\\IdeaProjects");
    File userFile = null;
    String fileName = username + ".txt";

    boolean foundFile = false;

    Collection<File> files = FileUtils.listFiles(root, null, true);

    for (File file : files) {
      if (file.getName().equals(fileName)) {
        System.out.println(file.getAbsolutePath());
        userFile = new File(file.getAbsolutePath());
        chatDatabase.setConversationsFile(userFile);
        // TODO: Fill Chat Database if userFile has text in it
        populateDatabase();
        foundFile = true;
      }
    }
    if (!foundFile) {
      userFile = new File(fileName);
      System.out.println("$ New File created! " + userFile.getAbsolutePath());
    }
    chatDatabase.setConversationsFile(userFile);
  }

  private void populateDatabase() {
    try {
      Scanner myReader = new Scanner(chatDatabase.getConversationsFile());
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        String topicName = data.substring(0, data.indexOf(' '));
        String message = data.substring(data.indexOf(' ') + 1);
        boolean inList = false;
        for (ChatTopic chatTopic : chatDatabase.getChatTopicList()) {
          if (chatTopic.getTopicName().equals(topicName)) {
            chatTopic.addMessage(message + "\n");
            inList = true;
          }
        }
        if (!inList) {
          System.out.println("$ Connecting to Topic: " + topicName);
          connectTopic(topicName);
          serverConnection.addToTopicList(topicName);
          chatDatabase.getActiveTopic().addMessage(message + "\n");
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
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
    String msg = chatDatabase.getUsername() + ": " + message + "\n";
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
