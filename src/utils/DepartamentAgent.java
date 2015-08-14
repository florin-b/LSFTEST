package utils;

import java.util.ArrayList;
import java.util.List;

import model.UserInfo;
import enums.EnumDepartExtra;

public class DepartamentAgent {

	public static String getDepartamentImplicit(String codDepartament, String tipUser) {

		if (tipUser.equals("KA")) {
			return "Distributie";
		} else {
			return getNumeDepartament(codDepartament);
		}
	}

	public static String getDepartamentAlternativ() {
		return "Magazin";
	}

	private static String getNumeDepartament(String codDepartament) {
		String numeDepart = "";

		if (codDepartament.equals("01")) {
			numeDepart = "Lemnoase";
		}

		if (codDepartament.equals("02")) {
			numeDepart = "Feronerie";
		}

		if (codDepartament.equals("03")) {
			numeDepart = "Parchet";
		}

		if (codDepartament.equals("04")) {
			numeDepart = "Materiale grele";
		}

		if (codDepartament.equals("05")) {
			numeDepart = "Electrice";
		}

		if (codDepartament.equals("06")) {
			numeDepart = "Gips";
		}

		if (codDepartament.equals("07")) {
			numeDepart = "Chimice";
		}

		if (codDepartament.equals("08")) {
			numeDepart = "Instalatii";
		}

		if (codDepartament.equals("09")) {
			numeDepart = "Hidroizolatii";
		}

		if (codDepartament.equals("11")) {
			numeDepart = "Magazin";
		}

		if (codDepartament.equals("00")) {
			numeDepart = "Distributie";
		}

		return numeDepart;
	}

	private static boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	private static boolean isAG() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD");
	}

	public static List<String> getDepartamenteAgent() {

		ArrayList<String> depart = new ArrayList<String>();

		if (isKA()) {

			for (EnumDepartExtra depKA : EnumDepartExtra.values()) {
				if (depKA.getCod().equals("00") || depKA.getCod().equals("11"))
					depart.add(depKA.getNume());
			}
		}

		if (isAG()) {
			depart.add(EnumDepartExtra.getNumeDepart(UserInfo.getInstance().getCodDepart()));

			if (UserInfo.getInstance().getDepartExtra().length() > 0) {
				String[] depExtra = UserInfo.getInstance().getDepartExtra().split(";");

				for (int i = 0; i < depExtra.length; i++) {
					depart.add(EnumDepartExtra.getNumeDepart(depExtra[i]));
				}
			}
			depart.add(EnumDepartExtra.getNumeDepart("11"));
		}

		return depart;
	}

}
