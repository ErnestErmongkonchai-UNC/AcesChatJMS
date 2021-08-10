package com.aceschatJMS;

import java.util.List;

public class ChatControlGUIImpl implements ChatControlGUI{
    private ChatGUI chatGUI;
    private ChatDatabase chatDatabase;

    public ChatControlGUIImpl(ChatDatabase chatDatabase, ChatGUI chatGUI) {
        this.chatDatabase = chatDatabase;
        this.chatGUI = chatGUI;
    }

    @Override
    public void printToScreen(String message) {
        chatGUI.printConsole(message);
    }

    @Override
    public void printChatTopic(String topicName, String message) {
        for(ChatTopic topic : chatDatabase.getChatTopicList()) {
            if(topic.getTopicName().equals(topicName)) {
                topic.addMessage(message);
                chatDatabase.addMessageToFile(topicName + " " + message);
            }
        }
        if(chatDatabase.getActiveTopic().getTopicName().equals(topicName)) {
            printToScreen(message);
        }
    }

    @Override
    public void printActiveTopic() {
        chatGUI.newChatConversation(chatDatabase.getActiveTopic().getTopicName(), chatDatabase.getActiveTopic().getConversation());
    }

    @Override
    public void addToTopicList(String topicName) {
        chatGUI.addTopicList(topicName);
    }
}
