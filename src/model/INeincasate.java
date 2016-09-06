package model;

import java.util.HashMap;
import java.util.List;

import listeners.ArticolNeincasatListener;
import beans.BeanArticolVanzari;

public interface INeincasate {
	void getArtDocNeincasat(HashMap<String, String> params);
	 void setArtNeincasatListener(ArticolNeincasatListener listener);
	 public List<BeanArticolVanzari> getArticole(Object result);
	
}
