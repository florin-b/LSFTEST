package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.ComenziDAOListener;
import lite.sfa.test.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import utils.UtilsGeneral;
import android.content.Context;
import android.widget.Toast;
import beans.BeanArticoleAfisare;
import beans.BeanComandaCreata;
import beans.BeanConditii;
import beans.BeanConditiiArticole;
import beans.BeanConditiiHeader;
import beans.DateLivrareAfisare;
import enums.EnumComenziDAO;

public class ComenziDAO implements IComenziDAO, AsyncTaskListener {

	private Context context;
	ComenziDAOListener listener;
	private EnumComenziDAO numeComanda;
	private List<BeanComandaCreata> listComenziCreate = new ArrayList<BeanComandaCreata>();

	ComenziDAO(Context context) {
		this.context = context;
	}

	public static ComenziDAO getInstance(Context context) {
		return new ComenziDAO(context);
	}

	public void getListComenzi(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.GET_LIST_COMENZI;
		performOperation(params);
	}

	public void getArticoleComanda(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.GET_ARTICOLE_COMANDA;
		performOperation(params);
	}

	public void getArticoleComandaJSON(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.GET_ARTICOLE_COMANDA_JSON;
		performOperation(params);
	}

	public void salveazaConditiiComanda(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.SALVEAZA_CONDITII_COMANDA;
		performOperation(params);
	}

	public void salveazaConditiiComandaSer(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.SALVEAZA_CONDITII_COMANDA_SER;
		performOperation(params);
	}

	public void getMotiveRespingere(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.GET_LISTA_RESPINGERE;
		performOperation(params);
	}

	public void opereazaComanda(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.OPERATIE_COMANDA;
		performOperation(params);
	}

	public void salveazaComandaDistrib(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.SALVEAZA_COMANDA_DISTRIB;
		performOperation(params);
	}

	public void salveazaComandaGed(HashMap<String, String> params) {
		numeComanda = EnumComenziDAO.SALVEAZA_COMANDA_GED;
		performOperation(params);

	}

	private void performOperation(HashMap<String, String> params) {
		AsyncTaskListener contextListener = (AsyncTaskListener) ComenziDAO.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, numeComanda.getComanda(), params);
		call.getCallResultsFromFragment();
	}

	public void setComenziDAOListener(ComenziDAOListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {

			Object resultObject = result;
			if (numeComanda.equals(EnumComenziDAO.GET_LIST_COMENZI)) {
				resultObject = deserializeListComenzi((String) result);

			}

			listener.operationComenziComplete(numeComanda, resultObject);
		}

	}

	public BeanArticoleAfisare deserializeArticoleComanda(String serializedResult) {

		BeanArticoleAfisare articoleComanda = new BeanArticoleAfisare();

		DateLivrareAfisare dateLivrare = null;
		ArrayList<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();
		ArticolComanda articol = null;

		BeanConditii conditii = new BeanConditii();
		BeanConditiiHeader headerConditii = new BeanConditiiHeader();
		List<BeanConditiiArticole> listArticoleConditii = new ArrayList<BeanConditiiArticole>();

		try {

			JSONObject jsonObject = (JSONObject) new JSONTokener(serializedResult).nextValue();

			if (jsonObject instanceof JSONObject) {

				JSONObject jsonLivrare = jsonObject.getJSONObject("dateLivrare");

				dateLivrare = new DateLivrareAfisare();
				dateLivrare.setPersContact(jsonLivrare.getString("persContact"));
				dateLivrare.setNrTel(jsonLivrare.getString("nrTel"));
				dateLivrare.setDateLivrare(jsonLivrare.getString("dateLivrare"));
				dateLivrare.setTransport(jsonLivrare.getString("Transport"));
				dateLivrare.setTipPlata(jsonLivrare.getString("tipPlata"));
				dateLivrare.setCantar(jsonLivrare.getString("Cantar"));
				dateLivrare.setFactRed(jsonLivrare.getString("factRed"));
				dateLivrare.setRedSeparat(jsonLivrare.getString("factRed"));
				dateLivrare.setOras(jsonLivrare.getString("Oras"));
				dateLivrare.setCodJudet(jsonLivrare.getString("codJudet"));
				dateLivrare.setNumeJudet(UtilsGeneral.getNumeJudet(jsonLivrare.getString("codJudet")));
				dateLivrare.setUnitLog(jsonLivrare.getString("unitLog"));
				dateLivrare.setTermenPlata(jsonLivrare.getString("termenPlata"));
				dateLivrare.setObsLivrare(jsonLivrare.getString("obsLivrare"));
				dateLivrare.setTipPersClient(jsonLivrare.getString("tipPersClient"));
				dateLivrare.setMail(jsonLivrare.getString("mail"));
				dateLivrare.setObsPlata(jsonLivrare.getString("obsPlata"));
				dateLivrare.setDataLivrare(Integer.valueOf(jsonLivrare.getString("dataLivrare")));
				dateLivrare.setAdresaD(jsonLivrare.getString("adresaD"));
				dateLivrare.setOrasD(jsonLivrare.getString("orasD"));
				dateLivrare.setCodJudetD(jsonLivrare.getString("codJudetD"));
				dateLivrare.setMacara(jsonLivrare.getString("macara"));
				dateLivrare.setNumeClient(jsonLivrare.getString("numeClient"));
				dateLivrare.setCnpClient(jsonLivrare.getString("cnpClient"));
				dateLivrare.setIdObiectiv(jsonLivrare.getString("idObiectiv"));
				dateLivrare.setAdresaObiectiv(Boolean.valueOf(jsonLivrare.getString("isAdresaObiectiv")));

				JSONArray jsonArticole = jsonObject.getJSONArray("articoleComanda");
				String tipAlert = "", subCmp = "";
				for (int i = 0; i < jsonArticole.length(); i++) {
					JSONObject articolObject = jsonArticole.getJSONObject(i);

					articol = new ArticolComanda();
					articol.setStatus(articolObject.getString("status"));
					articol.setCodArticol(articolObject.getString("codArticol"));
					articol.setNumeArticol(articolObject.getString("numeArticol"));
					articol.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
					articol.setDepozit(articolObject.getString("depozit"));
					articol.setPretUnit(Double.valueOf(articolObject.getString("pretUnit")));
					articol.setPretUnitarClient(Double.valueOf(articolObject.getString("pretUnit")));
					articol.setUm(articolObject.getString("um"));
					articol.setProcent(Double.valueOf(articolObject.getString("procent")));
					articol.setProcentFact(Double.valueOf(articolObject.getString("procentFact")));
					articol.setConditie(Boolean.valueOf(articolObject.getString("conditie")));
					articol.setCmp(Double.valueOf(articolObject.getString("cmp")));
					articol.setDiscClient(Double.valueOf(articolObject.getString("discClient")));
					articol.setDiscountAg(Double.valueOf(articolObject.getString("discountAg")));
					articol.setDiscountSd(Double.valueOf(articolObject.getString("discountSd")));
					articol.setDiscountDv(Double.valueOf(articolObject.getString("discountDv")));
					articol.setProcAprob(Double.valueOf(articolObject.getString("procAprob")));
					articol.setPermitSubCmp(articolObject.getString("permitSubCmp"));
					articol.setMultiplu(Double.valueOf(articolObject.getString("multiplu")));
					articol.setPret(Double.valueOf(articolObject.getString("pret")));
					articol.setInfoArticol(articolObject.getString("infoArticol"));
					articol.setCantUmb(Double.valueOf(articolObject.getString("cantUmb")));
					articol.setUmb(articolObject.getString("Umb"));
					articol.setUnitLogAlt(articolObject.getString("unitLogAlt"));
					articol.setDepart(articolObject.getString("depart"));
					articol.setTipArt(articolObject.getString("tipArt"));
					articol.setConditii(false);

					tipAlert = " ";
					if (UserInfo.getInstance().getTipAcces().equals("9")) {
						if (articol.getProcAprob() > articol.getDiscountAg())
							tipAlert = "SD";
					}

					if (articol.getProcAprob() > articol.getDiscountSd())
						tipAlert = tipAlert.trim() + "DV";

					articol.setTipAlert(tipAlert);

					subCmp = "0";
					if (articol.getPretUnit() < articol.getCmp())
						subCmp = "1";
					articol.setAlteValori(subCmp);

					articol.setPonderare(Integer.valueOf(articolObject.getString("ponderare")));
					articol.setPretMediu(Double.valueOf(articolObject.getString("pretMediu")));
					articol.setAdaosMediu(Double.valueOf(articolObject.getString("adaosMediu")));
					articol.setUnitMasPretMediu(articolObject.getString("unitMasPretMediu"));
					articol.setDepartSintetic(articolObject.getString("departSintetic"));
					articol.setCoefCorectie(Double.valueOf(articolObject.getString("coefCorectie")));

					listArticole.add(articol);

				}

			}

			JSONObject jsonConditii = jsonObject.getJSONObject("conditii");

			JSONObject jsonHeader = null;
			if (!jsonConditii.isNull("header")) {
				jsonHeader = jsonConditii.getJSONObject("header");

				headerConditii.setId(Integer.parseInt(jsonHeader.getString("id")));
				headerConditii.setConditiiCalit(Double.parseDouble(jsonHeader.getString("conditiiCalit")));
				headerConditii.setNrFact(Integer.parseInt(jsonHeader.getString("nrFact")));
				headerConditii.setObservatii(jsonHeader.getString("observatii"));

			}

			JSONArray jsonArticole;
			if (!jsonConditii.isNull("articole")) {
				jsonArticole = jsonConditii.getJSONArray("articole");

				BeanConditiiArticole articolCond = null;

				for (int i = 0; i < jsonArticole.length(); i++) {

					JSONObject articolObject = jsonArticole.getJSONObject(i);

					articolCond = new BeanConditiiArticole();
					articolCond.setCod(articolObject.getString("cod"));
					articolCond.setNume(articolObject.getString("nume"));
					articolCond.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
					articolCond.setUm(articolObject.getString("um"));
					articolCond.setValoare(Double.valueOf(articolObject.getString("valoare")));
					articolCond.setMultiplu(Double.valueOf(articolObject.getString("multiplu")));
					listArticoleConditii.add(articolCond);

				}

			}

			conditii.setHeader(headerConditii);
			conditii.setArticole(listArticoleConditii);

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		articoleComanda.setDateLivrare(dateLivrare);

		articoleComanda.setListArticole(listArticole);
		articoleComanda.setConditii(conditii);

		return articoleComanda;
	}

	private ArrayList<BeanComandaCreata> deserializeListComenzi(String serializedListComenzi) {

		BeanComandaCreata comanda = null;
		ArrayList<BeanComandaCreata> listComenzi = new ArrayList<BeanComandaCreata>();

		try {

			Object json = new JSONTokener(serializedListComenzi).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray(serializedListComenzi);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject comandaObject = jsonObject.getJSONObject(i);

					comanda = new BeanComandaCreata();
					comanda.setId(comandaObject.getString("idComanda"));
					comanda.setNumeClient(comandaObject.getString("numeClient"));
					comanda.setCodClient(comandaObject.getString("codClient"));
					comanda.setData(comandaObject.getString("dataComanda"));
					comanda.setSuma(comandaObject.getString("sumaComanda"));
					comanda.setStare(InfoStrings.statusAprobCmd(Integer.valueOf(comandaObject.getString("stareComanda"))));
					comanda.setMoneda(comandaObject.getString("monedaComanda"));
					comanda.setSumaTva(comandaObject.getString("sumaTVA"));
					comanda.setMonedaTva(comandaObject.getString("monedaTVA"));
					comanda.setCmdSap(comandaObject.getString("cmdSap"));
					comanda.setCanalDistrib(comandaObject.getString("canalDistrib"));
					comanda.setTipClient(InfoStrings.getTipClient(comandaObject.getString("tipClient")));
					comanda.setDivizieAgent(comandaObject.getString("divizieAgent"));
					comanda.setFiliala(comandaObject.getString("filiala"));
					comanda.setFactRed(comandaObject.getString("factRed"));
					comanda.setAccept1(comandaObject.getString("accept1"));
					comanda.setAccept2(comandaObject.getString("accept2"));
					comanda.setNumeAgent(comandaObject.getString("numeAgent"));
					comanda.setTermenPlata(comandaObject.getString("termenPlata"));
					comanda.setCursValutar(comandaObject.getString("cursValutar"));
					comanda.setDocInsotitor(comandaObject.getString("docInsotitor"));
					comanda.setAdresaNoua(comandaObject.getString("adresaNoua"));
					comanda.setAdresaLivrare(comandaObject.getString("adresaLivrare"));
					comanda.setDivizieComanda(comandaObject.getString("divizieComanda"));
					comanda.setPondere30(comandaObject.getString("pondere30"));
					comanda.setAprobariNecesare(comandaObject.getString("aprobariNecesare"));
					comanda.setAprobariPrimite(comandaObject.getString("aprobariPrimite"));
					comanda.setCodClientGenericGed(comandaObject.getString("codClientGenericGed"));
					comanda.setConditiiImpuse(comandaObject.getString("conditiiImpuse"));

					listComenzi.add(comanda);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		listComenziCreate = listComenzi;
		return listComenzi;

	}

	public List<BeanComandaCreata> getComenziDivizie(String divizie) {
		CriteriulDivizie criteriu = new CriteriulDivizie();
		List<BeanComandaCreata> listCmd = criteriu.indeplinesteCriteriu(listComenziCreate, divizie);
		return listCmd;
	}

}