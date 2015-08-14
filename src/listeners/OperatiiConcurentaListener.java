package listeners;

import enums.EnumOperatiiConcurenta;

public interface OperatiiConcurentaListener {
	public void operationComplete(EnumOperatiiConcurenta numeComanda, Object result);

}
