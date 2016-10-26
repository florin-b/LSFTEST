package patterns;

import java.util.Comparator;

import beans.ArticolCLP;

public class ClpDepartComparator implements Comparator<ArticolCLP> {

	@Override
	public int compare(ArticolCLP art1, ArticolCLP art2) {
		return art1.getDepart().compareTo(art2.getDepart());
	}

}
