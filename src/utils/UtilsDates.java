package utils;

import java.util.Calendar;
import java.util.Locale;

public class UtilsDates {

	public static String getDateMonthString(int addMonth) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, addMonth);
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ro"));
	}

	public static int getYearMonthDate(int addMonth) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, addMonth);

		String date = String.valueOf(cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1);

		return Integer.parseInt(date);

	}

	public static String getMonthName(String strDate) {

		if (strDate == null || strDate.trim().length() == 0)
			return " ";

		int monthNumber = Integer.parseInt(strDate.substring(4, 6));

		String monthName = "";
		switch (monthNumber) {
		case 1:
			monthName = "ianuarie";
			break;

		case 2:
			monthName = "februarie";
			break;

		case 3:
			monthName = "martie";
			break;

		case 4:
			monthName = "aprilie";
			break;

		case 5:
			monthName = "mai";
			break;

		case 6:
			monthName = "iunie";
			break;

		case 7:
			monthName = "iulie";
			break;

		case 8:
			monthName = "august";
			break;

		case 9:
			monthName = "septembrie";
			break;

		case 10:
			monthName = "octombrie";
			break;

		case 11:
			monthName = "noiembrie";
			break;

		case 12:
			monthName = "decembrie";
			break;

		}

		return monthName;

	}

}
