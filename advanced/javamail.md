# JavaMail API

It's a programming interface that makes it easy for Java developers to write code that automatically sends an email. It depends on JavaBeans Activation Framework (JAF) API.

When you send an email message, the message is first sent from the mail client software on your computer to your mail server using the SMTP protocol. Then, your mail server uses SMTP to send the mail to the recipient's mail server. Finally, the recipient's mail client uses POP protocol or IMAP protocol to retrieve the mail from the recipient's mail server. Another protocol MIME is used to define how the content of an email message and its attachments are formatted.

|Protocol |Description |
----------|:-----------:|
|SMTP | Simple Mail Transfer Protocol - to send a message from one mail server to another. |
|POP | Post Office Protocol - to retrieve messages from a mail server. It transfers all messages from the mail server to the mail client. POP3 is latest version. |
|IMAP | Internet Message Access Protocol - used by web-based mail services like Gmail and Yahoo. It allows web browser to read messages that are stored on the mail server. IMAP4 is the latest version. |
|MIME | Multipurpose Internet Mail Extension - specifies type of content that can be sent as a message or attachment |

javax.mail.jar - contains Java classes for the JavaMail API

activation.jar - contains Java classes for JavaBean Activation Framework. These are necessary for the JavaMail API to run. It is included in Java SE 6 or later.

- Some of the properties that can be set for a Session object.

mail.transport.protocol - specifies the protocol that's used for the session.For sending emails, it's usually smtp or smtps.

mail.smtp.host - specifies host computer for the SMTP server. If SMTP server is running on the same server as the web application, then this property is set to 'localhost'.

mail.smtp.port - specifies the port that the SMTP server is using.

mail.smtp.auth - specifies whether authentication is required to log in to the SMTP server.

mail.smtp.quitwait - can be set to false to prevent an SSLException when you attempt to connect to a Gamil SMTP server.

- How to get a mail session for a local SMTP server.

```Java
Properties props = new Properties();
props.put("mail.smtp.host", "localhost");
Session session = Session.getDefaultInstance(props);
```

```Java
Properties props = new Properties();
props.put("mail.transport.protocol", "smtp");
props.put("mail.smtp.host", "localhost");
props.put("mail.smtp.port", 25);
Session session = Session.getDefaultInstance(props);
session.setDebug(true);
```

- Mail session for remote SMTP server

```Java
Properties props = new Properties();
props.put("mail.transport.protocol", "smtps");
props.put("mail.smtps.host", smtp.gmail.com");
props.put("mail.smtps.port", 465);
props.put("mail.smtps.auth", "true");
props.put("mail.smtps.quitwait", "false");
Session session = Sessiong.getDefaultInstance(props);
session.setDebut(true);
```

- The static getDefaultInstance method ofthe Session class returns the default Session object. If you change properties of a session object, you must restart the Tomcat server.

## Create the message of the mail

After receiving the Session object, that object is passed to MimeMessage class to create MimeMessage object. Then, subject, body and addresses for the message can be set as required. It's also possible to set content to HTML using setContent method. Then, we can display images, text and provide links to web resources.

```Java
Message message = new MimeMessage(session);
message.setSubject("Hello there");
message.setText("Thanks for all the time you have given");
message.setContent("<h1>Thanks for the wonderful time!</h1>", "text/html");
```

- Message can be sent to recipient, to set a recipient as CC(carbon copy) or BCC (blind carbon copy) we use setRecipient method. To send messgae to multiple recipients, you can use the setRecipients method.
- To define email address, use the InternetAddress class.

```Java
Address fromAddress = new InternetAddress("example@example.com");
message.setFrom(fromAddress);
```

```Java
Address toAddress = new InternetAddress("anu@yahoo.com");
message.setRecipient(Message.RecipientType.TO, toAddress);
```

```Java
Address ccAddress = new InternetAddress("ted@yahoo.com");
message.setRecipient(Message.RecipientType.CC, ccAddress);
```

- Similarly, recipient can be set as Message.RecipientType.BCC

- To include email address and name you can use following method.

```Java
Address toAddress = new InternetAddress("piyu@gmail.com", "Piyush Patel");
```

- Multiple recipients

```Java
Address[] mails = {
  new InternetAddress("piyu1@gmail.com"),
  new InternetAddress("piyu2@gmail.com"),
  new InternetAddress("piyu3@gmail.com")
};
message.setRecipients(Message.RecipientType.TO, mails);
```

If you already have set Recipient and want to add more recipient, you can use addRecipient method.

```Java
message.addRecipient(Message.RecipientType.TO, toAddress);
```

We can use static send method of Transport class with MimeMessage object as the argument. You can use connect method to specify username and password that can be used to connect to the server. If message can't be sent, the send and sendMessage method throws an exception of the SendFailedException type. It contains a list of invalid addresses to which the messgae couldn't be sent, valid addresses to which message wasn't sent and valid addresses to which the message was sent.

- no authentication

```Java
Transport.send(message);
```

- When authentication required

```Java
Transport transport = session.getTransport();
transport.connect(email, password);
transport.sendMessage(message, message.getAllRecipients());
transport.close();
```

**Simple class for sending email from local SMTP server**

```Java
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailUtilLocal {
  public static void sendMail(String to, String from, String subject, String body, boolean bodyIsHTML) throws MessagingException {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.host", "localhost");
    props.put("mail.smtp.port", 25);
    Session session = Session.getDefaultInstance(props);
    session.setDebug(true);

    Message message = new MimeMessage(session);
    message.setSubject(subject);

    if(bodyIsHTML)
      message.setContent(body, "text/html");
    else
      message.setText(body);

    Address fromAddress = new InternetAddress(from);
    Address toAddress = new InternetAddress(to);
    message.setFrom(fromAddress);
    message.setRecipient(message.RecipientType.TO, toAddress);

    Transport.send(message);
  }
}
```

**class for sending email with a remote SMTP server**

```Java
public class MailUtilGmail {
  public static void sendMail(String to, String from, String subject, String body, boolean bodyIsHTML) throws MessagingException {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtps");
    props.put("mail.smtps.host", "smtp.gmail.com");
    props.put("mail.smtps.port", 465);
    props.put("mail.smtps.auth", "true");
    props.put("mail.smtps.quitwait", "false");
    Session session = Session.getDefaultInstance(props);
    session.setDebut(true);

    Message message = new MimeMessage(session);
    message.setSubject(subject);

    if(bodyIsHTML) {
      message.setContent(body, "text/html");
    } else {
      message.setText(body);
    }

    Address fromAddress = new InternetAddress(from);
    Address toAddress = new InternetAddress(to);
    message.setFrom(fromAddress);
    message.setRecipient(Message.RecipientType.TO, toAddress);

    Transport transport = session.getTransport();
    transport.connect("email@gmail.com", "password");
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
  }
}
```
