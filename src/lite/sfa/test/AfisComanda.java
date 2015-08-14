/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.ClientDialogListener;
import listeners.ComenziDAOListener;
import listeners.CustomSpinnerClass;
import listeners.CustomSpinnerListener;
import listeners.DivizieDialogListener;
import listeners.IntervalDialogListener;
import listeners.OperatiiAgentListener;
import model.ArticolComanda;
import model.ComandaCreata;
import model.ComenziDAO;
import model.InfoStrings;
import model.OperatiiAgent;
import model.OperatiiFiliala;
import model.UserInfo;
import utils.UtilsGeneral;
import adapters.ArticolAfisAdapter;
import adapters.ComandaAfisAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.BeanArticoleAfisare;
import beans.BeanComandaCreata;
import beans.DateLivrareAfisare;
import beans.ValoriComanda;
import dialogs.SelectClientDialog;
import dialogs.SelectDivizieDialog;
import dialogs.SelectIntervalDialog;
import enums.EnumComenziDAO;

public class AfisComanda extends Activity implements CustomSpinnerListener, OperatiiAgentListener, IntervalDialogListener, ClientDialogListener,
		ComenziDAOListener, DivizieDialogListener {

	Button quitBtn, cmdBtn, slideButtonCmdAfis;
	String filiala = "", nume = "", cod = "", codAgent = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";

	private static int restrictiiAfisare = 0;
	private String intervalAfisare = "0";

	ArrayList<HashMap<String, String>> listApprov = null;
	Spinner spinnerCmd;
	ListView spinnerApprovalList, listViewArticole;
	SimpleAdapter adapterAgenti, adapterFiliale, adapterClienti;

	static ArrayList<HashMap<String, String>> listComenzi = new ArrayList<HashMap<String, String>>();
	static ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();
	static ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();

	String selectedCmd = "-1", selectedAgent = "-1", selectedFiliala = "-1", selectedClient = "", selectedCodDepart = "-1";
	Integer selectedClientIndex = -1;
	String cmdNr = null, agentNr = null;
	String[] tokenAgent = null, tokenFiliala = null;
	TextView textTipPlata, textAdrLivr, textTotalCmd, textTotalTvaCmd, textPondereArtBAfis;
	TextView textPersContact, textTelefon, textCantar, textTransport, textJudet, textOras, textValoareMarja, textObsLivrare;
	static String tipAcces;
	Spinner spinnerAgentiAfisCmd, spinnerFiliale, spinnerTipUser;
	LinearLayout layoutTotalCmd;
	HashMap<String, String> artMap = null;
	double totalComenzi = 0, totalComenziTva = 0;
	Dialog dialogSelClient, dialogSelInterval, approvalListDialog;
	boolean totiClientiiRadioBtnSelected = true;
	int tipInterval = -1;

	String dataSelStart = "", dataSelStop = "";
	SlidingDrawer slidingDrawerCmdAfis;
	String tipAgent = "AV";

	CustomSpinnerClass spinnerListener = new CustomSpinnerClass();
	OperatiiAgent agent;
	String selectedDivizie = "00";
	ComenziDAO comenzi;

	LinearLayout layoutMarja, layoutDetaliiCmd;

	enum TipUser {
		AG("Agenti", "AG"), KA("Keyaccounti", "KA"), CV("Consilieri", "CV");

		String numeTip;
		String codTip;

		TipUser(String numeTip, String codTip) {
			this.numeTip = numeTip;
			this.codTip = codTip;
		}

		String getNumeTip() {
			return numeTip;
		}

		String getCodTip() {
			return codTip;
		}

	}

	private TipUser getEnumReducere(String numeTip) {
		for (TipUser enumTip : TipUser.values())
			if (enumTip.getCodTip().equals(numeTip))
				return enumTip;

		return null;
	}

	private TipUser selectedTipUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afiscomandaheader);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare comanda");
		actionBar.setDisplayHomeAsUpEnabled(true);

		comenzi = ComenziDAO.getInstance(this);
		comenzi.setComenziDAOListener(this);

		spinnerTipUser = (Spinner) findViewById(R.id.spinnerTipUser);
		populateSpinnerTipUser();

		if (!isDirectorDistrib()) {
			spinnerTipUser.setVisibility(View.INVISIBLE);
		}

		checkStaticVars();

		agent = OperatiiAgent.getInstance();
		agent.setOperatiiAgentListener(this);

		spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);
		addSpinnerCmdListener();
		spinnerCmd.setVisibility(View.INVISIBLE);

		spinnerListener.setListener(this);

		adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });

		listViewArticole = (ListView) findViewById(R.id.listArt);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textCantar = (TextView) findViewById(R.id.textCantar);
		textTransport = (TextView) findViewById(R.id.textTransport);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textOras = (TextView) findViewById(R.id.textOras);

		textValoareMarja = (TextView) findViewById(R.id.textValoareMarja);

		textPondereArtBAfis = (TextView) findViewById(R.id.textPondereArtBAfis);
		textPondereArtBAfis.setVisibility(View.INVISIBLE);

		textObsLivrare = (TextView) findViewById(R.id.textObsLivrare);

		spinnerAgentiAfisCmd = (Spinner) findViewById(R.id.spinnerAgentiAfisCmd);
		spinnerAgentiAfisCmd.setVisibility(View.INVISIBLE);

		spinnerFiliale = (Spinner) findViewById(R.id.spinnerFiliala);
		spinnerFiliale.setVisibility(View.INVISIBLE);

		selectedFiliala = UserInfo.getInstance().getUnitLog();

		intervalAfisare = "0";
		restrictiiAfisare = 0;

		layoutMarja = (LinearLayout) findViewById(R.id.layoutMarja);
		layoutMarja.setVisibility(View.GONE);

		layoutDetaliiCmd = (LinearLayout) findViewById(R.id.layoutDetaliiCmd);
		layoutDetaliiCmd.setVisibility(View.INVISIBLE);

		layoutTotalCmd = (LinearLayout) findViewById(R.id.layoutAfisTotalCmd);
		layoutTotalCmd.setVisibility(View.INVISIBLE);

		textTotalCmd = (TextView) findViewById(R.id.textValTotalCmd);
		textTotalTvaCmd = (TextView) findViewById(R.id.textValTotalCmdTva);

		this.slideButtonCmdAfis = (Button) findViewById(R.id.slideButtonCmdAfis);
		slidingDrawerCmdAfis = (SlidingDrawer) findViewById(R.id.slidingDrawerCmdAfis);
		addDrowerEvents();

		if (isDirector()) {
			setUpDirectorLayoutOptions();
		}

		if (isSefDepart()) {
			performGetAgenti();

		}

		if (isAgent()) {
			selectedAgent = UserInfo.getInstance().getCod();
			performGetComenzi();

		}

	}

	private void setUpDirectorLayoutOptions() {
		populateListFiliale();
	}

	boolean isDirectorDistrib() {
		return UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12");
	}

	boolean isDirector() {
		return UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")
				|| UserInfo.getInstance().getTipAcces().equals("35");
	}

	boolean isSefDepart() {
		return UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("18");
	}

	boolean isAgent() {
		return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
				|| UserInfo.getInstance().getTipAcces().equals("17");
	}

	@Override
	public void onResume() {
		super.onResume();
		checkStaticVars();
	}

	public void addDrowerEvents() {
		slidingDrawerCmdAfis.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButtonCmdAfis.setBackgroundResource(R.drawable.slideright32);
			}
		});

		slidingDrawerCmdAfis.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slideButtonCmdAfis.setBackgroundResource(R.drawable.slideleft32);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {

		MenuItem mnu1 = menu.add(0, 0, 0, "Interval");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		MenuItem mnu2 = menu.add(0, 1, 1, "Comenzi");
		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu3 = menu.add(0, 2, 2, "Clienti");
		mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		if (isKA()) {
			MenuItem mnu4 = menu.add(0, 3, 3, "Divizie");
			mnu4.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		}

	}

	private boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	private void populateListFiliale() {

		spinnerFiliale.setVisibility(View.VISIBLE);
		listFiliale = new ArrayList<HashMap<String, String>>();
		listFiliale = OperatiiFiliala.getInstance().getListToateFiliale();
		adapterFiliale = new SimpleAdapter(this, listFiliale, R.layout.rowlayoutagenti, new String[] { "numeFiliala", "codFiliala" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });

		spinnerFiliale.setAdapter(adapterFiliale);
		spinnerFiliale.setOnItemSelectedListener(spinnerListener);

	}

	private void performGetAgenti() {

		if (!isDirectorDistrib())
			selectedCodDepart = UserInfo.getInstance().getCodDepart();

		// dka
		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			selectedCodDepart = "10";
		}

		// sm
		if (UserInfo.getInstance().getTipAcces().equals("18")) {
			selectedCodDepart = "11";
		}

		agent.getListaAgenti(selectedFiliala, selectedCodDepart, this, true);

	}

	boolean isDateValid(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (Exception e) {
			Log.e("Error", e.toString());
			return false;
		}
	}

	private void populateSpinnerTipUser() {

		spinnerTipUser.setVisibility(View.VISIBLE);
		ArrayList<HashMap<String, String>> listTipUsers = new ArrayList<HashMap<String, String>>();

		SimpleAdapter adapterTypes = new SimpleAdapter(this, listTipUsers, R.layout.generic_rowlayout, new String[] { "stringName", "stringId" },
				new int[] { R.id.textName, R.id.textId });

		HashMap<String, String> temp;

		for (TipUser tipUser : TipUser.values()) {
			temp = new HashMap<String, String>();
			temp.put("stringName", tipUser.getNumeTip());
			temp.put("stringId", tipUser.getCodTip());
			listTipUsers.add(temp);

		}

		spinnerTipUser.setAdapter(adapterTypes);
		spinnerTipUser.setOnItemSelectedListener(spinnerListener);

	}

	private void populateListaAgenti(TipUser tipUser) {
		selectedCodDepart = "00";
		switch (tipUser) {
		case KA:
			selectedCodDepart = "10";
			break;
		case AG:
			selectedCodDepart = UserInfo.getInstance().getCodDepart();
			break;
		case CV:
			selectedCodDepart = "11";
			break;
		}

		agent.getListaAgenti(selectedFiliala, selectedCodDepart, AfisComanda.this, true);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			SelectIntervalDialog intervalDialog = new SelectIntervalDialog(this);
			intervalDialog.setIntervalDialogListener(this);
			intervalDialog.showDialog();
			return true;

		case 1:
			final CharSequence[] constrCmd = { "Emise", "Respinse" };
			AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			AlertDialog alertConstr;
			builderConstr.setTitle("Afiseaza comenzile ");
			builderConstr.setCancelable(true);
			builderConstr.setSingleChoiceItems(constrCmd, restrictiiAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (constrCmd[which].equals("Emise")) {
						restrictiiAfisare = 0;
					} else if (constrCmd[which].equals("Respinse")) {
						restrictiiAfisare = 1;
					}

					performGetComenzi();

					dialog.cancel();

				}
			});

			alertConstr = builderConstr.create();
			alertConstr.setCancelable(true);
			alertConstr.show();
			return true;

		case 2:
			SelectClientDialog client = new SelectClientDialog(this, selectedAgent, selectedFiliala);
			client.setClientDialogListener(this);
			client.showDialog();
			return true;

		case 3:
			SelectDivizieDialog dialog = new SelectDivizieDialog(this, selectedDivizie);
			dialog.setDivizieDialogListener(this);
			dialog.showDialog();
			return true;

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	private void populateAgentiList(ArrayList<HashMap<String, String>> listAgenti) {

		spinnerAgentiAfisCmd.setVisibility(View.VISIBLE);

		adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });
		spinnerAgentiAfisCmd.setAdapter(adapterAgenti);

		if (listAgenti.size() > 0) {
			spinnerAgentiAfisCmd.setOnItemSelectedListener(spinnerListener);
		}

	}

	public void performGetComenzi() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(selectedAgent)).toString();

			String filCrnt = "", tipUser = "", paramDepart = UserInfo.getInstance().getCodDepart();

			if (!selectedFiliala.equals("")) {
				filCrnt = selectedFiliala;
			} else
				filCrnt = UserInfo.getInstance().getUnitLog();

			if (UserInfo.getInstance().getTipAcces().equals("17") || (selectedTipUser != null && selectedTipUser.getCodTip().equals("CV"))) // consilier
			{
				tipUser = "CV";
				paramDepart = "11";
			}

			if (UserInfo.getInstance().getTipAcces().equals("18")) // sef de
																	// magazin
			{
				tipUser = "SM";
				paramDepart = "11";
			}

			if (UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("35")) // ka
			{
				tipUser = "KA";
				paramDepart = "10";
			}

			if (selectedTipUser != null && selectedTipUser.getCodTip().equals("KA")) // ka
			{
				tipUser = "KA";
				paramDepart = "10";
			}

			if (selectedTipUser != null && selectedTipUser.getCodTip().equals("AG")) // ka
			{
				tipUser = "AV";
			}

			String paramInterval = intervalAfisare;
			if (intervalAfisare.equals("3")) {
				String dataStart = dataSelStart.replace("#", ".");
				String dataStop = dataSelStop.replace("#", ".");
				paramInterval = dataStart + "#" + dataStop;
			}

			params.put("filiala", filCrnt);
			params.put("codUser", fullCode);
			params.put("tipCmd", "0");
			params.put("depart", paramDepart);
			params.put("interval", paramInterval);
			params.put("restrictii", String.valueOf(restrictiiAfisare));
			params.put("codClient", selectedClient);
			params.put("tipUser", tipUser);

			comenzi.getListComenzi(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void performArtCmd() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "";
			if (UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("35"))
				tipUser = "KA";

			params.put("nrCmd", selectedCmd);
			params.put("afisCond", "1");
			params.put("tipUser", tipUser);

			comenzi.getArticoleComandaJSON(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	boolean isCVorSM() {
		return UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18");
	}

	private void populateArtCmdList(BeanArticoleAfisare articoleComanda) {

		layoutDetaliiCmd.setVisibility(View.VISIBLE);

		DateLivrareAfisare dateLivrare = articoleComanda.getDateLivrare();
		List<ArticolComanda> listArticole = articoleComanda.getListArticole();

		if (isCVorSM()) {
		} else {
			textPondereArtBAfis.setVisibility(View.VISIBLE);
		}

		textTipPlata.setText(UtilsGeneral.getDescTipPlata(dateLivrare.getTipPlata()));
		textTransport.setText(UtilsGeneral.getDescTipTransport(dateLivrare.getTransport()));
		textCantar.setText(dateLivrare.getCantar().equals("1") ? "Cu cantarire" : "Fara cantarire");
		textOras.setText(dateLivrare.getOras());
		textJudet.setText(InfoStrings.numeJudet(dateLivrare.getCodJudet()));
		textAdrLivr.setText(dateLivrare.getDateLivrare());
		textPersContact.setText(dateLivrare.getPersContact());
		textTelefon.setText(dateLivrare.getNrTel());
		textObsLivrare.setText(dateLivrare.getObsLivrare());

		ArticolAfisAdapter adapter = new ArticolAfisAdapter(this, listArticole);
		listViewArticole.setAdapter(adapter);

		ValoriComanda valoriComanda = adapter.getValoriComanda(listArticole);

		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			layoutMarja.setVisibility(View.VISIBLE);
			textValoareMarja.setText(String.format("%.02f", valoriComanda.getMarja()) + "  RON");
		}

		if (textPondereArtBAfis.getVisibility() == View.VISIBLE) {
			textPondereArtBAfis.setText(String.format("%.02f", (valoriComanda.getPondereB() / valoriComanda.getTotal()) * 100) + "%");

		}

	}

	private void showListComenzi(List<BeanComandaCreata> listComenziCreate) {

		listViewArticole.setAdapter(null);

		if (listComenziCreate.size() > 0) {

			spinnerCmd.setVisibility(View.VISIBLE);

			layoutTotalCmd.setVisibility(View.VISIBLE);

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			ComandaAfisAdapter adapterComenzi = new ComandaAfisAdapter(listComenziCreate, this);
			spinnerCmd.setAdapter(adapterComenzi);

			selectedCmd = adapterComenzi.getItem(0).getId();

			textTotalCmd.setText(nf2.format(ComandaCreata.getInstance().getTotalComenzi(listComenziCreate)) + " RON");
			textTotalTvaCmd.setText(nf2.format(ComandaCreata.getInstance().getTotalComenziTva(listComenziCreate)) + " RON");

		} else {

			noComenziLayout();
		}

	}

	void noComenziLayout() {

		spinnerCmd.setVisibility(View.INVISIBLE);
		selectedCmd = "-1";

		listViewArticole.setAdapter(null);
		spinnerCmd.setAdapter(new ComandaAfisAdapter(new ArrayList<BeanComandaCreata>(), AfisComanda.this));

		textValoareMarja.setText("");

		layoutTotalCmd.setVisibility(View.INVISIBLE);
		layoutDetaliiCmd.setVisibility(View.INVISIBLE);

		totalComenzi = 0;
		totalComenziTva = 0;

		textTotalCmd.setText("0.00 RON");
		textTotalTvaCmd.setText("0.00 RON");

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

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	@Override
	public String toString() {
		return "AfisComanda [codAgent=" + codAgent + ", cmdNr=" + cmdNr + "]";
	}

	void addSpinnerCmdListener() {
		spinnerCmd.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				textTipPlata.setText("");
				textAdrLivr.setText("");
				textPersContact.setText("");
				textObsLivrare.setText("");
				textTelefon.setText("");
				textCantar.setText("");
				textTransport.setText("");
				textJudet.setText("");
				textOras.setText("");

				textPondereArtBAfis.setText("");
				textValoareMarja.setText("");

				if (!selectedCmd.equals("-1")) {
					BeanComandaCreata objCmd = (BeanComandaCreata) arg0.getAdapter().getItem(arg2);
					selectedCmd = objCmd.getId();
					performArtCmd();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map, int position) {

		switch (spinnerId) {

		case R.id.spinnerFiliala:

			String filNr = map.get("codFiliala");

			if (filNr.trim().equals(""))
				filNr = "-1"; // fara selectie
			if (filNr.equals("00000000"))
				filNr = "0";

			totalComenzi = 0;
			totalComenziTva = 0;

			textTotalCmd.setText("0.00 RON");
			textTotalTvaCmd.setText("0.00 RON");

			selectedFiliala = filNr;

			selectedCmd = "-1";
			selectedAgent = "-1";

			listAgenti.clear();
			adapterAgenti.notifyDataSetChanged();

			spinnerCmd.setAdapter(new ComandaAfisAdapter(new ArrayList<BeanComandaCreata>(), AfisComanda.this));

			if (!filNr.equals("-1")) {
				performGetAgenti();
			}
			break;

		case R.id.spinnerAgentiAfisCmd:

			agentNr = map.get("codAgent");

			if (agentNr.trim().equals(""))
				agentNr = "0"; // fara selectie
			if (agentNr.equals("00000000"))
				agentNr = "0";

			selectedAgent = agentNr;

			selectedCmd = "-1";

			if (spinnerAgentiAfisCmd.getSelectedItemPosition() > 0) {
				performGetComenzi();
			} else {
				noComenziLayout();
			}

			break;

		case R.id.spinnerTipUser:

			if (isDirectorDistrib()) {
				selectedTipUser = getEnumReducere(map.get("stringId"));
				populateListaAgenti(selectedTipUser);
			}

			break;

		}

	}

	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateAgentiList(listAgenti);

	}

	public void operationIntervalComplete(String intervalAfisare, String dataSelStart, String dataSelStop) {
		this.intervalAfisare = intervalAfisare;
		this.dataSelStart = dataSelStart;
		this.dataSelStop = dataSelStop;

		if (isAgentSelected())
			performGetComenzi();

	}

	boolean isAgentSelected() {
		if (spinnerAgentiAfisCmd.getVisibility() == View.VISIBLE)
			return spinnerAgentiAfisCmd.getSelectedItemPosition() > 0;
		else
			return true;
	}

	public void operationClientComplete(String client) {
		this.selectedClient = client;
		performGetComenzi();
	}

	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {

		switch (methodName) {
		case GET_LIST_COMENZI:
			showListComenzi(comenzi.getComenziDivizie(selectedDivizie));
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			populateArtCmdList(comenzi.deserializeArticoleComanda((String) result));
			break;
		default:
			break;

		}

	}

	public void divizieSelected(String divizie) {
		this.selectedDivizie = divizie;
		showListComenzi(comenzi.getComenziDivizie(selectedDivizie));

	}

}