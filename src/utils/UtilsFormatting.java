package utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsFormatting {

	public static String formatDate(String dateString) {

		String formattedDate = "";

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("ro"));

		try {
			formattedDate = df.format(dateFormat.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedDate;

	}

	public static boolean isNumeric(String stringToCheck) {
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(stringToCheck.trim());
		return matcher.find();

	}

	public static String removeComma(String stringNumber) {
		return stringNumber.replaceAll(",", "");
	}

	public static String format2Decimals(double doubleValue, boolean groupSeparator) {

		NumberFormat nf = NumberFormat.getInstance();

		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(groupSeparator);

		return nf.format(doubleValue);

	}

}
