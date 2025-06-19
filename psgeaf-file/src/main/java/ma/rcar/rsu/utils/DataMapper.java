package ma.rcar.rsu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataMapper<T> {

	private Class<?> clazz;
	private Map<String, Field> fields = new HashMap<>();
	Map<String, String> errors = new HashMap<>();

	public DataMapper(Class<?> clazz) {
		this.clazz = clazz;

		List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());

		for (Field field : fieldList) {
			Col col = field.getAnnotation(Col.class);
			if (col != null) {
				field.setAccessible(true);
				fields.put(col.name().toLowerCase(), field);

			}
		}
	}

	@SuppressWarnings("unchecked")
	public T map(Map<String, Object> row) throws SQLException {
		try {
			T dto = (T) clazz.getConstructor().newInstance();
			if (row != null) {
				for (Map.Entry<String, Object> entity : row.entrySet()) {
					if (entity.getValue() == null) {
						continue; // Don't set DBNULL
					}
					String column = entity.getKey();
					Field field = fields.get(column.toLowerCase());
					if (field != null) {
						Col col = field.getAnnotation(Col.class);
						boolean isNumber = col != null && col.isMontant();
						field.set(dto, convertInstanceOfObject(entity.getValue(), isNumber));
					}
				}
			}

			return dto;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new SQLException("Problem with data Mapping. See logs.");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new SQLException("Problem with data Mapping. See logs.");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new SQLException("Problem with data Mapping. See logs.");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new SQLException("Problem with data Mapping. See logs.");
		}
	}

	public List<T> map(List<Map<String, Object>> rows) throws SQLException {
		List<T> list = new LinkedList<>();
		if (rows != null) {
			for (Map<String, Object> row : rows) {
				list.add(map(row));
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private T convertInstanceOfObject(Object o, boolean isNumber) {
		try {
			if (o == null)
				return (T) "";

			if (o.getClass() == Timestamp.class)
				return (T) new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Timestamp) o);

			if (o instanceof Clob) {
				return (T) clobToString((Clob) o);
			}

			if (o instanceof Blob) {
				Blob blob = (Blob) o;
				return (T) blobToByte(blob);
			}

			if (o.getClass() == Date.class || o.getClass() == Time.class)
				return (T) new SimpleDateFormat("dd/MM/yyyy").format((Date) o);
			if (isNumber) {// pour formatter les montants
				NumberFormat df = NumberFormat.getInstance(Locale.FRANCE);
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(2);
				df.setGroupingUsed(true);

				if (o.getClass() == String.class) {

					return (T) df.format(Utils.stringToDouble(o.toString()));

				} else
					return (T) df.format((BigDecimal) o);
			}
			return (T) o.toString();
		} catch (ClassCastException e) {
			return null;
		}
	}

	private String clobToString(java.sql.Clob data) {
		final StringBuilder sb = new StringBuilder();

		try {
			final Reader reader = data.getCharacterStream();
			final BufferedReader br = new BufferedReader(reader);

			int b;
			while (-1 != (b = br.read())) {
				sb.append((char) b);
			}

			br.close();
		} catch (SQLException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		}

		return sb.toString();
	}

	private byte[] blobToByte(java.sql.Blob blob) {
		byte[] blobByte = null;
		try {
			int blobLength = (int) blob.length();
			blobByte = blob.getBytes(1, blobLength);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blobByte;
	}
}
