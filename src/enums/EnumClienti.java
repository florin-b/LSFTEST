package enums;

public enum EnumClienti {

	GET_LISTA_CLIENTI("getListClientiCautare"), GET_DETALII_CLIENT("getDetaliiClient"), GET_LISTA_CLIENTI_CV("getListClientiCV"), GET_ADRESE_LIVRARE(
			"getAdreseLivrareClient"), GET_LISTA_MESERIASI("getListMeseriasi"), GET_STARE_TVA("getStareTVA"), GET_MESERIASI("getMeseriasi");

	String numeComanda;

	EnumClienti(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
