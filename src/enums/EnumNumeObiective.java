package enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumNumeObiective {

	
	NUME_OBIECTIV("Nume obiectiv", 0), STARE_OBIECTIV("Stare obiectiv", 1), DATA_CREARE("Data creare", 2), JUDET("Adresa", 3), BENEFICIAR("Beneficiar", 4), ANTREPRENOR(
			"Antreprenor general", 5), ARHITECT("Nume arhitect", 6), CATEGORIE("Categorie obiectiv", 7), VALOARE(
			"Valoare obiectiv", 8), NR_AUTORIZ("Nr. autorizatie constructie", 9), DATA_AUTORIZ(
			"Data emitere autorizatie", 10), DATA_EXPIR_AUTORIZ("Data expirare autorizatie", 11), PRIMARIA(
			"Primaria emitenta", 12), VALOARE_FUNDATIE("Valoare fundatie si suprastructura", 13), STADIU_FUNDATIE("Stadiu fundatie si suprastructura", 14), STADIU_ACOPERIS(
			"Stadiu acoperis", 15), STADIU_INSTALATII("Stadiu instalatii", 16), STADIU_ELECTRICE("Stadiu electrice", 17), STADIU_INTERIOARE(
			"Stadiu interioare", 18);	
	
	/*
	NUME_OBIECTIV("Nume obiectiv", 0), DATA_CREARE("Data creare", 1), JUDET("Adresa", 2), BENEFICIAR("Beneficiar", 3), ANTREPRENOR(
			"Antreprenor general", 4), ARHITECT("Nume arhitect", 5), CATEGORIE("Categorie obiectiv", 6), VALOARE(
			"Valoare obiectiv", 7), NR_AUTORIZ("Nr. autorizatie constructie", 8), DATA_AUTORIZ(
			"Data emitere autorizatie", 9), DATA_EXPIR_AUTORIZ("Data expirare autorizatie", 10), PRIMARIA(
			"Primaria emitenta", 11), VALOARE_FUNDATIE("Valoare fundatie si suprastructura", 12), STADIU_FUNDATIE("Stadiu fundatie si suprastructura", 13), STADIU_ACOPERIS(
			"Stadiu acoperis", 14), STADIU_INSTALATII("Stadiu instalatii", 15), STADIU_ELECTRICE("Stadiu electrice", 16), STADIU_INTERIOARE(
			"Stadiu interioare", 17);
			*/

	private String nume;
	private int cod;

	EnumNumeObiective(String nume, int cod) {
		this.cod = cod;
		this.nume = nume;
	}

	public String getNume() {
		return nume;
	}

	public int getCod() {
		return cod;
	}

	public static List<String> getNumeList() {
		List<String> listNumeObiective = new ArrayList<String>();

		for (EnumNumeObiective enumOb : EnumNumeObiective.values()) {
			listNumeObiective.add(enumOb.nume);
		}

		return listNumeObiective;

	}

	public static String getNumeObiectiv(int codObiectiv) {

		for (EnumNumeObiective enumOb : EnumNumeObiective.values()) {
			if (enumOb.cod == codObiectiv)
				return enumOb.nume;

		}

		return "";
	}
}
