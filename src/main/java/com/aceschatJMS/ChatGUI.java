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

  private String username;
  private String password;
  private String broker;

  private DefaultListModel<ChatComm> topics;
  private ChatComm currentConversation;

  private javax.jms.Connection connect = null;
  private javax.jms.Session pubSession = null;
  private javax.jms.Session subSession = null;

  private ChatControlComm chatControlComm;

  public ChatGUI(ChatControlComm chatControlComm) {
    this.chatControlComm = chatControlComm;
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
            broker = JOptionPane.showInputDialog("Enter Broker");
            username = JOptionPane.showInputDialog("Enter Username");
            password = JOptionPane.showInputDialog("Enter Password");
            if (broker.equals("")) {
              broker = DEFAULT_BROKER_NAME;
            }
            if (password.equals("")) {
              password = DEFAULT_PASSWORD;
            }
            System.out.println("$ Connecting " + username + " to " +  broker);
            //TODO: Add listener

            /*
            try {
              javax.jms.ConnectionFactory factory;
              factory = new ActiveMQConnectionFactory(username, password, broker);
              connect = factory.createConnection(username, password);
              pubSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
              subSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
              System.out.println("$ Connected successfully!");
            } catch (javax.jms.JMSException jmse) {
              System.err.println("error: Cannot connect to Broker - " + broker);
              jmse.printStackTrace();
              System.exit(1);
            }

             */
          }
        });

    //Setting JList Model
    topics = new DefaultListModel<>();
    topicList.setModel(topics);

    topicList.getSelectionModel()
        .addListSelectionListener(
            new ListSelectionListener() {
              @Override
              public void valueChanged(ListSelectionEvent e) {
                ChatComm c = (ChatComm) topicList.getSelectedValue();
                newChat(c.getAppTopic(), c.getConversation());
                currentConversation = c;

                //add getChatComm from ChatControl
              }
            });

    addPeerButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String topic = JOptionPane.showInputDialog("Enter Topic");
            currentConversation = new ChatComm(username, topic, connect, pubSession, subSession);
            topics.addElement(currentConversation);

            //add createTopicChat
          }
        });

    typingArea.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              try {
                currentConversation.postMessage(typingArea.getText());
              } catch (JMSException jmsException) {
                jmsException.printStackTrace();
              }
              System.out.println(typingArea.getText());
              typingArea.setText("");
            }

            //add sendMessage
          }
        });
  }

  public void newChat(String topic, List<String> conversation) {
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

}
