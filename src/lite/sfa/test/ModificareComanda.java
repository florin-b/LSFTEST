/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import listeners.ArticolModificareListener;
import listeners.AsyncTaskListener;
import listeners.ComenziDAOListener;
import model.ArticolComanda;
import model.Comanda;
import model.ComenziDAO;
import model.DateLivrare;
import model.InfoStrings;
import model.ListaArticoleComandaGed;
import model.ListaArticoleModificareComanda;
import model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.UtilsFormatting;
import utils.UtilsGeneral;
import adapters.ArticolModificareAdapter;
import adapters.ComandaModificareAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.BeanArticoleAfisare;
import beans.BeanComandaCreata;
import beans.BeanConditii;
import beans.BeanConditiiArticole;
import beans.BeanConditiiHeader;
import beans.DateLivrareAfisare;
import enums.EnumComenziDAO;

public class ModificareComanda extends Activity implements AsyncTaskListener, ComenziDAOListener, ArticolModificareListener, Observer {

	Button quitBtn, stocBtn, clientBtn, articoleBtn, livrareBtn, salveazaComandaBtn, stergeComandaBtn, btnCommentariiCond;
	String filiala = "", nume = "", cod = "", globalSubCmp = "0";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";

	private int listViewSelPos = -1;

	private Spinner spinnerComenzi;
	public SimpleAdapter adapterComenzi;

	public static String selectedCmd = "";
	private String selectedCmdSAP = "-1";
	private String selectedClientCode = "-1", selectedUnitLog = "-1";
	private BeanComandaCreata comandaSelectata;
	private TextView textTipPlata, textAdrLivr, textTotalCmd, textOras, textJudet;
	private TextView textPersContact, textTelefon, textCantar, textTransport, textFactRed, textPondereB, textTaxaVerde;

	private TextView textCondProcB, textCondNrFacturi, textCondComentarii;

	public static String codClientVar = "";
	public static String numeClientVar = "";
	public static String tipClientVar = "";
	public static String articoleComanda = "", numeArtSelContextMenu = "", codArtSelContextMenu = "";
	public static double totalComanda = 0, stocArtCond = 0;

	public static boolean isComandaDistrib = true;

	private static ArticolComanda[] objArticol = new ArticolComanda[70];

	public static String filialaAlternativaM = "NN10";

	private ProgressBar mProgress;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler logonHandler = new Handler();

	private String comandaBlocata = "0";
	private String globalAlertSDKA = "", globalAlertDVKA = "";
	private String clientFinalStr = "", conditieID = "";
	private int idOperatieComanda = 3;

	private boolean alertSD = false;
	private boolean alertDV = false;

	public static String tipAcces;
	ListView listViewArticole, listViewArtCond;

	private LinearLayout layoutTaxaVerde, layoutConditiiHeader, layoutCondProcB, layoutCondNrFact, layoutCondObs;

	public static String depozitUnic = "";
	private String selectedCmdAdrLivr = "";
	private String mailAlertTipDest = "";

	public static String divizieComanda = "";

	private LinearLayout layoutDetaliiCmd;

	String serializedResult;
	private String comandaJson;
	private ComenziDAO operatiiComenzi;
	private List<BeanComandaCreata> listComenzi;
	private ArrayList<ArticolComanda> listArticoleComanda;
	private List<BeanConditiiArticole> conditiiComandaArticole;
	private ArticolModificareAdapter adapterArticole;
	private String codTipReducere = "-1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.modificarecomandaheader);

		operatiiComenzi = ComenziDAO.getInstance(this);
		operatiiComenzi.setComenziDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Modificare comanda");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		spinnerComenzi = (Spinner) findViewById(R.id.spinnerCmd);
		spinnerComenzi.setVisibility(View.INVISIBLE);
		addListenerSpinnerComenzi();

		layoutDetaliiCmd = (LinearLayout) findViewById(R.id.layoutDetaliiCmd);
		listViewArticole = (ListView) findViewById(R.id.listArtModif);

		if (isUserCV())
			ListaArticoleComandaGed.getInstance().addObserver(this);
		else
			ListaArticoleModificareComanda.getInstance().addObserver(this);

		addListenerListArtModif();
		registerForContextMenu(listViewArticole);

		layoutTaxaVerde = (LinearLayout) findViewById(R.id.layoutTaxaVerde);
		layoutTaxaVerde.setVisibility(View.INVISIBLE);

		textTaxaVerde = (TextView) findViewById(R.id.textTaxaVerde);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textCantar = (TextView) findViewById(R.id.textCantar);
		textTransport = (TextView) findViewById(R.id.textTransport);
		textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);
		textFactRed = (TextView) findViewById(R.id.textFactRed);
		textPondereB = (TextView) findViewById(R.id.textPondereB);

		layoutConditiiHeader = (LinearLayout) findViewById(R.id.layoutConditiiHeader);
		layoutCondProcB = (LinearLayout) findViewById(R.id.layoutCondProcB);
		layoutCondNrFact = (LinearLayout) findViewById(R.id.layoutCondNrFact);
		layoutCondObs = (LinearLayout) findViewById(R.id.layoutCondObs);

		textCondProcB = (TextView) findViewById(R.id.textCondProcB);
		textCondNrFacturi = (TextView) findViewById(R.id.textCondNrFacturi);
		textCondComentarii = (TextView) findViewById(R.id.textCondComentarii);

		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);

		this.salveazaComandaBtn = (Button) findViewById(R.id.saveCmdBtn);
		this.salveazaComandaBtn.setVisibility(View.INVISIBLE);
		addListenerSaveCmdBtn();

		this.stergeComandaBtn = (Button) findViewById(R.id.delCmdBtn);

		addListenerDelCmdBtn();

		mProgress = (ProgressBar) findViewById(R.id.progress_bar_savecmd);
		mProgress.setVisibility(View.INVISIBLE);

		loadListComenzi();

	}

	private void CreateMenu(Menu menu) {

		if (!isUserCV()) {
			MenuItem mnu2 = menu.add(0, 0, 0, "Articole");
			mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		MenuItem mnu3 = menu.add(0, 1, 1, "Livrare");
		mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			if (codClientVar.length() > 0) {

				Intent nextScreen = null;
				if (isUserCV() || isComandaGed()) {
					nextScreen = new Intent(getApplicationContext(), SelectArtCmdGed.class);
					nextScreen.putExtra("totalNegociat", false);
					nextScreen.putExtra("codClientVar", selectedClientCode);
					nextScreen.putExtra("depozitUnic", depozitUnic);
					nextScreen.putExtra("tipComanda", "X");
					nextScreen.putExtra("rezervStoc", false);
					nextScreen.putExtra("filialaAlternativa", selectedUnitLog);
					nextScreen.putExtra("canalDistrib", ModificareComanda.isComandaDistrib ? "10" : "20");

				} else {
					nextScreen = new Intent(getApplicationContext(), SelectArtModificareCmd.class);

				}
				startActivity(nextScreen);

			} else {
				Toast.makeText(getApplicationContext(), "Selectati o comanda!", Toast.LENGTH_SHORT).show();
			}

			return true;

		case 1:

			if (codClientVar.length() > 0) {

				Intent nextScreenLivr = null;
				if (isUserCV() || isComandaGed()) {
					nextScreenLivr = new Intent(getApplicationContext(), SelectAdrLivrCmdGed.class);
					nextScreenLivr.putExtra("codClient", selectedClientCode);

				} else {
					nextScreenLivr = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
				}

				selectedCmdAdrLivr = selectedCmd;
				startActivity(nextScreenLivr);

			} else {
				Toast.makeText(getApplicationContext(), "Selectati o comanda!", Toast.LENGTH_SHORT).show();
			}

			return true;

		case android.R.id.home:

			articoleComanda = "";
			numeClientVar = "";
			codClientVar = "";

			UserInfo.getInstance().setParentScreen("");
			clearAllData();

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	boolean isUserCV() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")
				|| UserInfo.getInstance().getTipUserSap().equals("KA3");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public void onResume() {

		super.onResume();
		checkStaticVars();

		if (DateLivrare.getInstance().getDateLivrare().length() > 0) {

			DateLivrare dateLivrareInstance = DateLivrare.getInstance();

			textJudet.setText(dateLivrareInstance.getNumeJudet());
			textOras.setText(dateLivrareInstance.getOras());
			textAdrLivr.setText(dateLivrareInstance.getStrada());
			textPersContact.setText(dateLivrareInstance.getPersContact());
			textTelefon.setText(dateLivrareInstance.getNrTel());

			// ***************cantar
			if (dateLivrareInstance.getCantar().equals("NU"))
				textCantar.setText("Nu");
			else
				textCantar.setText("Da");
			// ***************sf. cantar

			// ***************tip reducere
			if (dateLivrareInstance.getRedSeparat().equals(" ")) {
				textFactRed.setText("1 factura (red. in pret)");
			}
			if (dateLivrareInstance.getRedSeparat().equals("X")) {
				textFactRed.setText("2 facturi");
			}
			if (dateLivrareInstance.getRedSeparat().equals("R")) {
				textFactRed.setText("1 factura (red. separat)");
			}
			// ***************sf. tip reducere

			// ***************tip plata
			String localTipPlata = "";
			if (dateLivrareInstance.getTipPlata().equals("B")) {
				localTipPlata = "Bilet la ordin";
			}
			if (dateLivrareInstance.getTipPlata().equals("C")) {
				localTipPlata = "Cec";
			}
			if (dateLivrareInstance.getTipPlata().equals("E")) {
				localTipPlata = "Plata in numerar";
			}
			if (dateLivrareInstance.getTipPlata().equals("L")) {
				localTipPlata = "Plata interna buget-trezorerie";
			}
			if (dateLivrareInstance.getTipPlata().equals("O")) {
				localTipPlata = "Ordin de plata";
			}
			if (dateLivrareInstance.getTipPlata().equals("U")) {
				localTipPlata = "Plata interna-alte institutii";
			}
			if (dateLivrareInstance.getTipPlata().equals("W")) {
				localTipPlata = "Plata in strainatate-banci";
			}

			textTipPlata.setText(localTipPlata);
			// ***************sf. tip plata

			// ***************tip transport
			String tipTransp = "";
			if (dateLivrareInstance.getTransport().equals("TCLI")) {
				tipTransp = "Transport client";
			}
			if (dateLivrareInstance.getTransport().equals("TRAP")) {
				tipTransp = "Transport Arabesque";
			}
			if (dateLivrareInstance.getTransport().equals("TERT")) {
				tipTransp = "Transport terti";
			}

			textTransport.setText(tipTransp);
			// ***************sf. tip transport

			clientFinalStr = codClientVar + "#" + dateLivrareInstance.getStrada() + "^" + dateLivrareInstance.getOras() + "^"
					+ dateLivrareInstance.getCodJudet() + "#" + dateLivrareInstance.getPersContact() + "#" + dateLivrareInstance.getNrTel() + "#"
					+ dateLivrareInstance.getCantar() + "#" + dateLivrareInstance.getTipPlata() + "#" + dateLivrareInstance.getTransport() + "#";

		}

	}

	private double getTotalComanda() {

		double localTotalComanda = 0;

		if (listArticoleComanda != null) {
			for (ArticolComanda articol : listArticoleComanda) {

				localTotalComanda += articol.getPretUnit() * articol.getCantitate();
			}
		}

		return localTotalComanda;

	}

	public void calculPondereB() {

		double totalArtB = 0, procentB = 0, localTotalComanda = 0;

		for (ArticolComanda articol : listArticoleComanda) {

			if (articol.getTipArt().equalsIgnoreCase("B"))
				totalArtB += articol.getPret();

			localTotalComanda += articol.getPretUnit() * articol.getCantitate();
		}

		if (localTotalComanda == 0) {
			procentB = 0;
		} else {
			procentB = totalArtB / localTotalComanda * 100;
		}

		textPondereB.setText(String.format("%.02f", procentB) + "%");
		textTotalCmd.setText(String.format("%.02f", localTotalComanda));

	}

	public void addListenerSaveCmdBtn() {
		salveazaComandaBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				try {

					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						if (listArticoleComanda.size() == 0) {
							Toast.makeText(getApplicationContext(), "Comanda nu contine articole!", Toast.LENGTH_SHORT).show();
							return false;
						}

						if (!selectedCmdAdrLivr.equals(selectedCmd)) {
							Toast.makeText(getApplicationContext(), "Verificati datele de livrare!", Toast.LENGTH_SHORT).show();
							return false;
						}

						if (!isConditiiAcceptate()) {
							Toast.makeText(getApplicationContext(), "Preluati toate conditiile!", Toast.LENGTH_SHORT).show();
							return false;
						}

						if (!isCommandaOkToSave()) {
							Toast.makeText(getApplicationContext(), "Comanda nu are toate aprobarile!", Toast.LENGTH_SHORT).show();
							return false;
						}

						mProgress.setVisibility(View.VISIBLE);
						mProgress.setProgress(0);
						progressVal = 0;
						myTimer = new Timer();
						myTimer.schedule(new UpdateProgress(), 40, 15);

						return true;

					case MotionEvent.ACTION_UP:

						if (listArticoleComanda.size() > 0 && myTimer != null) {
							myTimer.cancel();
							mProgress.setVisibility(View.INVISIBLE);
						}
						return true;

					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

				return false;
			}

		});

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (mProgress.getProgress() == 50) {
				logonHandler.post(new Runnable() {
					public void run() {

						DateLivrare dateLivrareInstance = DateLivrare.getInstance();

						prepareArtForDelivery();

						if (dateLivrareInstance.getTipPlata().equals("E") && totalComanda > 5000 && tipClientVar.equals("PJ")) {
							Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de 5000 RON!", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						clientFinalStr += String.valueOf(totalComanda) + "#" + UserInfo.getInstance().getUnitLog() + "#"
								+ UserInfo.getInstance().getCod() + "#-1#";

						dateLivrareInstance.setTotalComanda(String.valueOf(totalComanda));
						dateLivrareInstance.setCodAgent(UserInfo.getInstance().getCod());
						dateLivrareInstance.setFactRed("-1");

						String alerteKA = globalAlertSDKA + "!" + globalAlertDVKA;

						// aprobare adr. livrare noua doar pentru agenti
						if (!UserInfo.getInstance().getTipAcces().equals("27")) {
							if (dateLivrareInstance.isAdrLivrNoua())
								comandaBlocata = "1";
						}

						String localRedSeparat = codTipReducere.equals("-1") ? dateLivrareInstance.getRedSeparat() : codTipReducere;

						if (isComandaGed())
							localRedSeparat = "NU";

						if (globalSubCmp.equals("1"))
							localRedSeparat = "X";

						NumberFormat nf3 = NumberFormat.getInstance();
						nf3.setMinimumFractionDigits(2);
						nf3.setMaximumFractionDigits(2);

						if (dateLivrareInstance.getObsPlata().equals("SO") && dateLivrareInstance.getTipPlata().equals("E")) {
							if (!dateLivrareInstance.isValIncModif()) {
								dateLivrareInstance.setValoareIncasare(nf3.format(ModificareComanda.totalComanda * 1.24));
							}
						}

						String localUserSite = " ", userSiteMail = " ", isValIncModif = " ", codJ = "", adrLivrareGED = "";

						if (dateLivrareInstance.isValIncModif())
							isValIncModif = "X";

						Comanda comanda = new Comanda();
						comanda.setCodClient(selectedClientCode);
						comanda.setComandaBlocata(comandaBlocata);
						comanda.setNrCmdSap(selectedCmdSAP);
						comanda.setConditieID(conditieID);

						comanda.setAlerteKA(alerteKA);
						comanda.setFactRedSeparat(localRedSeparat);
						comanda.setFilialaAlternativa(ModificareComanda.filialaAlternativaM);
						comanda.setUserSite(localUserSite);
						comanda.setUserSiteMail(userSiteMail);
						comanda.setIsValIncModif(isValIncModif);
						comanda.setCodJ(codJ);
						comanda.setAdresaLivrareGed(adrLivrareGED);
						comanda.setNumeClient(dateLivrareInstance.getNumeClient());
						comanda.setCnpClient(dateLivrareInstance.getCnpClient());

						comandaJson = serializeComanda(comanda);

						performSaveCmd();

					}
				});

				myTimer.cancel();
			} else {
				mProgress.setProgress(progressVal);
			}

		}
	}

	private void performSaveCmd() {
		try {

			// comanda cv cu conditii se salveaza direct
			if (isComandaGed() && UserInfo.getInstance().getTipUser().equals("CV") && !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
				alertSD = false;
				alertDV = false;
			}

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "AV";
			if (UserInfo.getInstance().getTipAcces().equals("27"))
				tipUser = "KA";
			else if (isComandaGed())
				tipUser = "CV";
			else
				tipUser = UserInfo.getInstance().getTipUser();

			params.put("comanda", " ");
			params.put("tipUser", tipUser);
			params.put("JSONArt", serializeArticole());
			params.put("JSONComanda", comandaJson);
			params.put("JSONDateLivrare", serializeDateLivrare());
			params.put("alertSD", String.valueOf(alertSD));
			params.put("alertDV", String.valueOf(alertDV));

			operatiiComenzi.salveazaComandaDistrib(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private boolean isComandaGed() {

		String tempDistribUL = InfoStrings.getDistribUnitLog(selectedUnitLog);

		if (InfoStrings.getClientGenericGed(tempDistribUL, "PF").equals(selectedClientCode)
				|| InfoStrings.getClientGenericGed(tempDistribUL, "PJ").equals(selectedClientCode))
			return true;
		else
			return false;
	}

	public String prepareArtForDelivery() {
		String retVal = "";

		String[] tokPret;
		double valCondPret = 0;
		double taxaVerde = 0;
		boolean hasTaxaVerde = false;

		alertSD = false;
		alertDV = false;
		comandaBlocata = "0";
		globalAlertSDKA = "?";
		globalAlertDVKA = "?";
		globalSubCmp = "0";

		totalComanda = 0;

		Collections.sort(listArticoleComanda, ArticolComanda.DepartComparator);
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
		ArticolComanda articolComanda = null;

		while (iterator.hasNext()) {

			articolComanda = iterator.next();

			if (articolComanda.getInfoArticol().contains(";")) {
				String[] condPret = articolComanda.getInfoArticol().split(";");

				for (int ii = 0; ii < condPret.length; ii++) {
					tokPret = condPret[ii].split(":");
					valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
					if (valCondPret != 0) {
						if (tokPret[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
							taxaVerde += valCondPret;
						}

					}
				}
			}

			if (articolComanda.getAlteValori().toString().equals("1"))
				globalSubCmp = "1";

			if (isUserExceptie()) {
				if (articolComanda.getTipAlert().contains("SD")) {
					comandaBlocata = "1";
					alertSD = true;

					if (!globalAlertSDKA.contains(articolComanda.getDepart()))
						globalAlertSDKA += articolComanda.getDepart() + "?";
				}

				if (articolComanda.getTipAlert().contains("DV")) {
					comandaBlocata = "1";
					alertDV = true;

					if (!globalAlertDVKA.contains(articolComanda.getDepart()))
						globalAlertDVKA += articolComanda.getDepart() + "?";
				}

			}

			if (isUserCV()) {
				comandaBlocata = "0";
				alertSD = false;
				alertDV = false;

			}

			if (articolComanda.getCodArticol().equals("00000000")) {
				hasTaxaVerde = true;
			}

			totalComanda += articolComanda.getPretUnit() * articolComanda.getCantitate();

		}

		if (ModificareComanda.isComandaDistrib) {
			if (hasTaxaVerde) {
				updateTaxaVerde(taxaVerde);
			} else {
				if (taxaVerde > 0)
					addTaxaVerde(taxaVerde);
			}
		}

		return retVal;
	}

	// userul este agent, sd sau ka
	boolean isUserExceptie() {
		return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
				|| UserInfo.getInstance().getTipAcces().equals("27");
	}

	private void updateTaxaVerde(double taxaVerde) {
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
		ArticolComanda articol = null;

		while (iterator.hasNext()) {
			articol = iterator.next();

			if (articol.getCodArticol().equals("00000000")) {
				articol.setTaxaVerde(taxaVerde);
				break;
			}
		}
	}

	private void addTaxaVerde(double taxaVerde) {

		ArticolComanda articol = new ArticolComanda();
		articol.setCodArticol("000000000000000000");
		articol.setCantitate(1.0);
		articol.setDepozit(listArticoleComanda.get(0).getDepozit());
		articol.setPretUnit(taxaVerde);
		articol.setProcent(0);
		articol.setUm("BUC");
		articol.setProcentFact(0);
		articol.setConditie(false);
		articol.setDiscClient(0);
		articol.setProcAprob(0);
		articol.setMultiplu(1);
		articol.setPret(taxaVerde);
		articol.setInfoArticol(" ");
		articol.setCantUmb(1);
		articol.setUmb("BUC");
		articol.setDepart(listArticoleComanda.get(0).getDepart());
		listArticoleComanda.add(articol);

	}

	private String serializeArticole() {
		JSONArray myArray = new JSONArray();

		JSONObject obj = null;

		try {
			for (int i = 0; i < listArticoleComanda.size(); i++) {

				if (listArticoleComanda.get(i).getStatus() != null && listArticoleComanda.get(i).getStatus().toLowerCase().contains("respins"))
					continue;

				obj = new JSONObject();
				obj.put("codArticol", listArticoleComanda.get(i).getCodArticol());
				obj.put("cantitate", listArticoleComanda.get(i).getCantitate());
				obj.put("depozit", listArticoleComanda.get(i).getDepozit());
				obj.put("pretUnit", listArticoleComanda.get(i).getPretUnit());
				obj.put("procent", listArticoleComanda.get(i).getProcent());
				obj.put("um", listArticoleComanda.get(i).getUm());
				obj.put("procentFact", listArticoleComanda.get(i).getProcentFact());
				obj.put("conditie", listArticoleComanda.get(i).getConditie());
				obj.put("discClient", listArticoleComanda.get(i).getDiscClient());
				obj.put("procAprob", listArticoleComanda.get(i).getProcAprob());
				obj.put("multiplu", listArticoleComanda.get(i).getMultiplu());
				obj.put("pret", listArticoleComanda.get(i).getPret());
				obj.put("infoArticol", listArticoleComanda.get(i).getInfoArticol());
				obj.put("cantUmb", listArticoleComanda.get(i).getCantUmb());
				obj.put("Umb", listArticoleComanda.get(i).getUmb());
				obj.put("depart", listArticoleComanda.get(i).getDepart());
				obj.put("ponderare", listArticoleComanda.get(i).getPonderare());
				myArray.put(obj);
			}
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

		serializedResult = myArray.toString();

		return serializedResult;

	}

	private String serializeComanda(Comanda comanda) {
		JSONObject obj = new JSONObject();

		try {
			obj.put("codClient", comanda.getCodClient());
			obj.put("numeClient", comanda.getNumeClient());
			obj.put("persoanaContact", comanda.getPersoanaContact());
			obj.put("telefon", comanda.getTelefon());
			obj.put("cantarire", comanda.getCantarire());
			obj.put("metodaPlata", comanda.getMetodaPlata());
			obj.put("tipTransport", comanda.getTipTransport());
			obj.put("comandaBlocata", comanda.getComandaBlocata());
			obj.put("nrCmdSap", comanda.getNrCmdSap());
			obj.put("alerteKA", comanda.getAlerteKA());
			obj.put("factRedSeparat", comanda.getFactRedSeparat());
			obj.put("filialaAlternativa", comanda.getFilialaAlternativa());
			obj.put("userSite", comanda.getUserSite());
			obj.put("userSiteMail", comanda.getUserSiteMail());
			obj.put("isValIncModif", comanda.getIsValIncModif());
			obj.put("codJ", comanda.getCodJ());
			obj.put("cnpClient", comanda.getCnpClient());
			obj.put("adresaLivrareGed", comanda.getAdresaLivrareGed());
			obj.put("adresaLivrare", comanda.getAdresaLivrare());
			obj.put("valoareIncasare", comanda.getValoareIncasare());
			obj.put("conditieID", comanda.getConditieID());
			obj.put("canalDistrib", ModificareComanda.isComandaDistrib ? "10" : "20");
			obj.put("valTransportSap", "0");

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return obj.toString();
	}

	private String serializeDateLivrare() {

		JSONObject obj = new JSONObject();

		try {

			obj.put("codJudet", DateLivrare.getInstance().getCodJudet());
			obj.put("numeJudet", DateLivrare.getInstance().getNumeJudet());
			obj.put("Oras", DateLivrare.getInstance().getOras());
			obj.put("Strada", DateLivrare.getInstance().getStrada());
			obj.put("persContact", DateLivrare.getInstance().getPersContact());
			obj.put("nrTel", DateLivrare.getInstance().getNrTel());
			obj.put("redSeparat", DateLivrare.getInstance().getRedSeparat());
			obj.put("Cantar", DateLivrare.getInstance().getCantar());
			obj.put("tipPlata", DateLivrare.getInstance().getTipPlata());
			obj.put("Transport", DateLivrare.getInstance().getTransport());
			obj.put("dateLivrare", DateLivrare.getInstance().getDateLivrare());
			obj.put("termenPlata", DateLivrare.getInstance().getTermenPlata());
			obj.put("obsLivrare", DateLivrare.getInstance().getObsLivrare());
			obj.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
			obj.put("adrLivrNoua", DateLivrare.getInstance().isAdrLivrNoua());
			obj.put("tipDocInsotitor", DateLivrare.getInstance().getTipDocInsotitor());
			obj.put("obsPlata", DateLivrare.getInstance().getObsPlata());
			obj.put("addrNumber", DateLivrare.getInstance().getAddrNumber());
			obj.put("valoareIncasare", DateLivrare.getInstance().getValoareIncasare());
			obj.put("isValIncModif", DateLivrare.getInstance().isValIncModif());
			obj.put("mail", DateLivrare.getInstance().getMail());
			obj.put("totalComanda", getTotalComanda());
			obj.put("unitLog", DateLivrare.getInstance().getUnitLog());
			obj.put("codAgent", DateLivrare.getInstance().getCodAgent());
			obj.put("idObiectiv", DateLivrare.getInstance().getIdObiectiv());
			obj.put("isAdresaObiectiv", DateLivrare.getInstance().isAdresaObiectiv() );

			if (isComandaGed())
				obj.put("factRed", "NU");
			else
				obj.put("factRed", codTipReducere.equals("-1") ? DateLivrare.getInstance().getFactRed() : codTipReducere);
			obj.put("macara", DateLivrare.getInstance().isMasinaMacara() ? "X" : " ");

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return obj.toString();

	}

	private boolean isConditiiAcceptate() {
		boolean isConditiiAcceptate = true;

		for (int i = 0; i < listArticoleComanda.size(); i++) {

			if (listArticoleComanda.get(i).hasConditii()) {
				isConditiiAcceptate = false;
				break;
			}
		}

		return isConditiiAcceptate;

	}

	private boolean isCommandaOkToSave() {
		boolean isOkToSave = true;
		String aprobariNecesare = comandaSelectata.getAprobariNecesare();
		String aprobariPrimite = comandaSelectata.getAprobariPrimite();
		String conditiiImpuse = comandaSelectata.getConditiiImpuse();

		for (int i = 0; i < listArticoleComanda.size(); i++) {

			if (aprobariNecesare.contains(listArticoleComanda.get(i).getDepartSintetic())) {
				if (!aprobariPrimite.contains(listArticoleComanda.get(i).getDepartSintetic())) {

					if (conditiiImpuse.contains(listArticoleComanda.get(i).getDepartSintetic()) && !listArticoleComanda.get(i).hasConditii()) {
						continue;
					} else {
						isOkToSave = false;
						break;
					}

				}

			}

		}

		return isOkToSave;
	}

	private void saveCmdStatus(String saveResponse) {
		if (!saveResponse.equals("-1")) {
			try {

				if (!saveResponse.equals("9")) {
					if (alertSD) {
						sendMailAlert(0);
					}
					if (alertDV) {

					}

					if (UserInfo.getInstance().getTipAcces().equals("27"))// alerta
																			// director
																			// ka
					{
						// este nevoie de cel putin o aprobare
						if (globalAlertSDKA.contains("0") || globalAlertDVKA.contains("0")) {
							sendMailAlert(3);
						}
					}

				}

				Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_LONG).show();

				clearAllData();
				loadListComenzi();

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
		}
	}

	public void sendMailAlert(int tip) {
		try {

			if (tip == 1) // alerta dv
			{
				mailAlertTipDest = "2";
			}

			if (tip == 3) // alerta director KA
			{
				mailAlertTipDest = "4";
			}

			if (tip == 1 || tip == 3) {
				HashMap<String, String> params = new HashMap<String, String>();

				params.put("ul", UserInfo.getInstance().getUnitLog());
				params.put("depart", UserInfo.getInstance().getCodDepart());
				params.put("dest", mailAlertTipDest); // tip alert
				params.put("agent", UserInfo.getInstance().getNume()); // agent
				params.put("clnt", numeClientVar); //
				params.put("suma", String.format("%.02f", totalComanda)); //

				AsyncTaskWSCall call = new AsyncTaskWSCall(this, "sendMailAlert", params);
				call.getCallResults();

			}

		} catch (Exception ex) {
			Log.e("Error", ex.toString());

		}
	}

	private void clearAllData() {

		DateLivrare.getInstance().resetAll();

		articoleComanda = "";
		numeClientVar = "";
		codClientVar = "";
		selectedCmd = "";
		totalComanda = 0;
		codTipReducere = "-1";

		try {
			ListaArticoleComandaGed.getInstance().clearArticoleComanda();
			ListaArticoleComandaGed.getInstance().deleteObserver(this);

			ListaArticoleModificareComanda.getInstance().clearArticoleComanda();
			ListaArticoleModificareComanda.getInstance().deleteObserver(this);
		} catch (Exception ex) {

		}

	}

	public void addListenerDelCmdBtn() {
		stergeComandaBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isUserCV()) {

					if (!isConditiiAcceptate()) {
						Toast.makeText(getApplicationContext(), "Preluati toate conditiile!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (!isCommandaOkToSave()) {
						Toast.makeText(getApplicationContext(), "Comanda nu are toate aprobarile!", Toast.LENGTH_SHORT).show();
						return;
					}

				}

				showConfirmationAlert();

			}
		});

	}

	public void showConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Stergeti comanda?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				idOperatieComanda = 3;
				opereazaComanda();

			}
		}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void opereazaComanda() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

			params.put("nrCmd", selectedCmd);
			params.put("nrCmdSAP", selectedCmdSAP);
			params.put("tipOp", String.valueOf(idOperatieComanda));
			params.put("codUser", fullCode);

			operatiiComenzi.opereazaComanda(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void loadListComenzi() {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("tipCmd", "1");
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipUser", UserInfo.getInstance().getTipUser());

		displayCmdDetails(false);
		operatiiComenzi.getListComenzi(params);

	}

	private void getArticoleComanda() {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("nrCmd", selectedCmd);
		params.put("afisCond", "1");
		params.put("tipUser", UserInfo.getInstance().getTipUser());

		operatiiComenzi.getArticoleComandaJSON(params);

	}

	private void afiseazaArticoleComanda(BeanArticoleAfisare articoleComanda) {

		DateLivrareAfisare dateLivrare = articoleComanda.getDateLivrare();

		DateLivrare.getInstance().setDateLivrareAfisare(dateLivrare);

		listArticoleComanda = articoleComanda.getListArticole();

		BeanConditii conditiiComanda = articoleComanda.getConditii();
		afisConditiiHeader(conditiiComanda.getHeader());

		conditieID = String.valueOf(conditiiComanda.getHeader().getId());

		conditiiComandaArticole = conditiiComanda.getArticole();

		if (isUserCV() || isComandaGed()) {
			ListaArticoleComandaGed.getInstance().setListaArticole(listArticoleComanda);
			ListaArticoleComandaGed.getInstance().addObserver(this);
			getPretTransport();
		} else {
			ListaArticoleModificareComanda.getInstance().setListaArticole(listArticoleComanda);
		}

		ListaArticoleComandaGed.getInstance().setConditiiArticole(conditiiComandaArticole);

		adapterArticole = new ArticolModificareAdapter(this, listArticoleComanda, conditiiComandaArticole, comandaSelectata);

		adapterArticole.setArticolModificareListener(this);
		listViewArticole.setAdapter(adapterArticole);

		textJudet.setText(dateLivrare.getNumeJudet());
		textOras.setText(dateLivrare.getOras());
		textAdrLivr.setText(dateLivrare.getDateLivrare());
		textPersContact.setText(dateLivrare.getPersContact());
		textTelefon.setText(dateLivrare.getNrTel());
		textCantar.setText(UtilsGeneral.getTipCantarire(dateLivrare.getCantar()));
		textTipPlata.setText(UtilsGeneral.getDescTipPlata(dateLivrare.getTipPlata()));
		textTransport.setText(UtilsGeneral.getDescTipTransport(dateLivrare.getTransport()));
		textFactRed.setText(UtilsGeneral.getTipReducere(dateLivrare.getRedSeparat()));

		tipClientVar = dateLivrare.getTipPersClient();

		calculPondereB();

		if (listArticoleComanda.get(0).getUnitLogAlt().equals("NN10"))
			filialaAlternativaM = UserInfo.getInstance().getUnitLog();
		else
			filialaAlternativaM = listArticoleComanda.get(0).getUnitLogAlt();

		displayCmdDetails(true);

		adapterArticole.notifyDataSetChanged();

	}

	private void getPretTransport() {

		for (int i = 0; i < listArticoleComanda.size(); i++) {
			if (listArticoleComanda.get(i).getNumeArticol().toLowerCase().contains("servicii")
					&& listArticoleComanda.get(i).getNumeArticol().toLowerCase().contains("transport")) {
				DateLivrare.getInstance().setValTransport(listArticoleComanda.get(i).getPretUnit());
				break;
			}

		}

	}

	private void afisConditiiHeader(BeanConditiiHeader conditiiHeader) {

		layoutConditiiHeader.setVisibility(View.GONE);
		boolean isHeaderVisible = false;
		NumberFormat nf2 = new DecimalFormat("#0.00");

		if (conditiiHeader.getConditiiCalit() > 0) {
			textCondProcB.setText(nf2.format(conditiiHeader.getConditiiCalit()));
			layoutCondProcB.setVisibility(View.VISIBLE);
			isHeaderVisible = true;
		} else {
			layoutCondProcB.setVisibility(View.GONE);
		}

		if (conditiiHeader.getNrFact() > 0) {
			textCondNrFacturi.setText(getDescNrFacturi(conditiiHeader.getNrFact()));
			layoutCondNrFact.setVisibility(View.VISIBLE);
			isHeaderVisible = true;
		} else {
			layoutCondNrFact.setVisibility(View.GONE);
		}

		if (!conditiiHeader.getObservatii().equals("null")) {
			textCondComentarii.setText(conditiiHeader.getObservatii());
			layoutCondObs.setVisibility(View.VISIBLE);
			isHeaderVisible = true;
		} else {
			layoutCondObs.setVisibility(View.GONE);
		}

		if (isHeaderVisible)
			layoutConditiiHeader.setVisibility(View.VISIBLE);

	}

	private String getDescNrFacturi(int codNrFacturi) {
		String descNrFacturi = "";
		switch (codNrFacturi) {
		case 1:
			descNrFacturi = "1 fact (red in pret)";
			codTipReducere = " ";
			break;
		case 2:
			descNrFacturi = "2 facturi";
			codTipReducere = "X";
			break;
		case 3:
			descNrFacturi = "1 fact (red separat)";
			codTipReducere = "R";
			break;

		}

		return descNrFacturi;

	}

	private void afiseazaListaComenzi(List<BeanComandaCreata> listComenzi) {

		if (listComenzi.size() > 0) {
			ComandaModificareAdapter adapter = new ComandaModificareAdapter(listComenzi, this);
			spinnerComenzi.setAdapter(adapter);
			displayComenziControl(true);

		} else {
			displayComenziControl(false);
		}

	}

	private void displayComenziControl(boolean isVisible) {
		if (isVisible) {
			spinnerComenzi.setVisibility(View.VISIBLE);
			salveazaComandaBtn.setVisibility(View.VISIBLE);
			stergeComandaBtn.setVisibility(View.VISIBLE);

		} else {
			spinnerComenzi.setVisibility(View.INVISIBLE);
			salveazaComandaBtn.setVisibility(View.INVISIBLE);
			stergeComandaBtn.setVisibility(View.INVISIBLE);

		}

	}

	public void addListenerListArtModif() {
		listViewArticole.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelPos = position;
				return false;

			}
		});

		listViewArticole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelPos = position;

				if ((listViewArticole.getFirstVisiblePosition() == listViewSelPos)
						|| (listViewArticole.getFirstVisiblePosition() + 1 == listViewSelPos)) {
					listViewArticole.smoothScrollToPositionFromTop(listViewSelPos - 1, 0);
				}

				if ((listViewArticole.getLastVisiblePosition() == listViewSelPos)
						|| (listViewArticole.getLastVisiblePosition() - 1 == listViewSelPos)) {
					listViewArticole.smoothScrollToPositionFromTop(listViewArticole.getFirstVisiblePosition() + 1, 0);
				}

			}
		});

	}

	private void checkStaticVars() {

		String locLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

		if (!locLang.toLowerCase(Locale.getDefault()).equals("en")) {

			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}

		// restart app la idle
		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}

	public double calculTaxaVerde() {

		double totalTaxaVerde = 0;

		try {

			String[] tokVal;
			Double valCondPret = 0.0;

			if (ModificareComanda.isComandaDistrib) {

				prepareArtForDelivery();

				for (int i = 0; i < objArticol.length; i++) {
					if (objArticol[i] != null) {
						if (!objArticol[i].getCodArticol().equals("-1")) {

							if (objArticol[i].getInfoArticol().contains(";")) {
								String[] tokInfoArt = objArticol[i].getInfoArticol().split(";");

								for (int ii = 0; ii < tokInfoArt.length; ii++) {
									tokVal = tokInfoArt[ii].split(":");
									valCondPret = Double.valueOf(tokVal[1].replace(',', '.').trim());
									if (valCondPret != 0) {
										if (tokVal[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
											totalTaxaVerde += valCondPret;
										}

									}

								}// for
							}// if

						}
					}
				}// sf. for

				totalComanda = getTotalComanda();
				totalComanda += totalTaxaVerde;

				textTotalCmd.setText(String.format("%.02f", totalComanda));

				if (totalTaxaVerde > 0) {
					layoutTaxaVerde.setVisibility(View.VISIBLE);
					textTaxaVerde.setText(String.valueOf(totalTaxaVerde) + ")");
				}

			}// sf. if

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return totalTaxaVerde;

	}

	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		clearAllData();

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	public void onTaskComplete(String methodName, Object result) {

		if (methodName.equals("sendMailAlert")) {
			clearAllData();
		}

	}

	private void displayCmdDetails(boolean isVisible) {
		if (isVisible) {
			layoutDetaliiCmd.setVisibility(View.VISIBLE);
			listViewArticole.setVisibility(View.VISIBLE);
			stergeComandaBtn.setVisibility(View.VISIBLE);
			salveazaComandaBtn.setVisibility(View.VISIBLE);
		} else {
			layoutDetaliiCmd.setVisibility(View.INVISIBLE);
			listViewArticole.setVisibility(View.INVISIBLE);
			stergeComandaBtn.setVisibility(View.INVISIBLE);
			salveazaComandaBtn.setVisibility(View.INVISIBLE);
		}

	}

	private void afiseazaArticoleComanda(BeanComandaCreata comanda) {

		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");

		textTotalCmd.setText("0.00");

		comandaSelectata = comanda;

		selectedCmd = comanda.getId();

		selectedCmdSAP = comanda.getCmdSap();

		totalComanda = getTotalComanda();

		textTotalCmd.setText(String.format("%.02f", totalComanda));

		selectedClientCode = getCodClient(comanda);

		selectedUnitLog = comanda.getFiliala();

		if (selectedUnitLog.substring(2, 3).equals("1"))
			isComandaDistrib = true;
		else
			isComandaDistrib = false;

		codClientVar = comanda.getCodClient();
		numeClientVar = comanda.getNumeClient();

		if (!comanda.getDocInsotitor().equals("-1")) {
			DateLivrare.getInstance().setTipDocInsotitor(comanda.getDocInsotitor());
		} else {
			DateLivrare.getInstance().setTipDocInsotitor("1");
		}

		getArticoleComanda();

		textTipPlata.setVisibility(View.VISIBLE);
		textAdrLivr.setVisibility(View.VISIBLE);
		textPersContact.setVisibility(View.VISIBLE);
		textTelefon.setVisibility(View.VISIBLE);
		textCantar.setVisibility(View.VISIBLE);
		textTransport.setVisibility(View.VISIBLE);

	}

	private String getCodClient(BeanComandaCreata comanda) {

		return UtilsFormatting.isNumeric(comanda.getCodClient()) ? comanda.getCodClient() : comanda.getCodClientGenericGed();

	}

	private void addListenerSpinnerComenzi() {
		spinnerComenzi.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				displayCmdDetails(false);
				afiseazaArticoleComanda(listComenzi.get(position));

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			this.listComenzi = (List<BeanComandaCreata>) result;
			afiseazaListaComenzi(this.listComenzi);
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			afiseazaArticoleComanda(operatiiComenzi.deserializeArticoleComanda((String) result));
			break;
		case OPERATIE_COMANDA:
			loadListComenzi();
			break;
		case SALVEAZA_COMANDA_DISTRIB:
			saveCmdStatus((String) result);
			break;
		default:
			break;
		}

	}

	public void articolModificat() {
		calculPondereB();
	}

	public void update(Observable observable, Object data) {
		if (observable instanceof ListaArticoleModificareComanda) {
			listArticoleComanda = ListaArticoleModificareComanda.getInstance().getListArticoleComanda();
			conditiiComandaArticole = ListaArticoleModificareComanda.getInstance().getConditiiArticole();
			adapterArticole.setListArticole(listArticoleComanda);
			adapterArticole.notifyDataSetChanged();

		}

		if (observable instanceof ListaArticoleComandaGed) {
			listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
			conditiiComandaArticole = ListaArticoleComandaGed.getInstance().getConditiiArticole();
			adapterArticole.setListArticole(listArticoleComanda);
			adapterArticole.notifyDataSetChanged();
		}

	}
}