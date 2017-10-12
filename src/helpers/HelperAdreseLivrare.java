package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lite.sfa.test.CreareComanda;
import lite.sfa.test.ModificareComanda;
import model.DateLivrare;
import model.UserInfo;
import android.content.Context;

public class HelperAdreseLivrare {

	private static final String livrareRapida = "TERR - Curier rapid";

	private static String localitatiAcceptate;

	public static boolean isConditiiCurierRapid() {

		if (!UserInfo.getInstance().getCodDepart().equals("02"))
			return false;

		if (CreareComanda.filialaAlternativa != null)
			return CreareComanda.filialaAlternativa.equalsIgnoreCase("BV90") && UserInfo.getInstance().getUnitLog().startsWith("BU");
		else if (ModificareComanda.filialaAlternativaM != null)
			return ModificareComanda.filialaAlternativaM.equalsIgnoreCase("BV90") && UserInfo.getInstance().getUnitLog().startsWith("BU");

		return false;

	}

	public static String[] adaugaTransportCurierRapid(String[] tipTransport) {

		String[] arrayTransport;

		List<String> listTransport = new ArrayList<String>(Arrays.asList(tipTransport));

		listTransport.add(livrareRapida);

		arrayTransport = (String[]) listTransport.toArray(new String[listTransport.size()]);

		return arrayTransport;

	}

	public static boolean isAdresaLivrareRapida(Context context) {
		return localitatiAcceptate.toUpperCase().contains(DateLivrare.getInstance().getOras().toUpperCase());
	}

	public static String getLocalitatiAcceptate() {
		return localitatiAcceptate;
	}

	public static void setLocalitatiAcceptate(String localitatiAcceptate) {
		HelperAdreseLivrare.localitatiAcceptate = localitatiAcceptate;
	}

}
