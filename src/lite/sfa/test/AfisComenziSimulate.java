/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import listeners.AsyncTaskListener;
import listeners.ComenziDAOListener;
import listeners.OfertaMailListener;
import listeners.OperatiiArticolListener;
import listeners.SelectClientListener;
import model.ArticolComanda;
import model.ComenziDAO;
import model.InfoStrings;
import model.OperatiiArticol;
import model.OperatiiArticolImpl;
import model.UserInfo;
import patterns.CriteriuComenziSimulate;
import utils.UtilsGeneral;
import adapters.ArticolSimulatAdapter;
import adapters.ComandaSimulataAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.ArticolSimulat;
import beans.BeanArticolStoc;
import beans.BeanArticoleAfisare;
import beans.BeanComandaCreata;
import beans.BeanComandaSimulata;
import beans.DateLivrareAfisare;
import dialogs.OfertaMailDialog;
import dialogs.SelectClientCmdSimDialog;
import enums.EnumArticoleDAO;
import enums.EnumComenziDAO;

public class AfisComenziSimulate extends Activity implements AsyncTaskListener, ComenziDAOListener, OperatiiArticolListener, OfertaMailListener,
		SelectClientListener {

	Button creeazaCmdSimBtn, stergeCmdSimBtn;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private SimpleAdapter adapter;

	private Spinner spinnerCmd;
	private SimpleAdapter adapterComenzi;
	private static ArrayList<HashMap<String, String>> listComenzi = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "", selectedCmdSAP = "";

	private String selectedClientCode = "-1";
	private String cmdNr = null, tipSelCmd = "";
	private TextView textTipPlata, textAdrLivr, textOras, textJudet;
	private TextView textPersContact, textTelefon, textTransport;

	public static String codClientVar = "";
	public static String numeClientVar = "";
	public static String articoleComanda = "", numeArtSelContextMenu = "", codArtSelContextMenu = "";
	public static double totalComanda = 0, stocArtCond = 0;
	public static String dateLivrare = "", tipOpCmd = "-1";

	private boolean touched = false;
	private Dialog dialogSelClient;
	public static String tipAcces;
	private ListView listArticoleSimulate;

	private String selectedClient = "";
	private Integer selectedClientIndex = -1;

	private HashMap<String, String> artMap = null;

	private ComenziDAO comenzi;
	private Button verificaStocButton;
	private List<ArticolSimulat> listArticole;
	private OperatiiArticol opArticol;
	private ArticolSimulatAdapter adapterSimulat;
	private LinearLayout detaliiLayout;
	private BeanComandaSimulata comandaCurenta;
	private DateLivrareAfisare dateLivrareCmdCurent;

	private List<BeanComandaCreata> comenziSimulate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afiscomenzisimulate);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Comenzi simulate");
		actionBar.setDisplayHomeAsUpEnabled(true);

		comenzi = ComenziDAO.getInstance(this);
		comenzi.setComenziDAOListener(this);

		opArticol = new OperatiiArticolImpl(this);
		opArticol.setListener(this);

		checkStaticVars();

		spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);

		adapterComenzi = new SimpleAdapter(this, listComenzi, R.layout.comsimulatecustomview, new String[] { "idCmd", "codClient", "numeClient", "data",
				"suma", "stare", "tipCmd", "ul", "cmdSap" }, new int[] { R.id.textIdCmd, R.id.textCodClient, R.id.textClient, R.id.textData, R.id.textSuma,
				R.id.textStare, R.id.textTipCmd, R.id.textUL, R.id.textCmdSAP });

		addSpinnerCmdListener();

		spinnerCmd.setVisibility(View.INVISIBLE);
		spinnerCmd.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				touched = true;
				return false;
			}

		});

		spinnerCmd.setAdapter(adapterComenzi);

		listArticoleSimulate = (ListView) findViewById(R.id.listArtModif);

		this.creeazaCmdSimBtn = (Button) findViewById(R.id.creazaCmdSimBtn);
		this.creeazaCmdSimBtn.setVisibility(View.INVISIBLE);
		addListenerCreazaCmdSimBtn();

		this.stergeCmdSimBtn = (Button) findViewById(R.id.stergeCmdSimBtn);
		this.stergeCmdSimBtn.setVisibility(View.INVISIBLE);
		addListenerStergeCmdSimBtn();

		adapter = new SimpleAdapter(this, list1, R.layout.comsimulatecustomrowview, new String[] { "nrCrt", "numeArt", "codArt", "cantArt", "umArt", "pretArt",
				"monedaArt", "depozit", "status", "procent", "procFact", "zDis", "tipAlert", "procAprob" }, new int[] { R.id.textNrCrt, R.id.textNumeArt,
				R.id.textCodArt, R.id.textCantArt, R.id.textUmArt, R.id.textPretArt, R.id.textMonedaArt, R.id.textDepozit, R.id.textStatusArt,
				R.id.textProcRed, R.id.textProcFact, R.id.textZDIS, R.id.textAlertUsr, R.id.textProcAprobModif });

		listArticoleSimulate.setAdapter(adapter);
		listArticoleSimulate.setVisibility(View.INVISIBLE);

		adapterSimulat = new ArticolSimulatAdapter(this, null);

		registerForContextMenu(listArticoleSimulate);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);

		textTransport = (TextView) findViewById(R.id.textTransport);

		textOras = (TextView) findViewById(R.id.textOrasModif);
		textJudet = (TextView) findViewById(R.id.textJudetModif);

		performGetComenzi();

		detaliiLayout = (LinearLayout) findViewById(R.id.detaliiLayout);
		detaliiLayout.setVisibility(View.INVISIBLE);

		verificaStocButton = (Button) findViewById(R.id.verificaStocBtn);
		verificaStocButton.setVisibility(View.INVISIBLE);
		setListenerVerificaStoc();

		comenziSimulate = new ArrayList<BeanComandaCreata>();

	}

	@Override
	public void onResume() {
		super.onResume();
		checkStaticVars();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Trimite mail");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu2 = menu.add(0, 1, 1, "Client");
		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	private void setListenerVerificaStoc() {
		verificaStocButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				verificaStocArticole();

			}
		});
	}

	private List<BeanArticolStoc> getArticoleComanda() {

		Iterator<ArticolSimulat> iterator = listArticole.iterator();

		List<BeanArticolStoc> listArtStoc = new ArrayList<BeanArticolStoc>();
		String codArticol;

		while (iterator.hasNext()) {
			ArticolComanda articolComanda = iterator.next();

			if (!InfoStrings.isMatTransport(articolComanda.getCodArticol()) && !InfoStrings.isMatTransportNume(articolComanda.getNumeArticol())) {
				BeanArticolStoc articolStoc = new BeanArticolStoc();

				if (articolComanda.getCodArticol().length() == 8)
					codArticol = "0000000000" + articolComanda.getCodArticol();
				else
					codArticol = articolComanda.getCodArticol();

				articolStoc.setCod(codArticol);
				articolStoc.setDepozit(articolComanda.getDepozit());

				articolStoc.setUnitLog(getUnitLog(articolComanda.getDepozit(), articolComanda.getUnitLogAlt()));
				articolStoc.setDepart(articolComanda.getDepartSintetic());
				listArtStoc.add(articolStoc);
			}
		}

		return listArtStoc;

	}

	private void verificaStocArticole() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("listArticole", opArticol.serializeListArtStoc(getArticoleComanda()));

		opArticol.getStocArticole(params);

	}

	private String getUnitLog(String depozit, String unitLog) {

		if (depozit.equals("MAV1")) {
			unitLog = unitLog.substring(0, 2) + "2" + unitLog.substring(3, 4);
		} else if (depozit.equals("WOOD")) {
			unitLog = unitLog.substring(0, 2) + "4" + unitLog.substring(3, 4);
		} else {
			unitLog = unitLog.substring(0, 2) + "1" + unitLog.substring(3, 4);
		}

		return unitLog;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:

			if (comandaCurenta == null) {
				return false;
			}

			showDialogSendMail();

			return true;

		case 1:
			showDialogSelectClient();
			return true;

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			list1.clear();
			listComenzi.clear();

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	private void showDialogSendMail() {
		OfertaMailDialog ofertaMail = new OfertaMailDialog(AfisComenziSimulate.this, dateLivrareCmdCurent.getMail());
		ofertaMail.setOfertaMailListener(AfisComenziSimulate.this);
		ofertaMail.show();
	}

	private void showDialogSelectClient() {

		SelectClientCmdSimDialog clientDialog = new SelectClientCmdSimDialog(this, getClientiComenzi());
		clientDialog.setSelectClientListener(this);
		clientDialog.show();
	}

	private TreeSet<String> getClientiComenzi() {

		TreeSet<String> setClienti = new TreeSet<String>();

		setClienti.add("TOTI CLIENTII");

		for (BeanComandaCreata comanda : comenziSimulate)
			setClienti.add(comanda.getNumeClient());

		return setClienti;
	}

	private void sendOfertaMail(String adresaMail) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrComanda", comandaCurenta.getId());
		params.put("mailAddress", adresaMail);

		comenzi.sendOfertaGedMail(params);

	}

	private void addSpinnerCmdListener() {
		spinnerCmd.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				textTipPlata.setText("");
				textAdrLivr.setText("");
				textPersContact.setText("");
				textTelefon.setText("");
				textTransport.setText("");

				if (!selectedCmd.equals("-1")) {

					comandaCurenta = (BeanComandaSimulata) arg0.getAdapter().getItem(arg2);

					selectedCmd = comandaCurenta.getId();
					setCreazaCmdBtnVisibleStatus(comandaCurenta);

					setupContextLayout(comandaCurenta);
					performGetArticoleComanda();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void setupContextLayout(BeanComandaSimulata comanda) {
		if (comanda.isAprobata())
			verificaStocButton.setVisibility(View.VISIBLE);
		else
			verificaStocButton.setVisibility(View.INVISIBLE);
	}

	private void setCreazaCmdBtnVisibleStatus(BeanComandaSimulata comanda) {

		if (comanda.getCodStare().equals("20"))
			creeazaCmdSimBtn.setVisibility(View.VISIBLE);
		else
			creeazaCmdSimBtn.setVisibility(View.INVISIBLE);
	}

	private boolean isCmdAvansOK(BeanComandaSimulata comanda, boolean allStock) {
		return comanda.getCodStare().equals("21") && comanda.isAprobata() && comanda.getAvans() > 0 && allStock;
	}

	public void addListenerCreazaCmdSimBtn() {
		creeazaCmdSimBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showCreateCmdConfirmationAlert();

			}
		});

	}

	public void showCreateCmdConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Creati comanda?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				// cu rezervare
				if (comandaCurenta.getCodStare().equals("20"))
					tipOpCmd = "9";

				// fara rezervare
				if (comandaCurenta.getCodStare().equals("21"))
					tipOpCmd = "10";

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
			params.put("tipOp", tipOpCmd);
			params.put("codUser", fullCode);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "operatiiComenzi", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void showDeleteConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Stergeti comanda?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				tipOpCmd = "3";
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

	public void addListenerStergeCmdSimBtn() {
		stergeCmdSimBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDeleteConfirmationAlert();

			}
		});

	}

	public void performGetComenzi() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "CV";

			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("9"))
				tipUser = "CV";

			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("codUser", UserInfo.getInstance().getCod());
			params.put("tipCmd", "4"); // comenzi simulate
			params.put("depart", "11");
			params.put("tipUser", tipUser);
			params.put("codClient", selectedClient);

			comenzi.getListComenzi(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void performGetArticoleComanda() {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("nrCmd", selectedCmd);
		params.put("afisCond", "1");
		params.put("tipUser", "CV");

		comenzi.getArticoleComandaJSON(params);

	}

	private void populateArticoleComanda(BeanArticoleAfisare articoleComanda) {

		dateLivrareCmdCurent = articoleComanda.getDateLivrare();
		listArticole = articoleComanda.getArticoleSimulate();

		adapterSimulat.setListArticole(listArticole);

		listArticoleSimulate.setAdapter(adapterSimulat);

		listArticoleSimulate.setVisibility(View.VISIBLE);

		textTipPlata.setText(UtilsGeneral.getDescTipPlata(dateLivrareCmdCurent.getTipPlata()));
		textTransport.setText(UtilsGeneral.getDescTipTransport(dateLivrareCmdCurent.getTransport()));
		textOras.setText(dateLivrareCmdCurent.getOras());
		textJudet.setText(InfoStrings.numeJudet(dateLivrareCmdCurent.getCodJudet()));
		textAdrLivr.setText(dateLivrareCmdCurent.getDateLivrare());
		textPersContact.setText(dateLivrareCmdCurent.getPersContact());
		textTelefon.setText(dateLivrareCmdCurent.getNrTel());

	}

	protected void populateListComenzi(List<BeanComandaCreata> cmdList) {

		if (cmdList.size() > 0) {

			creeazaCmdSimBtn.setVisibility(View.VISIBLE);
			stergeCmdSimBtn.setVisibility(View.VISIBLE);

			verificaStocButton.setVisibility(View.VISIBLE);
			detaliiLayout.setVisibility(View.VISIBLE);

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			list1.clear();
			listComenzi.clear();
			spinnerCmd.setVisibility(View.VISIBLE);

			ComandaSimulataAdapter adapterComenzi1 = new ComandaSimulataAdapter(copyCollection(cmdList), this);
			spinnerCmd.setAdapter(adapterComenzi1);

			

		} else {
			creeazaCmdSimBtn.setVisibility(View.INVISIBLE);
			stergeCmdSimBtn.setVisibility(View.INVISIBLE);

			verificaStocButton.setVisibility(View.INVISIBLE);
			detaliiLayout.setVisibility(View.INVISIBLE);

			spinnerCmd.setVisibility(View.INVISIBLE);
			listArticoleSimulate.setVisibility(View.INVISIBLE);

			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	private ArrayList<BeanComandaSimulata> copyCollection(Collection<BeanComandaCreata> listCreate) {
		ArrayList<BeanComandaSimulata> listSimulate = new ArrayList<BeanComandaSimulata>();

		for (BeanComandaCreata creata : listCreate) {
			BeanComandaSimulata simulata = new BeanComandaSimulata();

			simulata.setId(creata.getId());
			simulata.setNumeClient(creata.getNumeClient());
			simulata.setData(creata.getData());
			simulata.setSuma(creata.getSuma());
			simulata.setStare(creata.getStare());
			simulata.setMoneda(creata.getMoneda());
			simulata.setSumaTva(creata.getSumaTva());
			simulata.setMoneda(creata.getMoneda());
			simulata.setMonedaTva(creata.getMonedaTva());
			simulata.setCmdSap(creata.getCmdSap());
			simulata.setTipClient(creata.getTipClient());
			simulata.setCodStare(creata.getCodStare());
			simulata.setAprobariNecesare(creata.getAprobariNecesare());
			simulata.setAprobariPrimite(creata.getAprobariPrimite());
			simulata.setAvans(creata.getAvans());
			listSimulate.add(simulata);
		}

		return listSimulate;
	}

	private void updateStocStatus(List<BeanArticolStoc> listArticoleStoc) {

		boolean allStock = true;

		for (ArticolSimulat articol : listArticole) {

			if (InfoStrings.isMatTransport(articol.getCodArticol()) || InfoStrings.isMatTransportNume(articol.getNumeArticol()))
				articol.setExceptie(true);
			else
				articol.setExceptie(false);

			for (BeanArticolStoc articolStoc : listArticoleStoc) {

				if (articolStoc.getCod().equals(articol.getCodArticol()) && articolStoc.getDepozit().equals(articol.getDepozit())) {
					if (articolStoc.getStoc() >= articol.getCantitate()) {
						articol.setStocOK(true);
						break;
					}
				}
			}

			if (!articol.isExceptie() && !articol.isStocOK())
				allStock = false;
		}

		adapterSimulat.setListArticole(listArticole);

		if (isCmdAvansOK(comandaCurenta, allStock))
			creeazaCmdSimBtn.setVisibility(View.VISIBLE);
		else
			creeazaCmdSimBtn.setVisibility(View.INVISIBLE);

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
		UserInfo.getInstance().setParentScreen("");
		list1.clear();
		listComenzi.clear();

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();

		return;
	}

	@SuppressWarnings("unchecked")
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateListComenzi((List<BeanComandaCreata>) result);
			comenziSimulate = (List<BeanComandaCreata>) result;
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			populateArticoleComanda(comenzi.deserializeArticoleComandaLight((String) result));
			break;
		default:
			break;

		}

	}

	@Override
	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {
		case GET_STOC_ARTICOLE:
			updateStocStatus(opArticol.derializeListArtStoc((String) result));
			break;
		default:
			break;

		}

	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("operatiiComenzi")) {
			Toast toast = Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_SHORT);
			toast.show();
			performGetComenzi();
		}

	}

	@Override
	public void sendMail(String mailAddress) {
		sendOfertaMail(mailAddress);
	}

	@Override
	public void clientSelected(String numeClient, int position) {

		if (position == 0)
			populateListComenzi(comenziSimulate);
		else {
			List<BeanComandaCreata> comenziFiltrate = new CriteriuComenziSimulate().getCmdByClient(comenziSimulate, numeClient);
			populateListComenzi(comenziFiltrate);
		}

	}

}