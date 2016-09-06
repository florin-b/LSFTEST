package listeners;

import enums.EnumOperatiiVenit;

public interface OperatiiVenitListener {
	public void operatiiVenitComplete(EnumOperatiiVenit numeComanda, Object result);
}
