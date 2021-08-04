package com.aceschatJMS;

import javax.swing.*;

public class Main {
  public static void main(String[] args)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException,
          IllegalAccessException {
    /*
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    SwingUtilities.invokeLater(
            new Runnable() {
              @Override
              public void run() {
                ChatGUI chatGUI = new ChatGUI();
                chatGUI.setVisible(true);
              }
            });
     */

    ChatDatabase chatDatabase = new ChatDatabase();

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    ChatGUI chatGUI = new ChatGUI();
    chatGUI.setVisible(true);

    ChatCommunication chatCommunication = new ChatCommunication();

    ChatControlComm chatControlComm = new ChatControlCommImpl(chatDatabase, chatCommunication);

    chatGUI.addControlListener(chatControlComm);

    ChatControlGUI chatControlGUI = new ChatControlGUIImpl(chatDatabase, chatGUI);
  }
}
