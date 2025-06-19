package ma.cdgp.af.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.List;

public class Utils {

	public static String dateToString(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("dd/MM/yy").format(value);
	}

	public static String dateToStringTime(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(value);
	}

	public static String dateToStringYYYYMMdd(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("yyyyMMdd").format(value);
	}
	
	public static String dateToStringddMMyyyy(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("ddMMyyyy").format(value);
	}
	
	
	public static String dateToStringYYYY_MM_dd(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("yyyy-MM-dd").format(value);
	}
	
	public static Date stringToDateTime(String value) {

		if (value == null)
			return null;

		try {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date stringToDate(String value) {

		if (value == null || value.equals(""))
			return null;

		try {
			return new SimpleDateFormat("dd/MM/yy").parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Double stringToDouble(String valeur) {

		if (isEmpty(valeur))
			return 0d;
		else {

			try {
				return Double.parseDouble(cleanMontant(valeur));
			} catch (Exception e) {
				return 0d;
			}
		}

	}

	public static boolean isEmpty(List<?> list) {
		return (list == null || list.isEmpty());
	}

	public static String cleanMontant(String mnt) {
		return (mnt != null) ? mnt.replace(",", ".").replaceAll("\\s+", "").replace("\t", "").replace(" ", "") : null;
	}

	public static boolean isEmpty(String s) {
		return (s == null || s.equals("") || s.equals("NaN") || s.equals("null"));
	}

	public static BigDecimal toBigDecimal(String nombre1) {
		return isEmpty(nombre1) ? BigDecimal.ZERO : new BigDecimal(nombre1);
	}

	public static BigDecimal division(String nombre1, String nombre2) {
		BigDecimal nombre1BigDecimal = toBigDecimal(nombre1);
		BigDecimal nombre2BigDecimal = toBigDecimal(nombre2);
		return nombre2BigDecimal.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
				: nombre1BigDecimal.divide(nombre2BigDecimal, RoundingMode.HALF_UP);
	}


	public static String getCurrentYear() {
		return Year.now().toString();
	}

	public static String getNextYear() {
		return String.valueOf(Year.now().getValue() + 1);
	}


	public static String dateToStringTimeWithSeconds(Date value) {

		if (value == null)
			return null;

		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(value);
	}
}
