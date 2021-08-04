package com.aceschatJMS;

public class ChatControlGUIImpl implements ChatControlGUI{
    private ChatGUI chatGUI;
    private ChatDatabase chatDatabase;

    public ChatControlGUIImpl(ChatDatabase chatDatabase, ChatGUI chatGUI) {
        this.chatDatabase = chatDatabase;
        this.chatGUI = chatGUI;
    }


}
