package utils;

import model.InfoStrings;

public class UtilsComenzi {

	public static String getStareComanda(int codStare) {
		String numeStare = "";

		switch (codStare) {

		case 1:
			numeStare = "Masina alocata";
			break;

		case 2:
			numeStare = "Borderou tiparit";
			break;

		case 3:
			numeStare = "Inceput incarcare";
			break;

		case 4:
			numeStare = "Terminat incarcare";
			break;

		case 6:
			numeStare = "Plecat in cursa";
			break;

		default:
			break;
		}

		return numeStare;
	}

	public static boolean isComandaGed(String unitLog, String codClient) {
		String distribUL = InfoStrings.getDistribUnitLog(unitLog);

		if (InfoStrings.getClientGenericGed(distribUL, "PF").equals(codClient) || InfoStrings.getClientGenericGed(distribUL, "PJ").equals(codClient)
				|| InfoStrings.getClientGenericGedWood(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientGenericGedWood(distribUL, "PJ").equals(codClient)
				|| InfoStrings.getClientGenericGedWood_faraFact(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientGenericGed_CONSGED_faraFactura(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientCVO_cuFact_faraCnp(distribUL, "").equals(codClient)
				|| InfoStrings.getClientGed_FaraFactura(distribUL).equals(codClient))

			return true;
		else
			return false;
	}

	public static String[] tipPlataGed() {
		return new String[] { "B - Bilet la ordin", "C - Cec", "E - Plata in numerar", "O - Ordin de plata", "BRD - Card BRD", "ING - Card ING",
				"UNI - Card Unicredit", "CBTR - Card Transilvania", "CGRB - Card Garanti Bonus", "CRFZ - Card Raiffeisen", "CCTL - Card Cetelem",
				"CAVJ - Card Avantaj", "E1 - Numerar sofer" };

	}

}
