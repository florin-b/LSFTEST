package enums;

public enum EnumOperatiiVenit {

	GET_VENIT_AG("salarizareAV");

	private String numeComanda;

	EnumOperatiiVenit(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

	public void setNumeComanda(String numeComanda) {
		this.numeComanda = numeComanda;
	}

}
