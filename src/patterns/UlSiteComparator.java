package patterns;

import java.util.Comparator;

import model.ArticolComanda;

public class UlSiteComparator implements Comparator<ArticolComanda> {

	@Override
	public int compare(ArticolComanda art1, ArticolComanda art2) {

		if (art1.getFilialaSite() == null || art2.getFilialaSite() == null)
			return -1;
		else
			return art1.getFilialaSite().compareTo(art2.getFilialaSite());
	}

}
