/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.AsyncTaskListener;
import listeners.MapListener;
import listeners.ObiectiveListener;
import listeners.OperatiiAdresaListener;
import model.ArticolComanda;
import model.Constants;
import model.DateLivrare;
import model.HandleJSONData;
import model.ListaArticoleComanda;
import model.OperatiiAdresa;
import model.OperatiiAdresaImpl;
import model.OperatiiObiective;
import model.UserInfo;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import utils.Exceptions;
import utils.UtilsFormatting;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.AdapterObiective;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.Address;
import beans.BeanAdresaLivrare;
import beans.BeanAdreseJudet;
import beans.BeanObiectivDepartament;

import com.google.android.gms.maps.model.LatLng;

import connectors.ConnectionStrings;
import dialogs.MapAddressDialog;
import enums.EnumLocalitate;
import enums.EnumOperatiiAdresa;
import enums.EnumOperatiiObiective;

public class SelectAdrLivrCmd extends Activity implements OnTouchListener, OnItemClickListener, AsyncTaskListener, OperatiiAdresaListener, ObiectiveListener,
		MapListener {

	private Button saveAdrLivrBtn;
	private EditText txtPers, txtTel, txtObservatii, txtValoareIncasare;

	private static final String METHOD_NAME = "getAdreseLivrareClient";

	String[] tipPlata = { "B - Bilet la ordin", "C - Cec", "E - Plata in numerar", "O - Ordin de plata" };

	String[] tipTransport = { "TRAP - Transport Arabesque", "TCLI - Transport client", "TFRN - Transport furnizor" };

	String[] tipResponsabil = { "AV - Agent vanzari", "SO - Sofer", "OF - Operator facturare" };

	String[] docInsot = { "Factura", "Aviz de expeditie" };

	public SimpleAdapter adapterJudete, adapterAdreseLivrare;

	private Spinner spinnerPlata, spinnerTransp, spinnerJudet, spinnerTermenPlata, spinnerDataLivrare, spinnerAdreseLivrare, spinnerDocInsot,
			spinnerTipReducere, spinnerResponsabil;
	private static ArrayList<HashMap<String, String>> listJudete = null, listAdreseLivrare = null;
	private ArrayAdapter<String> adapterDataLivrare, adapterTermenPlata, adapterResponsabil;
	private LinearLayout layoutAdrese, layoutAdr1, layoutAdr2, layoutValoareIncasare;
	private String globalCodClient = "";
	private HashMap<String, String> artMap = null;
	private RadioButton radioLista, radioText, radioObiectiv;
	private boolean adrNouaModifCmd = false;
	private int selectedAddrModifCmd = -1;
	private CheckBox checkMacara;
	private Spinner spinnerObiective;
	private LinearLayout layoutObiective;

	private ListPopupWindow lpw;
	private String[] listPersCont, listTel;
	CheckBox checkModifValInc;
	NumberFormat nf2;
	private OperatiiAdresa operatiiAdresa;
	private OperatiiObiective operatiiObiective;
	private BeanObiectivDepartament obiectivSelectat;
	private AutoCompleteTextView textLocalitate, textStrada;
	private Button btnPozitieAdresa;
	private TextView textCoordAdresa;
	private EditText textNrStr;
	
	private boolean adresaFromListHasNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectadrlivrcmdheader);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Date livrare");
		actionBar.setDisplayHomeAsUpEnabled(true);

		this.saveAdrLivrBtn = (Button) findViewById(R.id.saveAdrLivrBtn);
		addListenerSaveAdr();

		this.layoutAdrese = (LinearLayout) findViewById(R.id.layoutListAdrese);
		layoutAdrese.setVisibility(View.GONE);

		this.layoutAdr1 = (LinearLayout) findViewById(R.id.layoutAdr1);
		this.layoutAdr2 = (LinearLayout) findViewById(R.id.layoutAdr2);

		textLocalitate = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocalitate);
		textLocalitate.setVisibility(View.INVISIBLE);

		textNrStr = (EditText) findViewById(R.id.textNrStr);
		

		textCoordAdresa = (TextView) findViewById(R.id.textCoordAdresa);
		afisCoordAdresa();

		btnPozitieAdresa = (Button) findViewById(R.id.btnPozitieAdresa);
		setListnerBtnPozitieAdresa();

		textStrada = (AutoCompleteTextView) findViewById(R.id.autoCompleteStrada);

		txtPers = (EditText) findViewById(R.id.txtPersCont);
		txtTel = (EditText) findViewById(R.id.txtTelefon);

		listPersCont = DateLivrare.getInstance().getPersContact().split(":");
		listTel = DateLivrare.getInstance().getNrTel().split(":");

		lpw = new ListPopupWindow(this);
		lpw.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPersCont));
		lpw.setAnchorView(txtPers);
		lpw.setModal(true);
		lpw.setOnItemClickListener(this);

		txtPers.setText(listPersCont[0]);
		txtPers.setOnTouchListener(this);

		layoutValoareIncasare = (LinearLayout) findViewById(R.id.layoutValoareIncasare);
		layoutValoareIncasare.setVisibility(View.GONE);

		txtValoareIncasare = (EditText) findViewById(R.id.txtValoareIncasare);
		txtValoareIncasare.setFocusable(false);

		checkModifValInc = (CheckBox) findViewById(R.id.checkboxModifValInc);
		addListenerModifValInc();

		nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		if (DateLivrare.getInstance().isValIncModif()) {
			txtValoareIncasare.setText(DateLivrare.getInstance().getValoareIncasare());
			checkModifValInc.setChecked(true);
		} else {
			txtValoareIncasare.setText(nf2.format(CreareComanda.totalComanda * Constants.TVA));
			checkModifValInc.setChecked(false);
		}

		txtTel.setText(listTel[0]);

		txtObservatii = (EditText) findViewById(R.id.txtObservatii);

		CreareComanda.dateLivrare = "";

		spinnerResponsabil = (Spinner) findViewById(R.id.spinnerResponsabil);
		adapterResponsabil = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipResponsabil);
		adapterResponsabil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		addListenerResponsabil();
		spinnerResponsabil.setAdapter(adapterResponsabil);

		spinnerPlata = (Spinner) findViewById(R.id.spinnerPlata);

		ArrayAdapter<String> adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlata);
		adapterSpinnerPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPlata.setAdapter(adapterSpinnerPlata);
		addListenerTipPlata();

		spinnerTransp = (Spinner) findViewById(R.id.spinnerTransp);
		ArrayAdapter<String> adapterSpinnerTransp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipTransport);
		adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTransp.setAdapter(adapterSpinnerTransp);
		addSpinnerTranspListener();

		spinnerJudet = (Spinner) findViewById(R.id.spinnerJudet);
		spinnerJudet.setOnItemSelectedListener(new regionSelectedListener());

		operatiiAdresa = new OperatiiAdresaImpl(this);
		operatiiAdresa.setOperatiiAdresaListener(this);

		listJudete = new ArrayList<HashMap<String, String>>();
		adapterJudete = new SimpleAdapter(this, listJudete, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] { R.id.textNumeJudet,
				R.id.textCodJudet });

		spinnerTermenPlata = (Spinner) findViewById(R.id.spinnerTermenPlata);
		adapterTermenPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapterTermenPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTermenPlata.setAdapter(adapterTermenPlata);

		spinnerDataLivrare = (Spinner) findViewById(R.id.spinnerDataLivrare);
		adapterDataLivrare = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapterDataLivrare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDataLivrare.setAdapter(adapterDataLivrare);
		fillSpinnerDataLivrare();

		spinnerAdreseLivrare = (Spinner) findViewById(R.id.spinnerAdreseLivrare);

		setListenerSpinnerAdreseLivrare();

		listAdreseLivrare = new ArrayList<HashMap<String, String>>();
		adapterAdreseLivrare = new SimpleAdapter(this, listAdreseLivrare, R.layout.row_layout_2_items, new String[] { "rowText", "rowId" }, new int[] {
				R.id.textRowName, R.id.textRowId });

		spinnerAdreseLivrare.setAdapter(adapterAdreseLivrare);
		selectedAddrModifCmd = -1;

		if (CreareComanda.termenPlata.trim().length() > 0) {
			String[] tokTermen = CreareComanda.termenPlata.split(";");
			int nrLivr = 0;
			for (nrLivr = 0; nrLivr < tokTermen.length; nrLivr++) {
				adapterTermenPlata.add(tokTermen[nrLivr]);
			}
			spinnerTermenPlata.setSelection(nrLivr);

			globalCodClient = CreareComanda.codClientVar;

		} else {

			adapterTermenPlata.add("C000");

			if (!ModificareComanda.codClientVar.equals("")) {
				globalCodClient = ModificareComanda.codClientVar;
				afisCoordAdresa();

			}

		}

		String numeJudSel = "";
		HashMap<String, String> temp;
		int i = 0;

		for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {
			temp = new HashMap<String, String>(50, 0.75f);
			temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
			temp.put("codJudet", UtilsGeneral.codJudete[i]);
			listJudete.add(temp);

			if (DateLivrare.getInstance().getCodJudet().equals(UtilsGeneral.codJudete[i])) {
				numeJudSel = UtilsGeneral.numeJudete[i];
			}

		}

		DateLivrare.getInstance().setNumeJudet(numeJudSel);
		spinnerJudet.setAdapter(adapterJudete);

		// document insotitor
		spinnerDocInsot = (Spinner) findViewById(R.id.spinnerDocInsot);
		ArrayAdapter<String> adapterSpinnerDocInsot = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docInsot);
		adapterSpinnerDocInsot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDocInsot.setAdapter(adapterSpinnerDocInsot);
		spinnerDocInsot.setSelection(Integer.valueOf(DateLivrare.getInstance().getTipDocInsotitor()) - 1);
		// sf. doc insot

		// tip plata
		for (i = 0; i < adapterSpinnerPlata.getCount(); i++) {
			if (adapterSpinnerPlata.getItem(i).toString().substring(0, 1).equals(DateLivrare.getInstance().getTipPlata())) {
				spinnerPlata.setSelection(i);
				break;
			}
		}

		spinnerTipReducere = (Spinner) findViewById(R.id.spinnerTipReducere);
		ArrayAdapter<String> adapterTipReducere = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, UtilsGeneral.tipReducere);
		adapterTipReducere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTipReducere.setAdapter(adapterTipReducere);

		// fact. red
		if (DateLivrare.getInstance().getRedSeparat().equals("X")) {
			spinnerTipReducere.setSelection(1); // 2 facturi

		} else {

			if (DateLivrare.getInstance().getRedSeparat().equals("R")) {
				spinnerTipReducere.setSelection(2); // 1 factura red. separat
			} else {
				spinnerTipReducere.setSelection(0); // 1 factura red in pret
			}

		}

		if (CreareComanda.canalDistrib.equals("20"))
			spinnerTipReducere.setEnabled(false);
		else
			spinnerTipReducere.setEnabled(true);

		// tip transport
		for (i = 0; i < adapterSpinnerTransp.getCount(); i++) {
			if (adapterSpinnerTransp.getItem(i).toString().substring(0, 4).equals(DateLivrare.getInstance().getTransport())) {
				spinnerTransp.setSelection(i);
				break;
			}
		}

		checkMacara = (CheckBox) findViewById(R.id.checkMacara);
		setMacaraVisible();
		setListenerCheckMacara();

		radioLista = (RadioButton) findViewById(R.id.radioLista);
		addListenerRadioLista();

		radioText = (RadioButton) findViewById(R.id.radioText);
		addListenerRadioText();

		radioObiectiv = (RadioButton) findViewById(R.id.radioObiectiv);
		addListenerRadioObiectiv();

		radioObiectiv.setVisibility(View.INVISIBLE);

		spinnerObiective = (Spinner) findViewById(R.id.spinnerObiective);
		setSpinnerObiectiveListener();
		layoutObiective = (LinearLayout) findViewById(R.id.layoutObiective);
		displayObiective(false);

		layoutAdrese.setVisibility(View.VISIBLE);
		layoutAdr1.setVisibility(View.GONE);
		layoutAdr2.setVisibility(View.GONE);
		performGetAdreseLivrare();

	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		try {
			txtPers.setText(listPersCont[position]);
			txtTel.setText(listTel[position]);
			lpw.dismiss();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		final int DRAWABLE_RIGHT = 2;

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (event.getX() >= (v.getWidth() - ((EditText) v).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
				lpw.show();
				return true;
			}
		}
		return false;
	}

	private void afisCoordAdresa() {
		if (DateLivrare.getInstance().getCoordonateAdresa() != null && DateLivrare.getInstance().getCoordonateAdresa().latitude != 0)
			textCoordAdresa.setText(DateLivrare.getInstance().getCoordonateAdresa().latitude + "," + DateLivrare.getInstance().getCoordonateAdresa().longitude);
		else
			textCoordAdresa.setText("");

	}

	private void addSpinnerTranspListener() {
		spinnerTransp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					checkMacara.setChecked(DateLivrare.getInstance().isMasinaMacara());
					setMacaraVisible();
				} else {
					checkMacara.setChecked(false);
					checkMacara.setVisibility(View.INVISIBLE);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setListenerCheckMacara() {
		checkMacara.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DateLivrare.getInstance().setMasinaMacara(isChecked);
			}
		});
	}

	private void displayObiective(boolean isVisible) {
		if (isVisible) {
			layoutObiective.setVisibility(View.VISIBLE);
		} else {
			layoutObiective.setVisibility(View.INVISIBLE);
		}
	}

	private void setSpinnerObiectiveListener() {
		spinnerObiective.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				if (pos > 0) {
					radioObiectiv.setVisibility(View.VISIBLE);
					radioObiectiv.setChecked(true);
					obiectivSelectat = (BeanObiectivDepartament) parent.getAdapter().getItem(pos);
					setAdresaLivrareFromObiectiv();
				} else {
					radioObiectiv.setVisibility(View.INVISIBLE);
					radioLista.setChecked(true);
					obiectivSelectat = null;
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				return;

			}
		});
	}

	private void addListenerModifValInc() {
		checkModifValInc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (checkModifValInc.isChecked()) {
					DateLivrare.getInstance().setValIncModif(true);
					txtValoareIncasare.setFocusableInTouchMode(true);
					txtValoareIncasare.setText(DateLivrare.getInstance().getValoareIncasare());

				} else {
					DateLivrare.getInstance().setValIncModif(false);
					txtValoareIncasare.setFocusable(false);

					double localValCmd = 0;
					if (!CreareComanda.codClientVar.equals(""))
						localValCmd = CreareComanda.totalComanda * Constants.TVA;
					else
						localValCmd = ModificareComanda.totalComanda * Constants.TVA;

					txtValoareIncasare.setText(nf2.format(localValCmd));

				}
			}
		});

	}

	private void fillSpinnerDataLivrare() {

		adapterDataLivrare.add("Selectati data livrare");

		adapterDataLivrare.add("Astazi");

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, 1);
		Date resultdate = new Date(c1.getTimeInMillis());
		adapterDataLivrare.add(sdf.format(resultdate));

		c1.add(Calendar.DAY_OF_MONTH, 1);
		resultdate = new Date(c1.getTimeInMillis());
		adapterDataLivrare.add(sdf.format(resultdate));

		c1.add(Calendar.DAY_OF_MONTH, 1);
		resultdate = new Date(c1.getTimeInMillis());
		adapterDataLivrare.add(sdf.format(resultdate));

		c1.add(Calendar.DAY_OF_MONTH, 1);
		resultdate = new Date(c1.getTimeInMillis());
		adapterDataLivrare.add(sdf.format(resultdate));

		adapterDataLivrare.notifyDataSetChanged();
		spinnerDataLivrare.setAdapter(adapterDataLivrare);
	}

	private void addListenerTipPlata() {
		spinnerPlata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				if (pos == 0 || pos == 1 || pos == 2 || pos == 3) {
					spinnerTermenPlata.setVisibility(View.VISIBLE);
				}

				if (pos == 2) {
					Toast.makeText(getApplicationContext(), "Valoare maxima comanda pentru PJ: 5000 RON", Toast.LENGTH_SHORT).show();
				}

				if (pos == 2 && spinnerResponsabil.getSelectedItemPosition() == 1) {
					layoutValoareIncasare.setVisibility(View.VISIBLE);

					if (DateLivrare.getInstance().isValIncModif()) {
						txtValoareIncasare.setText(DateLivrare.getInstance().getValoareIncasare());

					} else {
						double localValCmd = 0;
						if (!CreareComanda.codClientVar.equals("")) {
							if (CreareComanda.canalDistrib.equals("10"))
								localValCmd = CreareComanda.totalComanda * Constants.TVA;
							else if (CreareComanda.canalDistrib.equals("20"))
								localValCmd = CreareComanda.totalComanda;
						} else
							localValCmd = ModificareComanda.totalComanda * Constants.TVA;

						txtValoareIncasare.setText(nf2.format(localValCmd));

					}

				} else {
					layoutValoareIncasare.setVisibility(View.GONE);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
	}

	private void addListenerResponsabil() {
		spinnerResponsabil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				if (pos == 1 && spinnerPlata.getSelectedItemPosition() == 2) {
					layoutValoareIncasare.setVisibility(View.VISIBLE);

					if (DateLivrare.getInstance().isValIncModif()) {
						txtValoareIncasare.setText(DateLivrare.getInstance().getValoareIncasare());

					} else {
						double localValCmd = 0;
						if (!CreareComanda.codClientVar.equals("")) {
							if (CreareComanda.canalDistrib.equals("10"))
								localValCmd = CreareComanda.totalComanda * Constants.TVA;
							else if (CreareComanda.canalDistrib.equals("20"))
								localValCmd = CreareComanda.totalComanda;
						} else
							localValCmd = ModificareComanda.totalComanda * Constants.TVA;

						txtValoareIncasare.setText(nf2.format(localValCmd));

					}

				} else {
					layoutValoareIncasare.setVisibility(View.GONE);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
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

	public void addListenerRadioLista() {

		radioLista.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAdresaLivrare();

			}
		});

		radioLista.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					layoutAdrese.setVisibility(View.VISIBLE);
					layoutAdr1.setVisibility(View.GONE);
					layoutAdr2.setVisibility(View.GONE);
					performGetAdreseLivrare();
				}

			}
		});
	}

	public void addListenerRadioText() {

		radioText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAdresaLivrare();

			}
		});

		radioText.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					layoutAdrese.setVisibility(View.GONE);
					layoutAdr1.setVisibility(View.VISIBLE);
					layoutAdr2.setVisibility(View.VISIBLE);

				}

			}
		});
	}

	private void addListenerRadioObiectiv() {

		radioObiectiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAdresaLivrare();

			}
		});

		radioObiectiv.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					layoutAdrese.setVisibility(View.GONE);
					layoutAdr1.setVisibility(View.GONE);
					layoutAdr2.setVisibility(View.GONE);
					spinnerObiective.setSelection(1);

				}

			}
		});
	}

	protected void performGetAdreseLivrare() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codClient", globalCodClient);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private void getObiectiveDepartament() {
		operatiiObiective = new OperatiiObiective(this);
		operatiiObiective.setObiectiveListener(this);

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();

		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("departament", UserInfo.getInstance().getCodDepart());
		params.put("codClient", CreareComanda.codClientVar);
		params.put("tipUser", UserInfo.getInstance().getTipUser());
		params.put("codUser", UserInfo.getInstance().getCod());

		operatiiObiective.getObiectiveDepartament(params);
	}

	private void fillListAdrese(String adrese) {

		HandleJSONData objClientList = new HandleJSONData(this, adrese);
		ArrayList<BeanAdresaLivrare> adresaArray = objClientList.decodeJSONAdresaLivrare();

		if (adresaArray.size() > 0) {
			listAdreseLivrare.clear();

			HashMap<String, String> temp;

			String strAdresa = "";
			for (int i = 0; i < adresaArray.size(); i++) {
				temp = new HashMap<String, String>();

				strAdresa = getNumeJudet(adresaArray.get(i).getCodJudet()) + "; " + adresaArray.get(i).getOras() + "; " + adresaArray.get(i).getStrada() + "; "
						+ adresaArray.get(i).getNrStrada() + ";";

				temp.put("rowText", strAdresa);
				temp.put("rowId", adresaArray.get(i).getCodAdresa());
				listAdreseLivrare.add(temp);

			}

			spinnerAdreseLivrare.setAdapter(adapterAdreseLivrare);
			adapterAdreseLivrare.notifyDataSetChanged();

			if (!ModificareComanda.selectedCmd.equals("")) {
				if (!adrNouaModifCmd)
					getCmdDateLivrare();
				else
					spinnerAdreseLivrare.setSelection(selectedAddrModifCmd);
			}

		} else {

		}

		if (UtilsUser.hasObiective() && CreareComanda.codClientVar.trim().length() > 0)
			getObiectiveDepartament();

	}

	private void getCmdDateLivrare() {

		// afisare date livrare pentru comenzile existente (la modificare
		// comanda)
		try {

			getDateLivrare date = new getDateLivrare(this);
			date.execute(("dummy"));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private class getDateLivrare extends AsyncTask<String, Void, String> {
		String errMessage = "";

		Context mContext;
		private ProgressDialog dialog;

		private getDateLivrare(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Incarcare date livrare...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), "getCmdDateLivrare");

				request.addProperty("idCmd", ModificareComanda.selectedCmd);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 25000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));

				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + "getCmdDateLivrare", envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();
			} catch (Exception e) {
				errMessage = e.getMessage();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT);
					toast.show();

				} else {
					fillDateLivrare(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void fillDateLivrare(String dateLivrare) {

		try {
			// incarcare date livrare comanda modificata
			String[] tokLivrare = dateLivrare.split("#");
			HashMap<String, String> artMapLivr = null;

			// adresa de livrare
			if (tokLivrare[13].equals("X")) // adresa noua
			{
				radioText.setChecked(true);
				int nrJudete = listJudete.size(), j = 0;
				String codJudet = "";

				for (j = 0; j < nrJudete; j++) {
					artMapLivr = (HashMap<String, String>) this.adapterJudete.getItem(j);
					codJudet = artMapLivr.get("codJudet").toString();

					if (tokLivrare[8].equals(codJudet)) {
						spinnerJudet.setSelection(j);
						break;
					}

				}// sf. for

				DateLivrare.getInstance().setOras(tokLivrare[7]);

				textLocalitate.setText(tokLivrare[7]);

				textStrada.setText(tokLivrare[4]);
				adrNouaModifCmd = true;

			} else {
				radioLista.setChecked(true);
				int nrAdrese = listAdreseLivrare.size();
				int j = 0;
				String adr_number = "";

				for (j = 0; j < nrAdrese; j++) {

					artMapLivr = (HashMap<String, String>) this.adapterAdreseLivrare.getItem(j);
					adr_number = artMapLivr.get("rowId").toString();

					if (tokLivrare[15].equals(adr_number)) {
						spinnerAdreseLivrare.setSelection(j);
						selectedAddrModifCmd = j;
						break;
					}

				}

			}

			// pers. contact
			txtPers.setText(tokLivrare[2]);

			// telefon
			txtTel.setText(tokLivrare[3]);

			// fact.red. separat
			if (tokLivrare[6].equals("X"))
				spinnerTipReducere.setSelection(1);
			if (tokLivrare[6].equals("R"))
				spinnerTipReducere.setSelection(2);
			if (tokLivrare[6].equals(" "))
				spinnerTipReducere.setSelection(0);

			// doc. insot
			if (tokLivrare[12].equals("1"))
				spinnerDocInsot.setSelection(0);
			else
				spinnerDocInsot.setSelection(1);

			// tip plata
			if (tokLivrare[12].equals("B"))
				spinnerPlata.setSelection(0);
			if (tokLivrare[12].equals("C"))
				spinnerPlata.setSelection(1);
			if (tokLivrare[12].equals("E"))
				spinnerPlata.setSelection(2);
			if (tokLivrare[12].equals("O"))
				spinnerPlata.setSelection(3);

			// termen plata
			adapterTermenPlata.clear();
			adapterTermenPlata.add("C000");
			if (!ModificareComanda.codClientVar.equals("")) {

				if (!tokLivrare[9].equals("C000")) {
					if (!DateLivrare.getInstance().getTermenPlata().trim().equals("")) {
						adapterTermenPlata.add(DateLivrare.getInstance().getTermenPlata());
						spinnerTermenPlata.setSelection(1);
					}
				}
			}

			// transport
			if (tokLivrare[5].equals("TRAP"))
				spinnerTransp.setSelection(0);
			if (tokLivrare[5].equals("TCLI"))
				spinnerTransp.setSelection(1);

			// data livrare
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH) + 1, cal1.get(Calendar.DAY_OF_MONTH));
			cal2.set(Integer.parseInt(tokLivrare[11].substring(0, 4)), Integer.parseInt(tokLivrare[11].substring(4, 6)),
					Integer.parseInt(tokLivrare[11].substring(6, 8)));

			long diff1 = cal2.getTimeInMillis() - cal1.getTimeInMillis();
			long diffDays = diff1 / (24 * 60 * 60 * 1000);

			if (diffDays <= 0)
				spinnerDataLivrare.setSelection(1);

			if (diffDays == 1)
				spinnerDataLivrare.setSelection(2);

			if (diffDays == 2)
				spinnerDataLivrare.setSelection(3);

			if (diffDays == 3)
				spinnerDataLivrare.setSelection(4);

			if (diffDays >= 4)
				spinnerDataLivrare.setSelection(5);

			// obs livrare
			txtObservatii.setText(tokLivrare[10]);

			// obs. plata (responsabil livrare)
			spinnerResponsabil.setSelection(0);

			if (tokLivrare[14].equals("AV")) {
				spinnerResponsabil.setSelection(0);
			}
			if (tokLivrare[14].equals("SO")) {
				spinnerResponsabil.setSelection(1);
			}
			if (tokLivrare[14].equals("OC")) {
				spinnerResponsabil.setSelection(2);
			}

			DateLivrare.getInstance().setValoareIncasare(tokLivrare[16]);
			if (tokLivrare[17].equals("X")) {
				DateLivrare.getInstance().setValIncModif(true);
				checkModifValInc.setChecked(true);
			} else {
				DateLivrare.getInstance().setValIncModif(false);
				checkModifValInc.setChecked(false);
			}

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void setMacaraVisible() {
		if (UtilsUser.isMacaraVisible() || isArtException())
			checkMacara.setVisibility(View.VISIBLE);
		else
			checkMacara.setVisibility(View.INVISIBLE);

	}

	private boolean isArtException() {

		List<String> articoleExceptii = Exceptions.getExceptionsMacara(this);
		List<ArticolComanda> articoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();

		for (String exceptie : articoleExceptii) {

			for (ArticolComanda articol : articoleComanda) {
				if (exceptie.equals(articol.getCodArticol()))
					return true;
			}

		}

		return false;
	}

	public class regionSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (spinnerJudet.getSelectedItemPosition() > 0) {

				HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
				DateLivrare.getInstance().setNumeJudet(tempMap.get("numeJudet"));
				DateLivrare.getInstance().setCodJudet(tempMap.get("codJudet"));

				HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
				params.put("codJudet", DateLivrare.getInstance().getCodJudet());
				operatiiAdresa.getAdreseJudet(params, null);

			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	private void populateListLocalitati(BeanAdreseJudet listAdrese) {

		textLocalitate.setVisibility(View.VISIBLE);
		textLocalitate.setText(DateLivrare.getInstance().getOras());

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		textLocalitate.setThreshold(0);
		textLocalitate.setAdapter(adapterLoc);

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		textStrada.setThreshold(0);
		textStrada.setAdapter(adapterStrazi);

		setListenerTextLocalitate();

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

	private void displayObiectiveDepartament(List<BeanObiectivDepartament> obiectiveDepart) {

		if (obiectiveDepart.size() > 0) {

			displayObiective(true);

			BeanObiectivDepartament dummyOb = new BeanObiectivDepartament();

			dummyOb.setNume("Selectati un obiectiv");
			dummyOb.setAdresa(" ");
			dummyOb.setBeneficiar(" ");
			dummyOb.setDataCreare(" ");
			dummyOb.setId(" ");

			obiectiveDepart.add(0, dummyOb);

			AdapterObiective adapterOb = new AdapterObiective(getApplicationContext(), obiectiveDepart);
			spinnerObiective.setAdapter(adapterOb);
		} else {
			displayObiective(false);
		}

	}

	private void setListnerBtnPozitieAdresa() {
		btnPozitieAdresa.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				android.app.FragmentManager fm = getFragmentManager();

				Address address = new Address();

				if (radioText.isChecked()) {
					address.setCity(textLocalitate.getText().toString().trim());
					address.setStreet(textStrada.getText().toString().trim());
					address.setNumber(textNrStr.getText().toString().trim());
					address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));

					if (!isAdresaComplet())
						return;

					
					String nrStrada = "";
					
					if (textNrStr.getText().toString().trim().length() > 0)
						nrStrada = " nr " + textNrStr.getText().toString().trim();
					
					DateLivrare.getInstance().setOras(address.getCity());
					DateLivrare.getInstance().setStrada(address.getStreet() + nrStrada);

				} else {
					address.setCity(DateLivrare.getInstance().getOras());
					address.setStreet(DateLivrare.getInstance().getStrada());
					address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));
				}

				MapAddressDialog mapDialog = new MapAddressDialog(address, SelectAdrLivrCmd.this, fm);
				mapDialog.setMapListener(SelectAdrLivrCmd.this);
				mapDialog.show();
			}
		});
	}

	private boolean isAdresaComplet() {
		if (spinnerJudet.getSelectedItemPosition() == 0) {
			Toast.makeText(this, "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (textLocalitate.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Completati localitatea", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void addListenerSaveAdr() {
		saveAdrLivrBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (radioText.isChecked())
					valideazaAdresaLivrare();
				else
					valideazaDateLivrare();

			}
		});

	}

	@SuppressWarnings("unchecked")
	private void valideazaDateLivrare() {
		String adresa = "";
		String pers = "";
		String telefon = "";
		String observatii = "", obsPlata = " ";

		DateLivrare dateLivrareInstance = DateLivrare.getInstance();

		if (layoutAdrese.getVisibility() == View.VISIBLE) { // adresa
			// din lista

			if (listAdreseLivrare.size() > 0) {
				artMap = (HashMap<String, String>) spinnerAdreseLivrare.getSelectedItem();

				setAdresaLivrareFromList(artMap);

			}

		} else {
			
			
			String nrStrada = "";
			
			if (textNrStr.getText().toString().trim().length() > 0)
				nrStrada = " NR " + textNrStr.getText().toString().trim();
			
			dateLivrareInstance.setStrada(textStrada.getText().toString().trim() + nrStrada);
			dateLivrareInstance.setAdrLivrNoua(true);
			dateLivrareInstance.setAddrNumber(" ");
			
		}

		setAdresaLivrareFromObiectiv();

		pers = txtPers.getText().toString().trim();
		telefon = txtTel.getText().toString().trim();
		observatii = txtObservatii.getText().toString().trim();
		obsPlata = spinnerResponsabil.getSelectedItem().toString().substring(0, 2);

		if (observatii.trim().length() == 0)
			observatii = " ";

		dateLivrareInstance.setPersContact(pers);
		dateLivrareInstance.setNrTel(telefon);

		if (DateLivrare.getInstance().getOras().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Selectati localitatea!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (DateLivrare.getInstance().getStrada().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Completati strada!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (dateLivrareInstance.getCodJudet().equals("")) {
			Toast.makeText(getApplicationContext(), "Selectati judetul!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!CreareComanda.codClientVar.equals("")) {

			if (layoutAdrese.getVisibility() == View.VISIBLE) {
				if (!adresaFromListHasNumber && DateLivrare.getInstance().getCoordonateAdresa() == null && getTipTransport().equals("TRAP")) {
					Toast.makeText(this, "Adresa de livrare imprecisa, pozitionati adresa pe harta", Toast.LENGTH_SHORT).show();
					return;
				}

			}
		}

		if (pers.equals("") || pers.equals("-")) {
			Toast.makeText(getApplicationContext(), "Completati persoana de contact!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (telefon.equals("") || telefon.equals("-")) {
			Toast.makeText(getApplicationContext(), "Completati nr. de telefon!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (spinnerDataLivrare.getSelectedItemPosition() == 0) {
			Toast.makeText(getApplicationContext(), "Selectati data livrare!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (layoutValoareIncasare.getVisibility() == View.VISIBLE && txtValoareIncasare.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "Completati valoarea incasarii!", Toast.LENGTH_SHORT).show();
			return;
		}

		String cantar = "NU";
		dateLivrareInstance.setCantar("NU");

		if (spinnerTipReducere.getSelectedItemPosition() == 0) {
			dateLivrareInstance.setRedSeparat(" ");
		}

		if (spinnerTipReducere.getSelectedItemPosition() == 1) {
			dateLivrareInstance.setRedSeparat("X");
		}

		if (spinnerTipReducere.getSelectedItemPosition() == 2) {
			dateLivrareInstance.setRedSeparat("R");
		}

		dateLivrareInstance.setTipPlata(spinnerPlata.getSelectedItem().toString().substring(0, 1));
		dateLivrareInstance.setTransport(spinnerTransp.getSelectedItem().toString().substring(0, 4));
		dateLivrareInstance.setDataLivrare(spinnerDataLivrare.getSelectedItemPosition() - 1);

		if (layoutValoareIncasare.getVisibility() == View.VISIBLE)
			dateLivrareInstance.setValoareIncasare(txtValoareIncasare.getText().toString());
		else
			dateLivrareInstance.setValoareIncasare("0");

		adresa = dateLivrareInstance.getNumeJudet() + " " + dateLivrareInstance.getOras() + " " + dateLivrareInstance.getStrada();

		dateLivrareInstance.setDateLivrare(adresa + "#" + pers + "#" + telefon + "#" + cantar + "#" + dateLivrareInstance.getTipPlata() + "#"
				+ dateLivrareInstance.getTransport() + "#" + dateLivrareInstance.getRedSeparat() + "#");

		dateLivrareInstance.setTermenPlata(spinnerTermenPlata.getSelectedItem().toString());

		dateLivrareInstance.setObsLivrare(observatii.replace("#", "-").replace("@", "-"));
		dateLivrareInstance.setObsPlata(obsPlata);

		dateLivrareInstance.setTipDocInsotitor(String.valueOf(spinnerDocInsot.getSelectedItemPosition() + 1));

		finish();

	}

	private String getTipTransport() {
		return spinnerTransp.getSelectedItem().toString().substring(0, 4);
	}

	private void setAdresaLivrareFromList(HashMap<String, String> selectedLine) {

		String[] tokenAdr = selectedLine.get("rowText").toString().split(";");
		DateLivrare.getInstance().setAddrNumber(artMap.get("rowId").toString());

		DateLivrare.getInstance().setNumeJudet(tokenAdr[0].trim());

		if (!tokenAdr[1].trim().equals("")) {
			DateLivrare.getInstance().setOras(tokenAdr[1].trim());
		} else {
			DateLivrare.getInstance().setOras("-");
		}

		if (!tokenAdr[2].trim().equals("")) {
			DateLivrare.getInstance().setStrada(tokenAdr[2].trim() + " " + tokenAdr[3].trim());
			adresaFromListHasNumber = UtilsFormatting.streetHasNumber(DateLivrare.getInstance().getStrada());
		} else {
			DateLivrare.getInstance().setStrada("-");
			adresaFromListHasNumber = false;
		}
		DateLivrare.getInstance().setCodJudet(getCodJudet(tokenAdr[0].trim()));
		
		DateLivrare.getInstance().setAdrLivrNoua(false);

	}

	private void valideazaAdresaLivrare() {

		if (isAdresaCompleta()) {
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("codJudet", DateLivrare.getInstance().getCodJudet());
			params.put("localitate", DateLivrare.getInstance().getOras());

			operatiiAdresa.isAdresaValida(params, EnumLocalitate.LOCALITATE_SEDIU);
		}

	}

	private boolean isAdresaCompleta() {

		if (!CreareComanda.codClientVar.equals("")) {

			if (textNrStr.getText().toString().trim().equals("") && DateLivrare.getInstance().getCoordonateAdresa() == null && getTipTransport().equals("TRAP")) {
				Toast.makeText(this, "Adresa de livrare imprecisa, pozitionati adresa pe harta", Toast.LENGTH_SHORT).show();
				return false;
			}

		}

		return true;
	}

	private void setListenerSpinnerAdreseLivrare() {
		spinnerAdreseLivrare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				artMap = (HashMap<String, String>) spinnerAdreseLivrare.getSelectedItem();
				setAdresaLivrareFromList(artMap);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void clearAdresaLivrare() {
		DateLivrare.getInstance().setOras("");
		DateLivrare.getInstance().setStrada("");
		DateLivrare.getInstance().setCodJudet("");
		DateLivrare.getInstance().setCoordonateAdresa(null);
		textCoordAdresa.setText("");

		spinnerJudet.setSelection(0);
		textLocalitate.setText("");
		textNrStr.setText("");
		textStrada.setText("");

	}

	private void setAdresaLivrareFromObiectiv() {

		if (radioObiectiv.getVisibility() == View.VISIBLE)
			if (radioObiectiv.isChecked()) {
				DateLivrare.getInstance().setAdresaObiectiv(true);
			} else {
				DateLivrare.getInstance().setAdresaObiectiv(false);
			}

		if (spinnerObiective.getSelectedItemPosition() > 0) {
			String[] arrObiectiv = obiectivSelectat.getAdresa().split("/");

			DateLivrare.getInstance().setIdObiectiv(obiectivSelectat.getId());

			if (radioObiectiv.isChecked()) {
				DateLivrare.getInstance().setCodJudet(arrObiectiv[0]);
				DateLivrare.getInstance().setNumeJudet(UtilsGeneral.getNumeJudet(arrObiectiv[0]));
				DateLivrare.getInstance().setOras(arrObiectiv[1]);
				if (arrObiectiv.length == 3)
					DateLivrare.getInstance().setStrada(arrObiectiv[2]);
				else
					DateLivrare.getInstance().setStrada("-");
			}

		}

	}

	private String getNumeJudet(String codJudet) {
		String numeJudet = "nedefinit";

		int i = 0;
		for (i = 0; i < UtilsGeneral.codJudete.length; i++) {
			if (codJudet.equals(UtilsGeneral.codJudete[i])) {
				numeJudet = UtilsGeneral.codJudete[i];
				break;
			}

		}

		return numeJudet;
	}

	private String getCodJudet(String numeJudet) {
		String codJudet = "00";

		int i = 0;
		for (i = 0; i < UtilsGeneral.codJudete.length; i++) {
			if (numeJudet.equals(UtilsGeneral.codJudete[i])) {
				codJudet = UtilsGeneral.codJudete[i];
				break;
			}

		}

		return codJudet;
	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals(METHOD_NAME)) {
			fillListAdrese((String) result);
		}

	}

	private void valideazaAdresaResponse(String result) {
		Boolean response = Boolean.valueOf(result);

		if (response)
			valideazaDateLivrare();
		else
			Toast.makeText(this, "Adresa invalida", Toast.LENGTH_SHORT).show();

	}

	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {
		switch (numeComanda) {
		case GET_ADRESE_JUDET:
			populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));
			break;
		case IS_ADRESA_VALIDA:
			valideazaAdresaResponse((String) result);
			break;
		default:
			break;
		}

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_OBIECTIVE_DEPARTAMENT:
			displayObiectiveDepartament(operatiiObiective.deserializeObiectiveDepart((String) result));
			break;
		default:
			break;
		}

	}

	@Override
	public void addressSelected(LatLng coord) {
		DateLivrare.getInstance().setCoordonateAdresa(coord);
		textCoordAdresa.setText(coord.latitude + "," + coord.longitude);

	}

}
