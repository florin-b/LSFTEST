package enums;

public enum EnumArticoleDAO {

	GET_ARTICOLE_DISTRIBUTIE("getArticoleDistributie"), GET_PRET("getPret"), GET_STOC_DEPOZIT("getStocDepozit"), GET_ARTICOLE_COMPLEMENTARE(
			"getArticoleComplementare"), GET_ARTICOLE_FURNIZOR("getArticoleFurnizor"), GET_SINTETICE_DISTRIBUTIE("getSinteticeDistributie"), GET_NIVEL1_DISTRIBUTIE(
			"getNivel1Distributie"), GET_PRET_GED("getPretGed"), GET_FACTOR_CONVERSIE("getArtFactConvUM"), GET_PRET_GED_JSON("getPretGedJson");

	String numeComanda;

	EnumArticoleDAO(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
