package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiArticolListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import utils.UtilsGeneral;
import android.content.Context;
import android.widget.Toast;
import beans.ArticolDB;
import beans.BeanGreutateArticol;
import beans.BeanParametruPretGed;
import beans.PretArticolGed;
import enums.EnumArticoleDAO;
import enums.EnumUnitMas;

public class OperatiiArticolImpl implements OperatiiArticol, AsyncTaskListener {

	private Context context;
	private OperatiiArticolListener listener;
	private EnumArticoleDAO numeComanda;
	private HashMap<String, String> params;

	public OperatiiArticolImpl(Context context) {
		this.context = context;
	}

	public void getPret(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET;
		this.params = params;
		performOperation();
	}

	public void getPretGed(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET_GED;
		this.params = params;
		performOperation();
	}

	public void getPretGedJson(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET_GED_JSON;
		this.params = params;
		performOperation();

	}

	public void getFactorConversie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_FACTOR_CONVERSIE;
		this.params = params;
		performOperation();
	}

	public void getStocDepozit(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_DEPOZIT;
		this.params = params;
		performOperation();
	}

	public void getArticoleDistributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	public void getArticoleFurnizor(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_FURNIZOR;
		this.params = params;
		performOperation();
	}

	public void getArticoleComplementare(List<ArticolComanda> listArticole) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_COMPLEMENTARE;

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("listaArticoleComanda", serializeListArticole(listArticole));
		this.params = params;

		performOperation();
	}

	public void getSinteticeDistributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_SINTETICE_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	public void getNivel1Distributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_NIVEL1_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public ArrayList<ArticolDB> deserializeArticoleVanzare(String serializedListArticole) {
		ArticolDB articol = null;
		ArrayList<ArticolDB> listArticole = new ArrayList<ArticolDB>();

		try {
			Object json = new JSONTokener(serializedListArticole).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListArticole);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject articolObject = jsonArray.getJSONObject(i);

					articol = new ArticolDB();
					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("nume"));
					articol.setSintetic(articolObject.getString("sintetic"));
					articol.setNivel1(articolObject.getString("nivel1"));
					articol.setUmVanz(articolObject.getString("umVanz"));
					articol.setUmVanz10(articolObject.getString("umVanz10"));
					articol.setTipAB(articolObject.getString("tipAB"));
					articol.setDepart(articolObject.getString("depart"));
					listArticole.add(articol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listArticole;

	}

	public String serializeParamPretGed(BeanParametruPretGed parametru) {

		JSONObject jsonParametru = new JSONObject();

		try {
			jsonParametru.put("client", parametru.getClient());
			jsonParametru.put("articol", parametru.getArticol());
			jsonParametru.put("cantitate", parametru.getCantitate());
			jsonParametru.put("depart", parametru.getDepart());
			jsonParametru.put("um", parametru.getUm());
			jsonParametru.put("ul", parametru.getUl());
			jsonParametru.put("depoz", parametru.getDepoz());
			jsonParametru.put("codUser", parametru.getCodUser());
			jsonParametru.put("tipUser", parametru.getTipUser());
			jsonParametru.put("canalDistrib", parametru.getCanalDistrib());
			jsonParametru.put("metodaPlata", parametru.getMetodaPlata());
			jsonParametru.put("codJudet", parametru.getCodJudet());
			jsonParametru.put("localitate", parametru.getLocalitate());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonParametru.toString();

	}

	private String serializeListArticole(List<ArticolComanda> listArticole) {
		String serializedResult = "";

		JSONArray myArray = new JSONArray();
		JSONObject obj = null;

		Iterator<ArticolComanda> iterator = listArticole.iterator();
		ArticolComanda articol;

		while (iterator.hasNext()) {
			articol = iterator.next();

			try {
				obj = new JSONObject();
				obj.put("cod", articol.getCodArticol());
				myArray.put(obj);
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

		}

		serializedResult = myArray.toString();

		return serializedResult;
	}

	public String serializeArticolePretTransport(List<ArticolComanda> listArticole) {
		String serializedResult = "";

		JSONArray myArray = new JSONArray();
		JSONObject obj = null;

		Iterator<ArticolComanda> iterator = listArticole.iterator();
		ArticolComanda articol;
		String localCodArticol = "";

		while (iterator.hasNext()) {
			articol = iterator.next();

			try {
				obj = new JSONObject();

				if (articol.getCodArticol().length() == 8)
					localCodArticol = "0000000000" + articol.getCodArticol();
				else
					localCodArticol = articol.getCodArticol();

				obj.put("cod", localCodArticol);
				obj.put("valoare", Double.valueOf(articol.getPret()));
				obj.put("promo", articol.getPromotie() <= 0 ? " " : "X");

				myArray.put(obj);
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

		}

		serializedResult = myArray.toString();

		return serializedResult;
	}

	public PretArticolGed deserializePretGed(Object result) {
		PretArticolGed pretArticol = new PretArticolGed();

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			if (jsonObject instanceof JSONObject) {
				pretArticol.setPret(jsonObject.getString("pret"));
				pretArticol.setUm(jsonObject.getString("um"));
				pretArticol.setFaraDiscount(jsonObject.getString("faraDiscount"));
				pretArticol.setCodArticolPromo(jsonObject.getString("codArticolPromo"));
				pretArticol.setCantitateArticolPromo(jsonObject.getString("cantitateArticolPromo"));
				pretArticol.setPretArticolPromo(jsonObject.getString("pretArticolPromo"));
				pretArticol.setUmArticolPromo(jsonObject.getString("umArticolPromo"));
				pretArticol.setPretLista(jsonObject.getString("pretLista"));
				pretArticol.setCantitate(jsonObject.getString("cantitate"));
				pretArticol.setConditiiPret(jsonObject.getString("conditiiPret"));
				pretArticol.setMultiplu(jsonObject.getString("multiplu"));
				pretArticol.setCantitateUmBaza(jsonObject.getString("cantitateUmBaza"));
				pretArticol.setUmBaza(jsonObject.getString("umBaza"));
				pretArticol.setCmp(jsonObject.getString("cmp"));
				pretArticol.setPretMediu(jsonObject.getString("pretMediu"));
				pretArticol.setAdaosMediu(jsonObject.getString("adaosMediu"));
				pretArticol.setUmPretMediu(jsonObject.getString("umPretMediu"));
				pretArticol.setCoefCorectie(Double.valueOf(jsonObject.getString("coefCorectie")));
				pretArticol.setProcTransport(Double.valueOf(jsonObject.getString("procTransport")));
				pretArticol.setDiscMaxAV(Double.valueOf(jsonObject.getString("discMaxAV")));
				pretArticol.setDiscMaxSD(Double.valueOf(jsonObject.getString("discMaxSD")));
				pretArticol.setDiscMaxDV(Double.valueOf(jsonObject.getString("discMaxDV")));
				pretArticol.setDiscMaxKA(Double.valueOf(jsonObject.getString("discMaxKA")));

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return pretArticol;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setListener(OperatiiArticolListener listener) {
		this.listener = listener;
	}

	public BeanGreutateArticol deserializeGreutateArticol(Object result) {

		BeanGreutateArticol greutateArticol = new BeanGreutateArticol();

		try {
			JSONObject jsonObject = new JSONObject((String) result);

			if (jsonObject instanceof JSONObject) {
				greutateArticol.setCodArticol(jsonObject.getString("codArticol"));
				greutateArticol.setGreutate(Double.valueOf(jsonObject.getString("greutate")));
				greutateArticol.setUnitMas(EnumUnitMas.valueOf(jsonObject.getString("um")));
				greutateArticol.setUnitMasCantiate(jsonObject.getString("umCantitate"));

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return greutateArticol;
	}

}
