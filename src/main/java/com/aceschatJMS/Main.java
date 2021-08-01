package com.aceschatJMS;

import javax.swing.*;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    SwingUtilities.invokeLater(
            new Runnable() {
              @Override
              public void run() {
                TestGUI testGUI = new TestGUI();
                testGUI.setVisible(true);
              }
            });

    /*
    while (true) {
      System.out.print("$ ");
      String text = scanner.nextLine();
      if(text.equals("quit")) {
        break;
      }
    }
     */
  }
}
