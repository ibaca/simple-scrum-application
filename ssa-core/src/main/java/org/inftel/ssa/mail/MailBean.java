package org.inftel.ssa.mail;

import java.util.Properties;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ManagedBean
@ApplicationScoped
public class MailBean {

    private static final String HOST = MailProperties.getValor("host");
    private static final String PORT = MailProperties.getValor("port_tls");
    private static final String USERNAME = MailProperties.getValor("username_gmail");
    private static final String PASSWORD = MailProperties.getValor("password_gmail");
    private static final String SENDER = MailProperties.getValor("sender");
    
    private String receiver;
    private String subject;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void send() {

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", HOST);
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.user", SENDER);
        props.setProperty("mail.smtp.auth", "true");


        Session session = Session.getInstance(props);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiver));
            message.setSubject(subject);
            message.setText(body);

            Transport transport = session.getTransport("smtp");
            transport.connect(USERNAME, PASSWORD);

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMailProjAssigned(String receiver, String project) {

        this.subject = MailProperties.getValor("subject_mail_project") + " " + project;
        this.body = MailProperties.getValor("body_mail_project") + " " + project;
        this.receiver = receiver;
        send();
    }

    public void sendMailTaskAssigned(String receiver, String task) {

        this.subject = MailProperties.getValor("subject_mail_task") + " " + task;
        this.body = MailProperties.getValor("body_mail_task") + " " + task;
        this.receiver = receiver;
        send();
    }
    
    public void sendMailTaskStatus(String receiver, String task, String status) {

        this.subject = MailProperties.getValor("subject_mail_status") + " " + task;
        this.body = MailProperties.getValor("body_mail_status") + " " + status;
        this.receiver = receiver;
        send();
    }
}
