package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiAdresaListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import enums.EnumLocalitate;
import enums.EnumOperatiiAdresa;

public class OperatiiAdresaImpl implements OperatiiAdresa, AsyncTaskListener {

	private Context context;
	private EnumOperatiiAdresa numeComanda;
	private HashMap<String, String> params;
	private OperatiiAdresaListener listener;
	private EnumLocalitate tipLocalitate;

	public OperatiiAdresaImpl(Context context) {
		this.context = context;
	}

	public void getLocalitatiJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate) {

		numeComanda = EnumOperatiiAdresa.GET_LOCALITATI_JUDET;
		this.params = params;
		this.tipLocalitate = tipLocalitate;
		performOperation();

	}

	public List<String> deserializeListLocalitati(Object resultList) {

		List<String> listLocalitati = new ArrayList<String>();

		try {
			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) resultList);

				for (int i = 0; i < jsonObject.length(); i++) {
					listLocalitati.add(jsonObject.get(i).toString());
				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listLocalitati;
	}

	public void setOperatiiAdresaListener(OperatiiAdresaListener listener) {
		this.listener = listener;
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operatiiAdresaComplete(numeComanda, result, tipLocalitate);
		}

	}

}
