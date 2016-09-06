package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiVenitListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import beans.VenitAG;
import beans.VenitTCF;
import beans.VenitTPR;
import enums.EnumOperatiiVenit;

public class CalculVenitImpl implements CalculVenit, AsyncTaskListener {

	private EnumOperatiiVenit numeComanda;
	private HashMap<String, String> params;
	private Context context;
	private OperatiiVenitListener listener;

	public CalculVenitImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getVenitTPR_TCF(HashMap<String, String> params) {
		numeComanda = EnumOperatiiVenit.GET_VENIT_AG;
		this.params = params;
		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setOperatiiVenitListener(OperatiiVenitListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null)
			listener.operatiiVenitComplete(numeComanda, result);
	}

	@Override
	public VenitAG getVenit(Object venitData) {

		VenitAG venitAG = new VenitAG();

		try {
			JSONObject jsonObject = new JSONObject((String) venitData);

			JSONArray arrayTcf = new JSONArray(jsonObject.getString("venitTcf"));

			List<VenitTCF> listTCF = new ArrayList<VenitTCF>();
			

			for (int i = 0; i < arrayTcf.length(); i++) {
				JSONObject object = arrayTcf.getJSONObject(i);
				
				VenitTCF venitTCF = new VenitTCF();
				venitTCF.setVenitGrInc(object.getString("venitGrInc"));
				venitTCF.setTargetPropus(object.getString("targetPropus"));
				venitTCF.setTargetRealizat(object.getString("targetRealizat"));
				venitTCF.setCoefAfectare(object.getString("coefAfectare"));
				venitTCF.setVenitTcf(object.getString("venitTcf"));
				listTCF.add(venitTCF);
			}

			JSONArray arrayTpr = new JSONArray(jsonObject.getString("venitTpr"));

			List<VenitTPR> listTPR = new ArrayList<VenitTPR>();
			

			for (int i = 0; i < arrayTpr.length(); i++) {
				JSONObject object = arrayTpr.getJSONObject(i);

				VenitTPR venitTPR = new VenitTPR();
				venitTPR.setCodN2(object.getString("codN2"));
				venitTPR.setNumeN2(object.getString("numeN2"));
				venitTPR.setVenitGrInc(object.getString("venitGrInc"));
				venitTPR.setPondere(object.getString("pondere"));
				venitTPR.setTargetPropCant(object.getString("targetPropCant"));
				venitTPR.setTargetRealCant(object.getString("targetRealCant"));
				venitTPR.setUm(object.getString("um"));
				venitTPR.setTargetPropVal(object.getString("targetPropVal"));
				venitTPR.setTargetRealVal(object.getString("targetRealVal"));
				venitTPR.setRealizareTarget(object.getString("realizareTarget"));
				venitTPR.setTargetPonderat(object.getString("targetPonderat"));

				listTPR.add(venitTPR);

			}
			
			
			venitAG.setVenitTcf(listTCF);
			venitAG.setVenitTpr(listTPR);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return venitAG;
	}

}
