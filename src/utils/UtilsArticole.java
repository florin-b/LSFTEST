package utils;

import java.util.Iterator;
import java.util.List;

import beans.ArticolDB;

public class UtilsArticole {

	public static void getArt111Only(List<ArticolDB> listArticole) {

		Iterator<ArticolDB> artIterator = listArticole.iterator();

		while (artIterator.hasNext()) {
			if (!artIterator.next().getCod().startsWith("111"))
				artIterator.remove();
		}

	}
}
