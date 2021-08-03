package com.aceschatJMS;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;


public class ChatControlCommImpl implements ChatControlComm {
    private ChatCommunication serverConnection;
    private ChatGUI chatGUI;

    private List<ChatTopic> chatTopicList;
    private ChatTopic activeTopic;

    public ChatControlCommImpl(){
        serverConnection = new ChatCommunication();
        chatTopicList = new ArrayList<>();
        /*
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        ChatGUI chatGUI = new ChatGUI();
                        ChatCommunication serverConnection = new ChatCommunication();
                        chatGUI.setVisible(true);
                    }
                });

         */
    }

    @Override
    public void connectBroker(String broker, String username, String password) {
        serverConnection.connectToBroker(broker, username, password);
    }

    @Override
    public void connectTopic(String topicName) {
        if(topicName.equals(activeTopic.getTopic().toString())) {
            return;
        }
        for (ChatTopic topic : chatTopicList) {
            if (chatTopicList.toString().equals(topicName)) {
                setActiveTopic(topicName);
                return;
            }
        }
        chatTopicList.add(serverConnection.connectToTopic(topicName));
        setActiveTopic(topicName);
    }

    @Override
    public void sendMessage(String message) throws JMSException {
        serverConnection.postMessage(activeTopic, message);
    }

    @Override
    public void setActiveTopic(String topicName) {
        for(ChatTopic topic : chatTopicList) {
            if(topic.getTopic().toString().equals(topicName)) {
                activeTopic = topic;
            }
        }
        //TODO: refresh GUI convo screen
    }
}
