package enums;

public enum EnumComenziDAO {

	GET_LIST_COMENZI("getListComenzi"), GET_ARTICOLE_COMANDA("getCmdArt"), SALVEAZA_CONDITII_COMANDA("saveCondAndroid"), GET_LISTA_RESPINGERE(
			"getListMotiveRespingere"), OPERATIE_COMANDA("operatiiComenzi"), SALVEAZA_COMANDA_DISTRIB("saveNewCmdAndroid"), SALVEAZA_COMANDA_GED("saveCmdGED"), GET_ARTICOLE_COMANDA_JSON(
			"getArticoleComandaVanzare"), SALVEAZA_CONDITII_COMANDA_SER("salveazaConditiiComanda"), SEND_OFERTA_GED_MAIL("sendMailOfertaGed");

	private String numeComanda;

	EnumComenziDAO(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
