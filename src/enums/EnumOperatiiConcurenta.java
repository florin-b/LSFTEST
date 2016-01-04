package enums;

public enum EnumOperatiiConcurenta {

	GET_ARTICOLE_CONCURENTA("getListArticoleConcurenta"), GET_COMPANII_CONCURENTE("getCompaniiConcurente"), GET_PRET_CONCURENTA("getPretConcurenta"), ADD_PRET_CONCURENTA(
			"addPretConcurenta"), GET_ARTICOLE_CONCURENTA_BULK("getListArticoleConcurentaBulk");

	String numeComanda;

	EnumOperatiiConcurenta(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
