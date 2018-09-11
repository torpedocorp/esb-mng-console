package kr.co.bizframe.esb.mng.utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	public static String numberToString(Number n) throws ArithmeticException {
		if ((n instanceof Float) && (((Float) n).isInfinite() || ((Float) n).isNaN())
				|| (n instanceof Double) && (((Double) n).isInfinite() || ((Double) n).isNaN()))
			throw new ArithmeticException("JSON can only serialize finite numbers.");
		String s = n.toString().toLowerCase();
		if (s.indexOf('e') < 0 && s.indexOf('.') > 0) {
			for (; s.endsWith("0"); s = s.substring(0, s.length() - 1))
				;
			if (s.endsWith("."))
				s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public static String quote(String string) {
		if (string == null || string.length() == 0)
			return "\"\"";
		int len = string.length();
		StringBuffer sb = new StringBuffer(len + 4);
		sb.append('"');
		for (int i = 0; i < len; i++) {
			char c = string.charAt(i);
			switch (c) {
			case 34: // '"'
			case 47: // '/'
			case 92: // '\\'
				sb.append('\\');
				sb.append(c);
				break;

			case 8: // '\b'
				sb.append("\\b");
				break;

			case 9: // '\t'
				sb.append("\\t");
				break;

			case 10: // '\n'
				sb.append("\\n");
				break;

			case 12: // '\f'
				sb.append("\\f");
				break;

			case 13: // '\r'
				sb.append("\\r");
				break;

			default:
				if (c < ' ' || c >= '\200') {
					String t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
				break;
			}
		}

		sb.append('"');
		return sb.toString();
	}

	public static String trim(String s) {
		return trim(s, null);
	}

	public static String trim(String s, String defaultValue) {
		if (s != null) {
			s = s.trim();
			if (s.length() == 0) {
				s = null;
			}
		}
		return (s != null) ? s : defaultValue;
	}

	public static boolean stringContains(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;
		else
			return str.indexOf(searchStr) >= 0;
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;

		final int length = searchStr.length();
		if (length == 0) {
			return true;
		}

		for (int i = str.length() - length; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, length)) {
				return true;
			}
		}

		return false;
	}

	public static boolean stringIsEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
	}

	public static String getExceptionStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		String stackTrace = sw.getBuffer().toString();

		StringBuffer sb = new StringBuffer();
		for (String s : stringReadLines(new StringReader(stackTrace))) {
			if (!s.contains("AbstractTraceAspect") && !s.contains("AjcClosure")) {
				sb.append(s + "\r\n");
			}
		}
		stackTrace = sb.toString();
		return stackTrace;
	}

	public static List<String> stringReadLines(Reader input) {
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(input);
			String line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
		} catch (Throwable ignore) {

		}
		return list;
	}

	public static List<String> split(String string, String delimiter) {
		if (string == null) {
			return Collections.emptyList();
		}

		StringTokenizer tokenizer = new StringTokenizer(string, delimiter);
		List<String> segments = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			String val = trim(tokenizer.nextToken().trim());
			if (val != null) {
				segments.add(val);
			}
		}
		return segments;
	}

	public static String unquote(String string) {
		if ((string == null) || (string.length() < 2)) {
			return string;
		}

		char first = string.charAt(0);
		char last = string.charAt(string.length() - 1);
		if ((first != last) || ((first != '"') && (first != '\'') && (first != '`'))) {
			return string;
		}

		return string.substring(1, string.length() - 1);
	}

	public static byte[] toUTF8(String str) {
		Charset charset = Charset.defaultCharset();
		String name = charset.name();

		if ("UTF-8".equalsIgnoreCase(name)) {
			return str.getBytes();
		}

		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String[] splitN(String line, int n, String splitExp) {

		String[] sv = null;

		if (line == null)
			return null;

		sv = new String[n];
		String[] tmps = line.trim().split(splitExp, n);
		if (tmps != null && tmps.length != n)
			return null;

		for (int i = 0; i < n; i++) {
			sv[i] = trim(tmps[i]);
		}
		return sv;
	}

	public static int getInteger(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s.trim());
		} catch (NumberFormatException numberformatexception) {
			i = 0;
		}
		return i;
	}

	public static String getListString(Collection<String> arr) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> ir = arr.iterator();
		boolean add = false;
		while (ir.hasNext()) {
			if (add) {
				sb.append(",");
			}
			String str = ir.next();
			sb.append(str.trim());
			add = true;
		}
		return sb.toString();
	}

	public static boolean isHangul(char ch) {
		UnicodeBlock block = UnicodeBlock.of(ch);

		if (UnicodeBlock.HANGUL_SYLLABLES == block || UnicodeBlock.HANGUL_JAMO == block
				|| UnicodeBlock.HANGUL_COMPATIBILITY_JAMO == block) {

			return true;
		}

		return false;
	}

	public static String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (final byte b : a)
			sb.append(String.format("%02x ", b & 0xff));
		return sb.toString();
	}

	public static Date UTC2Date(Date source) throws ParseException {
		String format = "yyyyMMddHHmmss";
		return UTC2Date(getDateTimeS(source, format), format);
	}

	public static Date UTC2Date(String timeS, String format) throws ParseException {
		if (timeS == null)
			return null;
		Date date = null;
		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss'Z'";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = formatter.parse(timeS);
		return date;
	}

	public static String getDateTimeS(Date date, String format) {

		String dts = null;
		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss";

		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREAN);
		dts = formatter.format(date);

		return dts;
	}

	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String[] splitCommandArgs(String command, String args) {
		List<String> matchList = new ArrayList<String>();
		matchList.add(command);
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher regexMatcher = regex.matcher(args);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				// Add double-quoted string without the quotes
				matchList.add(regexMatcher.group(1));
			} else if (regexMatcher.group(2) != null) {
				// Add single-quoted string without the quotes
				matchList.add(regexMatcher.group(2));
			} else {
				// Add unquoted word
				matchList.add(regexMatcher.group());
			}
		}
		String[] out = new String[matchList.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = matchList.get(i);
		}
		return out;
	}

	public static boolean isUUID(String uuid) {
		if (uuid == null)
			return false;
		try {
			UUID fromStringUUID = UUID.fromString(uuid);
			String toStringUUID = fromStringUUID.toString();
			return toStringUUID.equals(uuid);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static String null2space(String s) {
		if (s == null || s.length() == 0)
			return "";
		else
			return s.trim();
	}
}
