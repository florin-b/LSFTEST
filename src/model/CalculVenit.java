package model;

import java.util.HashMap;

import listeners.OperatiiVenitListener;
import beans.VenitAG;

public interface CalculVenit {

	public void getVenitTPR_TCF(HashMap<String, String> params);

	public void setOperatiiVenitListener(OperatiiVenitListener listener);

	public VenitAG getVenit(Object venitData);
}
