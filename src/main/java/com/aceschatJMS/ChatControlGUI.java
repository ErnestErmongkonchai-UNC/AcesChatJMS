package com.aceschatJMS;

import java.util.List;

public interface ChatControlGUI {
    void printToScreen(String message);

    void printChatTopic(String topicName, String message);

    void printActiveTopic();
}
