/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

package kr.co.bizframe.esb.mng.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
	public static String getCurrentDateTime(String format) {

		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss";

		String current = "";
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		Date currTime = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREAN);
		current = formatter.format(currTime);

		return current;
	}

	public static String getDateTimeS(Date date, String format) {

		String dts = null;
		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss";

		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREAN);
		dts = formatter.format(date);

		return dts;
	}

	public static Date parseDateTime(String s, String format) throws ParseException {
		Date date = null;

		if (null == format || "".equals(format))
			format = "yyyy-MM-dd'T'HH:mm:ss";

		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREAN);
		date = formatter.parse(s);
		return date;
	}

	public static Date getCurrentDate() {

		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		Date currTime = cal.getTime();
		return currTime;
	}

	public static String addYearFromCurrentTime(int year) {
		String formattype = "yyyyMMdd";
		String date = "";
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		cal.add(Calendar.YEAR, year);
		Date time = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formattype, Locale.KOREAN);
		date = formatter.format(time);

		return date;

	}

	public static String addYear(String fromTime, String fromDateFormat, int year) throws ParseException {
		String formattype = "yyyyMMdd";
		fromTime = convertDate(fromTime, fromDateFormat, formattype, "");
		String date = "";
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		setTime(cal, fromTime);
		cal.add(Calendar.YEAR, year);

		Date time = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formattype, Locale.KOREAN);
		date = formatter.format(time);

		return date;

	}

	public static String addMonth(String fromTime, String fromDateFormat, int month) throws ParseException {
		String formattype = "yyyyMMdd";
		fromTime = convertDate(fromTime, fromDateFormat, formattype, "");

		String date = "";
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		setTime(cal, fromTime);
		cal.add(Calendar.MONTH, month);
		Date time = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formattype, Locale.KOREAN);
		date = formatter.format(time);
		return date;
	}

	public static String addDay(String fromTime, String fromDateFormat, int day) throws Exception {
		String formattype = "yyyyMMdd";

		fromTime = convertDate(fromTime, fromDateFormat, formattype, "");

		String date = "";
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		setTime(cal, fromTime);
		cal.add(Calendar.DATE, day);
		Date time = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formattype, Locale.KOREAN);
		date = formatter.format(time);
		return date;
	}

	private static void setTime(Calendar cal, String date) {

		if (date == null || date.length() < 8) {
			return;
		}

		String syear = date.substring(0, 4);
		String smon = date.substring(4, 6);
		String sday = date.substring(6, 8);

		int year = Integer.parseInt(syear);
		int mon = getMonth(smon);
		int day = Integer.parseInt(sday);
		cal.set(year, mon, day);

	}

	private static int getMonth(String month) {

		if (month == null) {

		} else if (month.equals("01")) {
			return Calendar.JANUARY;
		} else if (month.equals("02")) {
			return Calendar.FEBRUARY;
		} else if (month.equals("03")) {
			return Calendar.MARCH;
		} else if (month.equals("04")) {
			return Calendar.APRIL;
		} else if (month.equals("05")) {
			return Calendar.MAY;
		} else if (month.equals("06")) {
			return Calendar.JUNE;
		} else if (month.equals("07")) {
			return Calendar.JULY;
		} else if (month.equals("08")) {
			return Calendar.AUGUST;
		} else if (month.equals("09")) {
			return Calendar.SEPTEMBER;
		} else if (month.equals("10")) {
			return Calendar.OCTOBER;
		} else if (month.equals("11")) {
			return Calendar.NOVEMBER;
		} else if (month.equals("12")) {
			return Calendar.DECEMBER;
		}
		return 0;
	}

	public static String toUTC(Date date, String format) {

		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss'Z'";
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREAN);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String utc = formatter.format(date);
		return utc;
	}

	public static Date UTC2Date(String s, String format) throws Exception {
		Date date = null;
		if (null == format || "".equals(format))
			format = "yyyyMMddHHmmss'Z'";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = formatter.parse(s);
		return date;
	}

	public static boolean isValidDate(String date) {
		try {
			if (date.length() != 8) {
				return false;
			}

			if (Strings.getInteger(date) == 0) {
				return false;
			}

			if (Integer.parseInt(date.substring(0, 4)) < 1900 || Integer.parseInt(date.substring(0, 4)) > 9999) {
				return false;
			}
			if (Integer.parseInt(date.substring(4, 6)) < 1 || Integer.parseInt(date.substring(4, 6)) > 12) {
				return false;
			}
			if (Integer.parseInt(date.substring(6, 8)) < 1
					|| Integer.parseInt(date.substring(6, 8)) > getLastDay(Integer.parseInt(date.substring(0, 4)),
							Integer.parseInt(date.substring(4, 6)))) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int getLastDay(int year, int month) {
		int arrDay[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arrDay[1] = 29;
		}
		return arrDay[month - 1];
	}

	public static String convertDate(String strSource, String fromDateFormat, String toDateFormat, String strTimeZone)
			throws ParseException {
		SimpleDateFormat simpledateformat = null;
		Date date = null;
		if (Strings.null2space(strSource).trim().equals("")) {
			return "";
		}
		if (Strings.null2space(fromDateFormat).trim().equals(""))
			fromDateFormat = "yyyyMMdd";
		if (Strings.null2space(toDateFormat).trim().equals(""))
			toDateFormat = "yyyy-MM-dd";

		simpledateformat = new SimpleDateFormat(fromDateFormat);
		date = simpledateformat.parse(strSource);
		if (!Strings.null2space(strTimeZone).trim().equals("")) {
			simpledateformat.setTimeZone(TimeZone.getTimeZone(strTimeZone));
		}
		simpledateformat = new SimpleDateFormat(toDateFormat);

		return simpledateformat.format(date);

	}

	public static String datetime(Date d) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int yy = cal.get(Calendar.YEAR);
		int mo = cal.get(Calendar.MONTH) + 1;
		int dd = cal.get(Calendar.DAY_OF_MONTH);

		int hr24 = cal.get(Calendar.HOUR_OF_DAY);
		int mi = cal.get(Calendar.MINUTE);

		String yyy = null;
		String mmo = null;
		String ddd = null;

		String hr24s = null;
		String mis = null;

		yyy = "" + yy;

		if (mo < 10) {
			mmo = "0" + mo;
		} else {
			mmo = "" + mo;
		}

		if (dd < 10) {
			ddd = "0" + dd;
		} else {
			ddd = "" + dd;
		}

		if (hr24 < 10) {
			hr24s = "0" + hr24;
		} else {
			hr24s = "" + hr24;
		}

		if (mi < 10) {
			mis = "0" + mi;
		} else {
			mis = "" + mi;
		}

		String addDate = "" + yyy + "-" + mmo + "-" + ddd + " " + hr24s + ":" + mis;
		return addDate;
	}

	public static Date check(String s, String format) throws java.text.ParseException {

		if (s == null) {
			throw new java.text.ParseException("date string to check is null", 0);
		}
		if (format == null) {
			throw new java.text.ParseException("format string to check date is null", 0);
		}

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);

		java.util.Date date = null;

		try {
			date = formatter.parse(s);
		} catch (java.text.ParseException e) {
			throw new java.text.ParseException(" wrong date:\"" + s + "\" with format \"" + format + "\"", 0);
		}

		if (!formatter.format(date).equals(s)) {
			throw new java.text.ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
		}

		return date;
	}

	public static String addYmdFromCurrentTime(int y, int m, int d) {
		String format = "yyyy-MM-dd";

		SimpleDateFormat FORMIS = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();

		if (y > 0)
			calendar.add(Calendar.YEAR, y);
		if (m > 0)
			calendar.add(Calendar.MONTH, m);
		if (d > 0)
			calendar.add(Calendar.DATE, d);

		String addDate = FORMIS.format(calendar.getTime());

		return addDate;
	}
}
