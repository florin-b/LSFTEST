package listeners;

import enums.EnumComenziDAO;

public interface ComenziDAOListener {
	public void operationComenziComplete(EnumComenziDAO methodName, Object result);
}
