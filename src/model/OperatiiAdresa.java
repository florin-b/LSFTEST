package model;

import java.util.HashMap;
import java.util.List;

import listeners.OperatiiAdresaListener;
import beans.BeanAdreseJudet;
import enums.EnumLocalitate;

public interface OperatiiAdresa {
	public void getLocalitatiJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate);
	public void getAdreseJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate);
	public BeanAdreseJudet deserializeListAdrese(Object values);
	public List<String> deserializeListLocalitati(Object values);
	public void setOperatiiAdresaListener(OperatiiAdresaListener listener);
	public void isAdresaValida(HashMap<String, String> params, EnumLocalitate tipLocalitate);
	public void getDateLivrare(HashMap<String, String> params);
	public void getAdreseLivrareClient(HashMap<String, String> params);
	public void getLocalitatiLivrareRapida();

}
