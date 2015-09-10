package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiConcurentaListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import beans.BeanCompanieConcurenta;
import beans.BeanPretConcurenta;
import enums.EnumOperatiiConcurenta;

public class OperatiiConcurentaImpl implements OperatiiConcurenta, AsyncTaskListener {

	private Context context;
	private OperatiiConcurentaListener listener;
	private EnumOperatiiConcurenta numeComanda;
	private HashMap<String, String> params;

	public OperatiiConcurentaImpl(Context context) {
		this.context = context;
	}

	public void getArticoleConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_ARTICOLE_CONCURENTA;
		this.params = params;
		performOperation();

	}

	public void getCompaniiConcurente(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_COMPANII_CONCURENTE;
		this.params = params;
		performOperation();
	}

	public void getPretConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_PRET_CONCURENTA;
		this.params = params;
		performOperation();
	}

	public void addPretConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.ADD_PRET_CONCURENTA;
		this.params = params;
		performOperation();
	}

	public List<BeanCompanieConcurenta> deserializeCompConcurente(Object resultList) {
		BeanCompanieConcurenta companie = null;
		ArrayList<BeanCompanieConcurenta> listCompanii = new ArrayList<BeanCompanieConcurenta>();

		try {
			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray((String) resultList);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject compObject = jsonArray.getJSONObject(i);
					companie = new BeanCompanieConcurenta();
					companie.setCod(compObject.getString("cod"));
					companie.setNume(compObject.getString("nume"));
					listCompanii.add(companie);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listCompanii;
	}

	public List<BeanPretConcurenta> deserializePreturiConcurenta(Object resultList) {
		BeanPretConcurenta unPret = null;
		ArrayList<BeanPretConcurenta> preturiList = new ArrayList<BeanPretConcurenta>();

		try {

			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) resultList);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject pretObject = jsonObject.getJSONObject(i);

					unPret = new BeanPretConcurenta();
					unPret.setData(pretObject.getString("data"));
					unPret.setValoare(pretObject.getString("valoare"));
					preturiList.add(unPret);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return preturiList;
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setListener(OperatiiConcurentaListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}

	}

}