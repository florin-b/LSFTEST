package enums;

public enum EnumDlDAO {

	GET_LIST_COMENZI("getListDl"), GET_ARTICOLE_COMANDA("getListArtClp"), OPERATIE_COMANDA("operatiiDl"), SALVEAZA_COMANDA("saveNewDl"), GET_ARTICOLE_COMANDA_JSON(
			"getListArtClpJSON"), GET_DL_EXPIRATE("getDLExpirate"), GET_ARTICOLE_DL_EXPIRAT("getArticoleDLExpirat"), SET_DL_FINALIZATA("setDLFinalizata"),
			SET_DL_DATALIVRARE("setDLDataLivrare");

	private String numeComanda;

	EnumDlDAO(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
