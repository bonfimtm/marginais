package br.com.marginais.controller;

import java.io.Serializable;
import java.security.Security;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.marginais.controller.util.JsfUtil;

/**
 *
 * @author Thiago Bonfim
 */
@ManagedBean
@SessionScoped
public class ContactController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5867581048145249760L;

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "25";
	// private static final String SMTP_PORT = "587";
	private static final String SMTP_SSL_PORT = "465";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String FROM = "razeckmail@gmail.com";
	private static final String PWD = "razeck24";
	private static final String CHARSET = "UTF-8";

	private String name;
	private String email;
	private String subject;
	private String message;

	public ContactController() {
	}

	public Map<String, String> getSubjectList() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Selecione o assunto", null);
		map.put("Consulta", "Consulta");
		map.put("Dúvida", "Duvida");
		map.put("Sugestão", "Sugestao");
		map.put("Problema", "Problema");
		map.put("Outros", "Outros");
		return map;
	}

	public String send() {
		if (!name.equals("") && email != null && !email.equals("") && message != null && !message.equals("")) {
			String[] recipients = new String[2];
			recipients[0] = "bonfimtm@gmail.com";
			recipients[1] = "marginaiscomunicacao@gmail.com";
			recipients[2] = "pub.atendimento@gmail.com";
			try {
				message = message.replaceAll("\n", "<br />");
				String msg = "<html><body>" + "Nome: " + name + "<br/><br/>" + "Remetente: " + email + "<br/><br/>"
						+ "Mensagem:<br/><br/>" + message + "</body></html>";
				sendSSLMessage(recipients, subject, msg);
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
				JsfUtil.addSuccessMessage("Obrigado pelo contato! Retornaremos o mais breve possível.");
			} catch (MessagingException ex) {
				Logger.getLogger(ContactController.class.getName()).log(Level.SEVERE, null, ex);
				JsfUtil.addErrorMessage(ex.toString());
			} finally {
				name = email = subject = message = null;
			}
		} else {
			JsfUtil.addErrorMessage("Todos os campos são obrigatórios!");
		}

		return null;
	}

	public static void sendMessage(String recipients[], String subject, String message) throws MessagingException {
		Properties p = new Properties();
		p.put("mail.transport.protocol", "smtp");
		p.put("mail.smtp.host", SMTP_HOST_NAME);
		p.put("mail.smtp.port", SMTP_PORT);

		Session mailSession = Session.getInstance(p);

		Message msg = new MimeMessage(mailSession);
		msg.setFrom(new InternetAddress(FROM));
		// msg.setRecipients(Message.RecipientType.TO,
		// InternetAddress.parse(TO));
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSentDate(new Date());
		msg.setSubject(subject);
		// msg.setText(message);
		msg.setContent(message, "text/html; charset=\"" + CHARSET + "\"");

		Transport.send(msg);
	}

	public static void sendSSLMessage(String recipients[], String subject, String message) throws MessagingException {
		Properties p = new Properties();
		p.put("mail.smtp.host", SMTP_HOST_NAME);
		p.put("mail.smtp.auth", "true");
		p.put("mail.debug", "true");
		p.put("mail.smtp.port", SMTP_SSL_PORT);
		p.put("mail.smtp.socketFactory.port", SMTP_SSL_PORT);
		p.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		p.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getInstance(p, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM, PWD);
			}
		});
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(FROM);
		msg.setFrom(addressFrom);
		msg.setHeader("Reply-To", FROM);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSentDate(new Date());
		msg.setSubject(subject);
		// msg.setText(message);
		msg.setContent(message, "text/html; charset=\"" + CHARSET + "\"");

		Transport.send(msg);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
