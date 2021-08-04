package com.aceschatJMS;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;

public class ChatCommunication implements javax.jms.MessageListener {
  private static final String APP_TOPIC = "jms.samples.chat";
  private static final String DEFAULT_USER = "Chatter";
  private static final String DEFAULT_BROKER_NAME = "tcp://localhost:61616";
  private static final String DEFAULT_PASSWORD = "password";

  private javax.jms.Connection connect = null;
  private javax.jms.Session pubSession = null;
  private javax.jms.Session subSession = null;

  public ChatCommunication() {}

  public void connectToBroker(String broker, String username, String password) {
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
  }

  public ChatTopic connectToTopic(String newTopic) {
    try {
      javax.jms.Topic topic = pubSession.createTopic(newTopic);
      javax.jms.MessageConsumer subscriber = subSession.createConsumer(topic);
      subscriber.setMessageListener(this);
      javax.jms.MessageProducer publisher = pubSession.createProducer(topic);
      connect.start();
      // TODO: connection error case
      return new ChatTopic(newTopic, topic, subscriber, publisher);
      // TODO: connect to server to create topic
    } catch (javax.jms.JMSException jmse) {
      jmse.printStackTrace();
    }

    return null;
  }

  public void postMessage(ChatTopic topic, String message) throws JMSException {
    javax.jms.TextMessage msg = pubSession.createTextMessage();
    msg.setText(message);
    topic.getPublisher().send(msg);
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
        // conversation.add(string);
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
    // Chat chat = new Chat(broker, username, password);
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
}
