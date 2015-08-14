package model;

import java.util.List;

import beans.BeanObiectiveConstructori;

public interface ICriteriuDepart {
	List<BeanObiectiveConstructori> constructoriDepart(List<BeanObiectiveConstructori> listConstructori, String codDepart);

}
