package model;

import java.util.ArrayList;
import java.util.List;

import beans.BeanComandaCreata;

public class CriteriulDivizie implements Criteriu {

	public List<BeanComandaCreata> indeplinesteCriteriu(List<BeanComandaCreata> listaComenzi, String divizie) {
		List<BeanComandaCreata> listComenzi = new ArrayList<BeanComandaCreata>();

		for (BeanComandaCreata comanda : listaComenzi) {
			if (comanda.getDivizieComanda().equals(divizie) || divizie.equals("00")) {
				listComenzi.add(comanda);
			}
		}

		return listComenzi;
	}
}
