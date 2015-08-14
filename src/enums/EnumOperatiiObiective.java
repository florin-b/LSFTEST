package enums;

public enum EnumOperatiiObiective {
	SALVEAZA_OBIECTIV("salveazaObiectiv"), GET_LIST_OBEICTIVE("getListObiectiveKA"), GET_DETALII_OBIECTIV("getDetaliiObiectiv"), GET_STARE_CLIENT(
			"getStareClient"), GET_CLIENTI_OBIECTIV("getClientiObiectiv"), SALVEAZA_EVENIMENT("salveazaEvenimentObiectiv"), GET_EVENIMENTE_CLIENT(
			"getEvenimenteObiectiv"), GET_OBIECTIVE_DEPARTAMENT("getObiectiveDepartament");

	private String numeObiectiv;

	EnumOperatiiObiective(String numeObiectiv) {
		this.numeObiectiv = numeObiectiv;
	}

	public String getNume() {
		return numeObiectiv;
	}

}
