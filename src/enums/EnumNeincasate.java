package enums;

public enum EnumNeincasate {
	GET_ART_DOC_NEINCASAT("getArtDocNeincasat");

	private String numeComanda;

	EnumNeincasate(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
