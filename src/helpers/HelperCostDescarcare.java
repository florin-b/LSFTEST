package helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.ArticolComanda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beans.ArticolCalculDesc;
import beans.ArticolDescarcare;
import beans.CostDescarcare;

public class HelperCostDescarcare {

	public static List<ArticolComanda> getArticoleDescarcare(CostDescarcare costDescarcare, double valoareCost, String filiala) {

		double procentReducere = valoareCost / costDescarcare.getValoareDescarcare();

		List<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();

		for (ArticolDescarcare artDesc : costDescarcare.getArticoleDescarcare()) {
			ArticolComanda articolComanda = new ArticolComanda();

			articolComanda.setCodArticol(artDesc.getCod());
			articolComanda.setNumeArticol("PREST.SERV.DESCARCARE PALET DIV " + artDesc.getDepart());
			articolComanda.setCantitate(artDesc.getCantitate());
			articolComanda.setCantUmb(artDesc.getCantitate());
			articolComanda.setPretUnit(artDesc.getValoare() * procentReducere);
			articolComanda.setPret(artDesc.getValoare() * procentReducere * artDesc.getCantitate());
			articolComanda.setPretUnitarClient(artDesc.getValoare() * procentReducere);
			articolComanda.setPretUnitarGed(artDesc.getValoare() * procentReducere);
			articolComanda.setProcent(0);
			articolComanda.setUm("BUC");
			articolComanda.setUmb("BUC");
			articolComanda.setDiscClient(0);
			articolComanda.setProcentFact(0);
			articolComanda.setMultiplu(1);
			articolComanda.setConditie(false);
			articolComanda.setProcAprob(0);
			articolComanda.setInfoArticol(" ");
			articolComanda.setObservatii("");
			articolComanda.setDepartAprob("");
			articolComanda.setIstoricPret("");
			articolComanda.setAlteValori("");
			articolComanda.setTipArt("");
			articolComanda.setDepart(artDesc.getDepart());
			articolComanda.setDepartSintetic(artDesc.getDepart());
			articolComanda.setDepozit(artDesc.getDepart() + "V1");
			articolComanda.setFilialaSite(filiala);

			listArticole.add(articolComanda);
		}

		return listArticole;

	}

	public static void eliminaCostDescarcare(List<ArticolComanda> listArticole) {

		Iterator<ArticolComanda> iterator = listArticole.iterator();

		while (iterator.hasNext()) {

			ArticolComanda articol = iterator.next();

			if (articol.getNumeArticol().toUpperCase().contains("PREST.SERV.DESCARCARE PALET"))
				iterator.remove();

		}

	}

	public static List<ArticolCalculDesc> getDateCalculDescarcare(List<ArticolComanda> listArticole) {

		List<ArticolCalculDesc> articoleCalcul = new ArrayList<ArticolCalculDesc>();

		for (ArticolComanda artCmd : listArticole) {
			ArticolCalculDesc articol = new ArticolCalculDesc();
			articol.setCod(artCmd.getCodArticol());
			articol.setCant(artCmd.getCantUmb());
			articol.setUm(artCmd.getUmb());
			articol.setDepoz(artCmd.getDepozit());
			articoleCalcul.add(articol);
		}

		return articoleCalcul;

	}

	public static CostDescarcare deserializeCostMacara(String dateCost) {

		CostDescarcare costDescarcare = new CostDescarcare();
		List<ArticolDescarcare> listArticole = new ArrayList<ArticolDescarcare>();

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(dateCost);

			costDescarcare.setSePermite(Boolean.valueOf(jsonObject.getString("sePermite")));

			JSONArray jsonArray = new JSONArray(jsonObject.getString("articoleDescarcare"));

			for (int i = 0; i < jsonArray.length(); i++) {
				ArticolDescarcare articol = new ArticolDescarcare();

				JSONObject object = jsonArray.getJSONObject(i);

				articol.setCod(object.getString("cod"));
				articol.setDepart(object.getString("depart"));
				articol.setValoare(Double.valueOf(object.getString("valoare")));
				articol.setCantitate(Double.valueOf(object.getString("cantitate")));
				articol.setValoareMin(Double.valueOf(object.getString("valoareMin")));
				listArticole.add(articol);

			}

			costDescarcare.setArticoleDescarcare(listArticole);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return costDescarcare;
	}

}
