package enums;

public enum EnumOperatiiAdresa {

	GET_LOCALITATI_JUDET("getLocalitatiJudet"), GET_ADRESE_JUDET("getAdreseJudet"), IS_ADRESA_VALIDA("isAdresaValid");

	private String numeComanda;

	EnumOperatiiAdresa(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
