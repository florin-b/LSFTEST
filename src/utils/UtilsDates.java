package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import model.Constants;
import beans.StatusIntervalLivrare;

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

	public static String getCurrentDate() {
		return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
	}

	private static Date getDateMidnight() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static int dateDiffinDays(Date dateStop) {

		long diff = dateStop.getTime() - getDateMidnight().getTime() + 1000;

		if (diff < 0)
			return -1;

		return (int) (diff / (24 * 60 * 60 * 1000));

	}

	public static StatusIntervalLivrare getStatusIntervalLivrare(Date dateStop) {

		StatusIntervalLivrare statusInterval = new StatusIntervalLivrare();

		int dateDiff = dateDiffinDays(dateStop);

		if (dateDiff < 0) {
			statusInterval.setValid(false);
			statusInterval.setMessage("Data livrare incorecta.");
			return statusInterval;
		}

		if (dateDiff > getNrZileLivrare()) {
			statusInterval.setValid(false);
			statusInterval.setMessage("Livrarea trebuie sa se faca in cel mult " + getNrZileLivrare() + " zile de la data curenta.");
			return statusInterval;
		}
		
		statusInterval.setValid(true);

		return statusInterval;
	}

	private static int getNrZileLivrare() {
		if (UtilsUser.isAV() || UtilsUser.isKA())
			return Constants.NR_ZILE_LIVRARE_AG;
		else
			return Constants.NR_ZILE_LIVRARE_CVA;
	}

	public Date addDaysToDate(Date date, int days) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();

	}

}
