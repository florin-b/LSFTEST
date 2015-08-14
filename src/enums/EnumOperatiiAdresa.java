package enums;

public enum EnumOperatiiAdresa {

	GET_LOCALITATI_JUDET("getLocalitatiJudet");

	private String numeComanda;

	EnumOperatiiAdresa(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
