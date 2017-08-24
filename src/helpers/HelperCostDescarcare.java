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

	public static List<ArticolComanda> getArticoleDescarcare(List<ArticolDescarcare> costDescarcare) {

		List<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();

		for (ArticolDescarcare artDesc : costDescarcare) {
			ArticolComanda articolComanda = new ArticolComanda();

			articolComanda.setCodArticol(artDesc.getCod());
			articolComanda.setNumeArticol("PREST.SERV.DESCARCARE PALET DIV " + artDesc.getDepart());
			articolComanda.setCantitate(1);
			articolComanda.setCantUmb(1);
			articolComanda.setPretUnit(Double.valueOf(artDesc.getValoare()));
			articolComanda.setPret(Double.valueOf(artDesc.getValoare()));
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
			articolComanda.setDepozit(artDesc.getDepart() + "V1");
			articolComanda.setTipArt("");
			articolComanda.setDepart(artDesc.getDepart());
			articolComanda.setDepartSintetic(artDesc.getDepart());

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
				articol.setValoare(object.getString("valoare"));
				listArticole.add(articol);

			}

			costDescarcare.setArticoleDescarcare(listArticole);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return costDescarcare;
	}

}
