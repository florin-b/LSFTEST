package model;

import java.util.ArrayList;
import java.util.List;

import beans.BeanDocumentRetur;

public class CriteriuNrDocRetur implements CriteriuDocumentRetur {

	public List<BeanDocumentRetur> indeplinesteCriteriul(List<BeanDocumentRetur> listDocumente, String nrDocument) {

		List<BeanDocumentRetur> listDocRetur = new ArrayList<BeanDocumentRetur>();

		for (BeanDocumentRetur docRet : listDocumente) {
			if (docRet.getNumar().startsWith(nrDocument)) {
				listDocRetur.add(docRet);
			}
		}

		return listDocRetur;
	}

}
