/**
 * @author florinb
 *
 */
package model;

import java.util.Locale;

public class InfoStrings {

	public static String statusSAPMsg(Integer msgId) {
		String retVal = "";

		switch (msgId) {
		case -1:
			retVal = "Comanda NU a fost salvata.";
			break;
		case 0:
			retVal = "Comanda salvata.";
			break;
		case 1:
			retVal = "Comanda in curs de validare.";
			break;
		case 2:
			retVal = "Comanda validata.";
			break;
		case 3:
			retVal = "Comanda in curs de modificare.";
			break;
		case 4:
			retVal = "Comanda modificata.";
			break;
		case 5:
			retVal = "Comanda in curs de stergere.";
			break;
		case 6:
			retVal = "Comanda stearsa.";
			break;
		case 8:
			retVal = "Comanda facturata si emisa.";
			break;
		case 9:
			retVal = "Comanda invalida. Stoc insuficient.";
			break;
		case 10:
			retVal = "Comanda invalida datorita limitei de credit.";
			break;
		case 11:
			retVal = "Comanda salvata.";
			break;
		case 12:
			retVal = "Cmd. angaj. in curs de aprobare.";
			break;
		case 13:
			retVal = "Cmd. angaj. aprobata.";
			break;
		case 14:
			retVal = "Cmd. angaj. respinsa.";
			break;
		case 16:
			retVal = "Comanda simulata.";
			break;

		case 30:
			retVal = "Comanda nealocata pe borderou";
			break;

		case 31:
			retVal = "Comanda alocata pe borderou";
			break;

		case 99:
			retVal = "Atentie! Clientul nu apartine diviziei din document.";
			break;
		default:
			retVal = "Stare comanda necunoscuta.";
			break;
		}

		return retVal;
	}

	public static String statusAprobCmd(Integer msgId) {
		String retVal = "";

		switch (msgId) {
		case -1:
			retVal = "Comanda NU a fost salvata.";
			break;

		case 0:
			retVal = "Comanda emisa.";
			break;

		case 1:
			retVal = "Comanda in curs de aprobare.";
			break;

		case 2:
			retVal = "Comanda aprobata.";
			break;

		case 3:
			retVal = "Comanda respinsa.";
			break;

		case 4:
			retVal = "Comanda supusa unor conditii.";
			break;

		case 5:
			retVal = "Comanda stearsa.";
			break;

		case 6:
			retVal = "Cmd. angaj. in curs de aprobare.";
			break;

		case 7:
			retVal = "Cmd. angaj. aprobata.";
			break;

		case 8:
			retVal = "Cmd. angaj. respinsa.";
			break;

		case 9:
			retVal = "Comanda invalida. Stoc insuficient.";
			break;

		case 10:
			retVal = "Comanda invalida datorita limitei de credit.";
			break;

		case 15:
			retVal = "Comanda facturata.";
			break;

		case 20:
			retVal = "Comanda simulata cu rezervare stoc."; // pt. comenzi GED
			break;

		case 21:
			retVal = "Comanda simulata fara rezervare stoc."; // pt. comenzi GED
			break;

		case 30:
			retVal = "Comanda nealocata pe borderou";
			break;

		case 31:
			retVal = "Comanda alocata pe borderou";
			break;

		}
		return retVal;
	}

	public static String getTipClient(String codTip) {
		String retVal = " ";

		if (codTip.equals("01"))
			retVal = "Client final";

		if (codTip.equals("02"))
			retVal = "Constructor general";

		if (codTip.equals("03"))
			retVal = "Constructor special";

		if (codTip.equals("04"))
			retVal = "Revanzator";

		if (codTip.equals("05"))
			retVal = "Producator mobila";

		if (codTip.equals("06"))
			retVal = "Debitor mat. lemnoase";

		if (codTip.equals("07"))
			retVal = "Tepar";

		if (codTip.equals("08"))
			retVal = "Nespecificat";

		if (codTip.equals("09"))
			retVal = "Client extern UE";

		if (codTip.equals("10"))
			retVal = "Client extern non-UE";

		return retVal;
	}

	public static String numeJudet(String codJudet) {
		String retVal = "Nedefinit";

		if (codJudet.equals("01"))
			retVal = "ALBA";

		if (codJudet.equals("02"))
			retVal = "ARAD";

		if (codJudet.equals("03"))
			retVal = "ARGES";

		if (codJudet.equals("04"))
			retVal = "BACAU";

		if (codJudet.equals("05"))
			retVal = "BIHOR";

		if (codJudet.equals("06"))
			retVal = "BISTRITA-NASAUD";

		if (codJudet.equals("07"))
			retVal = "BOTOSANI";

		if (codJudet.equals("09"))
			retVal = "BRAILA";

		if (codJudet.equals("08"))
			retVal = "BRASOV";

		if (codJudet.equals("40"))
			retVal = "BUCURESTI";

		if (codJudet.equals("10"))
			retVal = "BUZAU";

		if (codJudet.equals("51"))
			retVal = "CALARASI";

		if (codJudet.equals("11"))
			retVal = "CARAS-SEVERIN";

		if (codJudet.equals("12"))
			retVal = "CLUJ";

		if (codJudet.equals("13"))
			retVal = "CONSTANTA";

		if (codJudet.equals("14"))
			retVal = "COVASNA";

		if (codJudet.equals("15"))
			retVal = "DAMBOVITA";

		if (codJudet.equals("16"))
			retVal = "DOLJ";

		if (codJudet.equals("17"))
			retVal = "GALATI";

		if (codJudet.equals("52"))
			retVal = "GIURGIU";

		if (codJudet.equals("18"))
			retVal = "GORJ";

		if (codJudet.equals("19"))
			retVal = "HARGHITA";

		if (codJudet.equals("20"))
			retVal = "HUNEDOARA";

		if (codJudet.equals("21"))
			retVal = "IALOMITA";

		if (codJudet.equals("22"))
			retVal = "IASI";

		if (codJudet.equals("23"))
			retVal = "ILFOV";

		if (codJudet.equals("24"))
			retVal = "MARAMURES";

		if (codJudet.equals("25"))
			retVal = "MEHEDINTI";

		if (codJudet.equals("26"))
			retVal = "MURES";

		if (codJudet.equals("27"))
			retVal = "NEAMT";

		if (codJudet.equals("28"))
			retVal = "OLT";

		if (codJudet.equals("29"))
			retVal = "PRAHOVA";

		if (codJudet.equals("31"))
			retVal = "SALAJ";

		if (codJudet.equals("30"))
			retVal = "SATU-MARE";

		if (codJudet.equals("32"))
			retVal = "SIBIU";

		if (codJudet.equals("33"))
			retVal = "SUCEAVA";

		if (codJudet.equals("34"))
			retVal = "TELEORMAN";

		if (codJudet.equals("35"))
			retVal = "TIMIS";

		if (codJudet.equals("36"))
			retVal = "TULCEA";

		if (codJudet.equals("38"))
			retVal = "VALCEA";

		if (codJudet.equals("37"))
			retVal = "VASLUI";

		if (codJudet.equals("39"))
			retVal = "VRANCEA";

		return retVal;
	}

	public static String getULFiliala(String numeUL) {
		String fl = "NN10";

		if (numeUL.equals("BACAU"))
			fl = "BC10";

		if (numeUL.equals("GALATI"))
			fl = "GL10";

		if (numeUL.equals("PITESTI"))
			fl = "AG10";

		if (numeUL.equals("TIMISOARA"))
			fl = "TM10";

		if (numeUL.equals("ORADEA"))
			fl = "BH10";

		if (numeUL.equals("FOCSANI"))
			fl = "VN10";

		if (numeUL.equals("GLINA"))
			fl = "BU10";

		if (numeUL.equals("CLUJ"))
			fl = "CJ10";

		if (numeUL.equals("BAIA"))
			fl = "MM10";

		if (numeUL.equals("MILITARI"))
			fl = "BU11";

		if (numeUL.equals("CONSTANTA"))
			fl = "CT10";

		if (numeUL.equals("BRASOV"))
			fl = "BV10";

		if (numeUL.equals("PLOIESTI"))
			fl = "PH10";

		if (numeUL.equals("PIATRA"))
			fl = "NT10";

		if (numeUL.equals("MUREA"))
			fl = "MS10";

		if (numeUL.equals("IASI"))
			fl = "IS10";

		if (numeUL.equals("OTOPENI"))
			fl = "BU12";

		return fl;

	}

	public static String getNumeUL(String numeUL) {
		String numeFil = "NN10";

		if (numeUL.equals("BC10"))
			numeFil = "BACAU";

		if (numeUL.equals("BZ10"))
			numeFil = "BUZAU";

		if (numeUL.equals("HD10"))
			numeFil = "HUNEDOARA";

		if (numeUL.equals("GL10"))
			numeFil = "GALATI";

		if (numeUL.equals("AG10"))
			numeFil = "ARGES";

		if (numeUL.equals("TM10"))
			numeFil = "TIMISOARA";

		if (numeUL.equals("BH10"))
			numeFil = "BIHOR";

		if (numeUL.equals("VN10"))
			numeFil = "VRANCEA";

		if (numeUL.equals("BU10"))
			numeFil = "BUC. GLINA";

		if (numeUL.equals("CJ10"))
			numeFil = "CLUJ";

		if (numeUL.equals("MM10"))
			numeFil = "MARAMURES";

		if (numeUL.equals("BU11"))
			numeFil = "BUC. MILITARI";

		if (numeUL.equals("CT10"))
			numeFil = "CONSTANTA";

		if (numeUL.equals("BV10"))
			numeFil = "BRASOV";

		if (numeUL.equals("BV90"))
			numeFil = "BRASOV CENTRAL";

		if (numeUL.equals("PH10"))
			numeFil = "PRAHOVA";

		if (numeUL.equals("NT10"))
			numeFil = "NEAMT";

		if (numeUL.equals("MS10"))
			numeFil = "MURES";

		if (numeUL.equals("IS10"))
			numeFil = "IASI";

		if (numeUL.equals("BU12"))
			numeFil = "BUC. OTOPENI";

		if (numeUL.equals("BU13"))
			numeFil = "BUC. ANDRONACHE";

		if (numeUL.equals("DJ10"))
			numeFil = "DOLJ";

		return numeFil;

	}

	public static String getDepartName(String departCode) {
		String retVal = "nedef";

		if (departCode.equals("01"))
			retVal = "Lemnoase";

		if (departCode.equals("02"))
			retVal = "Feronerie";

		if (departCode.equals("03"))
			retVal = "Parchet";

		if (departCode.equals("04"))
			retVal = "Mat.grele";

		if (departCode.equals("05"))
			retVal = "Electrice";

		if (departCode.equals("06"))
			retVal = "Gips";

		if (departCode.equals("07"))
			retVal = "Chimice";

		if (departCode.equals("08"))
			retVal = "Instalatii";

		if (departCode.equals("09"))
			retVal = "Hidroizolatii";

		return retVal;
	}

	public static String getNumeDepart(String codDepart) {
		String dpt = "NEDE";

		if (codDepart.equals("07"))
			dpt = "CHIM";

		if (codDepart.equals("10"))
			dpt = "DIVE";

		if (codDepart.equals("05"))
			dpt = "ELEC";

		if (codDepart.equals("02"))
			dpt = "FERO";

		if (codDepart.equals("06"))
			dpt = "GIPS";

		if (codDepart.equals("08"))
			dpt = "INST";

		if (codDepart.equals("01"))
			dpt = "LEMN";

		if (codDepart.equals("04"))
			dpt = "MATE";

		if (codDepart.equals("03"))
			dpt = "PARC";

		if (codDepart.equals("09"))
			dpt = "HIDR";

		return dpt;

	}

	public static String getClientGenericGed(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000023";

			if (filiala.equals("BC10"))
				codClient = "4119000024";

			if (filiala.equals("BH10"))
				codClient = "4119000025";

			if (filiala.equals("BV10"))
				codClient = "4119000030";
			
			if (filiala.equals("BZ10"))
				codClient = "4119000255";

			if (filiala.equals("CJ10"))
				codClient = "4119000031";

			if (filiala.equals("CT10"))
				codClient = "4119000080";

			if (filiala.equals("DJ10"))
				codClient = "4119000032";

			if (filiala.equals("GL10"))
				codClient = "4119000033";

			if (filiala.equals("HD10"))
				codClient = "4119000260";
			
			if (filiala.equals("IS10"))
				codClient = "4119000034";

			if (filiala.equals("MM10"))
				codClient = "4119000035";

			if (filiala.equals("MS10"))
				codClient = "4119000036";

			if (filiala.equals("NT10"))
				codClient = "4119000037";

			if (filiala.equals("PH10"))
				codClient = "4119000038";

			if (filiala.equals("TM10"))
				codClient = "4119000039";

			if (filiala.equals("VN10"))
				codClient = "4119000040";

			if (filiala.equals("BU13"))
				codClient = "4119000029";

			if (filiala.equals("BU10"))
				codClient = "4119000028";

			if (filiala.equals("BU11"))
				codClient = "4119000027";

			if (filiala.equals("BU12"))
				codClient = "4119000026";

		}

		if (tipClient.equals("PJ")) {
			if (filiala.equals("AG10"))
				codClient = "4119000059";

			if (filiala.equals("BC10"))
				codClient = "4119000060";

			if (filiala.equals("BH10"))
				codClient = "4119000061";

			if (filiala.equals("BV10"))
				codClient = "4119000066";

			if (filiala.equals("BZ10"))
				codClient = "4119000256";
			
			if (filiala.equals("CJ10"))
				codClient = "4119000067";

			if (filiala.equals("CT10"))
				codClient = "4119000082";

			if (filiala.equals("DJ10"))
				codClient = "4119000068";

			if (filiala.equals("GL10"))
				codClient = "4119000069";

			if (filiala.equals("HD10"))
				codClient = "4119000261";
			
			if (filiala.equals("IS10"))
				codClient = "4119000070";

			if (filiala.equals("MM10"))
				codClient = "4119000071";

			if (filiala.equals("MS10"))
				codClient = "4119000072";

			if (filiala.equals("NT10"))
				codClient = "4119000073";

			if (filiala.equals("PH10"))
				codClient = "4119000074";

			if (filiala.equals("TM10"))
				codClient = "4119000075";

			if (filiala.equals("VN10"))
				codClient = "4119000076";

			if (filiala.equals("BU13"))
				codClient = "4119000065";

			if (filiala.equals("BU10"))
				codClient = "4119000064";

			if (filiala.equals("BU11"))
				codClient = "4119000063";

			if (filiala.equals("BU12"))
				codClient = "4119000062";

		}

		return codClient;
	}

	public static String getTipUser(String tipUser) {
		String tipAcces = "NN";

		// AGENTI
		if (UserInfo.getInstance().getTipAcces().equals("9")) {
			tipAcces = "AV";
		}

		// SEFI DE DEPARTAMENT
		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipAcces = "SD";
		}

		// DIRECTORI DE VANZARI, DEPARTAMENT
		if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")) {
			tipAcces = "DV";
		}

		// KEY ACCOUNTI
		if (UserInfo.getInstance().getTipAcces().equals("27")) {
			tipAcces = "KA";
		}

		// DIRECTOR KA
		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			tipAcces = "DK";
		}

		// CONSILIERI
		if (UserInfo.getInstance().getTipAcces().equals("17")) {
			tipAcces = "CV";
		}

		// SEFI DE MAGAZIN
		if (UserInfo.getInstance().getTipAcces().equals("18")) {
			tipAcces = "SM";
		}

		return tipAcces;
	}

	public static String getTipPlata(String codPlata) {

		String strPlata = "";

		if (codPlata.equals("B"))
			strPlata = "Bilet la ordin";

		if (codPlata.equals("C"))
			strPlata = "Cec";

		if (codPlata.equals("E"))
			strPlata = "Plata in numerar";

		if (codPlata.equals("O"))
			strPlata = "Ordin de plata";

		if (codPlata.equals("E1"))
			strPlata = "Numerar la sofer";

		return strPlata;

	}

	public static String getTipTransport(String codTrasnport) {

		String strTransport = "";

		if (codTrasnport.equals("TRAP"))
			strTransport = "Arabesque";

		if (codTrasnport.equals("TCLI"))
			strTransport = "Client";

		if (codTrasnport.equals("TFRN"))
			strTransport = "Furnizor";

		if (codTrasnport.equals("TERT"))
			strTransport = "Curier";

		return strTransport;

	}

	public static String getTipAprobare(String codAprobare) {

		String strAprobare = "";

		if (codAprobare.toUpperCase(Locale.getDefault()).equals("X"))
			strAprobare = "Da";
		else
			strAprobare = "Nu";

		return strAprobare;

	}

	public static String getDistribUnitLog(String gedUnitLog)
	{
		return gedUnitLog.substring(0, 2) + "1" + gedUnitLog.subSequence(3, 4);
	}

	public static boolean isMatTransport(String codArt) {
		boolean isMat = false;

		String[] articolePermise = { "000000000030101220", "000000000030101221", "000000000030101223", "000000000030101224", "000000000030101225",
				"000000000030101226", "000000000030101227", "000000000030101228", "000000000030101230", "000000000030101222", "000000000030101111",
				"000000000030101240" };

		for (int i = 0; i < articolePermise.length; i++) {
			if (articolePermise[i].equals(codArt)) {
				isMat = true;
				break;
			}
		}

		return isMat;
	}

}
