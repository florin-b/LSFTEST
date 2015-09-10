package model;

import java.util.HashMap;

public interface IComenziDAO {
	 void getListComenzi(HashMap<String, String> params);

	 void getArticoleComanda(HashMap<String, String> params);

	 void salveazaConditiiComanda(HashMap<String, String> params);

	 void getMotiveRespingere(HashMap<String, String> params);

	 void opereazaComanda(HashMap<String, String> params);
	
	 void salveazaComandaDistrib(HashMap<String, String> params);
	
	 void salveazaComandaGed(HashMap<String, String> params);
}