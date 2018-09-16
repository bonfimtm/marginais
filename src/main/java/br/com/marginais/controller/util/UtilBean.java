package br.com.marginais.controller.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Thiago Bonfim
 */
@javax.faces.bean.ManagedBean
@javax.faces.bean.ApplicationScoped
public class UtilBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3148178307985015779L;

	public static String maskBoolean(boolean b) {
		return b ? "Sim" : "Nao";
	}

	public static String maskDate(Date date) {
		if (date == null) {
			return null;
		}
		
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
		ddMMyyyy.setTimeZone(TimeZone.getTimeZone("America/Belem"));
		return ddMMyyyy.format(date);
	}

	public static String maskTime(Date date) {
		if (date == null) {
			return null;
		}
		
		SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		hhmmss.setTimeZone(TimeZone.getTimeZone("America/Belem"));
		return hhmmss.format(date);
	}

	public static String maskDateAndTime(Date date) {
		if (date == null) {
			return null;
		}
		
		SimpleDateFormat ddMMyyyhhmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		ddMMyyyhhmmss.setTimeZone(TimeZone.getTimeZone("America/Belem"));
		return ddMMyyyhhmmss.format(date);
	}

	public static String maskTimestamp(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		
		Date date = new Date(timestamp.getTime());
		SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		hhmmss.setTimeZone(TimeZone.getTimeZone("America/Belem"));
		return hhmmss.format(date);
	}

	public static String maskDateAndTimestamp(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		
		Date date = new Date(timestamp.getTime());
		SimpleDateFormat ddMMyyyhhmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		ddMMyyyhhmmss.setTimeZone(TimeZone.getTimeZone("America/Belem"));
		return ddMMyyyhhmmss.format(date);
	}

}
