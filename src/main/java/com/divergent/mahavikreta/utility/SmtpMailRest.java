package com.divergent.mahavikreta.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.lingala.zip4j.core.ZipFile;

@Component
public class SmtpMailRest {

    public static String fromEmail;


    public static String password;


    public static String host;


    public static String port;


    public static boolean auth;


    public static boolean startTls;


    public static String protocol;


    public static boolean debug;

    @SuppressWarnings("static-access")
    public static boolean sendAttachmentEmail(boolean events, String toEmail, String body,
                                              String subject, String message, boolean isDeleteAttendee, boolean isUpdate, ZipFile zipfile,
                                              List objectList, List<Map> urlList, StringBuffer sb,boolean eventDetelet) {
        boolean b = false;
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol",protocol);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", startTls);
            props.put("mail.smtp.auth", auth);
            // create Authenticator object to pass in Session.getInstance argument
//			Authenticator auth = new Authenticator() {
//				// override the getPasswordAuthentication method
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(fromEmail, password);
//				}
//			};
            Session session = Session.getDefaultInstance(props);
            Transport transport = session.getTransport();
            transport.connect(host, fromEmail, password);
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("mahavikreta", "Mahavikreta CRM"));

            msg.setReplyTo(InternetAddress.parse("", false));
//			msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
//			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(""));
            if (events) {
                Multipart multipart = new MimeMultipart();
                if (objectList != null) {
                    if (!objectList.isEmpty() && objectList.size() > 0) {
                        for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
                            File object = (File) iterator.next();
                            if (object.getName() != null) {
                                BodyPart messageBodyPart = new MimeBodyPart();
                                messageBodyPart = new MimeBodyPart();
                                DataSource source = new FileDataSource(object);
                                messageBodyPart.setDataHandler(new DataHandler(source));
                                messageBodyPart.setFileName(object.getName());
                                multipart.addBodyPart(messageBodyPart);
                            }
                        }

                    }
                }
                if (isDeleteAttendee||eventDetelet) {
                    MimeBodyPart textBodyPart = new MimeBodyPart();
                    textBodyPart.setContent("<b>Event has been canceled<b>", "text/html; charset=utf-8");
                    multipart.addBodyPart(textBodyPart);
                }

                // Create the message body part
                BodyPart messageBodyPart = new MimeBodyPart();
                // Create a multipart message for attachment

                messageBodyPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
                messageBodyPart.setContent(sb.toString(), "text/calendar;method=CANCEL; charset=utf-8");
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                msg.setContent(multipart);
            } else {
                Multipart mimeMultipart = new MimeMultipart();
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setContent(message, "text/plain; charset=utf-8");
                if (zipfile != null) {
                    File zipFile = new File("appointment.zip");
                    DataSource dataSource = new FileDataSource(zipFile);
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.setDataHandler(new DataHandler(dataSource));
                    mbp2.setFileName("appointment.zip");
                    mimeMultipart.addBodyPart(mbp2);
                }
                if (!subject.equals("PasswordChange")) {
                    BodyPart messageBodyPart = new MimeBodyPart();
                    // Create a multipart message for attachment
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
                    messageBodyPart.setContent(sb.toString(), "text/calendar;method=CANCEL");
                    mimeMultipart.addBodyPart(messageBodyPart);
                }
                mimeMultipart.addBodyPart(textBodyPart);
                msg.setContent(mimeMultipart);
            }
            // Send message
            //transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
            b = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {


        }
        return b;
    }

    public static boolean sendMail(boolean events, String body, String toEmails, String subject, String message,
                                   boolean isDeleteAttendee, boolean isUpdate, ZipFile zipfile, List objectList, List<Map> urlList,
                                   StringBuffer sb,boolean eventDetelet) {

//		Properties props = new Properties();
//		props.put("mail.smtp.host", host); // SMTP Host
//		props.put("mail.smtp.port", port); // TLS Port
//		props.put("mail.smtp.auth", auth); // enable authentication
//		props.put("mail.smtp.starttls.enable", startTls); // enable STARTTLS
//		props.put("mail.transport.protocol", protocol);
//		props.put("mail.debug", debug);
//
//		Properties props = System.getProperties();
//    	props.put("mail.transport.protocol",protocol);
//    	props.put("mail.smtp.port", port);
//    	props.put("mail.smtp.starttls.enable", startTls);
//    	props.put("mail.smtp.auth", auth);
        // create Authenticator object to pass in Session.getInstance argument
//		Authenticator auth = new Authenticator() {
//			// override the getPasswordAuthentication method
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(fromEmail, password);
//			}
//		};
//		Session session = Session.getDefaultInstance(props);

        //javax.mail.Session session = javax.mail.Session.getInstance(props, auth);
        boolean b = sendAttachmentEmail(events, toEmails, body, subject, message, isDeleteAttendee, isUpdate,
                zipfile, objectList, urlList, sb,eventDetelet);
        return b;
    }



    @Value("${app.fromEmail}")
    public void setFromEmail(String fromEmail) {
        SmtpMailRest.fromEmail = fromEmail;
    }


    @Value("${app.passwrod}")
    public void setPassword(String password) {
        SmtpMailRest.password = password;
    }

    @Value("${app.host}")
    public  void setHost(String host) {
        SmtpMailRest.host = host;
    }

    @Value("${app.port}")
    public  void setPort(String port) {
        SmtpMailRest.port = port;
    }

    @Value("${app.auth}")
    public  void setAuth(boolean auth) {
        SmtpMailRest.auth = auth;
    }

    @Value("${app.startTls}")
    public  void setStartTls(boolean startTls) {
        SmtpMailRest.startTls = startTls;
    }

    @Value("${app.protocol}")
    public  void setProtocol(String protocol) {
        SmtpMailRest.protocol = protocol;
    }

    @Value("${app.debug}")
    public  void setDebug(boolean debug) {
        SmtpMailRest.debug = debug;
    }

}
