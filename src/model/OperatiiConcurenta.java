package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.OperatiiConcurentaListener;
import beans.BeanArticolConcurenta;
import beans.BeanCompanieConcurenta;
import beans.BeanNewPretConcurenta;
import beans.BeanPretConcurenta;

public interface OperatiiConcurenta {

	public void setListener(OperatiiConcurentaListener listener);

	public void getArticoleConcurenta(HashMap<String, String> params);

	public void getArticoleConcurentaBulk(HashMap<String, String> params);

	public void getCompaniiConcurente(HashMap<String, String> params);

	public List<BeanCompanieConcurenta> deserializeCompConcurente(Object listCompanii);

	public List<BeanPretConcurenta> deserializePreturiConcurenta(Object resultList);

	public void getPretConcurenta(HashMap<String, String> params);

	public void addPretConcurenta(HashMap<String, String> params);

	public ArrayList<BeanArticolConcurenta> deserializeArticoleConcurenta(String serializedListArticole);

	public void saveListPreturi(HashMap<String, String> params);

	public String serializePreturi(List<BeanNewPretConcurenta> listPreturi);

}
