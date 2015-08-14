package listeners;

import enums.EnumArticoleDAO;

public interface OperatiiArticolListener {
	public void operationComplete(EnumArticoleDAO methodName, Object result);
}
