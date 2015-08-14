package listeners;

import enums.EnumClpDAO;

public interface ClpDAOListener {
	public void operationClpComplete(EnumClpDAO methodName, Object result);
}
