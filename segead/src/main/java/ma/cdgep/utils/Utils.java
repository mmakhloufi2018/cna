/**
 * 
 */
package ma.cdgep.utils;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author joreich
 *
 */
public class Utils {

	public static final String FORMAT_DATE = "dd/MM/yyyy";
	public static final String FOURMAT_DATE_TIME_SIMPLE = "dd/MM/yyyy HH:mm:ss";
	public static final String FOURMAT_DATE_STRING = "yyyy-MM-dd";
	private static final String[] formats = { "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ssZ",
			"dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
			"MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS", "MM/dd/yyyy'T'HH:mm:ssZ",
			"MM/dd/yyyy'T'HH:mm:ss", "yyyy:MM:dd HH:mm:ss", "yyyyMMdd", "dd/MM/yyyy HH:mm:ss" };
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	public static DateFormat dateFormatdd = new SimpleDateFormat("dd");
	public static DateFormat dateFormatMM = new SimpleDateFormat("MM");
	public static DateFormat dateFormatyyyy = new SimpleDateFormat("yyyy");

	public static boolean isEmpty(Collection<?> coll) {
		return (coll == null || coll.isEmpty());
	}

	public static boolean isNotEmpty(Collection<?> coll) {
		return !Utils.isEmpty(coll);
	}

	/**
	 * Method that convert a Date Object to String according to default format
	 * 
	 * @param date   a date to be converted
	 * @param format
	 * @return Date
	 */
	public static String dateToString(Date date, String dateFormat) {

		if (dateFormat == null) {
			dateFormat = FORMAT_DATE;
		}

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that convert a String to Date Object according to default format
	 * 
	 * @param sDate  of String to be converted
	 * @param format
	 * @return Date
	 */
	public static Date stringToDate(String sDate, String format) {

		if (format == null) {
			format = FORMAT_DATE;
		}
		Date date = null;
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		try {
			date = formatter.parse(sDate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	public static Boolean isDate(String sDate, String format) {

		if (format == null) {
			format = FORMAT_DATE;
		}
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		try {
			formatter.parse(sDate);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getStringDateFormat(String dateString) {
		if (dateString != null) {
			for (String parse : formats) {
				SimpleDateFormat sdf = new SimpleDateFormat(parse);
				try {
					sdf.parse(dateString);
					return parse;
				} catch (ParseException e) {

				}
			}
		}
		return null;
	}

	public static boolean isValidDateFormat(String dateStr, String dateFormat) {
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static Long stringToLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static String longToString(Long value) {
		try {
			return String.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static String fullName(String firstName, String lastName) {
		try {
			return new StringBuilder(firstName).append(" ").append(lastName).toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static String fromatDateHHmmss(String date) {
		return date + " 23:59:59";
	}

	public static Double stringToDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static String longToDouble(Double value) {
		try {
			return String.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer stringToInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean isEmpty(List<?> list) {

		return list == null || list.isEmpty();
	}

	public static Double convertValue(String value) {
		Double nbr = null;
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,###.00", decimalFormatSymbols);

			nbr = df.parse(value.replace(".", ",")).doubleValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nbr;
	}

	public static String convertValue4AfterComma(Double value) {
		String nbr = null;
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,##0.0000", decimalFormatSymbols);

			nbr = df.format(value);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nbr;
	}

	public static String convertValue3AfterComma(Double value) {
		String nbr = null;
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,##0.000", decimalFormatSymbols);

			nbr = df.format(value);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nbr;
	}

	public static String roundValue(Double value) {
		String nbr = null;
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,##0.00", decimalFormatSymbols);

			nbr = df.format(value);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nbr;
	}

	public static String roundIntegerValue(Double value) {
		String nbr = null;
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,##0", decimalFormatSymbols);

			nbr = df.format(value);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return nbr;
	}

	public static String withOutRoundValue(Double value) {
		return "" + value.intValue();
	}

	public static double round(double value) {
		long factor = (long) Math.pow(10, 2);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static double round(double value, Integer arrondi) {
		long factor = (long) Math.pow(10, arrondi);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static int getDifferenceDays(long d1, long d2) {
		int daysdiff = 0;
		long diff = d2 - d1;
		long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
		daysdiff = (int) diffDays;
		return daysdiff;
	}

	public static Date addMonths(Date d, int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int last = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int current = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.MONTH, month);
		if (last == current)
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static LocalDate minusMonthToStringDate(String stringDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate periodDate = LocalDate.parse(stringDate, formatter);

		return periodDate.minusMonths(1);
	}

	public static boolean between(Date date1, Date date2, Date dateDebut, Date dateFin) {

		if (dateDebut.compareTo(date1) == 0 && dateFin.compareTo(date2) == 0)
			return true;
		else if (dateDebut.compareTo(date1) < 0 && dateFin.compareTo(date2) == 0)
			return true;
		else if (dateDebut.compareTo(date1) == 0 && dateFin.compareTo(date2) < 0)
			return true;
		else if (dateDebut.compareTo(date1) > 0 && dateFin.compareTo(date2) <= 0)
			return true;
		return false;
	}

	public static String currentDateDDMMYYYY() {
		return dateToString(new Date(), "dd/MM/yyyy");

	}

	public static String padLeft(String inputString, int length, Character character) {
		if (inputString == null)
			inputString = "";
		if (inputString.length() >= length) {
			return inputString.substring(0, length);
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append(character);
		}
		sb.append(inputString);

		return sb.toString();
	}

	public static String padRight(String inputString, int length, Character character) {
		if (inputString == null)
			inputString = "";
		if (inputString.length() >= length) {
			return inputString.substring(0, length);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(inputString);
		while (sb.length() < length) {
			sb.append(character);
		}

		return sb.toString();
	}

	public static String formatMonth(int month, Locale locale) {
		DateFormat formatter = new SimpleDateFormat("MMMM", locale);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month - 1);
		return formatter.format(calendar.getTime());
	}

	public static Boolean stringToBoolean(String value) {

		if ("OUI".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value))
			return true;
		else
			return false;
	}

	public static boolean isNumeroMassarValide(String numMassar) {
		String motif = "[A-Za-z]\\d{9}";
		Pattern pattern = Pattern.compile(motif);
		Matcher matcher = pattern.matcher(numMassar);
		if (matcher.matches())
			return true;
		return false;
	}

	public static boolean isDouble(String scoreRsu) {
		try {
			DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
			decimalFormatSymbols.setDecimalSeparator(',');
			decimalFormatSymbols.setGroupingSeparator(' ');
			DecimalFormat df = new DecimalFormat("###,###.00", decimalFormatSymbols);

			df.parse(scoreRsu.replace(".", ",")).doubleValue();
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public static boolean isAgeMoin6Mois(String dateNaissance, String dateDemande) {
		if(fromPeriod(dateDemande).minusMonths(6).isAfter(fromPeriod(dateNaissance)))
			return false;
		return true;
	}
	
	public static boolean isDateAfter011223(String dateNaissance) {
		if(fromPeriod(dateNaissance).isAfter(fromPeriod("2023-11-30")))
			return true;
		return false;
	}

	public static LocalDate fromPeriod(String period) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.FOURMAT_DATE_STRING);
		return LocalDate.parse(period, formatter);

	}
}
