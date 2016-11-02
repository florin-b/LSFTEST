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

import listeners.ArtComplDialogListener;
import listeners.AsyncTaskListener;
import listeners.ComenziDAOListener;
import listeners.PaletAlertListener;
import listeners.PretTransportDialogListener;
import listeners.ValoareNegociataDialogListener;
import model.ArticolComanda;
import model.Comanda;
import model.ComenziDAO;
import model.Constants;
import model.DateLivrare;
import model.InfoStrings;
import model.ListaArticoleComanda;
import model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapters.ArticoleCreareAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
import dialogs.ArtComplDialog;
import dialogs.PaletAlertDialog;
import dialogs.PretTransportDialog;
import dialogs.ValoareNegociataDialog;
import enums.EnumComenziDAO;
import enums.EnumDaNuOpt;

public class CreareComanda extends Activity implements AsyncTaskListener, ValoareNegociataDialogListener, ComenziDAOListener, PretTransportDialogListener,
		ArtComplDialogListener, Observer, PaletAlertListener {

	Button stocBtn, clientBtn, articoleBtn, livrareBtn, saveCmdBtn, slideButtonCmd;
	String filiala = "", nume = "", cod = "";
	String codClient = "", numeClient = "";

	private TextView textClient, textTotalCmd, textTipPlata, textAdrLivr, labelTotal, textCursValut;
	private TextView textPersContact, textTelefon, textCantar, textTransport, textNrFact, textLimCrd, textRestCrd, textPondereB, textTaxaVerde;

	private int listViewSelPos = -1;

	private static ArrayList<HashMap<String, String>> arrayListArticole = new ArrayList<HashMap<String, String>>();

	public static String codClientVar = "";
	public static String numeClientVar = "";
	public static String tipClientVar = "";
	public static String articoleComanda = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	public static String unitLog = "";
	public static double totalComanda = 0;
	public static double limitaCredit = 0;
	public static double restCredit = 0;
	public static double cursValutar = 0;
	public static String dateLivrare = "";
	public static String canalDistrib = "";
	public static String codJudet = "";
	public static String judet = "";
	public static String oras = "";
	public static String strada = "";
	public static String persContact = "";
	public static String telefon = "";
	public static String termenPlata = " ";
	public static boolean cmdAngajament = false;
	public static boolean isTotalNegociat = false;
	public static double valNegociat = 0;
	public static String depozitUnic = "";
	public static String filialaAlternativa = UserInfo.getInstance().getUnitLog();

	String serializedResult;

	private boolean alertSD = false, alertDV = false, alertCredite = false;

	private String nrCmdGED = "";

	private String comandaFinalaStr = "", globalAlertSDKA = "", globalAlertDVKA = "", globalSubCmp = "0";
	private String articoleFinaleStr = "";
	private String clientFinalStr = "";
	private String comandaBlocata = "0";
	private ProgressBar mProgress;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler logonHandler = new Handler();
	public static String tipAcces;
	private ListView listArtCmd;
	private SlidingDrawer slidingDrawerCmd;
	Dialog dialog, dlgTransp, dialogValNegociat;
	private String mailAlertTipDest = "";
	private LinearLayout layoutHeader, layoutTaxaVerde;
	private NumberFormat nf3;
	private List<ArticolComanda> listArticole = null;
	private String comandaJson;

	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

			setTheme(R.style.LRTheme);
			setContentView(R.layout.crearecomandaheader);
			checkStaticVars();

			ListaArticoleComanda.getInstance().addObserver(this);

			listArtCmd = (ListView) findViewById(R.id.listArtCmd);

			layoutHeader = (LinearLayout) findViewById(R.id.layoutHeader);
			layoutHeader.setVisibility(View.INVISIBLE);

			layoutTaxaVerde = (LinearLayout) findViewById(R.id.layoutTaxaVerde);
			layoutTaxaVerde.setVisibility(View.INVISIBLE);

			textTaxaVerde = (TextView) findViewById(R.id.textTaxaVerde);
			textTaxaVerde.setText("");

			listArtCmd.setClickable(true);
			addListenerListArtCmd();
			registerForContextMenu(listArtCmd);

			if (arrayListArticole.size() == 0) {
				listArtCmd.setVisibility(View.GONE);
			} else
				listArtCmd.setVisibility(View.VISIBLE);

			slidingDrawerCmd = (SlidingDrawer) findViewById(R.id.slidingDrawerCmd);
			this.slideButtonCmd = (Button) findViewById(R.id.slideButtonCmd);
			slidingDrawerCmd.setVisibility(View.GONE);
			addDrowerEvents();

			this.saveCmdBtn = (Button) findViewById(R.id.saveCmdBtn);
			addListenerSaveCmdBtn();

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Comanda distributie");
			actionBar.setDisplayHomeAsUpEnabled(true);

			textClient = (TextView) findViewById(R.id.textClient);
			textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);

			textTipPlata = (TextView) findViewById(R.id.textTipPlata);
			textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
			textPersContact = (TextView) findViewById(R.id.textPersContact);
			textTelefon = (TextView) findViewById(R.id.textTelefon);
			textCantar = (TextView) findViewById(R.id.textCantar);
			textTransport = (TextView) findViewById(R.id.textTransport);
			labelTotal = (TextView) findViewById(R.id.labelTotal);

			textNrFact = (TextView) findViewById(R.id.textNrFact);

			textPondereB = (TextView) findViewById(R.id.textPondereB);

			textLimCrd = (TextView) findViewById(R.id.textLimCrd);
			textRestCrd = (TextView) findViewById(R.id.textRestCrd);
			textCursValut = (TextView) findViewById(R.id.textCursValut);

			textClient.setVisibility(View.INVISIBLE);
			textTotalCmd.setVisibility(View.INVISIBLE);
			textTipPlata.setVisibility(View.INVISIBLE);
			textAdrLivr.setVisibility(View.INVISIBLE);
			textPersContact.setVisibility(View.INVISIBLE);
			textTelefon.setVisibility(View.INVISIBLE);
			textCantar.setVisibility(View.INVISIBLE);
			textTransport.setVisibility(View.INVISIBLE);
			textNrFact.setVisibility(View.INVISIBLE);

			labelTotal.setVisibility(View.INVISIBLE);

			textLimCrd.setVisibility(View.INVISIBLE);
			textRestCrd.setVisibility(View.INVISIBLE);
			textCursValut.setVisibility(View.INVISIBLE);
			textPondereB.setVisibility(View.INVISIBLE);

			textClient.setText("");
			textTotalCmd.setText("");
			textTipPlata.setText("");
			textAdrLivr.setText("");
			textPersContact.setText("");
			textTelefon.setText("");
			textCantar.setText("");
			textTransport.setText("");
			textNrFact.setText("");

			mProgress = (ProgressBar) findViewById(R.id.progress_bar_savecmd);
			mProgress.setVisibility(View.INVISIBLE);

			nf3 = NumberFormat.getInstance();
			nf3.setMinimumFractionDigits(2);
			nf3.setMaximumFractionDigits(2);

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Client");

		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu2 = menu.add(0, 1, 1, "Articole");

		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu3 = menu.add(0, 2, 2, "Livrare");

		mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu4 = menu.add(0, 3, 3, "Valoare negociata");

		mnu4.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int nrArticole = ListaArticoleComanda.getInstance().getNrArticoleComanda();

		switch (item.getItemId()) {

		case 0:
			if (nrArticole == 0) {

				Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
				startActivity(nextScreen);
			} else {
				Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
			}
			return true;
		case 1:

			if (codClientVar.length() > 0) {
				Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
				startActivity(nextScreen);

			} else {
				Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
			}

			return true;
		case 2:

			if (codClientVar.length() > 0 && nrArticole > 0) {
				Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
				startActivity(nextScreen);
			} else {
				Toast.makeText(getApplicationContext(), "Selectati mai intai clientul si articolele din comanda!", Toast.LENGTH_SHORT).show();
			}

			return true;

		case 3:

			if (CreareComanda.canalDistrib.equals("10")) {
				if (nrArticole == 0) {
					showValNegociatDialogBox();
				} else {
					if (!CreareComanda.isTotalNegociat) {
						Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();

					} else {
						showValNegociatDialogBox();
					}

				}
			} else {
				Toast.makeText(getApplicationContext(), "Doar pe vanzarile din distributie!", Toast.LENGTH_SHORT).show();
			}

			return true;

		case android.R.id.home:

			returnToHome();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void returnToHome() {
		if (numeClientVar.equals("")) {

			resetAllVars();
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage("Datele se vor pierde. Continuati?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					resetAllVars();
					UserInfo.getInstance().setParentScreen("");

					Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
					startActivity(nextScreen);
					finish();
				}
			}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).setTitle("Atentie!").setIcon(R.drawable.warning96);

			AlertDialog alert = builder.create();
			alert.show();

		}

	}

	public void showValNegociatDialogBox() {
		ValoareNegociataDialog valDialog = new ValoareNegociataDialog(this);
		valDialog.setValoareNegociataListener(this);
		valDialog.showDialog(valNegociat, isTotalNegociat);

	}

	public void addDrowerEvents() {
		slidingDrawerCmd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButtonCmd.setBackgroundResource(R.drawable.slideright32);
				listArtCmd.setEnabled(false);
			}
		});

		slidingDrawerCmd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slideButtonCmd.setBackgroundResource(R.drawable.slideleft32);
				listArtCmd.setEnabled(true);
			}
		});

	}

	@Override
	public void onResume() {

		// !! Se modifica din 2 locuri, User si selectArtCmd
		if (!filialaAlternativa.equals("BV90"))
			filialaAlternativa = UserInfo.getInstance().getUnitLog();

		super.onResume();
		checkStaticVars();

		if (numeClientVar.length() > 0) {
			int maxLen = numeClientVar.length();
			if (numeClientVar.length() > 70)
				maxLen = 70;

			textClient.setVisibility(View.VISIBLE);
			textClient.setText(numeClientVar.trim().substring(0, maxLen));
			clientFinalStr = codClientVar;

		} else {
			if (textClient != null)
				textClient.setText("Fara client");
		}

		String varUnitLog = UserInfo.getInstance().getUnitLog();

		if (CreareComanda.canalDistrib.equals("20"))
			varUnitLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

		if (DateLivrare.getInstance().getDateLivrare().length() > 0) {

			textTotalCmd.setVisibility(View.VISIBLE);
			textTipPlata.setVisibility(View.VISIBLE);
			textAdrLivr.setVisibility(View.VISIBLE);
			textPersContact.setVisibility(View.VISIBLE);
			textTelefon.setVisibility(View.VISIBLE);
			textCantar.setVisibility(View.VISIBLE);
			textTransport.setVisibility(View.VISIBLE);
			slidingDrawerCmd.setVisibility(View.VISIBLE);

			textNrFact.setVisibility(View.VISIBLE);
			textLimCrd.setVisibility(View.VISIBLE);
			textRestCrd.setVisibility(View.VISIBLE);
			textCursValut.setVisibility(View.VISIBLE);

			textLimCrd.setText("LC: " + nf3.format(limitaCredit));
			textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

			String[] tokenLivrare = DateLivrare.getInstance().getDateLivrare().split("#");
			String factRed = "NU";
			textAdrLivr.setText(tokenLivrare[0]);
			textPersContact.setText(tokenLivrare[1]);
			textTelefon.setText(tokenLivrare[2]);

			// ********* cantar
			if (tokenLivrare[3].equals("DA")) {
				textCantar.setText("Se cantareste");
				textCantar.setVisibility(View.VISIBLE);

			}
			if (tokenLivrare[3].equals("NU")) {
				textCantar.setVisibility(View.GONE);
			}
			// ********* sf. cantar

			// ********* tip plata
			String varTipPlata = "";

			if (tokenLivrare[4].equals("B")) {
				varTipPlata = "Bilet la ordin";
			}

			if (tokenLivrare[4].equals("C")) {
				varTipPlata = "Cec";
			}

			if (tokenLivrare[4].equals("E")) {
				varTipPlata = "Plata in numerar";
			}

			if (tokenLivrare[4].equals("O")) {
				varTipPlata = "Ordin de plata";
			}

			textTipPlata.setText(varTipPlata);
			// ********* sf. tip plata

			// ********* tip transport
			String varTipTransp = "";
			if (tokenLivrare[5].equals("TRAP")) {
				varTipTransp = "Transport Arabesque";
			}

			if (tokenLivrare[5].equals("TCLI")) {
				varTipTransp = "Transport client";
			}

			textTransport.setText(varTipTransp);
			// ********* sf. tip transport

			textCursValut.setText("\u20ac: " + String.valueOf(CreareComanda.cursValutar));

			DateLivrare dateLivrareInstance = DateLivrare.getInstance();

			if (dateLivrareInstance.getRedSeparat().equals("X")) {
				textNrFact.setText("2 facturi");
			}
			if (dateLivrareInstance.getRedSeparat().equals("R")) {
				textNrFact.setText("1 factura (red. separat)");
			}
			if (dateLivrareInstance.getRedSeparat().equals(" ")) {
				textNrFact.setText("1 factura (red. in pret)");
			}

			clientFinalStr += "#" + dateLivrareInstance.getStrada() + "^" + dateLivrareInstance.getOras() + "^" + dateLivrareInstance.getCodJudet() + "#"
					+ tokenLivrare[1] + "#" + tokenLivrare[2] + "#" + tokenLivrare[3] + "#" + tokenLivrare[4].substring(0, 1).trim() + "#"
					+ tokenLivrare[5].substring(0, 4).trim() + "#" + String.valueOf(totalComanda) + "#" + varUnitLog + "#" + UserInfo.getInstance().getCod()
					+ "#" + factRed + "#";

			dateLivrareInstance.setPersContact(tokenLivrare[1]);
			dateLivrareInstance.setNrTel(tokenLivrare[2]);
			dateLivrareInstance.setCantar(tokenLivrare[3]);
			dateLivrareInstance.setTipPlata(tokenLivrare[4]);
			dateLivrareInstance.setTransport(tokenLivrare[5]);
			dateLivrareInstance.setTotalComanda(String.valueOf(totalComanda));
			dateLivrareInstance.setUnitLog(varUnitLog);
			dateLivrareInstance.setCodAgent(UserInfo.getInstance().getCod());
			dateLivrareInstance.setFactRed(factRed);

		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();

		if (menuItemIndex == 0) // stergere
		{

			if (listViewSelPos >= 0) {
				// recalculare total

				ListaArticoleComanda.getInstance().removeArticolComanda(listViewSelPos);
				displayArticoleComanda();

				newTotal();
				listViewSelPos = -1;

				// pentru comenzile cu total negociat se calculeaza reducerile
				if (CreareComanda.isTotalNegociat) {
					calculProcReducere();
				}

				// recalculare pondere articole B
				calculPondereB();

			}

		}

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listArtCmd) {

			ListView listView = (ListView) v;
			AdapterView.AdapterContextMenuInfo adapterMenu = (AdapterContextMenuInfo) menuInfo;
			ArticolComanda articol = (ArticolComanda) listView.getItemAtPosition(adapterMenu.position);
			menu.setHeaderTitle(articol.getNumeArticol());
			menu.add(Menu.NONE, 0, 0, "Sterge");

		}

	}

	// calcul pondere articole de tip B
	public void calculPondereB() {

		double procentB = ListaArticoleComanda.getInstance().calculPondereB();
		textPondereB.setText("Pondere art. B: " + String.format("%.02f", procentB) + "%");

	}

	public double calculTaxaVerde() {

		double totalTaxaVerde = 0;

		try {

			if (CreareComanda.canalDistrib.equals("10")) {

				totalTaxaVerde = ListaArticoleComanda.getInstance().calculTaxaVerde();

				totalComanda += totalTaxaVerde;
				textTotalCmd.setText(String.format("%.02f", totalComanda));

				if (totalTaxaVerde > 0) {
					layoutTaxaVerde.setVisibility(View.VISIBLE);
					textTaxaVerde.setText(String.valueOf(totalTaxaVerde) + ")");
				}

			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return totalTaxaVerde;

	}

	// calcul procent reducere pentru comenzi cu valoare totala negociata
	public void calculProcReducere() {
		NumberFormat nf2 = new DecimalFormat("#0.00");

		ListaArticoleComanda.getInstance().calculProcReducere();
		displayArticoleComanda();

		totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();
		textTotalCmd.setText(nf2.format(totalComanda));
		textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

	}

	public void addListenerSaveCmdBtn() {

		saveCmdBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				boolean goSaveCmd = false;

				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						int count = ListaArticoleComanda.getInstance().getNrArticoleComanda();

						if (count == 0) {
							Toast.makeText(getApplicationContext(), "Comanda nu contine articole!", Toast.LENGTH_SHORT).show();

							slidingDrawerCmd.animateClose();
						}
						if (textAdrLivr.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(), "Comanda nu contine datele de livrare!", Toast.LENGTH_SHORT).show();

							slidingDrawerCmd.animateClose();
						}

						if (DateLivrare.getInstance().getTermenPlata().trim().equals("")) {
							Toast.makeText(getApplicationContext(), "Verificati datele de livrare!", Toast.LENGTH_SHORT).show();
							return true;
						}

						if (isCondPF10_000()) {
							Toast.makeText(getApplicationContext(), "Valoarea comenzii este mai mare de 10000 RON.", Toast.LENGTH_SHORT).show();
							return true;
						}

						if (count > 0 && !textAdrLivr.getText().toString().equals("")) {

							if (CreareComanda.canalDistrib.equals("10")) {
								if (!cmdAngajament) {
									if (CreareComanda.restCredit >= CreareComanda.totalComanda) {
										goSaveCmd = true;
									} else {
										goSaveCmd = true;
										alertCredite = true;
										Toast.makeText(getApplicationContext(), "Limita de credit a fost depasita!", Toast.LENGTH_SHORT).show();
									}
								} else {
									goSaveCmd = true;
								}
							} else {
								goSaveCmd = true;
							}

							if (goSaveCmd) {
								mProgress.setVisibility(View.VISIBLE);
								mProgress.setProgress(0);
								progressVal = 0;
								myTimer = new Timer();
								myTimer.schedule(new UpdateProgress(), 40, 15);
							}

						}

						return true;

					case MotionEvent.ACTION_UP:
						if (mProgress.getVisibility() == View.VISIBLE) {
							myTimer.cancel();
							mProgress.setVisibility(View.INVISIBLE);
							return true;
						}

					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

				return false;
			}

		});

	}

	private boolean isCondPF10_000() {
		return CreareComanda.tipClientVar.equals("PF") && DateLivrare.getInstance().getTipPlata().equals("E") && totalComanda > 10000;
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (mProgress.getProgress() == 50) {
				logonHandler.post(new Runnable() {
					public void run() {

						DateLivrare dateLivrareInstance = DateLivrare.getInstance();

						if (dateLivrareInstance.getTipPlata().equals("E") && totalComanda > 5000 && CreareComanda.tipClientVar.equals("PJ")) {
							Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de 5000 RON!", Toast.LENGTH_SHORT).show();
							return;
						}

						articoleFinaleStr = prepareArtForDelivery();

						if (articoleFinaleStr.equals("")) {
							Toast.makeText(getApplicationContext(), "Eroare salvare, repetati comanda!", Toast.LENGTH_SHORT).show();
							return;
						}

						String cmdSAP = "-1"; // se foloseste doar la modificare
												// comanda

						String alerteKA = globalAlertSDKA + "!" + globalAlertDVKA;

						// aprobare adr. livrare noua doar pentru agenti
						if (!UserInfo.getInstance().getTipAcces().equals("27")) {
							if (dateLivrareInstance.isAdrLivrNoua())
								comandaBlocata = "1";
						}

						String localRedSeparat = dateLivrareInstance.getRedSeparat();

						if (isReducere())
							localRedSeparat = "X";

						if (dateLivrareInstance.getObsPlata().equals("SO") && dateLivrareInstance.getTipPlata().equals("E")) {
							if (!dateLivrareInstance.isValIncModif()) {
								dateLivrareInstance.setValoareIncasare(nf3.format(CreareComanda.totalComanda * Constants.TVA));
							}
						}

						String userSiteMail = " ", isValIncModif = " ", codJ = "", adrLivrareGED = "";

						if (dateLivrareInstance.isValIncModif())
							isValIncModif = "X";

						comandaFinalaStr = clientFinalStr + comandaBlocata + "#" + cmdSAP + "#-1" + "#" + dateLivrareInstance.getTermenPlata() + "#"
								+ dateLivrareInstance.getObsLivrare() + "#" + dateLivrareInstance.getDataLivrare().toString() + "#"
								+ dateLivrareInstance.isAdrLivrNoua() + "#" + dateLivrareInstance.getTipDocInsotitor() + "#" + alerteKA + "#"
								+ dateLivrareInstance.getObsPlata() + "#" + dateLivrareInstance.getAddrNumber() + "#" + localRedSeparat + "#"
								+ CreareComanda.filialaAlternativa + "#" + dateLivrareInstance.getValoareIncasare() + "#"
								+ UserInfo.getInstance().getUserSite() + "#" + userSiteMail + "#" + isValIncModif + "#" + codJ + "#" + adrLivrareGED + "@"
								+ articoleFinaleStr;

						Comanda comanda = new Comanda();
						comanda.setCodClient(codClientVar);
						comanda.setComandaBlocata(comandaBlocata);
						comanda.setNrCmdSap(cmdSAP);
						comanda.setAlerteKA(alerteKA);
						comanda.setFactRedSeparat(localRedSeparat);
						comanda.setFilialaAlternativa(CreareComanda.filialaAlternativa);
						comanda.setUserSite(UserInfo.getInstance().getUserSite());
						comanda.setUserSiteMail(userSiteMail);
						comanda.setIsValIncModif(isValIncModif);
						comanda.setCodJ(codJ);
						comanda.setAdresaLivrareGed(adrLivrareGED);

						comandaJson = serializeComanda(comanda);
						articoleFinaleStr = serializedResult;

						if (comandaHasPalet())
							displayAlertPalet();
						else
							displayArtComplDialog();

					}
				});

				myTimer.cancel();
			} else {
				mProgress.setProgress(progressVal);
			}

		}
	}

	private boolean isReducere() {
		return globalSubCmp.equals("1") && !UserInfo.getInstance().getCodDepart().equals("07") && !UserInfo.getInstance().getCodDepart().equals("04");
	}

	private void performSaveCmd() {
		try {

			String tipUser = "AV";
			HashMap<String, String> params = new HashMap<String, String>();

			if (UserInfo.getInstance().getTipAcces().equals("27"))
				tipUser = "KA";

			params.put("comanda", comandaFinalaStr);
			params.put("alertSD", String.valueOf(alertSD));
			params.put("alertDV", String.valueOf(alertDV));
			params.put("cmdAngajament", String.valueOf(cmdAngajament));
			params.put("tipUser", tipUser);
			params.put("JSONArt", articoleFinaleStr);
			params.put("JSONComanda", comandaJson);
			params.put("JSONDateLivrare", serializeDateLivrare());

			ComenziDAO comanda = ComenziDAO.getInstance(this);
			comanda.setComenziDAOListener(this);
			comanda.salveazaComandaDistrib(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void displayAlertPalet() {

		PaletAlertDialog infoDialog = new PaletAlertDialog(this);
		infoDialog.setPaletAlertListener(this);
		infoDialog.showAlertDialog();

	}

	private boolean comandaHasPalet() {

		List<ArticolComanda> listaArticole = ListaArticoleComanda.getInstance().getListArticoleComanda();
		Iterator<ArticolComanda> iterator = listaArticole.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().isUmPalet())
				return true;
		}

		return false;

	}

	private String prepareArtForDelivery() {
		String retVal = "";

		listArticole = new ArrayList<ArticolComanda>();
		ArticolComanda articol = null;

		try {

			comandaBlocata = "0";
			alertSD = false;
			alertDV = false;
			alertCredite = false;
			globalAlertSDKA = "";
			globalAlertDVKA = "";
			globalSubCmp = "0";
			double dblLocalTaxaVerde = 0;

			String tokPretArticol, tokCantArticol, tokCodArticol, tokDepozArticol, tokProcent, tokUM, tokProcentFact, tokDiscClient, tokProcAprob, tokMultiplu, tokValArticol, tokInfoArticol, tokCantUmb, tokUmb;
			String[] tokAlteValori;
			String tokCond = " ", varAlteValori = " ";
			String[] tokPret;
			double valCondPret = 0;
			String localCodDepart = "";

			List<ArticolComanda> listaArticole = ListaArticoleComanda.getInstance().getListArticoleComanda();

			// sortare articole dupa departament
			Collections.sort(listaArticole, ArticolComanda.DepartComparator);

			Iterator<ArticolComanda> iterator = listaArticole.iterator();

			while (iterator.hasNext()) {
				articol = iterator.next();
				tokPretArticol = String.valueOf(articol.getPretUnit());
				tokCantArticol = String.valueOf(articol.getCantitate());
				tokCodArticol = articol.getCodArticol();
				tokDepozArticol = articol.getDepozit();
				tokProcent = String.valueOf(articol.getProcent());
				tokUM = articol.getUm();
				tokProcentFact = String.valueOf(articol.getProcentFact());
				tokDiscClient = String.valueOf(articol.getDiscClient());
				tokProcAprob = String.valueOf(articol.getProcAprob());
				tokMultiplu = String.valueOf(articol.getMultiplu());
				tokValArticol = String.valueOf(articol.getPret());
				tokInfoArticol = articol.getInfoArticol();

				// calcul taxa verde
				if (tokInfoArticol.contains(";")) {
					String[] condPret = tokInfoArticol.split(";");

					for (int ii = 0; ii < condPret.length; ii++) {
						tokPret = condPret[ii].split(":");
						valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
						if (valCondPret != 0) {
							if (tokPret[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
								dblLocalTaxaVerde += valCondPret;
							}

						}
					}
				}
				// sf. taxa verde

				tokCantUmb = String.valueOf(articol.getCantUmb());
				tokUmb = articol.getUmb();

				if (articol.getAlteValori() == null) {
					retVal = "";
					break;
				}

				varAlteValori = articol.getAlteValori();

				if (varAlteValori.contains("!")) {
					tokAlteValori = varAlteValori.split("!");
					if (tokAlteValori[6].equals("1"))
						globalSubCmp = "1";
				}

				tokCond = " ";
				if (articol.getConditie())
					tokCond = "X";

				if (isUserExceptie()) {
					if (articol.getObservatii() != null) {

						localCodDepart = articol.getDepart();

						if (isArtGedExceptie(articol)) {
							localCodDepart = articol.getDepartAprob();

						}

						if (articol.getObservatii().contains("SD")) {
							comandaBlocata = "1";
							alertSD = true;

							if (!globalAlertSDKA.contains(localCodDepart))
								globalAlertSDKA += localCodDepart + "?";

						}

						if (articol.getObservatii().contains("DV")) {
							comandaBlocata = "1";
							alertDV = true;

							if (!globalAlertDVKA.contains(localCodDepart))

								globalAlertDVKA += localCodDepart + "?";
						}
					}

				}

				ArticolComanda articolCmd = new ArticolComanda();
				articolCmd.setCodArticol(tokCodArticol);
				articolCmd.setCantitate(Double.valueOf(tokCantArticol));
				articolCmd.setDepozit(tokDepozArticol);
				articolCmd.setPretUnit(Double.valueOf(tokPretArticol));
				articolCmd.setProcent(Double.valueOf(tokProcent));
				articolCmd.setUm(tokUM);
				articolCmd.setProcentFact(Double.valueOf(tokProcentFact));
				articolCmd.setConditie(Boolean.valueOf(tokCond));
				articolCmd.setDiscClient(Double.valueOf(tokDiscClient));
				articolCmd.setProcAprob(Double.valueOf(tokProcAprob));
				articolCmd.setMultiplu(Double.valueOf(tokMultiplu));
				articolCmd.setPret(Double.valueOf(tokValArticol));
				articolCmd.setInfoArticol(tokInfoArticol);
				articolCmd.setCantUmb(Double.valueOf(tokCantUmb));
				articolCmd.setUmb(tokUmb);
				articolCmd.setDepart(articol.getDepart());
				articolCmd.setDepartAprob(articol.getDepartAprob());
				if (isArtGedExceptie(articol))
					articolCmd.setObservatii(articol.getObservatii());

				listArticole.add(articolCmd);

				retVal += tokCodArticol + "#" + tokCantArticol + "#" + tokDepozArticol + "#" + tokPretArticol + "#" + tokProcent + "#" + tokUM + "#"
						+ tokProcentFact + "#" + tokCond + "#" + tokDiscClient + "#" + tokProcAprob + "#" + tokMultiplu + "#" + tokValArticol + "#"
						+ tokInfoArticol + "#" + tokCantUmb + "#" + tokUmb + "@";

			}

			// adaugare material taxa verde
			if (CreareComanda.canalDistrib.equals("10")) {
				if (dblLocalTaxaVerde > 0) {

					ArticolComanda articolCmd = new ArticolComanda();
					articolCmd.setCodArticol("000000000000000000");
					articolCmd.setCantitate(1.0);
					articolCmd.setDepozit(articol.getDepozit());
					articolCmd.setPretUnit(dblLocalTaxaVerde);
					articolCmd.setProcent(0);
					articolCmd.setUm("BUC");
					articolCmd.setProcentFact(0);
					articolCmd.setConditie(false);
					articolCmd.setDiscClient(0);
					articolCmd.setProcAprob(0);
					articolCmd.setMultiplu(1);
					articolCmd.setPret(dblLocalTaxaVerde);
					articolCmd.setInfoArticol(" ");
					articolCmd.setCantUmb(1);
					articolCmd.setUmb("BUC");
					articolCmd.setDepart(articol.getDepart());
					articolCmd.setObservatii("");
					listArticole.add(articolCmd);

					retVal += "000000000000000000" + "#" + "1" + "#" + " " + "#" + String.valueOf(dblLocalTaxaVerde) + "#" + "0" + "#" + "BUC" + "#" + "0"
							+ "#" + " " + "#" + "0" + "#" + "0" + "#" + "1" + "#" + String.valueOf(dblLocalTaxaVerde) + "#" + " " + "#" + "1" + "#" + "BUC"
							+ "@";

				}
			}

			if (CreareComanda.canalDistrib.equals("20")) {
				alertSD = false;
				alertDV = false;
				alertCredite = false;
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		serializeArticole();

		return retVal;
	}

	// userul este agent, sd sau ka
	boolean isUserExceptie() {
		return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
				|| UserInfo.getInstance().getTipAcces().equals("27");
	}

	private boolean isArtGedExceptie(ArticolComanda articolComanda) {
		return articolComanda.getDepozit().equals("MAV1") && articolComanda.getDepart().equals("11") && !articolComanda.getDepartAprob().equals("00");
	}

	private String serializeArticole() {
		JSONArray myArray = new JSONArray();

		JSONObject obj = null;

		try {
			for (int i = 0; i < listArticole.size(); i++) {
				obj = new JSONObject();
				obj.put("codArticol", listArticole.get(i).getCodArticol());
				obj.put("cantitate", listArticole.get(i).getCantitate());
				obj.put("depozit", listArticole.get(i).getDepozit());
				obj.put("pretUnit", listArticole.get(i).getPretUnit());
				obj.put("procent", listArticole.get(i).getProcent());
				obj.put("um", listArticole.get(i).getUm());
				obj.put("procentFact", listArticole.get(i).getProcentFact());
				obj.put("conditie", listArticole.get(i).getConditie());
				obj.put("discClient", listArticole.get(i).getDiscClient());
				obj.put("procAprob", listArticole.get(i).getProcAprob());
				obj.put("multiplu", listArticole.get(i).getMultiplu());
				obj.put("pret", listArticole.get(i).getPret());
				obj.put("infoArticol", listArticole.get(i).getInfoArticol());
				obj.put("cantUmb", listArticole.get(i).getCantUmb());
				obj.put("Umb", listArticole.get(i).getUmb());
				obj.put("depart", listArticole.get(i).getDepart());
				obj.put("observatii", listArticole.get(i).getObservatii());
				obj.put("departAprob", listArticole.get(i).getDepartAprob());
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
			obj.put("adresaLivrareGed", comanda.getAdresaLivrareGed());
			obj.put("adresaLivrare", comanda.getAdresaLivrare());
			obj.put("valoareIncasare", comanda.getValoareIncasare());
			obj.put("conditieID", comanda.getConditieID());
			obj.put("canalDistrib", CreareComanda.canalDistrib);
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
			obj.put("Strada", DateLivrare.getInstance().getStrada() + " ");
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
			obj.put("totalComanda", ListaArticoleComanda.getInstance().getTotalComanda());
			obj.put("unitLog", DateLivrare.getInstance().getUnitLog());
			obj.put("codAgent", DateLivrare.getInstance().getCodAgent());
			obj.put("factRed", DateLivrare.getInstance().getFactRed());
			obj.put("macara", DateLivrare.getInstance().isMasinaMacara() ? "X" : " ");
			obj.put("idObiectiv", DateLivrare.getInstance().getIdObiectiv());
			obj.put("isAdresaObiectiv", DateLivrare.getInstance().isAdresaObiectiv());
			obj.put("coordonateGps", getCoordAdresa());
			obj.put("tonaj", DateLivrare.getInstance().getTonaj());
			obj.put("prelucrare", DateLivrare.getInstance().getPrelucrare());

		} catch (JSONException ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return obj.toString();

	}

	private String getCoordAdresa() {
		if (DateLivrare.getInstance().getCoordonateAdresa() != null)
			return DateLivrare.getInstance().getCoordonateAdresa().latitude + "#" + DateLivrare.getInstance().getCoordonateAdresa().longitude;
		else
			return "0#0";
	}

	public void addListenerClientBtn() {
		clientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (ListaArticoleComanda.getInstance().getNrArticoleComanda() == 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerArticoleBtn() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClientVar.length() > 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerLivrareBtn() {
		livrareBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClientVar.length() > 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void newTotal() {

		totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();
		textTotalCmd.setText(String.format("%.02f", totalComanda));
		textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

	}

	public void displayArticoleComanda() {
		ArticoleCreareAdapter adapterArticole = new ArticoleCreareAdapter(new ArrayList<ArticolComanda>(), this);
		listArtCmd.setAdapter(adapterArticole);

		adapterArticole.setListArticole(ListaArticoleComanda.getInstance().getListArticoleComanda());

	}

	private void saveCmdStatus(String saveResponse) {
		if (!saveResponse.equals("-1")) {
			try {

				if (CreareComanda.canalDistrib.equals("10")) {
					// comanda a fost salvata

					if (!saveResponse.equals("9")) {
						// pentru stoc insuficient nu se mai fac alertari

						if (alertSD) {
							clearAllData();
						}

						if (alertDV) {

							clearAllData();

						}

						if (alertCredite) {
							sendMailAlert(2);
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

						if (!alertSD && !alertDV && !alertCredite) {
							clearAllData();
						}
					} else {
						clearAllData();
					}

					Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

				} else {
					// instiintare pret transport client (daca e cazul) si apoi
					// confirmare salvare
					if (saveResponse.contains("#")) {
						// comanda ged cu transport client, se afiseaza pretul
						String[] varResp = saveResponse.split("#");
						nrCmdGED = varResp[2];
						displayDlgPretTransp(varResp[1]);
					} else
					// comanda ged fara transport client
					{
						Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

						clearAllData();
					}
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
			slidingDrawerCmd.animateClose();
		}
	}

	public void sendMailAlert(int tip) {
		try {

			if (tip == 1) // alerta dv
			{
				mailAlertTipDest = "2";
			}

			if (tip == 2) // alerta ofiter credite
			{
				mailAlertTipDest = "3";
			}

			if (tip == 3) // alerta director KA
			{
				mailAlertTipDest = "4";
			}

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ul", UserInfo.getInstance().getUnitLog());
			params.put("depart", UserInfo.getInstance().getCodDepart());
			params.put("dest", mailAlertTipDest);
			params.put("agent", UserInfo.getInstance().getNume()); // nume agent
			params.put("clnt", numeClientVar); //
			params.put("suma", String.format("%.02f", totalComanda)); //

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "sendMailAlert", params);
			call.getCallResults();

		} catch (Exception ex) {
			Log.e("Error", ex.toString());

		}
	}

	private void clearAllData() {

		arrayListArticole.clear();

		textClient.setText("");
		textTotalCmd.setText("");
		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");
		textLimCrd.setText("");
		textRestCrd.setText("");
		textClient.setVisibility(View.GONE);
		textTotalCmd.setVisibility(View.GONE);
		textTipPlata.setVisibility(View.GONE);
		textAdrLivr.setVisibility(View.GONE);
		textPersContact.setVisibility(View.GONE);
		textTelefon.setVisibility(View.GONE);
		textCantar.setVisibility(View.GONE);
		textTransport.setVisibility(View.GONE);
		slidingDrawerCmd.setVisibility(View.GONE);
		labelTotal.setVisibility(View.GONE);
		textLimCrd.setVisibility(View.GONE);
		textRestCrd.setVisibility(View.GONE);

		textNrFact.setVisibility(View.GONE);
		listArtCmd.setVisibility(View.GONE);
		textCursValut.setVisibility(View.GONE);
		layoutHeader.setVisibility(View.INVISIBLE);
		textPondereB.setVisibility(View.INVISIBLE);

		// reset variabile
		resetAllVars();

	}

	private void resetAllVars() {
		arrayListArticole.clear();

		numeClientVar = "";
		tipClientVar = "";
		articoleComanda = "";
		dateLivrare = "";

		articoleComanda = "";
		articoleFinaleStr = "";
		clientFinalStr = "";
		comandaBlocata = "0";
		numeClientVar = "";
		codClientVar = "";
		totalComanda = 0;
		canalDistrib = "";
		cursValutar = 0;
		limitaCredit = 0;
		restCredit = 0;
		depozitUnic = "";
		globalAlertSDKA = "";
		globalAlertDVKA = "";
		termenPlata = "";
		globalSubCmp = "0";
		valNegociat = 0;

		DateLivrare.getInstance().resetAll();
		filialaAlternativa = UserInfo.getInstance().getUnitLog();

		ListaArticoleComanda.getInstance().clearArticoleComanda();

	}

	private void displayDlgPretTransp(String valTransp) {
		PretTransportDialog transportDialog = new PretTransportDialog(this, CreareComanda.totalComanda, valTransp);
		transportDialog.setTransportDialogListener(this);
		transportDialog.showDialog();

	}

	private void displayArtComplDialog() {
		slidingDrawerCmd.animateClose();

		ArtComplDialog artCompl = new ArtComplDialog(this);
		artCompl.setArtComplListener(this);
		artCompl.showDialog(arrayListArticole, ListaArticoleComanda.getInstance().getListArticoleComanda());

	}

	private void performSaveCmdGED() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("comanda", nrCmdGED);

			ComenziDAO comanda = ComenziDAO.getInstance(this);
			comanda.setComenziDAOListener(this);
			comanda.salveazaComandaGed(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void displayCmdGEDStatus(String saveResponse) {

		if (!saveResponse.equals("-1")) {

			clearAllData();
			Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
			slidingDrawerCmd.animateClose();
		}
	}

	public void addListenerListArtCmd() {
		listArtCmd.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelPos = position;
				return false;

			}
		});
	}

	private void checkStaticVars() {

		// resetare locale la idle
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

	@Override
	public void onBackPressed() {
		returnToHome();
		return;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("sendMailAlert")) {
			clearAllData();
		}

	}

	public void operationComplete(double valNegociat, boolean isTotalNegociat) {
		CreareComanda.valNegociat = valNegociat;
		CreareComanda.isTotalNegociat = isTotalNegociat;
		if (isTotalNegociat) {
			calculProcReducere();
		}

	}

	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case SALVEAZA_COMANDA_DISTRIB:
			saveCmdStatus((String) result);
			break;
		case SALVEAZA_COMANDA_GED:
			displayCmdGEDStatus((String) result);
			break;
		default:
			break;

		}

	}

	public void opTransportComplete(boolean operationOk) {
		if (operationOk) {
			performSaveCmdGED();
		}

	}

	public void operationArtComplComplete(boolean btnSaveCmd) {
		if (btnSaveCmd) {
			performSaveCmd();
		}

	}

	private void setHeaderVisibility(boolean isVisible) {
		if (isVisible) {
			listArtCmd.setVisibility(View.VISIBLE);
			labelTotal.setVisibility(View.VISIBLE);
			textTotalCmd.setVisibility(View.VISIBLE);
			textPondereB.setVisibility(View.VISIBLE);
			layoutHeader.setVisibility(View.VISIBLE);
		} else {
			listArtCmd.setVisibility(View.INVISIBLE);
			labelTotal.setVisibility(View.INVISIBLE);
			textTotalCmd.setVisibility(View.INVISIBLE);
			textPondereB.setVisibility(View.INVISIBLE);
			layoutHeader.setVisibility(View.INVISIBLE);
		}
	}

	public void update(Observable observable, Object data) {

		if (observable instanceof ListaArticoleComanda) {
			setHeaderVisibility(true);
			displayArticoleComanda();

			totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();

			textTotalCmd.setText(String.format("%.02f", totalComanda));
			textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

			// pentru comenzile cu total negociat se recalculeaza reducerile
			if (CreareComanda.isTotalNegociat) {
				calculProcReducere();
			}

			calculPondereB();
			calculTaxaVerde();
		}

	}

	@Override
	public void paletDialogResponse(EnumDaNuOpt response) {
		switch (response) {
		case DA:
			displayArtComplDialog();
			break;
		default:
			break;
		}

	}

}
