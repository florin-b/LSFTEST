package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lite.sfa.test.CreareComanda;
import lite.sfa.test.ModificareComanda;
import model.DateLivrare;
import model.UserInfo;
import android.content.Context;
import beans.BeanAdresaLivrare;

import com.google.android.gms.maps.model.LatLng;

public class HelperAdreseLivrare {

	private static final String livrareRapida = "TERR - Curier rapid";
	private static final double DIST_MIN_ADR_KM = 0.5;
	private static final double DIST_MIN_ADR_NOUA_KM = 0.15;

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

	public static int getTonajSpinnerPos(String tonaj) {
		if (tonaj == null || tonaj.equals("0"))
			return 0;

		if (tonaj.equals("3.5"))
			return 1;
		else if (tonaj.equals("10"))
			return 2;
		else if (tonaj.equals("20"))
			return 3;

		return 0;
	}

	public static String getTonajSpinnerValue(int selectedPos) {

		switch (selectedPos) {
		case 1:
			return "3.5";
		case 2:
			return "10";
		case 3:
		default:
			return "20";

		}

	}

	public static String getTonajAdresaNoua(List<BeanAdresaLivrare> listAdrese, LatLng coordAdresa) {

		String tonajAdresa = "0";

		for (BeanAdresaLivrare adresa : listAdrese) {

			String coords[] = adresa.getCoords().split(",");

			double distAdresa = distanceXtoY(coordAdresa.latitude, coordAdresa.longitude, Double.valueOf(coords[0]), Double.valueOf(coords[1]), "K");
			if (distAdresa <= DIST_MIN_ADR_KM) {
				tonajAdresa = adresa.getTonaj();
				break;
			}

		}

		return tonajAdresa;
	}

	public static int verificaDistantaAdresaNoua(List<BeanAdresaLivrare> listAdrese, LatLng coordAdresa) {

		int selectedPos = -1;

		int pos = 0;
		for (BeanAdresaLivrare adresa : listAdrese) {
			String coords[] = adresa.getCoords().split(",");

			double distAdresa = distanceXtoY(coordAdresa.latitude, coordAdresa.longitude, Double.valueOf(coords[0]), Double.valueOf(coords[1]), "K");
			if (distAdresa < DIST_MIN_ADR_NOUA_KM) {
				selectedPos = pos;
				break;
			}

			pos++;
		}

		return selectedPos;
	}

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
