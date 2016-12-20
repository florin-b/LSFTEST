package model;

import enums.EnumTipComanda;

public interface ClientReturListener {
	public void clientSelected(String codClient, String numeClient, String interval, EnumTipComanda tipComanda);

}
