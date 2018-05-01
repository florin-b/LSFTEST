package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiClientListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import beans.BeanAdresaLivrare;
import beans.BeanClient;
import beans.BeanDatePersonale;
import beans.DetaliiClient;
import beans.PlatitorTva;
import enums.EnumClienti;

public class OperatiiClient implements AsyncTaskListener {

	Context context;
	OperatiiClientListener listener;
	EnumClienti numeComanda;

	public OperatiiClient(Context context) {
		this.context = context;
	}

	public void getListClienti(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_CLIENTI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getListMeseriasi(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_MESERIASI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getListClientiCV(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_CLIENTI_CV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getDetaliiClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_DETALII_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getAdreseLivrareClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_ADRESE_LIVRARE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getStarePlatitorTva(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_STARE_TVA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getMeseriasi(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_MESERIASI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getCnpClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_CNP_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public ArrayList<BeanClient> deserializeListClienti(String serializedListClienti) {
		BeanClient client = null;
		ArrayList<BeanClient> listClienti = new ArrayList<BeanClient>();

		try {
			Object json = new JSONTokener(serializedListClienti).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListClienti);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					client = new BeanClient();
					client.setCodClient(object.getString("codClient"));
					client.setNumeClient(object.getString("numeClient"));
					client.setTipClient(object.getString("tipClient"));
					listClienti.add(client);

				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listClienti;
	}

	public DetaliiClient deserializeDetaliiClient(String serializedDetaliiClient) {
		DetaliiClient detaliiClient = new DetaliiClient();

		try {
			JSONObject jsonObject = new JSONObject(serializedDetaliiClient);

			if (jsonObject instanceof JSONObject) {

				detaliiClient.setRegiune(jsonObject.getString("regiune"));
				detaliiClient.setOras(jsonObject.getString("oras"));
				detaliiClient.setStrada(jsonObject.getString("strada"));
				detaliiClient.setNrStrada(jsonObject.getString("nrStrada"));
				detaliiClient.setLimitaCredit(jsonObject.getString("limitaCredit"));
				detaliiClient.setRestCredit(jsonObject.getString("restCredit"));
				detaliiClient.setStare(jsonObject.getString("stare"));
				detaliiClient.setPersContact(jsonObject.getString("persContact"));
				detaliiClient.setTelefon(jsonObject.getString("telefon"));
				detaliiClient.setFiliala(jsonObject.getString("filiala"));
				detaliiClient.setMotivBlocare(jsonObject.getString("motivBlocare"));
				detaliiClient.setCursValutar(jsonObject.getString("cursValutar"));
				detaliiClient.setTermenPlata(jsonObject.getString("termenPlata"));
				detaliiClient.setTipClient(InfoStrings.getTipClient(jsonObject.getString("tipClient")));
				detaliiClient.setFurnizor(Boolean.valueOf(jsonObject.getString("isFurnizor")));
				detaliiClient.setDivizii(jsonObject.getString("divizii"));
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return detaliiClient;

	}

	public List<BeanAdresaLivrare> deserializeAdreseLivrare(String adreseLivrare) {
		BeanAdresaLivrare oAdresa = null;
		ArrayList<BeanAdresaLivrare> objectsList = new ArrayList<BeanAdresaLivrare>();

		JSONArray jsonObject = null;

		try {

			Object json = new JSONTokener(adreseLivrare).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(adreseLivrare);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					oAdresa = new BeanAdresaLivrare();
					oAdresa.setOras(clienObject.getString("oras"));
					oAdresa.setStrada(clienObject.getString("strada"));
					oAdresa.setNrStrada(clienObject.getString("nrStrada"));
					oAdresa.setCodJudet(clienObject.getString("codJudet"));
					oAdresa.setCodAdresa(clienObject.getString("codAdresa"));

					objectsList.add(oAdresa);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public PlatitorTva deserializePlatitorTva(String result) {
		PlatitorTva platitorTva = new PlatitorTva();

		try {
			JSONObject jsonObject = new JSONObject(result);

			if (jsonObject instanceof JSONObject) {

				platitorTva.setPlatitor(Boolean.parseBoolean(jsonObject.getString("isPlatitor")));
				platitorTva.setNumeClient(jsonObject.getString("numeClient"));
				platitorTva.setNrInreg(jsonObject.getString("nrInreg"));
				platitorTva.setErrMessage(jsonObject.getString("errMessage") != "null" ? jsonObject.getString("errMessage") : "");

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return platitorTva;
	}

	public List<BeanDatePersonale> deserializeDatePersonale(String result) {
		List<BeanDatePersonale> listDate = new ArrayList<BeanDatePersonale>();

		try {

			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray(result);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject dateObject = jsonObject.getJSONObject(i);

					BeanDatePersonale datePersonale = new BeanDatePersonale();

					datePersonale.setCnp(dateObject.getString("cnp"));
					datePersonale.setNume(dateObject.getString("nume"));
					datePersonale.setCodjudet(dateObject.getString("codjudet"));
					datePersonale.setLocalitate(dateObject.getString("localitate"));
					datePersonale.setStrada(dateObject.getString("strada"));
					listDate.add(datePersonale);

				}

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listDate;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setOperatiiClientListener(OperatiiClientListener listener) {
		this.listener = listener;
	}

}
