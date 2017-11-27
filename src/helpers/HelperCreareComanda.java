package helpers;

import java.util.List;

import model.ArticolComanda;
import model.DateLivrare;

public class HelperCreareComanda {

	private static final String CATEGORIE_AMBAJAJ_1 = "AM";
	private static final String CATEGORIE_AMBAJAJ_2 = "PA";
	private static final double LUNGIME_MIN_FIER_BETON_CM = 600;

	public static boolean isComandaAmbalaje(List<ArticolComanda> listArticole) {

		if (DateLivrare.getInstance().isClientFurnizor())
			return false;

		boolean existaCateg = false;

		for (ArticolComanda articol : listArticole) {

			if (articol.getCategorie() == null || articol.getCategorie().trim().isEmpty())
				continue;

			existaCateg = true;

			if (!articol.getCategorie().equals(CATEGORIE_AMBAJAJ_1) && !articol.getCategorie().equals(CATEGORIE_AMBAJAJ_2))
				return false;
		}

		return existaCateg ? true : false;

	}

	public static boolean isConditiiIndoire(List<ArticolComanda> listArticole) {
		boolean isArt04Lung = false;

		for (ArticolComanda articol : listArticole) {
			if (articol.getDepart().contains("04")) {

				if (articol.getLungime() > LUNGIME_MIN_FIER_BETON_CM)
					isArt04Lung = true;

			}
		}

		boolean isTonajMic = DateLivrare.getInstance().getTonaj().equals("3.5") || DateLivrare.getInstance().getTonaj().equals("10");

		return DateLivrare.getInstance().getPrelucrare().equals("-1") && isArt04Lung && isTonajMic;

	}

}
