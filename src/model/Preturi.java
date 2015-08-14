package model;

import java.util.HashMap;

import listeners.AsyncTaskListener;
import listeners.PreturiListener;
import lite.sfa.test.AsyncTaskWSCall;
import android.content.Context;

public class Preturi implements IPreturi, AsyncTaskListener {

	PreturiListener listener;

	private Preturi() {

	}

	public static Preturi getInstance() {
		return new Preturi();
	}

	public void getPret(HashMap<String, String> params, String methodName, Context context) {
		AsyncTaskListener contextListener = (AsyncTaskListener) Preturi.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, methodName, params);
		call.getCallResultsFromFragment();

	}

	public void setPreturiListener(PreturiListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {

		if (listener != null) {
			listener.taskComplete((String) result);
		}

	}

}
