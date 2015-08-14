package listeners;

import enums.EnumClienti;

public interface OperatiiClientListener {
	public void operationComplete(EnumClienti methodName, Object result);
}
