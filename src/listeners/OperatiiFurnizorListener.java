package listeners;

import enums.EnumOperatiiFurnizor;

public interface OperatiiFurnizorListener {
	public void operationComplete(EnumOperatiiFurnizor methodName, Object result);
}
