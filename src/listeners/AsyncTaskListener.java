package listeners;


public interface AsyncTaskListener {
	void onTaskComplete(String methodName, Object result);

}
