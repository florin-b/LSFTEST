package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.ClpDAOListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import beans.ArticolCLP;
import beans.ComandaCLP;
import beans.DateLivrareCLP;
import enums.EnumClpDAO;

public class ClpDAO implements IClpDAO, AsyncTaskListener {

	private Context context;
	private EnumClpDAO numeComanda;
	private ClpDAOListener listener;

	public ClpDAO(Context context) {
		this.context = context;
	}

	public void getListComenzi(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_LIST_COMENZI;
		performOperation(params);
	}

	public void getArticoleComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_ARTICOLE_COMANDA;
		performOperation(params);
	}

	public void getArticoleComandaJSON(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_ARTICOLE_COMANDA_JSON;
		performOperation(params);
	}

	public void operatiiComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.OPERATIE_COMANDA;
		performOperation(params);
	}

	public void salveazaComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.SALVEAZA_COMANDA;
		performOperation(params);
	}

	private void performOperation(HashMap<String, String> params) {
		AsyncTaskListener contextListener = (AsyncTaskListener) ClpDAO.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, numeComanda.getComanda(), params);
		call.getCallResultsFromFragment();
	}

	public void setClpDAOListener(ClpDAOListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationClpComplete(numeComanda, result);
		}

	}

	public ComandaCLP decodeArticoleComanda(String JSONString) {

		ComandaCLP comanda = new ComandaCLP();
		DateLivrareCLP dateLivrare = new DateLivrareCLP();
		List<ArticolCLP> listArticole = new ArrayList<ArticolCLP>();

		try {

			JSONObject json = (JSONObject) new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONObject) {

				JSONObject jsonLivrare = json.getJSONObject("dateLivrare");
				dateLivrare.setPersContact(jsonLivrare.getString("persContact"));
				dateLivrare.setTelefon(jsonLivrare.getString("telefon"));
				dateLivrare.setAdrLivrare(jsonLivrare.getString("adrLivrare"));
				dateLivrare.setOras(jsonLivrare.getString("oras"));
				dateLivrare.setJudet(InfoStrings.numeJudet(jsonLivrare.getString("codJudet")));
				dateLivrare.setData(jsonLivrare.getString("data"));
				dateLivrare.setTipMarfa(jsonLivrare.getString("tipMarfa"));
				dateLivrare.setMasa(jsonLivrare.getString("masa"));
				dateLivrare.setTipCamion(jsonLivrare.getString("tipCamion"));
				dateLivrare.setTipIncarcare(jsonLivrare.getString("tipIncarcare"));
				dateLivrare.setTipPlata(InfoStrings.getTipPlata(jsonLivrare.getString("tipPlata")));
				dateLivrare.setMijlocTransport(InfoStrings.getTipTransport(jsonLivrare.getString("mijlocTransport")));
				dateLivrare.setAprobatOC(InfoStrings.getTipAprobare(jsonLivrare.getString("aprobatOC")));
				dateLivrare.setDeSters(jsonLivrare.getString("deSters"));
				dateLivrare.setStatusAprov(jsonLivrare.getString("statusAprov"));
				dateLivrare.setValComanda(jsonLivrare.getString("valComanda"));
				dateLivrare.setObsComanda(jsonLivrare.getString("obsComanda"));
				dateLivrare.setValTransp(jsonLivrare.getString("valTransp"));
				dateLivrare.setProcTransp(jsonLivrare.getString("procTransp"));

				ArticolCLP articol;
				JSONArray jsonArticole = json.getJSONArray("articole");

				for (int i = 0; i < jsonArticole.length(); i++) {
					JSONObject articolObject = jsonArticole.getJSONObject(i);

					articol = new ArticolCLP();
					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("nume"));
					articol.setCantitate(articolObject.getString("cantitate"));
					articol.setUmBaza(articolObject.getString("umBaza"));
					articol.setDepozit(articolObject.getString("depozit"));

					if (articolObject.getString("status").equals("9"))
						articol.setStatus("Stoc insuficient");
					else
						articol.setStatus("");

					listArticole.add(articol);

				}

			}

			comanda.setDateLivrare(dateLivrare);
			comanda.setArticole(listArticole);

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return comanda;

	}

}
