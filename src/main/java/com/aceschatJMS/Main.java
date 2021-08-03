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


    while (true) {
      System.out.print("$ ");
      String text = scanner.nextLine();
      if(text.equals("quit")) {
        break;
      }
    }
     */
    ChatControlComm chatControlComm = new ChatControlCommImpl();
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    ChatGUI chatGUI = new ChatGUI(chatControlComm);
  }
}
