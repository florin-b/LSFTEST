package enums;

public enum EnumOperatiiAdresa {

	GET_LOCALITATI_JUDET("getLocalitatiJudet"), GET_ADRESE_JUDET("getAdreseJudet"), IS_ADRESA_VALIDA("isAdresaValid"), GET_DATE_LIVRARE("getCmdDateLivrare"), GET_ADRESE_LIVR_CLIENT(
			"getAdreseLivrareClient");
	private String numeComanda;

	EnumOperatiiAdresa(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
