package listeners;

import enums.EnumDlDAO;

public interface DlDAOListener {
	public void operationDlComplete(EnumDlDAO methodName, Object result);
}
