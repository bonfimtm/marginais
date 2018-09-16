/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marginais.controller.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Thiago Bonfim
 */
public class Util {

	public static byte[] resizeImage(byte[] bytes, int newMaxWidth) {
		return resizeImage(bytes, newMaxWidth, 1.0f);
	}

	public static byte[] resizeImage(byte[] bytes, int newMaxWidth, float quality) {
		try {

			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytes));

			int newW;
			if (originalImage.getWidth() < newMaxWidth) {
				newW = originalImage.getWidth();
			} else {
				newW = newMaxWidth;
			}
			double scale = newW / (double) originalImage.getWidth();
			int newH = (int) (originalImage.getHeight() * scale);

			BufferedImage resizedImage = new BufferedImage(newW, newH, originalImage.getType());

			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, newW, newH, null);
			g.dispose();

			ImageWriter writer = (ImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			writer.setOutput(ImageIO.createImageOutputStream(baos));
			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(quality);

			writer.write(null, new IIOImage(resizedImage, null, null), param);

			return baos.toByteArray();

		} catch (IOException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static byte[] compressImage(byte[] bytes, int maxSize) {
		try {

			int fileSize = bytes.length;
			byte[] compressedBytes = bytes;
			float quality = 1.f;

			while (fileSize > maxSize && quality > .09f) {
				quality -= .1f;

				BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
				ImageWriter writer = (ImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				writer.setOutput(ImageIO.createImageOutputStream(baos));
				ImageWriteParam param = writer.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(quality);
				writer.write(null, new IIOImage(image, null, null), param);

				compressedBytes = baos.toByteArray();
				fileSize = compressedBytes.length;
			}

			return compressedBytes;

		} catch (IOException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static boolean isValidDate(int day, int month, int year) {
		if (month == Calendar.FEBRUARY) {
			boolean isLeap = new GregorianCalendar().isLeapYear(year);
			if (isLeap) {
				return day <= 29;
			} else {
				return day <= 28;
			}
		} else if (month == Calendar.APRIL || month == Calendar.JUNE || month == Calendar.SEPTEMBER
				|| month == Calendar.NOVEMBER) {
			return day <= 30;
		} else {
			return true;
		}
	}

	public static String encrypt(String string) {
		try {
			String nstr;
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(string.getBytes()));
			nstr = hash.toString(16);
			return nstr;
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static String maskText(String text) {
		return text != null ? text.replace("\n", "<br/>") : null;
	}

	public static String trimCheck(String text) {
		if (text != null) {
			text = text.trim();
			if (!text.equals("")) {
				return text;
			}
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
		InternetAddress addressFrom = new InternetAddress();
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

	// private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
	// private static final String SMTP_PORT = "25";
	private static final String SMTP_PORT = "587";
	private static final String SMTP_SSL_PORT = "465";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	// private static final String FROM = "razeckmail@gmail.com";
	private static final String FROM = "cloudbees_bonfimtm";
	// private static final String PWD = "razeck24";
	private static final String PWD = "razeck24";
	private static final String CHARSET = "UTF-8";
}
