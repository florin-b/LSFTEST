/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import listeners.OperatiiAgentListener;
import listeners.OperatiiClientListener;
import model.HandleJSONData;
import model.OperatiiAgent;
import model.OperatiiClient;
import model.UserInfo;
import utils.ScreenUtils;
import utils.UtilsGeneral;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import beans.BeanClient;
import beans.DetaliiClient;
import enums.EnumClienti;
import enums.EnumDepartamente;

public class CLPFragment1 extends Fragment implements OperatiiClientListener, OperatiiAgentListener {

	private static EditText txtOras, txtStrada, txtPersCont, txtTelefon, txtTipMarfa, txtMasaMarfa;
	public static EditText txtNumeClient;

	private static TextView txtDataLivrare, textLimitaCredit, textRestCredit;

	private Button cautaClientBtn, saveClntBtn;

	private static ArrayList<HashMap<String, String>> listClienti = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterClienti, adapterJudete, adapterFiliale, adapterPlata;

	String[] judete = { "ALBA", "ARAD", "ARGES", "BACAU", "BIHOR", "BISTRITA-NASAUD", "BOTOSANI", "BRAILA", "BRASOV", "BUCURESTI", "BUZAU", "CALARASI",
			"CARAS-SEVERIN", "CLUJ", "CONSTANTA", "COVASNA", "DAMBOVITA", "DOLJ", "GALATI", "GIURGIU", "GORJ", "HARGHITA", "HUNEDOARA", "IALOMITA", "IASI",
			"ILFOV", "MARAMURES", "MEHEDINTI", "MURES", "NEAMT", "OLT", "PRAHOVA", "SALAJ", "SATU-MARE", "SIBIU", "SUCEAVA", "TELEORMAN", "TIMIS", "TULCEA",
			"VALCEA", "VASLUI", "VRANCEA" };

	String[] codJudete = { "01", "02", "03", "04", "05", "06", "07", "09", "08", "40", "10", "51", "11", "12", "13", "14", "15", "16", "17", "52", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31", "30", "32", "33", "34", "35", "36", "38", "37", "39" };

	String[] numeFiliala = { "Bacau", "Baia Mare", "Brasov", "Buzau", "Brasov-central", "Buc. Andronache", "Buc. Militari", "Buc. Otopeni", "Buc. Glina",
			"Constanta", "Cluj", "Craiova", "Focsani", "Galati", "Hunedoara", "Iasi", "Oradea", "Piatra Neamt", "Pitesti", "Ploiesti", "Timisoara", "Tg. Mures" };

	String[] codFiliala = { "BC10", "MM10", "BV10", "BZ10", "BV90", "BU13", "BU11", "BU12", "BU10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "HD10", "IS10",
			"BH10", "NT10", "AG10", "PH10", "TM10", "MS10" };

	String[] tipPlata = { "B - Bilet la ordin", "C - Cec", "E - Plata in numerar", "O - Ordin de plata" };

	String[] tipTransport = { "TRAP - Transport Arabesque", "TCLI - Transport client", "TERT - Transport curier" };

	String[] depozite = { "V1 - vanzare", "V2 - vanzare", "V3 - vanzare", "G1 - gratuite", "G2 - gratuite", "G3 - gratuite", "D1 - deteriorate",
			"D2 - deteriorate", "D3 - deteriorate", "DESC", "01RZ", "TAMP" };

	String[] tipCamion = { "Prelata", "Perdea", "Platforma", "Descoperita", "Macara" };
	String[] tipIncarcare = { "Complet", "Partial" };

	private ListView listViewClienti;
	private LinearLayout layoutClient;

	private static ArrayList<HashMap<String, String>> listJudete = null, listFiliale = null;

	String codClient = "", numeClient = "", canalTipClient = "10";

	private TextView codClientText;
	private TextView numeClientText, labelAgentiClp;

	public static EditText textSelClient;

	static final int DATE_DIALOG_ID = 1;

	private TextView labelCodClient, labelNumeClient, labelLimitaCredit, labelRestCredit, labelClient;
	LinearLayout selClientLayout;
	SlidingDrawer slidingDrawer;
	public static Spinner spinnerJudetCLP, spinnerFilialaCLP, spinnerPlataCLP, spinnerTransportCLP, spinnerDepozClp_Dest, spinnerAgentiCLP, spinnerDepartament,
			spinnerTipCamion, spinnerTipIncarcare;

	public static EditText txtObservatiiCLP;

	private static ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterAgenti;

	private static ArrayList<HashMap<String, String>> listDepart = null;

	private int mYear;
	private int mMonth;
	private int mDay;

	private HashMap<String, String> artMap = null;

	NumberFormat nf2;
	ToggleButton tglTipClient;

	LinearLayout layoutSelAgentiClp, layoutDepartament, layoutTipClient;

	public static RadioButton radioClient, radioFiliala, radioClientPF, radioClientPJ;
	public static String departamentConsilier = "";

	public static final CLPFragment1 newInstance() {
		CLPFragment1 f = new CLPFragment1();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	private OperatiiClient operatiiClient;
	private OperatiiAgent operatiiAgent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.clp_fragment_1, container, false);

		try {

			this.layoutClient = (LinearLayout) v.findViewById(R.id.layoutClient);
			layoutClient.setVisibility(View.INVISIBLE);

			operatiiClient = new OperatiiClient(v.getContext());
			operatiiClient.setOperatiiClientListener(this);

			operatiiAgent = OperatiiAgent.getInstance();
			operatiiAgent.setOperatiiAgentListener(this);

			txtNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
			txtNumeClient.setHint("Introduceti nume client");

			this.cautaClientBtn = (Button) v.findViewById(R.id.clientBtn);
			addListenerCautaClient();

			listClienti = new ArrayList<HashMap<String, String>>();
			adapterClienti = new SimpleAdapter(getActivity(), listClienti, R.layout.customrownumeclient, new String[] { "numeClient", "codClient" }, new int[] {
					R.id.textNumeClient, R.id.textCodClient });

			listViewClienti = (ListView) v.findViewById(R.id.listClienti);
			listViewClienti.setOnItemClickListener(new MyOnItemSelectedListener());
			listViewClienti.setVisibility(View.INVISIBLE);

			this.slidingDrawer = (SlidingDrawer) v.findViewById(R.id.clientSlidingDrawer);
			addDrawerListener();
			slidingDrawer.setVisibility(View.GONE);

			codClientText = (TextView) v.findViewById(R.id.textCodClient);
			numeClientText = (TextView) v.findViewById(R.id.textNumeClient);

			labelCodClient = (TextView) v.findViewById(R.id.labelCodClient);
			labelNumeClient = (TextView) v.findViewById(R.id.labelNumeClient);

			this.saveClntBtn = (Button) v.findViewById(R.id.saveClntBtn);

			this.selClientLayout = (LinearLayout) v.findViewById(R.id.clientData);

			textSelClient = (EditText) v.findViewById(R.id.numeSelClient);

			spinnerJudetCLP = (Spinner) v.findViewById(R.id.spinnerJudetCLP);
			spinnerJudetCLP.setOnItemSelectedListener(new regionSelectedListener());

			listJudete = new ArrayList<HashMap<String, String>>();
			adapterJudete = new SimpleAdapter(getActivity(), listJudete, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
					R.id.textNumeJudet, R.id.textCodJudet });
			fillJudete();

			spinnerFilialaCLP = (Spinner) v.findViewById(R.id.spinnerFilialaCLP);
			listFiliale = new ArrayList<HashMap<String, String>>();
			adapterFiliale = new SimpleAdapter(getActivity(), listFiliale, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
					R.id.textNumeJudet, R.id.textCodJudet });

			spinnerFilialaCLP.setOnItemSelectedListener(new filialaSelectedListener());

			fillFiliale();

			txtOras = (EditText) v.findViewById(R.id.txtOrasCLP);
			addTxtOrasListener();

			txtStrada = (EditText) v.findViewById(R.id.txtStradaCLP);
			addTxtStradaListener();

			txtPersCont = (EditText) v.findViewById(R.id.txtPersContCLP);
			addTxtPersContListener();

			spinnerTipCamion = (Spinner) v.findViewById(R.id.spinnerTipCamion);
			ArrayAdapter<String> adapterSpinnerTransp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipCamion);
			adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTipCamion.setAdapter(adapterSpinnerTransp);

			spinnerTipIncarcare = (Spinner) v.findViewById(R.id.spinnerTipIncarcare);
			ArrayAdapter<String> adapterSpinnerIncarcare = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipIncarcare);
			adapterSpinnerIncarcare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTipIncarcare.setAdapter(adapterSpinnerIncarcare);

			txtTelefon = (EditText) v.findViewById(R.id.txtTelefonCLP);
			addTxtTelefonListener();

			txtTipMarfa = (EditText) v.findViewById(R.id.txtTipMarfa);
			addTxtTipMarfaListener();

			txtMasaMarfa = (EditText) v.findViewById(R.id.txtMasaMarfa);
			addTxtMasaMarfaListener();

			txtDataLivrare = (TextView) v.findViewById(R.id.textDataLivrareCLP);
			addTxtDataLivrareListener();

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			String currentDate = sdf.format(new Date());
			txtDataLivrare.setText(currentDate);

			spinnerPlataCLP = (Spinner) v.findViewById(R.id.spinnerPlata);
			ArrayAdapter<String> adapterSpinnerPlata = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipPlata);
			adapterSpinnerPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerPlataCLP.setAdapter(adapterSpinnerPlata);
			spinnerPlataCLP.setOnItemSelectedListener(new OnSelectTipPlata());

			spinnerTransportCLP = (Spinner) v.findViewById(R.id.spinnerTransp);
			ArrayAdapter<String> adapterSpinnerTransport = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipTransport);
			adapterSpinnerTransport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTransportCLP.setAdapter(adapterSpinnerTransport);
			spinnerTransportCLP.setOnItemSelectedListener(new OnSelectTipTransport());

			labelLimitaCredit = (TextView) v.findViewById(R.id.labelLimitaCredit);
			textLimitaCredit = (TextView) v.findViewById(R.id.textLimitaCredit);
			labelRestCredit = (TextView) v.findViewById(R.id.labelRestCredit);
			textRestCredit = (TextView) v.findViewById(R.id.textRestCredit);

			tglTipClient = (ToggleButton) v.findViewById(R.id.tglTipClient);
			addListenerTglTipClient();

			labelClient = (TextView) v.findViewById(R.id.labelClient);

			radioClient = (RadioButton) v.findViewById(R.id.radioClient);
			addListenerRadioClient();

			radioFiliala = (RadioButton) v.findViewById(R.id.radioFiliala);
			addListenerRadioFiliala();

			spinnerDepozClp_Dest = (Spinner) v.findViewById(R.id.spinnerDepozClp_Dest);
			ArrayAdapter<String> adapterSpinnerDepoz = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, depozite);
			adapterSpinnerDepoz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDepozClp_Dest.setAdapter(adapterSpinnerDepoz);

			layoutSelAgentiClp = (LinearLayout) v.findViewById(R.id.layoutSelAgentiClp);
			layoutSelAgentiClp.setVisibility(View.GONE);

			spinnerAgentiCLP = (Spinner) v.findViewById(R.id.spinnerAgentiClp);

			adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
					R.id.textNumeAgent, R.id.textCodAgent });
			spinnerAgentiCLP.setAdapter(adapterAgenti);

			labelAgentiClp = (TextView) v.findViewById(R.id.labelAgentiClp);

			layoutDepartament = (LinearLayout) v.findViewById(R.id.layoutDepartament);
			layoutDepartament.setVisibility(View.GONE);

			layoutTipClient = (LinearLayout) v.findViewById(R.id.layoutTipClient);
			layoutTipClient.setVisibility(View.GONE);

			radioClientPF = (RadioButton) v.findViewById(R.id.radioClientPF);
			radioClientPJ = (RadioButton) v.findViewById(R.id.radioClientPJ);

			txtObservatiiCLP = (EditText) v.findViewById(R.id.txtObservatiiCLP);

			// consilieri, se face selectie departament
			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")) {
				layoutTipClient.setVisibility(View.VISIBLE);
				spinnerDepartament = (Spinner) v.findViewById(R.id.spinnerDepartament);
				loadDepartamentSpinner();
			} else {
				addListenerTextSelClient();
				addListenerSaveClient();
			}

			// pentru agenti fara selectie filiala
			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
					|| UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")) {
				radioClient.setVisibility(View.GONE);
				radioFiliala.setVisibility(View.GONE);
				spinnerAgentiCLP.setVisibility(View.INVISIBLE);
				labelAgentiClp.setVisibility(View.GONE);
			} else {
				layoutSelAgentiClp.setVisibility(View.VISIBLE);
				labelAgentiClp.setVisibility(View.VISIBLE);
				performGetAgenti();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	private void loadDepartamentSpinner() {
		layoutDepartament.setVisibility(View.VISIBLE);

		listDepart = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterDepart = new SimpleAdapter(getActivity(), listDepart, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" },
				new int[] { R.id.textNumeJudet, R.id.textCodJudet });

		HashMap<String, String> temp;

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati departamentul");
		temp.put("codJudet", "");
		listDepart.add(temp);

		for (EnumDepartamente depart : EnumDepartamente.values()) {
			temp = new HashMap<String, String>();
			temp.put("numeJudet", depart.toString());
			temp.put("codJudet", depart.name().substring(1, 3));
			listDepart.add(temp);

		}

		spinnerDepartament.setAdapter(adapterDepart);
		spinnerDepartament.setOnItemSelectedListener(new onSelectedDepart());

	}

	public class onSelectedDepart implements OnItemSelectedListener {

		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			HashMap<String, String> map = (HashMap<String, String>) parent.getAdapter().getItem(pos);
			departamentConsilier = map.get("codJudet").toString();
			CLPFragment2.clearArticoleList();

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void addDrawerListener() {
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

			}

		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slidingDrawer.setVisibility(View.GONE);
			}
		});

	}

	public void addListenerCautaClient() {
		cautaClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (txtNumeClient.length() > 0) {
					performListClients();
				} else {
					Toast.makeText(CLPFragment1.this.getActivity(), "Introduceti nume client!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public class MyOnItemSelectedListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			HashMap<String, String> artMap = (HashMap<String, String>) adapterClienti.getItem(pos);

			numeClient = artMap.get("numeClient");
			codClient = artMap.get("codClient");

			CreareClp.codClient = codClient;

			performClientDetails();

		}

	}

	private void performClientDetails() {

		String departSel = UserInfo.getInstance().getCodDepart();
		if (canalTipClient.equals("20"))
			departSel = "11";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codClient", codClient);
		params.put("depart", departSel);

		operatiiClient.getDetaliiClient(params);

	}

	private void listClientDetails(DetaliiClient detaliiClient) {
		{

			nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			layoutClient.setVisibility(View.VISIBLE);
			saveClntBtn.setVisibility(View.VISIBLE);
			labelCodClient.setVisibility(View.VISIBLE);
			labelNumeClient.setVisibility(View.VISIBLE);

			numeClientText.setText(numeClient);
			codClientText.setText(codClient);

			labelLimitaCredit.setText("Limita credit");
			labelRestCredit.setText("Rest credit");

			textLimitaCredit.setText(nf2.format(Double.valueOf(detaliiClient.getLimitaCredit())));
			textRestCredit.setText(nf2.format(Double.valueOf(detaliiClient.getRestCredit())));

			if (detaliiClient.getStare().equals("X")) {
				textRestCredit.setText("Client blocat - " + detaliiClient.getMotivBlocare());
				saveClntBtn.setVisibility(View.INVISIBLE);
			} else {
				saveClntBtn.setVisibility(View.VISIBLE);
			}

		}

	}

	private void performListClients() {

		String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

		String departSel = UserInfo.getInstance().getCodDepart();
		if (canalTipClient.equals("20"))
			departSel = "11";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("depart", departSel);
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		operatiiClient.getListClienti(params);

	}

	private void loadClientsList(String clientResponse) {

		if (clientResponse.length() > 0) {
			listClienti.clear();
			adapterClienti.notifyDataSetChanged();
			populateListViewClient(clientResponse);
			ScreenUtils.hideSoftKeyboard(getActivity(), txtNumeClient);
			txtNumeClient.setText("");
		}
	}

	public void populateListViewClient(String clientResponse) {

		HandleJSONData objClientList = new HandleJSONData(getActivity(), clientResponse);
		ArrayList<BeanClient> clientArray = objClientList.decodeJSONClientList();

		if (clientArray.size() > 0) {
			HashMap<String, String> temp;

			for (int i = 0; i < clientArray.size(); i++) {
				temp = new HashMap<String, String>();
				temp.put("numeClient", clientArray.get(i).getNumeClient());
				temp.put("codClient", clientArray.get(i).getCodClient());
				listClienti.add(temp);
			}

			listViewClienti.setVisibility(View.VISIBLE);
			listViewClienti.setAdapter(adapterClienti);

		} else {
			Toast.makeText(getActivity(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

		}

	}

	public void addListenerSaveClient() {
		saveClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClient.length() == 0) {
					Toast.makeText(getActivity(), "Selectati un client!", Toast.LENGTH_SHORT).show();
				} else {

					textSelClient.setText(numeClient);
					CreareClp.codClient = codClient;
					slidingDrawer.animateClose();

					numeClientText.setText("");
					codClientText.setText("");
					listClienti.clear();

					labelLimitaCredit.setText("");
					textLimitaCredit.setText("");
					labelRestCredit.setText("");
					textRestCredit.setText("");

					labelCodClient.setVisibility(View.GONE);
					labelNumeClient.setVisibility(View.GONE);

					saveClntBtn.setVisibility(View.GONE);

				}

			}
		});

	}

	public void fillJudete() {

		HashMap<String, String> temp;
		int i = 0;

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati judetul");
		temp.put("codJudet", "");
		listJudete.add(temp);

		for (i = 0; i < judete.length; i++) {
			temp = new HashMap<String, String>();
			temp.put("numeJudet", judete[i]);
			temp.put("codJudet", codJudete[i]);
			listJudete.add(temp);

		}

		spinnerJudetCLP.setAdapter(adapterJudete);

	}

	public void fillFiliale() {

		HashMap<String, String> temp;
		int i = 0;

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati filiala");
		temp.put("codJudet", "");
		listFiliale.add(temp);

		for (i = 0; i < numeFiliala.length; i++) {
			temp = new HashMap<String, String>();
			temp.put("numeJudet", numeFiliala[i]);
			temp.put("codJudet", codFiliala[i]);
			listFiliale.add(temp);

		}

		spinnerFilialaCLP.setAdapter(adapterFiliale);

	}

	public class regionSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) adapterJudete.getItem(pos);
			CreareClp.codJudet = artMap.get("codJudet");

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void addTxtOrasListener() {

		txtOras.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.oras = txtOras.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtStradaListener() {

		txtStrada.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.strada = txtStrada.getText().toString().trim();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtPersContListener() {

		txtPersCont.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.persCont = txtPersCont.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtTelefonListener() {

		txtTelefon.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.telefon = txtTelefon.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtTipMarfaListener() {

		txtTipMarfa.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.tipMarfa = txtTipMarfa.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtMasaMarfaListener() {

		txtMasaMarfa.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.masaMarfa = txtMasaMarfa.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtDataLivrareListener() {

		txtDataLivrare.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				CreareClp.dataLivrare = txtDataLivrare.getText().toString().trim();

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

		});

		txtDataLivrare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Time today = new Time(Time.getCurrentTimezone());
				today.setToNow();

				DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener, today.year, today.month, today.monthDay);

				dialog.getDatePicker().setMinDate(new Date().getTime() - 1000);

				dialog.show();

			}
		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
			Calendar clnd = new GregorianCalendar(mYear, mMonth, mDay);

			txtDataLivrare.setText(format.format(clnd.getTime()));
			CreareClp.dataLivrare = txtDataLivrare.getText().toString();

		}
	};

	public class filialaSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) adapterFiliale.getItem(pos);
			CreareClp.codFilialaDest = artMap.get("codJudet");

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public static void clearClientData() {
		spinnerJudetCLP.setSelection(0);
		spinnerFilialaCLP.setSelection(0);
		spinnerPlataCLP.setSelection(0);
		spinnerTransportCLP.setSelection(0);
		spinnerDepozClp_Dest.setSelection(0);
		spinnerAgentiCLP.setSelection(0);

		textSelClient.setText("");
		txtOras.setText("");
		txtStrada.setText("");
		txtPersCont.setText("");
		txtTelefon.setText("");
		txtObservatiiCLP.setText("");

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
		txtDataLivrare.setText(sdf.format(new Date()));
	}

	public void addListenerTglTipClient() {
		tglTipClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTipClient.isChecked()) {
					canalTipClient = "20";
				} else {
					canalTipClient = "10";
				}

				listClienti.clear();
				listViewClienti.setAdapter(adapterClienti);
				numeClientText.setText("");
				codClientText.setText("");

				labelLimitaCredit.setText("");
				textLimitaCredit.setText("");

				labelRestCredit.setText("");
				textRestCredit.setText("");

				codClient = "";
				numeClient = "";

				labelCodClient.setVisibility(View.GONE);
				labelNumeClient.setVisibility(View.GONE);

			}
		});

	}

	public class OnSelectTipPlata implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			String[] varPlata = spinnerPlataCLP.getSelectedItem().toString().split("-");
			CreareClp.tipPlata = varPlata[0].trim();

		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

	public class OnSelectTipTransport implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			String[] varTransport = spinnerTransportCLP.getSelectedItem().toString().split("-");
			CreareClp.tipTransport = varTransport[0].trim();

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void addListenerTextSelClient() {

		textSelClient.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				try {
					if (radioClient.isChecked()) {
						slidingDrawer.setVisibility(View.VISIBLE);
						slidingDrawer.animateOpen();
					}

				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

				return false;
			}
		});

	}

	public void addListenerRadioClient() {
		radioClient.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					textSelClient.setText("");
					textSelClient.setVisibility(View.VISIBLE);
					labelClient.setText("Client");

					spinnerJudetCLP.setEnabled(true);
					txtOras.setEnabled(true);
					txtStrada.setEnabled(true);
					txtPersCont.setEnabled(true);
					txtTelefon.setEnabled(true);

					layoutSelAgentiClp.setVisibility(View.VISIBLE);

				}

			}
		});
	}

	public void addListenerRadioFiliala() {
		radioFiliala.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					CreareClp.codClient = UserInfo.getInstance().getUnitLog();
					textSelClient.setText(UserInfo.getInstance().getUnitLog());
					labelClient.setText("Filiala");

					spinnerJudetCLP.setEnabled(false);
					spinnerJudetCLP.setSelection(0);
					CreareClp.codJudet = " ";

					txtOras.setEnabled(false);
					txtOras.setText("");
					CreareClp.oras = " ";

					txtStrada.setEnabled(false);
					txtStrada.setText("");
					CreareClp.strada = " ";

					txtPersCont.setEnabled(false);
					txtPersCont.setText("");
					CreareClp.persCont = " ";

					txtTelefon.setEnabled(false);
					txtTelefon.setText("");
					CreareClp.telefon = " ";

					layoutSelAgentiClp.setVisibility(View.GONE);

				}

			}
		});
	}

	private void performGetAgenti() {
		operatiiAgent.getListaAgenti(UserInfo.getInstance().getUnitLog(), UserInfo.getInstance().getCodDepart(), getActivity(), true);

	}

	protected void populateAgentiList(ArrayList<HashMap<String, String>> listAgenti) {

		spinnerAgentiCLP.setOnItemSelectedListener(new OnSelectAgent());

		if (listAgenti.size() >= 2) {
			listAgenti.remove(0);
			listAgenti.remove(0);
		}

		HashMap<String, String> tmpHash = new HashMap<String, String>();
		tmpHash.put("numeAgent", "Selectati un agent");
		tmpHash.put("codAgent", " ");

		listAgenti.add(0, tmpHash);

		adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });

		spinnerAgentiCLP.setAdapter(adapterAgenti);

	}

	public class OnSelectAgent implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			String[] tokenAgent = spinnerAgentiCLP.getSelectedItem().toString().split(",");
			String agentNr = tokenAgent[0].substring(tokenAgent[0].indexOf('=') + 1, tokenAgent[0].length());

			if (agentNr.trim().equals("") || agentNr.equals("00000000"))
				agentNr = "0";

			CreareClp.selectedAgent = agentNr;

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_LISTA_CLIENTI:
			loadClientsList((String) result);
			break;
		case GET_DETALII_CLIENT:
			listClientDetails(operatiiClient.deserializeDetaliiClient((String) result));
			break;
		default:
			break;
		}

	}

	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateAgentiList(listAgenti);

	}
}
