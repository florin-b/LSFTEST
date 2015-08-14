package model;

import java.util.HashMap;
import java.util.List;

import listeners.OperatiiAdresaListener;
import enums.EnumLocalitate;

public interface OperatiiAdresa {
	public void getLocalitatiJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate);
	public List<String> deserializeListLocalitati(Object values);
	public void setOperatiiAdresaListener(OperatiiAdresaListener listener);

}
