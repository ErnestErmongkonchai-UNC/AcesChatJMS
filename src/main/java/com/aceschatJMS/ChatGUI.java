package com.aceschatJMS;

import javax.jms.JMSException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ChatGUI extends JFrame implements Runnable {
  private static final String APP_TOPIC = "jms.samples.chat";
  private static final String DEFAULT_USER = "Chatter";
  private static final String DEFAULT_BROKER_NAME = "tcp://localhost:61616";
  private static final String DEFAULT_PASSWORD = "password";

  private JButton addPeerButton;
  private JPanel myPanel;
  private JTextField typingArea;
  private JTextArea conversationArea;
  private JTextField chatName;
  private JScrollPane listScrolling;
  private JPanel leftPanel;
  private JButton sendButton;
  private JList topicList;
  private JScrollPane convoScroll;
  private JMenu settingMenu;
  private JMenuBar mainMenu;

  private Thread threadGUI;

  private DefaultListModel<String> topics;
  //private String currentConversation;

  private ChatControlComm chatControlComm = null;

  public ChatGUI() {
    threadGUI = new Thread(this, "GUIThread");
    threadGUI.start();
  }

  @Override
  public void run() {
    //Creating GridPanel
    add(myPanel);
    setTitle("ChatGUI");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(880, 550);
    setLocationRelativeTo(null);

    //Creating MenuBar
    settingMenu = new JMenu("Setting");
    JMenuItem brokerName = new JMenuItem("Broker");
    settingMenu.add(brokerName);
    mainMenu = new JMenuBar();
    mainMenu.add(settingMenu);
    this.add(mainMenu, BorderLayout.NORTH);

    brokerName.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // Create a connection.
            String broker = JOptionPane.showInputDialog("Enter Broker");
            String username = JOptionPane.showInputDialog("Enter Username");
            String password = JOptionPane.showInputDialog("Enter Password");
            if (broker.equals("")) {
              broker = DEFAULT_BROKER_NAME;
            }
            if (password.equals("")) {
              password = DEFAULT_PASSWORD;
            }
            System.out.println("$ Connecting " + username + " to " +  broker);

            // Notify Communications Controller of new broker
            chatControlComm.connectBroker(broker, username, password);
          }
        });

    addPeerButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String topic = JOptionPane.showInputDialog("Enter Topic");
            // if topic is new then add to JList
            if (!chatControlComm.topicExists(topic)) {
              topics.addElement(topic);
            }
            // process topic
            chatControlComm.connectTopic(topic);
          }
        });

    // Setting JList Model
    topics = new DefaultListModel<>();
    topicList.setModel(topics);

    topicList.getSelectionModel()
        .addListSelectionListener(
            new ListSelectionListener() {
              @Override
              public void valueChanged(ListSelectionEvent e) {
                String topicName = (String) topicList.getSelectedValue();

                chatControlComm.connectTopic(topicName);
              }
            });

    typingArea.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              try {
                chatControlComm.sendMessage(typingArea.getText());
              } catch (JMSException jmsException) {
                jmsException.printStackTrace();
              }
              System.out.println(typingArea.getText());
              typingArea.setText("");
            }
          }
        });
  }

  public void newChatConversation(String topic, List<String> conversation) {
    chatName.setText(topic);
    conversationArea.setText("");
    for (int i = 0; i < conversation.size(); i++) {
      conversationArea.append(conversation.get(i));
    }
    conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
  }

  public void printConsole(String message) {
    conversationArea.append(message + "\n\r");
    conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
  }

  public void addControlListener(ChatControlComm chatAdd) {
    chatControlComm = chatAdd;
  }
}
