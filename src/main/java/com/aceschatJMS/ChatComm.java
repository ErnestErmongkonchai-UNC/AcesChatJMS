package com.aceschatJMS;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

public class ChatComm implements javax.jms.MessageListener{

  private static final String APP_TOPIC = "jms.samples.chat";
  private static final String DEFAULT_USER = "Chatter";
  private static final String DEFAULT_BROKER_NAME = "tcp://localhost:61616";
  private static final String DEFAULT_PASSWORD = "password";

  private javax.jms.Connection connect = null;
  private javax.jms.Session pubSession = null;
  private javax.jms.Session subSession = null;
  private javax.jms.MessageProducer publisher = null;

  private String username;
  private String newTopic;
  private List<String> conversation;

  public ChatComm(String username, String newTopic, javax.jms.Connection connect, javax.jms.Session pubSession, javax.jms.Session subSession) {
    this.username = username;
    this.newTopic = newTopic;
    this.connect = connect;
    this.pubSession = pubSession;
    this.subSession = subSession;

    this.conversation = new ArrayList<>();

    // Create JMS client for publishing and subscribing to messages.
    // Create Publisher and Subscriber to 'chat' topics
    try {
      javax.jms.Topic topic = pubSession.createTopic(newTopic);
      javax.jms.MessageConsumer subscriber = subSession.createConsumer(topic);
      subscriber.setMessageListener(this);
      publisher = pubSession.createProducer(topic);
      // Now that setup is complete, start the Connection
      connect.start();
    } catch (javax.jms.JMSException jmse) {
      jmse.printStackTrace();
    }

  }

  public String getUsername() {
    return username;
  }

  public List<String> getConversation() {
    return conversation;
  }

  public String getAppTopic() {
    return newTopic;
  }

  public void postMessage(String message) throws JMSException {
    javax.jms.TextMessage msg = pubSession.createTextMessage();
    msg.setText(username + ": " + message);
    publisher.send(msg);
  }

  /** Handle the message (as specified in the javax.jms.MessageListener interface). */
  @Override
  public void onMessage(javax.jms.Message aMessage) {
    try {
      // Cast the message as a text message.
      javax.jms.TextMessage textMessage = (javax.jms.TextMessage) aMessage;

      // This handler reads a single String from the
      // message and prints it to the standard output.
      try {
        String string = textMessage.getText();
        conversation.add(string);
        System.out.println(string);
      } catch (javax.jms.JMSException jmse) {
        jmse.printStackTrace();
      }
    } catch (java.lang.RuntimeException rte) {
      rte.printStackTrace();
    }
  }

  /** Cleanup resources and then exit. */
  private void exit() {
    try {
      connect.close();
    } catch (javax.jms.JMSException jmse) {
      jmse.printStackTrace();
    }

    System.exit(0);
  }

  /** Main program entry point. */
  public static void main(String[] argv) {
    // Is there anything to do?
    if (argv.length == 0) {
      printUsage();
      System.exit(1);
    }

    // Values to be read from parameters
    String broker = DEFAULT_BROKER_NAME;
    String username = null;
    String password = DEFAULT_PASSWORD;

    // Check parameters
    for (int i = 0; i < argv.length; i++) {
      String arg = argv[i];
      System.out.println("args::" + arg);

      if (arg.equals("-b")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
          System.err.println("error: missing broker name:port");
          System.exit(1);
        }
        broker = argv[++i];
        continue;
      }

      if (arg.equals("-u")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
          System.err.println("error: missing user name");
          System.exit(1);
        }
        username = argv[++i];
        continue;
      }

      if (arg.equals("-p")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
          System.err.println("error: missing password");
          System.exit(1);
        }
        password = argv[++i];
        continue;
      }

      if (arg.equals("-h")) {
        printUsage();
        System.exit(1);
      }

      // Invalid argument
      System.err.println("error: unexpected argument: " + arg);
      printUsage();
      System.exit(1);
    }

    // Check values read in.
    if (username == null) {
      System.err.println("error: user name must be supplied");
      printUsage();
      System.exit(1);
    }

    // Start the JMS client for the "chat".
    //Chat chat = new Chat(broker, username, password);
  }

  /** Prints the usage. */
  private static void printUsage() {

    StringBuffer use = new StringBuffer();
    use.append("usage: java Chat (options) ...\n\n");
    use.append("options:\n");
    use.append("  -b name:port Specify name:port of broker.\n");
    use.append("               Default broker: " + DEFAULT_BROKER_NAME + "\n");
    use.append("  -u name      Specify unique user name. (Required)\n");
    use.append("  -p password  Specify password for user.\n");
    use.append("               Default password: " + DEFAULT_PASSWORD + "\n");
    use.append("  -h           This help screen.\n");
    System.err.println(use);
  }

  @Override
  public String toString() {
    return newTopic;
  }
}

/*
package com.aceschatJMS;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

public class Chat implements javax.jms.MessageListener, Runnable{

  private static final String APP_TOPIC = "jms.samples.chat";
  private static final String DEFAULT_USER = "Chatter";
  private static final String DEFAULT_BROKER_NAME = "tcp://localhost:61616";
  private static final String DEFAULT_PASSWORD = "password";

  private javax.jms.Connection connect = null;
  private javax.jms.Session pubSession = null;
  private javax.jms.Session subSession = null;
  private javax.jms.MessageProducer publisher = null;

  private String username;
  private String newTopic;
  private List<String> conversation;

  private Thread chatThread;

  public Chat(String username, String topic, javax.jms.Connection connect, javax.jms.Session pubSession, javax.jms.Session subSession) {
    /*
    if (broker.equals("")) {
      this.broker = DEFAULT_BROKER_NAME;
    } else {
      this.broker = broker;
    }
    if (password.equals("")) {
      this.password = DEFAULT_PASSWORD;
    } else {
      this.password = password;
    }
    this.username = username;
     *//*

    this.username = username;
            this.newTopic = topic;
            this.connect = connect;
            this.pubSession = pubSession;
            this.subSession = subSession;

            this.conversation = new ArrayList<>();

        chatThread = new Thread(this, "chatThread");
        chatThread.start();
        }

public String getUsername() {
        return username;
        }

public List<String> getConversation() {
        return conversation;
        }

*/
/** Create JMS client for publishing and subscribing to messages. *//*

private void chatter() {
    */
/*
    // Create a connection.
    try {
      //put this in TestGUI
      javax.jms.ConnectionFactory factory;
      factory = new ActiveMQConnectionFactory(username, password, broker);
      connect = factory.createConnection(username, password);
      pubSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
      subSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
    } catch (javax.jms.JMSException jmse) {
      System.err.println("error: Cannot connect to Broker - " + broker);
      jmse.printStackTrace();
      System.exit(1);
    }

     *//*


        // Create Publisher and Subscriber to 'chat' topics
        try {
        javax.jms.Topic topic = pubSession.createTopic(newTopic);
        javax.jms.MessageConsumer subscriber = subSession.createConsumer(topic);
        subscriber.setMessageListener(this);
        publisher = pubSession.createProducer(topic);
        // Now that setup is complete, start the Connection
        connect.start();
        } catch (javax.jms.JMSException jmse) {
        jmse.printStackTrace();
        }
    */
/*
    try {
      // Read all standard input and send it as a message.
      System.out.println(
          "\nChat application:\n"
              + "=================\n"
              + "The application user "
              + username
              + " connects to the broker at "
              + DEFAULT_BROKER_NAME
              + ".\n"
              + "The application will publish messages to the "
              + newTopic
              + " topic.\n"
              + "The application also subscribes to that topic to consume any messages published there.\n\n"
              + "Type some text, and then press Enter to publish it as a TextMesssage from "
              + username
              + ".\n");

      java.io.BufferedReader stdin = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

      while (true) {
        String s = stdin.readLine();

        if (s == null) exit();
        else if (s.length() > 0) {
          javax.jms.TextMessage msg = pubSession.createTextMessage();
          msg.setText(username + ": " + s);
          publisher.send(msg);
        }
      }

    } catch (java.io.IOException ioe) {
      ioe.printStackTrace();
    } catch (javax.jms.JMSException jmse) {
      jmse.printStackTrace();
    }

     *//*

        }

public void addMessage(String message) throws JMSException {
        javax.jms.TextMessage msg = pubSession.createTextMessage();
        msg.setText(username + ": " + message);
        publisher.send(msg);
        }

*/
/** Handle the message (as specified in the javax.jms.MessageListener interface). *//*

@Override
public void onMessage(javax.jms.Message aMessage) {
        try {
        // Cast the message as a text message.
        javax.jms.TextMessage textMessage = (javax.jms.TextMessage) aMessage;

        // This handler reads a single String from the
        // message and prints it to the standard output.
        try {
        String string = textMessage.getText();
        //conversation.add(string);
        System.out.println(string);
        } catch (javax.jms.JMSException jmse) {
        jmse.printStackTrace();
        }
        } catch (java.lang.RuntimeException rte) {
        rte.printStackTrace();
        }
        }

*/
/** Cleanup resources and then exit. *//*

private void exit() {
        try {
        connect.close();
        } catch (javax.jms.JMSException jmse) {
        jmse.printStackTrace();
        }

        System.exit(0);
        }

//
// NOTE: the remainder of this sample deals with reading arguments
// and does not utilize any JMS classes or code.
//

*/
/** Main program entry point. *//*

public static void main(String[] argv) {
        // Is there anything to do?
        if (argv.length == 0) {
        printUsage();
        System.exit(1);
        }

        // Values to be read from parameters
        String broker = DEFAULT_BROKER_NAME;
        String username = null;
        String password = DEFAULT_PASSWORD;

        // Check parameters
        for (int i = 0; i < argv.length; i++) {
        String arg = argv[i];
        System.out.println("args::" + arg);

        if (arg.equals("-b")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
        System.err.println("error: missing broker name:port");
        System.exit(1);
        }
        broker = argv[++i];
        continue;
        }

        if (arg.equals("-u")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
        System.err.println("error: missing user name");
        System.exit(1);
        }
        username = argv[++i];
        continue;
        }

        if (arg.equals("-p")) {
        if (i == argv.length - 1 || argv[i + 1].startsWith("-")) {
        System.err.println("error: missing password");
        System.exit(1);
        }
        password = argv[++i];
        continue;
        }

        if (arg.equals("-h")) {
        printUsage();
        System.exit(1);
        }

        // Invalid argument
        System.err.println("error: unexpected argument: " + arg);
        printUsage();
        System.exit(1);
        }

        // Check values read in.
        if (username == null) {
        System.err.println("error: user name must be supplied");
        printUsage();
        System.exit(1);
        }

        // Start the JMS client for the "chat".
        //Chat chat = new Chat(broker, username, password);
        }

*/
/** Prints the usage. *//*

private static void printUsage() {

        StringBuffer use = new StringBuffer();
        use.append("usage: java Chat (options) ...\n\n");
        use.append("options:\n");
        use.append("  -b name:port Specify name:port of broker.\n");
        use.append("               Default broker: " + DEFAULT_BROKER_NAME + "\n");
        use.append("  -u name      Specify unique user name. (Required)\n");
        use.append("  -p password  Specify password for user.\n");
        use.append("               Default password: " + DEFAULT_PASSWORD + "\n");
        use.append("  -h           This help screen.\n");
        System.err.println(use);
        }

@Override
public String toString() {
        return newTopic;
        }

@Override
public void run() {
        this.chatter();
        }
        }*/
