package utils;

import java.util.ArrayList;

import model.ArticolComanda;

public class UtilsComenziGed {

	public static double getValoareTransportSap(ArrayList<ArticolComanda> listArticole) {

		double totalTransp = 0;

		for (ArticolComanda art : listArticole) {
			totalTransp += art.getValTransport();
		}

		return totalTransp;
	}

}
