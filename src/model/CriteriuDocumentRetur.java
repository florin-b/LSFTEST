package model;

import java.util.List;

import beans.BeanDocumentRetur;

public interface CriteriuDocumentRetur {
	public List<BeanDocumentRetur> indeplinesteCriteriul(List<BeanDocumentRetur> listDocumente, String nrDocument);
}
