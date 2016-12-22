package utils;

public class UtilsCheck {

	public static boolean isCnpValid(String cnp) {

		cnp = cnp.replaceAll("\\s+", "");

		if (cnp.length() != 13) {
			return false;
		}

		int suma = Integer.parseInt(Character.toString(cnp.charAt(0))) * 2 + Integer.parseInt(Character.toString(cnp.charAt(1))) * 7
				+ Integer.parseInt(Character.toString(cnp.charAt(2))) * 9 + Integer.parseInt(Character.toString(cnp.charAt(3))) * 1
				+ Integer.parseInt(Character.toString(cnp.charAt(4))) * 4 + Integer.parseInt(Character.toString(cnp.charAt(5))) * 6
				+ Integer.parseInt(Character.toString(cnp.charAt(6))) * 3 + Integer.parseInt(Character.toString(cnp.charAt(7))) * 5
				+ Integer.parseInt(Character.toString(cnp.charAt(8))) * 8 + Integer.parseInt(Character.toString(cnp.charAt(9))) * 2
				+ Integer.parseInt(Character.toString(cnp.charAt(10))) * 7 + Integer.parseInt(Character.toString(cnp.charAt(11))) * 9;

		if (suma % 11 < 10 && suma % 11 == Integer.parseInt(Character.toString(cnp.charAt(12)))) {
			return true;
		} else {
			return false;
		}

	}

}
