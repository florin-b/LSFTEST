/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.AsyncTaskListener;
import listeners.MapListener;
import listeners.OperatiiAdresaListener;
import listeners.OperatiiClientListener;
import model.DateLivrare;
import model.ListaArticoleComandaGed;
import model.OperatiiAdresa;
import model.OperatiiAdresaImpl;
import model.OperatiiClient;
import model.UserInfo;
import utils.MapUtils;
import utils.UtilsAddress;
import utils.UtilsComenzi;
import utils.UtilsDates;
import utils.UtilsGeneral;
import utils.UtilsUser;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.Address;
import beans.BeanAdresaLivrare;
import beans.BeanAdreseJudet;
import beans.BeanClient;
import beans.GeocodeAddress;
import beans.StatusIntervalLivrare;

import com.google.android.gms.maps.model.LatLng;

import dialogs.MapAddressDialog;
import dialogs.SelectDateDialog;
import enums.EnumClienti;
import enums.EnumJudete;
import enums.EnumLocalitate;
import enums.EnumOperatiiAdresa;

public class SelectAdrLivrCmdGed extends Activity implements AsyncTaskListener, OperatiiClientListener, OperatiiAdresaListener, MapListener {

	private Button saveAdrLivrBtn;
	private EditText txtPers, txtTel, txtObservatii;

	private TextView txtObsPlata, textMail;

	private static final String METHOD_NAME = "getClientJud";

	String[] tipPlataOnline = { "E - Numerar la livrare", "INS - Card online", "O - Virament bancar" };

	String[] tipTransport = { "TRAP - Transport Arabesque", "TCLI - Transport client" };

	String[] tipTransportOnline = { "TRAP - Transport Arabesque", "TCLI - Transport client", "TERT - Transport tert" };

	String[] docInsot = { "Factura", "Aviz de expeditie" };

	public SimpleAdapter adapterJudete, adapterJudeteLivrare, adapterAdreseLivrare;

	private Spinner spinnerPlata, spinnerTransp, spinnerJudet, spinnerTermenPlata, spinnerDocInsot, spinnerJudetLivrare;
	private static ArrayList<HashMap<String, String>> listJudete = null, listJudeteLivrare = null;
	private ArrayAdapter<String> adapterTermenPlata;
	private LinearLayout layoutAdr1, layoutAdr2, layoutMail;

	int posJudetSel = 0;
	private NumberFormat nf;
	String articoleSite, umSite, cantSite, valSite;
	private String codJudetLivrare = "";
	private Spinner spinnerAdreseLivrare;

	public static RadioButton radioAdresaSediu, radioAltaAdresa;
	LinearLayout layoutAdrLivrare1, layoutAdrLivrare2;

	private ArrayList<HashMap<String, String>> listAdreseLivrare = null;
	private OperatiiClient operatiiClient;
	private List<BeanAdresaLivrare> listAdrese = null;
	private RadioButton radioListAdrese, radioTextAdrese;
	private LinearLayout layoutHeaderAdrese, layoutListAdrese, layoutAdrOras, layoutAdrStrada;
	private CheckBox checkMacara, chkbClientLaRaft;
	private OperatiiAdresa operatiiAdresa;
	private AutoCompleteTextView textLocalitate, textStrada, textLocalitateLivrare, textStradaLivrare;
	private Button btnPozitieAdresa;
	private EditText textNrStr;
	private Spinner spinnerIndoire, spinnerTonaj;
	private TextView textDataLivrare;
	private Button btnDataLivrare;
	private Spinner spinnerMeseriasi;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectadrlivrcmd_ged_header);

		try {

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Date livrare");
			actionBar.setDisplayHomeAsUpEnabled(true);

			nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);

			this.saveAdrLivrBtn = (Button) findViewById(R.id.saveAdrLivrBtn);
			addListenerSaveAdr();

			this.layoutAdr1 = (LinearLayout) findViewById(R.id.layoutAdr1);
			this.layoutAdr2 = (LinearLayout) findViewById(R.id.layoutAdr2);

			textLocalitate = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocalitate);

			textNrStr = (EditText) findViewById(R.id.textNrStr);

			btnPozitieAdresa = (Button) findViewById(R.id.btnPozitieAdresa);
			setListnerBtnPozitieAdresa();

			chkbClientLaRaft = (CheckBox) findViewById(R.id.clientRaft);
			addListenerClientLaRaft();

			Bundle bundle = getIntent().getExtras();

			if (bundle.getString("parrentClass") != null && bundle.getString("parrentClass").equals("ModificareComanda")) {
				LinearLayout layoutRaft = (LinearLayout) findViewById(R.id.layoutClientRaft);
				layoutRaft.setVisibility(View.GONE);
			}

			textLocalitateLivrare = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocLivrare);

			textStrada = (AutoCompleteTextView) findViewById(R.id.autoCompleteStrada);
			textStrada.setText(UtilsAddress.getStreetFromAddress(DateLivrare.getInstance().getStrada()));

			textNrStr.setText(UtilsAddress.getStreetNumber(DateLivrare.getInstance().getStrada()));

			textStradaLivrare = (AutoCompleteTextView) findViewById(R.id.autoCompleteStradaLivrare);

			layoutMail = (LinearLayout) findViewById(R.id.layoutMail);
			textMail = (EditText) findViewById(R.id.txtMail);

			DateLivrare dateLivrareInstance = DateLivrare.getInstance();

			txtPers = (EditText) findViewById(R.id.txtPersCont);
			txtTel = (EditText) findViewById(R.id.txtTelefon);
			txtPers.setText(dateLivrareInstance.getPersContact());
			txtTel.setText(dateLivrareInstance.getNrTel());

			txtObservatii = (EditText) findViewById(R.id.txtObservatii);
			txtObservatii.setText(dateLivrareInstance.getObsLivrare());

			txtObsPlata = (EditText) findViewById(R.id.txtObsPlata);
			txtObsPlata.setText(dateLivrareInstance.getObsPlata());

			chkbClientLaRaft.setChecked(dateLivrareInstance.isClientRaft());

			spinnerPlata = (Spinner) findViewById(R.id.spinnerPlata);

			ArrayAdapter<String> adapterSpinnerPlata;
			ArrayAdapter<String> adapterSpinnerTransp;

			layoutMail.setVisibility(View.VISIBLE);
			textMail.setText(dateLivrareInstance.getMail().trim().replace("~", "@"));

			setTipTransportOptions();

			if (UserInfo.getInstance().getUserSite().equals("X")) {
				adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataOnline);
				adapterSpinnerTransp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipTransportOnline);

			} else {

				boolean isRestrictieMetPlata = !DateLivrare.getInstance().isFacturaCmd();

				if (!DateLivrare.getInstance().getTipPersClient().equalsIgnoreCase("PF"))
					isRestrictieMetPlata = false;

				adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UtilsComenzi.tipPlataGed(isRestrictieMetPlata));
				adapterSpinnerTransp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipTransport);

			}

			adapterSpinnerPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerPlata.setAdapter(adapterSpinnerPlata);

			UtilsComenzi.setDefaultPlataMethod(spinnerPlata);

			addListenerTipPlata();

			checkMacara = (CheckBox) findViewById(R.id.checkMacara);
			setMacaraVisible();
			setListenerCheckMacara();

			spinnerIndoire = (Spinner) findViewById(R.id.spinnerIndoire);
			setupSpinnerIndoire();

			spinnerTransp = (Spinner) findViewById(R.id.spinnerTransp);
			spinnerTonaj = (Spinner) findViewById(R.id.spinnerTonaj);
			setupSpinnerTonaj();

			adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTransp.setAdapter(adapterSpinnerTransp);
			addListenerTipTransport();

			spinnerJudet = (Spinner) findViewById(R.id.spinnerJudet);
			spinnerJudet.setOnItemSelectedListener(new regionSelectedListener());

			spinnerJudet.setOnTouchListener(new regioClickListener());

			operatiiAdresa = new OperatiiAdresaImpl(this);
			operatiiAdresa.setOperatiiAdresaListener(this);

			listJudete = new ArrayList<HashMap<String, String>>();
			adapterJudete = new SimpleAdapter(this, listJudete, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
					R.id.textNumeJudet, R.id.textCodJudet });

			spinnerTermenPlata = (Spinner) findViewById(R.id.spinnerTermenPlata);
			adapterTermenPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapterTermenPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTermenPlata.setAdapter(adapterTermenPlata);

			addAdresaLivrare();

			adapterTermenPlata.add("C000");
			if (DateLivrare.getInstance().getTermenPlata().trim().length() > 0) {
				String[] tokTermen = DateLivrare.getInstance().getTermenPlata().split(";");
				int nrLivr = 0;
				for (nrLivr = 0; nrLivr < tokTermen.length; nrLivr++) {
					if (!tokTermen[nrLivr].equals("C000"))
						adapterTermenPlata.add(tokTermen[nrLivr]);
				}

			}

			addListenerTermenPlata();

			int i = 0;

			// document insotitor
			spinnerDocInsot = (Spinner) findViewById(R.id.spinnerDocInsot);
			ArrayAdapter<String> adapterSpinnerDocInsot = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docInsot);
			adapterSpinnerDocInsot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDocInsot.setAdapter(adapterSpinnerDocInsot);
			spinnerDocInsot.setSelection(Integer.valueOf(dateLivrareInstance.getTipDocInsotitor()) - 1);
			// sf. doc insot

			String strTipPlata = "";
			// tip plata
			for (i = 0; i < adapterSpinnerPlata.getCount(); i++) {

				strTipPlata = adapterSpinnerPlata.getItem(i).toString().substring(0, adapterSpinnerPlata.getItem(i).toString().indexOf("-") - 1).trim();

				if (strTipPlata.equals(dateLivrareInstance.getTipPlata())) {
					spinnerPlata.setSelection(i);
					break;
				}
			}

			// tip transport
			for (i = 0; i < adapterSpinnerTransp.getCount(); i++) {
				if (adapterSpinnerTransp.getItem(i).toString().substring(0, 4).equals(dateLivrareInstance.getTransport())) {
					spinnerTransp.setSelection(i);
					break;
				}
			}

			layoutAdr1.setVisibility(View.VISIBLE);
			layoutAdr2.setVisibility(View.VISIBLE);

			performGetJudete();

			radioListAdrese = (RadioButton) findViewById(R.id.radioListAdrese);
			radioTextAdrese = (RadioButton) findViewById(R.id.radioTextAdrese);

			layoutHeaderAdrese = (LinearLayout) findViewById(R.id.layoutHeaderAdrese);
			layoutListAdrese = (LinearLayout) findViewById(R.id.layoutListAdrese);

			layoutAdrOras = (LinearLayout) findViewById(R.id.layoutAdr1);
			layoutAdrStrada = (LinearLayout) findViewById(R.id.layoutAdr2);

			layoutHeaderAdrese.setVisibility(View.GONE);
			layoutListAdrese.setVisibility(View.GONE);

			operatiiClient = new OperatiiClient(this);
			operatiiClient.setOperatiiClientListener(SelectAdrLivrCmdGed.this);

			if (UserInfo.getInstance().getTipUserSap().equals("KA3") && DateLivrare.getInstance().getTipPersClient().equals("D")) {

				setupListAdreseLayout();
			}

			textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

			if (!dateLivrareInstance.getDataLivrare().isEmpty())
				textDataLivrare.setText(dateLivrareInstance.getDataLivrare());
			else {
				textDataLivrare.setText(UtilsDates.getCurrentDate());
				dateLivrareInstance.setDataLivrare(UtilsDates.getCurrentDate());
			}

			btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
			addListenerDataLivrare();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codFiliala", UserInfo.getInstance().getUnitLog());

			spinnerMeseriasi = (Spinner) findViewById(R.id.spinnerMeseriasi);
			//operatiiClient.getMeseriasi(params);

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void addListenerDataLivrare() {
		btnDataLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Locale.setDefault(new Locale("ro"));

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SelectDateDialog datePickerDialog = new SelectDateDialog(SelectAdrLivrCmdGed.this, datePickerListener, year, month, day);
				datePickerDialog.setTitle("Data livrare");

				datePickerDialog.show();

			}
		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				StatusIntervalLivrare statusInterval = UtilsDates.getStatusIntervalLivrare(calendar.getTime());

				if (statusInterval.isValid()) {
					textDataLivrare.setText(displayFormat.format(calendar.getTime()));
					DateLivrare.getInstance().setDataLivrare(displayFormat.format(calendar.getTime()));
				} else
					Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	};

	private void addListenerClientLaRaft() {

		chkbClientLaRaft.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					chkbClientLaRaft.setText("Da");
				} else {
					chkbClientLaRaft.setText("Nu");
				}

				DateLivrare.getInstance().setClientRaft(isChecked);
			}

		});

	}

	private void setupListAdreseLayout() {

		layoutAdrOras.setVisibility(View.GONE);
		layoutAdrStrada.setVisibility(View.GONE);

		layoutHeaderAdrese.setVisibility(View.VISIBLE);
		layoutListAdrese.setVisibility(View.VISIBLE);

		setListenerRadioLista();

		setListenerRadioText();

		spinnerAdreseLivrare = (Spinner) findViewById(R.id.spinnerAdreseLivrare);
		getAdreseLivrareClient();
		setSpinnerAdreseItemSelectedListener();

		spinnerAdreseLivrare.setOnTouchListener(new SpinnerAdreseTouchListener());

	}

	private void setTipTransportOptions() {
		if (UtilsUser.isUserExceptieBV90Ged()) {
			List<String> tempArray = new ArrayList<String>(Arrays.asList(tipTransport));
			tempArray.add("TERT - Transport tert");
			tipTransport = tempArray.toArray(new String[tempArray.size()]);

		}

	}

	private void setListenerCheckMacara() {
		checkMacara.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DateLivrare.getInstance().setMasinaMacara(isChecked);
			}

		});
	}

	public class SpinnerAdreseTouchListener implements View.OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (existaArticole()) {
					Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele.", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			return false;
		}

	}

	private void setSpinnerAdreseItemSelectedListener() {
		spinnerAdreseLivrare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				DateLivrare.getInstance().setCodJudet(listAdrese.get(arg2).getCodJudet());
				DateLivrare.getInstance().setOras(listAdrese.get(arg2).getOras());
				DateLivrare.getInstance().setStrada(listAdrese.get(arg2).getOras());

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void getAdreseLivrareClient() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codClient", CreareComandaGed.codClientVar);
		operatiiClient.getAdreseLivrareClient(params);

	}

	private void setListenerRadioLista() {
		radioListAdrese.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (radioListAdrese.isChecked()) {
					layoutListAdrese.setVisibility(View.VISIBLE);
					layoutAdrOras.setVisibility(View.GONE);
					layoutAdrStrada.setVisibility(View.GONE);
				}

			}
		});
	}

	private void setListenerRadioText() {
		radioTextAdrese.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (radioTextAdrese.isChecked()) {
					layoutListAdrese.setVisibility(View.GONE);
					layoutAdrOras.setVisibility(View.VISIBLE);
					layoutAdrStrada.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	private void setupSpinnerTonaj() {

		String[] tonajValues = { "Selectati tonajul", "3.5 T", "10 T", "Fara restrictie de tonaj" };

		ArrayAdapter<String> adapterTonaj = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tonajValues);
		adapterTonaj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTonaj.setAdapter(adapterTonaj);

		if (DateLivrare.getInstance().getTonaj() != null)
			for (int i = 0; i < spinnerTonaj.getCount(); i++)
				if (spinnerTonaj.getItemAtPosition(i).toString().toUpperCase().contains(DateLivrare.getInstance().getTonaj())) {
					spinnerTonaj.setSelection(i);
					break;
				}

	}

	private void setupSpinnerIndoire() {

		String[] indoireValues = { "Tip prelucrare fier-beton 6 m", "TAIERE", "INDOIRE" };

		ArrayAdapter<String> adapterIndoire = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, indoireValues);
		adapterIndoire.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerIndoire.setAdapter(adapterIndoire);

	}

	private void addAdresaLivrare() {

		radioAdresaSediu = (RadioButton) findViewById(R.id.radioAdresaSediu);
		radioAltaAdresa = (RadioButton) findViewById(R.id.radioAltaAdresa);

		addListenerRadioAdresaSediu();
		addListenerRadioAltaAdresa();

		layoutAdrLivrare1 = (LinearLayout) findViewById(R.id.layoutAdrLivrare1);
		layoutAdrLivrare1.setVisibility(View.GONE);
		layoutAdrLivrare2 = (LinearLayout) findViewById(R.id.layoutAdrLivrare2);
		layoutAdrLivrare2.setVisibility(View.GONE);

		spinnerJudetLivrare = (Spinner) findViewById(R.id.spinnerJudetLivrare);
		spinnerJudetLivrare.setOnItemSelectedListener(new regionLivrareSelectedListener());

		if (DateLivrare.getInstance().isAltaAdresa()) {
			radioAltaAdresa.setChecked(true);

			textLocalitateLivrare.setText(DateLivrare.getInstance().getOrasD());
			textStradaLivrare.setText(DateLivrare.getInstance().getAdresaD());
			setJudetLivrare();
		}

	}

	private void setJudetLivrare() {

		if (DateLivrare.getInstance().getCodJudetD() != null) {

			listJudeteLivrare = new ArrayList<HashMap<String, String>>();

			adapterJudeteLivrare = new SimpleAdapter(this, listJudeteLivrare, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
					R.id.textNumeJudet, R.id.textCodJudet });

			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("numeJudet", UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudetD()));
			temp.put("codJudet", DateLivrare.getInstance().getCodJudetD());
			listJudeteLivrare.add(temp);

			spinnerJudetLivrare.setAdapter(adapterJudeteLivrare);
			spinnerJudetLivrare.setSelection(0);

		}

	}

	private void addJudetLivrare() {

		listJudeteLivrare = new ArrayList<HashMap<String, String>>(listJudete);

		adapterJudeteLivrare = new SimpleAdapter(this, listJudeteLivrare, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
				R.id.textNumeJudet, R.id.textCodJudet });

		spinnerJudetLivrare.setAdapter(adapterJudeteLivrare);
		spinnerJudetLivrare.setSelection(0);

		textStradaLivrare.setText("");

	}

	private void addListenerRadioAltaAdresa() {

		radioAltaAdresa.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (existaArticole()) {
					Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele.", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});

		radioAltaAdresa.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {

					layoutAdrLivrare1.setVisibility(View.VISIBLE);
					layoutAdrLivrare2.setVisibility(View.VISIBLE);
					DateLivrare.getInstance().setAltaAdresa(true);
					addJudetLivrare();

				}

			}
		});

	}

	private void addListenerRadioAdresaSediu() {

		radioAdresaSediu.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (existaArticole()) {
					Toast.makeText(getApplicationContext(), "Stergeti toate articolele", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});

		radioAdresaSediu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					layoutAdrLivrare1.setVisibility(View.GONE);
					layoutAdrLivrare2.setVisibility(View.GONE);
					DateLivrare.getInstance().setCodJudetD(null);
					DateLivrare.getInstance().setOrasD(null);
					DateLivrare.getInstance().setAdresaD(null);
					DateLivrare.getInstance().setAltaAdresa(false);

				}

			}
		});

	}

	private void performGetJudete() {

		if (UtilsUser.isUserSite()) {

			fillJudeteClient(EnumJudete.getRegionCodes());

		} else {
			String unitLog = UserInfo.getInstance().getUnitLog();

			if (unitLog.equals("NN10"))
				unitLog = "AG10";

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filiala", unitLog);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
			call.getCallResults();
		}

	}

	private void fillJudeteClient(String arrayJudete) {

		HashMap<String, String> temp;
		String numeJudSel = "";
		int i;
		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati judetul");
		temp.put("codJudet", "");
		listJudete.add(temp);

		int nrJud = 0;
		for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {

			if (arrayJudete.contains(UtilsGeneral.codJudete[i])) {
				temp = new HashMap<String, String>();
				temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
				temp.put("codJudet", UtilsGeneral.codJudete[i]);
				listJudete.add(temp);

				nrJud++;

				if (DateLivrare.getInstance().getCodJudet().equals(UtilsGeneral.codJudete[i])) {
					posJudetSel = nrJud;
					numeJudSel = UtilsGeneral.numeJudete[i];
				}
			}

		}

		spinnerJudet.setAdapter(adapterJudete);

		if (posJudetSel > 0) {
			DateLivrare.getInstance().setNumeJudet(numeJudSel);
			spinnerJudet.setSelection(posJudetSel);
		}

	}

	private void addListenerTermenPlata() {
		spinnerTermenPlata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				DateLivrare.getInstance().setTermenPlata(spinnerTermenPlata.getSelectedItem().toString());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void addListenerTipPlata() {

		spinnerPlata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				String rawTipPlataStr = spinnerPlata.getSelectedItem().toString();
				DateLivrare.getInstance().setTipPlata(rawTipPlataStr.substring(0, rawTipPlataStr.indexOf("-") - 1).trim());

				if (pos == 0 || pos == 1 || pos == 2 || pos == 3) {
					if (spinnerTermenPlata != null)
						spinnerTermenPlata.setVisibility(View.VISIBLE);
				}

				if (pos == 2) {

					if (DateLivrare.getInstance().getTipPersClient().equals("PJ"))
						Toast.makeText(getApplicationContext(), "Valoare maxima comanda: 5000 RON", Toast.LENGTH_SHORT).show();

				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void addListenerTipTransport() {
		spinnerTransp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				if (pos == 0) {
					spinnerTonaj.setVisibility(View.VISIBLE);

				} else if (pos == 1) {
					DateLivrare.getInstance().setValTransport(0);
					DateLivrare.getInstance().setValTransportSAP(0);
					DateLivrare.getInstance().setMasinaMacara(false);
					checkMacara.setVisibility(View.INVISIBLE);
					spinnerTonaj.setVisibility(View.INVISIBLE);
					spinnerTonaj.setSelection(0);

				}

				else {
					checkMacara.setChecked(DateLivrare.getInstance().isMasinaMacara());
					setMacaraVisible();
					spinnerTonaj.setVisibility(View.INVISIBLE);
					spinnerTonaj.setSelection(0);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void setMacaraVisible() {
		if (UtilsUser.isMacaraVisible())
			checkMacara.setVisibility(View.VISIBLE);
		else
			checkMacara.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class regionSelectedListener implements OnItemSelectedListener {

		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (spinnerJudet.getAdapter().getCount() == 1)
				return;

			if (spinnerJudet.getSelectedItemPosition() > 0) {

				HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
				DateLivrare.getInstance().setNumeJudet(tempMap.get("numeJudet"));
				DateLivrare.getInstance().setCodJudet(tempMap.get("codJudet"));

				HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
				params.put("codJudet", DateLivrare.getInstance().getCodJudet());
				operatiiAdresa.getAdreseJudet(params, EnumLocalitate.LOCALITATE_SEDIU);

			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}

	}

	private void populateListLocSediu(BeanAdreseJudet listAdrese) {

		textLocalitate.setVisibility(View.VISIBLE);
		textLocalitate.setText(DateLivrare.getInstance().getOras());

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		textLocalitate.setThreshold(0);
		textLocalitate.setAdapter(adapterLoc);
		setListenerTextLocalitate();

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		textStrada.setThreshold(0);
		textStrada.setAdapter(adapterStrazi);
		textStrada.setText(UtilsAddress.getStreetFromAddress(DateLivrare.getInstance().getStrada()));
		textNrStr.setText(UtilsAddress.getStreetNumber(DateLivrare.getInstance().getStrada()));

		setListenerTextStrada();

	}

	private void setListenerTextLocalitate() {

		textLocalitate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DateLivrare.getInstance().setOras(textLocalitate.getText().toString().trim());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setListenerTextStrada() {

		textStrada.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DateLivrare.getInstance().setStrada(textStrada.getText().toString().trim());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void populateListLocLivrare(BeanAdreseJudet listAdrese) {

		textLocalitateLivrare.setVisibility(View.VISIBLE);
		textLocalitateLivrare.setText(DateLivrare.getInstance().getOrasD().trim());

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		textLocalitateLivrare.setThreshold(0);
		textLocalitateLivrare.setAdapter(adapterLoc);
		setListenerTextLocalitateLivrare();

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		textStradaLivrare.setVisibility(View.VISIBLE);
		textStradaLivrare.setThreshold(0);
		textStradaLivrare.setAdapter(adapterStrazi);
		setListenerTextStradaLivrare();

	}

	private void setListenerTextLocalitateLivrare() {

		textLocalitateLivrare.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DateLivrare.getInstance().setOrasD(textLocalitateLivrare.getText().toString().trim());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setListenerTextStradaLivrare() {

		textStradaLivrare.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DateLivrare.getInstance().setAdresaD(textStradaLivrare.getText().toString().trim());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	public class regioClickListener implements View.OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (existaArticole()) {
					Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele.", Toast.LENGTH_SHORT).show();
					return true;
				}
			}

			return false;
		}

	}

	public class regionLivrareSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (spinnerJudetLivrare.getSelectedItemPosition() > 0) {

				HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudetLivrare.getSelectedItem();
				codJudetLivrare = tempMap.get("codJudet");

				DateLivrare.getInstance().setCodJudetD(codJudetLivrare);

				if (!codJudetLivrare.trim().equals("")) {
					HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
					params.put("codJudet", DateLivrare.getInstance().getCodJudetD());

					OperatiiAdresa operatiiAdresa1 = new OperatiiAdresaImpl(SelectAdrLivrCmdGed.this);
					operatiiAdresa1.setOperatiiAdresaListener(SelectAdrLivrCmdGed.this);

					operatiiAdresa1.getAdreseJudet(params, EnumLocalitate.LOCALITATE_LIVRARE);

				}
			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	private boolean existaArticole() {
		return ListaArticoleComandaGed.getInstance().getListArticoleComanda() != null
				&& ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0;
	}

	public void addListenerSaveAdr() {
		saveAdrLivrBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				valideazaAdresaLivrare();

			}
		});

	}

	private void valideazaDateLivrare() {
		String adresa = "";
		String strada = "";
		String pers = "";
		String telefon = "";
		String observatii = "", obsPlata = " ", strMailAddr = " ";

		String nrStrada = "";

		if (textNrStr.getText().toString().trim().length() > 0)
			nrStrada = " NR " + textNrStr.getText().toString().trim();

		strada = textStrada.getText().toString().trim() + " " + nrStrada;

		DateLivrare dateLivrareInstance = DateLivrare.getInstance();

		dateLivrareInstance.setStrada(strada);
		dateLivrareInstance.setAdrLivrNoua(true);
		dateLivrareInstance.setAddrNumber(" ");

		pers = txtPers.getText().toString().trim();
		telefon = txtTel.getText().toString().trim();
		observatii = txtObservatii.getText().toString().trim();
		obsPlata = txtObsPlata.getText().toString().trim();
		strMailAddr = textMail.getText().toString().trim();

		if (observatii.trim().length() == 0)
			observatii = " ";

		if (obsPlata.trim().length() == 0)
			obsPlata = " ";

		if (strMailAddr.trim().length() == 0)
			strMailAddr = " ";

		if (!(layoutListAdrese.getVisibility() == View.VISIBLE) && !(DateLivrare.getInstance().isAltaAdresa())) {
			if (dateLivrareInstance.getCodJudet().equals("")) {
				Toast.makeText(getApplicationContext(), "Selectati judetul!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (DateLivrare.getInstance().getOras().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Selectati localitatea!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (strada.trim().equals("") && !hasCoordinates()) {
				Toast.makeText(getApplicationContext(), "Completati strada sau pozitionati adresa pe harta!", Toast.LENGTH_SHORT).show();
				return;
			}

		}

		if (spinnerTransp.getSelectedItem().toString().toLowerCase().contains("tcli") && DateLivrare.getInstance().getTipPersClient().equals("PF")
				&& !DateLivrare.getInstance().isFacturaCmd()) {
			if (pers.equals(""))
				pers = " ";

			if (telefon.equals(""))
				telefon = " ";

		} else {
			if (pers.equals("")) {
				Toast.makeText(getApplicationContext(), "Completati persoana de contact!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (telefon.equals("")) {
				Toast.makeText(getApplicationContext(), "Completati nr. de telefon!", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (spinnerTransp.getSelectedItem().toString().toLowerCase().contains("arabesque") && spinnerTonaj.getSelectedItemPosition() == 0) {
			Toast.makeText(getApplicationContext(), "Selectati tonajul!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (radioAltaAdresa.isChecked()) {

			if (DateLivrare.getInstance().getOrasD().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Completati localitatea!", Toast.LENGTH_SHORT).show();
				return;
			}

		}

		dateLivrareInstance.setPersContact(pers);
		dateLivrareInstance.setNrTel(telefon);

		dateLivrareInstance.setTransport(spinnerTransp.getSelectedItem().toString().substring(0, 4));

		if (!isAdresaCorecta()) {
			Toast.makeText(getApplicationContext(), "Completati adresa corect sau pozitionati adresa pe harta.", Toast.LENGTH_LONG).show();
			return;
		}

		String cantar = "NU";

		dateLivrareInstance.setCantar("NU");

		String factRed = "NU";
		dateLivrareInstance.setRedSeparat(" ");

		String rawTipPlataStr = spinnerPlata.getSelectedItem().toString();

		dateLivrareInstance.setTipPlata(rawTipPlataStr.substring(0, rawTipPlataStr.indexOf("-") - 1).trim());

		adresa = dateLivrareInstance.getNumeJudet() + " " + dateLivrareInstance.getOras() + " " + dateLivrareInstance.getStrada();

		if (layoutHeaderAdrese.getVisibility() == View.VISIBLE && radioListAdrese.isChecked()) {
			int selectedAdress = spinnerAdreseLivrare.getSelectedItemPosition();
			adresa = UtilsGeneral.getNumeJudet(listAdrese.get(selectedAdress).getCodJudet()) + " " + listAdrese.get(selectedAdress).getOras() + " "
					+ listAdrese.get(selectedAdress).getStrada();

			dateLivrareInstance.setCodJudet(listAdrese.get(selectedAdress).getCodJudet());
			dateLivrareInstance.setOras(listAdrese.get(selectedAdress).getOras());
			dateLivrareInstance.setStrada(listAdrese.get(selectedAdress).getStrada());

		}

		if (DateLivrare.getInstance().isAltaAdresa()) {

			dateLivrareInstance.setAdresaD(textStradaLivrare.getText().toString().trim());

		}

		dateLivrareInstance.setDateLivrare(adresa + "#" + pers + "#" + telefon + "#" + cantar + "#" + dateLivrareInstance.getTipPlata() + "#"
				+ spinnerTransp.getSelectedItem().toString() + "#" + factRed + "#");

		dateLivrareInstance.setTermenPlata(spinnerTermenPlata.getSelectedItem().toString());

		dateLivrareInstance.setObsLivrare(observatii.replace("#", "-").replace("@", "-"));
		dateLivrareInstance.setObsPlata(obsPlata.replace("#", "-").replace("@", "-"));

		dateLivrareInstance.setMail(strMailAddr.replace("#", "-").replace("@", "~"));

		dateLivrareInstance.setTipDocInsotitor(String.valueOf(spinnerDocInsot.getSelectedItemPosition() + 1));

		dateLivrareInstance.setTonaj("-1");

		if (radioAltaAdresa.isChecked() && !DateLivrare.getInstance().isAltaAdresa()) {
			dateLivrareInstance.setDateLivrare(getAdrLivrareJSON());
		}

		if (isConditiiTonaj(spinnerTransp, spinnerTonaj)) {
			String[] tonaj = spinnerTonaj.getSelectedItem().toString().split(" ");
			dateLivrareInstance.setTonaj(tonaj[0]);
		} else
			dateLivrareInstance.setTonaj("-1");

		if (spinnerIndoire.getVisibility() == View.VISIBLE && spinnerIndoire.getSelectedItemPosition() > 0) {
			dateLivrareInstance.setPrelucrare(spinnerIndoire.getSelectedItem().toString());
		} else
			dateLivrareInstance.setPrelucrare("-1");

		//dateLivrareInstance.setCodMeserias(((BeanClient) spinnerMeseriasi.getSelectedItem()).getCodClient());
		
		dateLivrareInstance.setCodMeserias("0");

		finish();

	}

	private boolean hasCoordinates() {
		if (DateLivrare.getInstance().getCoordonateAdresa() == null)
			return false;
		else if (DateLivrare.getInstance().getCoordonateAdresa().latitude == 0)
			return false;

		return true;
	}

	private boolean isConditiiTonaj(Spinner spinnerTransp, Spinner spinnerTonaj) {
		return spinnerTransp.getSelectedItem().toString().toLowerCase().contains("arabesque")
				&& spinnerTonaj.getSelectedItem().toString().split(" ")[1].equals("T");

	}

	private void setAdresaLivrare(Address address) {

		if (address.getCity() != null && !address.getCity().isEmpty())
			textLocalitate.setText(address.getCity());

		if (address.getStreet() != null && !address.getStreet().isEmpty())
			textStrada.setText(address.getStreet());

		if (address.getNumber() != null && !address.getNumber().isEmpty())
			textNrStr.setText(address.getNumber());

	}

	private void valideazaAdresaLivrare() {

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codJudet", DateLivrare.getInstance().getCodJudet());
		params.put("localitate", DateLivrare.getInstance().getOras());

		operatiiAdresa.isAdresaValida(params, EnumLocalitate.LOCALITATE_SEDIU);

	}

	private String getTipTransport() {
		return spinnerTransp.getSelectedItem().toString().substring(0, 4);
	}

	private String getAdrLivrareJSON() {
		String jsonData = "";

		HashMap<String, String> adrData = new HashMap<String, String>();
		adrData.put("codJudet", codJudetLivrare);
		adrData.put("oras", DateLivrare.getInstance().getOrasD().trim());
		adrData.put("strada", textStradaLivrare.getText().toString().trim());

		EncodeJSONData jsonAdrLivrare = new EncodeJSONData(this, adrData);

		jsonData = jsonAdrLivrare.encodeAdresaLivrareCV();

		return jsonData;

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	private void dealTranspCmdSite(String result) {
		String[] pretResponse = {};

		if (!result.equals("-1")) {
			pretResponse = result.split("#");

			if (!spinnerTransp.getSelectedItem().toString().substring(0, 4).equals(pretResponse[1].toUpperCase(Locale.getDefault()))) {
				Toast.makeText(getApplicationContext(), "Tipul de transport recomandat este " + pretResponse[1], Toast.LENGTH_LONG).show();
			}
		} else {
			pretResponse[0] = "0.0";
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals(METHOD_NAME)) {
			fillJudeteClient((String) result);
		}

		if (methodName.equals("getValTransportComandaSite")) {
			dealTranspCmdSite((String) result);
		}

		if (methodName.equals("getValTransportConsilieri")) {

		}

	}

	private void setListnerBtnPozitieAdresa() {
		btnPozitieAdresa.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				android.app.FragmentManager fm = getFragmentManager();

				if (!isAdresaComplet())
					return;

				MapAddressDialog mapDialog = new MapAddressDialog(getAddressFromForm(), SelectAdrLivrCmdGed.this, fm);
				mapDialog.setMapListener(SelectAdrLivrCmdGed.this);
				mapDialog.show();
			}
		});
	}

	private Address getAddressFromForm() {
		Address address = new Address();

		if (radioAdresaSediu.isChecked()) {
			address.setCity(DateLivrare.getInstance().getOras());
			address.setStreet(UtilsAddress.getStreetNoNumber(DateLivrare.getInstance().getStrada()));
			address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));
		} else if (radioAltaAdresa.isChecked()) {
			address.setCity(DateLivrare.getInstance().getOrasD());
			address.setStreet(UtilsAddress.getStreetNoNumber(DateLivrare.getInstance().getAdresaD()));
			address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudetD()));
		}

		return address;
	}

	private boolean isAdresaComplet() {
		if (DateLivrare.getInstance().getCodJudet().equals("") || DateLivrare.getInstance().getCodJudetD().equals("")) {
			Toast.makeText(this, "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (DateLivrare.getInstance().getOras().equals("") || DateLivrare.getInstance().getOrasD().equals("")) {
			Toast.makeText(this, "Completati localitatea", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean isAdresaCorecta() {
		if (DateLivrare.getInstance().getTransport().equals("TRAP") && !hasCoordinates())
			return isAdresaGoogleOk();
		else
			return true;

	}

	private boolean isAdresaGoogleOk() {

		GeocodeAddress geoAddress = MapUtils.geocodeAddress(getAddressFromForm(), getApplicationContext());
		DateLivrare.getInstance().setCoordonateAdresa(geoAddress.getCoordinates());

		return geoAddress.isAdresaValida();

	}

	private void fillAdreseLivrareClient(String adreseLivrare) {

		listAdrese = operatiiClient.deserializeAdreseLivrare(adreseLivrare);

		listAdreseLivrare = new ArrayList<HashMap<String, String>>();
		adapterAdreseLivrare = new SimpleAdapter(this, listAdreseLivrare, R.layout.simplerowlayout_1, new String[] { "rowText", "rowId" }, new int[] {
				R.id.textRowName, R.id.textRowId });

		if (listAdrese.size() > 0) {

			HashMap<String, String> temp;

			String strAdresa = "";
			for (int i = 0; i < listAdrese.size(); i++) {
				temp = new HashMap<String, String>();

				strAdresa = UtilsGeneral.getNumeJudet(listAdrese.get(i).getCodJudet()) + "; " + listAdrese.get(i).getOras() + "; "
						+ listAdrese.get(i).getStrada() + "; " + listAdrese.get(i).getNrStrada() + ";";

				temp.put("rowText", strAdresa);
				temp.put("rowId", listAdrese.get(i).getCodAdresa());
				listAdreseLivrare.add(temp);

			}

			spinnerAdreseLivrare.setAdapter(adapterAdreseLivrare);
			adapterAdreseLivrare.notifyDataSetChanged();

		}

	}

	private void fillSpinnerMeseriasi(String result) {
		List<BeanClient> listMeseriasi = operatiiClient.deserializeListClienti(result);

		BeanClient client = new BeanClient();
		client.setNumeClient("Selectati un meserias");
		client.setCodClient("0");
		listMeseriasi.add(0, client);

		ArrayAdapter<BeanClient> spinnerArrayAdapter = new ArrayAdapter<BeanClient>(this, android.R.layout.simple_spinner_item, listMeseriasi);

		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMeseriasi.setAdapter(spinnerArrayAdapter);

		if (!DateLivrare.getInstance().getCodMeserias().isEmpty()) {

			for (int i = 0; i < spinnerMeseriasi.getAdapter().getCount(); i++)
				if (((BeanClient) spinnerMeseriasi.getAdapter().getItem(i)).getCodClient().equals(DateLivrare.getInstance().getCodMeserias()))
					spinnerMeseriasi.setSelection(i);
		}

	}

	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_ADRESE_LIVRARE:
			fillAdreseLivrareClient((String) result);
			break;
		case GET_MESERIASI:
			fillSpinnerMeseriasi((String) result);
			break;
		default:
			break;

		}

	}

	private void valideazaAdresaResponse(String result) {

		valideazaDateLivrare();

	}

	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {

		if (numeComanda == EnumOperatiiAdresa.IS_ADRESA_VALIDA) {
			valideazaAdresaResponse((String) result);
		} else {
			switch (tipLocalitate) {
			case LOCALITATE_SEDIU:
				populateListLocSediu(operatiiAdresa.deserializeListAdrese(result));
				break;
			case LOCALITATE_LIVRARE:
				populateListLocLivrare(operatiiAdresa.deserializeListAdrese(result));
				break;
			default:
				break;

			}
		}

	}

	@Override
	public void addressSelected(LatLng coord, android.location.Address address) {
		DateLivrare.getInstance().setCoordonateAdresa(coord);
		setAdresaLivrare(MapUtils.getAddress(address));

	}

}
