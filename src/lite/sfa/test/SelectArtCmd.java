/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.OperatiiArticolListener;
import model.ArticolComanda;
import model.InfoStrings;
import model.ListaArticoleComanda;
import model.OperatiiArticol;
import model.OperatiiArticolFactory;
import model.UserInfo;
import utils.DepartamentAgent;
import utils.UtilsGeneral;
import adapters.CautareArticoleAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import beans.ArticolDB;
import enums.EnumArticoleDAO;
import enums.EnumDepartExtra;

public class SelectArtCmd extends ListActivity implements OperatiiArticolListener {

	Button articoleBtn, saveArtBtn, pretBtn;
	String filiala = "", nume = "", cod = "", umStoc = "";
	String articolResponse = "";
	String pretResponse = "";
	String codArticol = "";
	String numeArticol = "", tipArticol = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "";
	LinearLayout redBtnTable, layoutStocKA, layoutPretMaxKA;
	EditText valRedIntText, valRedDecText;

	public String globalDepozSel = "", artPromoText = "", globalCodDepartSelectetItem = "";

	ToggleButton tglButton, tglTipArtBtn;

	private EditText txtNumeArticol, textProcRed;
	private TextView textCodArticol, txtPretArt;
	private TextView textNumeArticol, textStocKA, textUmKA;

	ToggleButton tglProc;

	private TextView textStoc;
	private TextView textCant, procDisc, textPretTVA, textMultipluArt;

	private TextView textUM, textPretMinKA;
	private TextView labelCant, labelStoc;
	private Spinner spinnerDepoz, spinnerUnitMas;

	private TextView textPromo, textCondPret;

	private boolean pretMod = false;

	private double initPrice = 0, cmpArt = 0;
	private double finalPrice = 0, minimKAPrice = 0;
	private double listPrice = 0, procDiscClient = 0;
	private double discMaxAV = 0, discMaxSD = 0;
	private double pretVanzare = 0, procentAprob = 0, selectedCant = 0;

	NumberFormat nf2;

	private static ArrayList<HashMap<String, String>> listUmVanz = null;

	SimpleAdapter adapterUmVanz;
	private double varProc = 0, valMultiplu = 0;

	String tipAlert = "", codPromo = "", infoArticol = "", Umb = "", cantUmb = "", selectedUnitMas = "";

	private HashMap<String, String> artMap = null;
	double procR = 0, globalCantArt = 0;
	private String depozUnic = "", unitLogUnic = "";
	Dialog dialogSelFilArt;

	LinearLayout resultLayout;
	OperatiiArticol opArticol;

	private String selectedDepartamentAgent;
	private ArrayAdapter<String> adapterSpinnerDepozite;
	private ArticolDB articolDBSelected;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectartcmdheader);

		initSelectionDepartament();

		if (!isCV())
			addSpinnerDepartamente();

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
		opArticol.setListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		resultLayout = (LinearLayout) findViewById(R.id.resLayout);
		resultLayout.setVisibility(View.INVISIBLE);

		nf2 = NumberFormat.getInstance();

		textStocKA = (TextView) findViewById(R.id.textStocKA);
		textUmKA = (TextView) findViewById(R.id.textUmKA);

		layoutStocKA = (LinearLayout) findViewById(R.id.layoutStocKA);
		layoutStocKA.setVisibility(View.INVISIBLE);

		textPretMinKA = (TextView) findViewById(R.id.textPretMaxKA);
		layoutPretMaxKA = (LinearLayout) findViewById(R.id.layoutPretMaxKA);
		layoutPretMaxKA.setVisibility(View.INVISIBLE);

		this.articoleBtn = (Button) findViewById(R.id.articoleBtn);
		addListenerBtnArticole();

		this.saveArtBtn = (Button) findViewById(R.id.saveArtBtn);
		addListenerBtnSaveArt();

		this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
		addListenerToggle();
		this.tglButton.setChecked(true);

		this.tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
		addListenerTglTipArtBtn();

		this.redBtnTable = (LinearLayout) findViewById(R.id.RedBtnTable);

		txtPretArt = (TextView) findViewById(R.id.txtPretArt);

		this.tglProc = (ToggleButton) findViewById(R.id.tglProc);
		addListenerTglProc();

		this.pretBtn = (Button) findViewById(R.id.pretBtn);
		addListenerPretBtn();

		textProcRed = (EditText) findViewById(R.id.textProcRed);
		textProcRed.setFocusableInTouchMode(true);
		addListenerProcArt();

		procDisc = (TextView) findViewById(R.id.procDisc);

		textMultipluArt = (TextView) findViewById(R.id.txtMultipluArt);

		txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
		textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
		textCodArticol = (TextView) findViewById(R.id.textCodArticol);
		textUM = (TextView) findViewById(R.id.textUm);
		textStoc = (TextView) findViewById(R.id.textStoc);
		textCant = (EditText) findViewById(R.id.txtCantArt);
		labelCant = (TextView) findViewById(R.id.labelCant);
		labelStoc = (TextView) findViewById(R.id.labelStoc);
		textCondPret = (TextView) findViewById(R.id.textCondPret);
		textPretTVA = (TextView) findViewById(R.id.textPretTVA);

		textPromo = (TextView) findViewById(R.id.textPromo);
		addListenerTextPromo();

		txtNumeArticol.setHint("Introduceti cod articol");

		CreareComanda.articoleComanda = "";

		spinnerDepoz = (Spinner) findViewById(R.id.spinnerDepoz);

		ArrayList<String> arrayListDepozite = new ArrayList<String>();
		arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteDistributie()));
		adapterSpinnerDepozite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListDepozite);

		adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDepoz.setAdapter(adapterSpinnerDepozite);
		spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

		spinnerUnitMas = (Spinner) findViewById(R.id.spinnerUnitMas);

		listUmVanz = new ArrayList<HashMap<String, String>>();
		adapterUmVanz = new SimpleAdapter(this, listUmVanz, R.layout.simplerowlayout, new String[] { "rowText" }, new int[] { R.id.textRowName });
		spinnerUnitMas.setVisibility(View.GONE);

		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol.setVisibility(View.INVISIBLE);
		textUM.setVisibility(View.INVISIBLE);

		textStoc.setVisibility(View.INVISIBLE);
		textCant.setVisibility(View.INVISIBLE);

		labelCant.setVisibility(View.INVISIBLE);

		txtPretArt.setVisibility(View.INVISIBLE);
		labelStoc.setVisibility(View.INVISIBLE);
		saveArtBtn.setVisibility(View.INVISIBLE);

		redBtnTable.setVisibility(View.INVISIBLE);
		textProcRed.setVisibility(View.INVISIBLE);
		pretBtn.setVisibility(View.INVISIBLE);
		textPromo.setVisibility(View.INVISIBLE);
		procDisc.setVisibility(View.INVISIBLE);
		textCondPret.setVisibility(View.INVISIBLE);
		textPretTVA.setVisibility(View.INVISIBLE);
		textMultipluArt.setVisibility(View.INVISIBLE);

	}

	private void addSpinnerDepartamente() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
				DepartamentAgent.getDepartamenteAgent());

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
		final Spinner spinnerDepartament = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

		spinnerDepartament.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerDepartament.getSelectedItem().toString());

				if (isDepartExtra() && selectedDepartamentAgent.equals("02"))
					CreareComanda.filialaAlternativa = "BV90";

				populateListViewArticol(new ArrayList<ArticolDB>());

				if (selectedDepartamentAgent.equals("11") || selectedDepartamentAgent.equals("05")) {
					adapterSpinnerDepozite.clear();
					adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());

					if (selectedDepartamentAgent.equals("11"))
						spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
					else
						spinnerDepoz.setSelection(0);
				} else {
					adapterSpinnerDepozite.clear();
					adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteDistributie());
					spinnerDepoz.setSelection(0);
				}

				saveArtBtn.setVisibility(View.INVISIBLE);

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerDepartament.setAdapter(adapter);
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	private void initSelectionDepartament() {

		selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

		if (isCV())
			selectedDepartamentAgent = "";

		if (isKA())
			selectedDepartamentAgent = "00";
	}

	private void CreateMenu(Menu menu) {

		if (isUserExceptie()) {
			MenuItem mnu1 = menu.add(0, 0, 0, "Filiala");
			{
				mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
				populateListViewArticol(new ArrayList<ArticolDB>());

			}
		}
	}

	private boolean isUserExceptie() {

		// pentru ag si sd de la 02 si 05 se ofera accesul la BV90
		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")) {
			if (UserInfo.getInstance().getCodDepart().equals("02") || UserInfo.getInstance().getCodDepart().equals("05"))
				return true;
		} else if (UserInfo.getInstance().getTipUser().equals("KA"))
			return true;

		return false;
	}

	private boolean isDepartExtra() {
		return !UserInfo.getInstance().getDepartExtra().equals("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	boolean isCV() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
	}

	// eveniment selectie depozit
	public class OnSelectDepozit implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (codArticol.length() > 0) {
				String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

				if (tokenDep[0].trim().length() == 2)
					globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
				else
					globalDepozSel = tokenDep[0].trim();

				performListArtStoc();
			}

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			showSelectFilArtDialogBox();
			return true;

		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void showSelectFilArtDialogBox() {
		dialogSelFilArt = new Dialog(SelectArtCmd.this);
		dialogSelFilArt.setContentView(R.layout.selectfilartdialogbox);
		dialogSelFilArt.setTitle("Selectati filiala");
		dialogSelFilArt.setCancelable(false);
		dialogSelFilArt.show();

		final RadioButton radioFilAg = (RadioButton) dialogSelFilArt.findViewById(R.id.radio1);
		radioFilAg.setText(UserInfo.getInstance().getUnitLog());

		radioFilAg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

				}

			}
		});

		final RadioButton radioFilBV90 = (RadioButton) dialogSelFilArt.findViewById(R.id.radio2);

		radioFilBV90.setText("BV90");

		radioFilBV90.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

				}

			}
		});

		if (CreareComanda.filialaAlternativa.equals(UserInfo.getInstance().getUnitLog()))
			radioFilAg.setChecked(true);
		else
			radioFilBV90.setChecked(true);

		Button btnOkFilArt = (Button) dialogSelFilArt.findViewById(R.id.btnOkSelFilArt);
		btnOkFilArt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (radioFilAg.isChecked()) {
					CreareComanda.filialaAlternativa = UserInfo.getInstance().getUnitLog();

				} else {

					CreareComanda.filialaAlternativa = "BV90";
				}

				if (!numeArticol.equals("")) {
					performListArtStoc();
				}

				dialogSelFilArt.dismiss();

			}
		});

	}

	public void addListenerTextPromo() {
		textPromo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!artPromoText.equals(""))
					showPromoWindow(artPromoText);

			}
		});
	}

	public void addListenerTglProc() {
		tglProc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (globalCantArt > 0) {

					if (tglProc.isChecked()) {

						nf2.setMinimumFractionDigits(3);
						nf2.setMaximumFractionDigits(3);

						varProc = -1;

						textProcRed.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
						textProcRed.requestFocus();
						textProcRed.selectAll();

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

						txtPretArt.setText("0");

						pretMod = true;
						finalPrice = initPrice;

						if (CreareComanda.canalDistrib.equals("10"))
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * 1.24)));
						else
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

					} else {

						nf2.setMinimumFractionDigits(3);
						nf2.setMaximumFractionDigits(3);

						varProc = 0;
						textProcRed.setText("");

						textProcRed.setSelection(textProcRed.getText().length());
						textProcRed.requestFocus();
						textProcRed.selectAll();
						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

						txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
						pretMod = false;
						finalPrice = initPrice;

						if (CreareComanda.canalDistrib.equals("10"))
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * 1.24)));
						else
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

					}
				}
			}
		});

	}

	public void addListenerProcArt() {

		textProcRed.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (textProcRed.hasFocus()) {
					InputMethodManager inputStatus = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputStatus.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}

			}
		});

		textProcRed.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					nf2.setMinimumFractionDigits(3);
					nf2.setMaximumFractionDigits(3);

					// verif. cantitate

					if (globalCantArt > 0) {

						if (!pretMod) // modificare valoare
						{
							if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

								if (varProc != -1) {
									varProc = Double.parseDouble(textProcRed.getText().toString());

									if (varProc >= 0) {

										double newPr = (((initPrice / globalCantArt) * valMultiplu - (initPrice / globalCantArt) * valMultiplu
												* (varProc / 100)));

										txtPretArt.setText(nf2.format(newPr));
										finalPrice = newPr;

										if (CreareComanda.canalDistrib.equals("10"))
											textPretTVA.setText(String.valueOf(nf2.format(finalPrice * 1.24)));
										else
											textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));
									}
								}

							} else {

								txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
								if (CreareComanda.canalDistrib.equals("10"))
									textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * 1.24)));
								else
									textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));
							}

						}// modificare procent
						else {

							if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

								double val1 = Double.parseDouble(textProcRed.getText().toString());

								procR = (((initPrice / globalCantArt * valMultiplu) - val1) / ((initPrice / globalCantArt * valMultiplu))) * 100;

								txtPretArt.setText(nf2.format(procR));
								finalPrice = Double.parseDouble(textProcRed.getText().toString());
							} else {
								txtPretArt.setText("0");
								finalPrice = 0;
							}

							if (CreareComanda.canalDistrib.equals("10"))
								textPretTVA.setText(String.valueOf(nf2.format(finalPrice * 1.24)));
							else
								textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));

						}

					}// sf. verif. cantitate

				} catch (Exception ex) {
					finalPrice = 0;
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});
	}

	public boolean isNumeric(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception ex) {
			Log.e("Error", ex.toString());
			return false;
		}
	}

	public void addListenerPretBtn() {
		pretBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				InputMethodManager mgr;

				try {

					if (textCant.getText().toString().trim().equals("")) {
						Toast.makeText(getApplicationContext(), "Cantitate invalida!", Toast.LENGTH_SHORT).show();
						return;
					}

					mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

					performGetPret();

				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	protected void performGetPret() {

		try {

			selectedCant = Double.parseDouble(textCant.getText().toString().trim());

			selectedUnitMas = "";
			if (listUmVanz.size() > 1) {
				artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
				selectedUnitMas = artMap.get("rowText");
			}

			if (selectedCant > 0) {

				actionGetPret();
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("unchecked")
	private void actionGetPret() {

		HashMap<String, String> params = new HashMap<String, String>();

		String depSel = "";
		String uLog = UserInfo.getInstance().getUnitLog();
		String tipUser = "";

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		depSel = globalCodDepartSelectetItem.substring(0, 2);

		if (CreareComanda.canalDistrib.equals("20") || globalDepozSel.equals("MAV1")) {
			depSel = "11";
			uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
		}

		if (UserInfo.getInstance().getTipAcces().equals("9")) {
			tipUser = "AV";
		}
		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipUser = "SD";
		}
		if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")) {
			tipUser = "DV";
		}

		String paramUnitMas = textUM.getText().toString();

		if (listUmVanz.size() > 1) {
			artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
			paramUnitMas = artMap.get("rowText");

		}

		params.put("client", CreareComanda.codClientVar);
		params.put("articol", codArticol);
		params.put("cantitate", textCant.getText().toString().trim());
		params.put("depart", depSel);
		params.put("um", paramUnitMas);
		params.put("ul", uLog);
		params.put("tipUser", tipUser);
		params.put("depoz", globalDepozSel);
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("canalDistrib", CreareComanda.canalDistrib);
		params.put("filialaAlternativa", CreareComanda.filialaAlternativa);

		opArticol.getPret(params);

	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {
					if (tglTipArtBtn.isChecked())
						txtNumeArticol.setHint("Introduceti cod sintetic");
					else
						txtNumeArticol.setHint("Introduceti cod articol");
				} else {
					if (tglTipArtBtn.isChecked())
						txtNumeArticol.setHint("Introduceti nume sintetic");
					else
						txtNumeArticol.setHint("Introduceti nume articol");
				}
			}
		});

	}

	public void addListenerTglTipArtBtn() {
		tglTipArtBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTipArtBtn.isChecked()) {
					if (!tglButton.isChecked())
						txtNumeArticol.setHint("Introduceti nume sintetic");
					else
						txtNumeArticol.setHint("Introduceti cod sintetic");
				} else {
					if (!tglButton.isChecked())
						txtNumeArticol.setHint("Introduceti nume articol");
					else
						txtNumeArticol.setHint("Introduceti cod articol");

				}
			}
		});

	}

	public void populateListViewArticol(List<ArticolDB> resultsList) {
		txtNumeArticol.setText("");
		resultLayout.setVisibility(View.INVISIBLE);
		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
		setListAdapter(adapterArticole);

	}

	boolean isNotDepartRestricted(String codDepart) {
		if (UserInfo.getInstance().getTipAcces().equals("27"))
			return true;
		else
			return UserInfo.getInstance().getCodDepart().equals(codDepart);
	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {

						textNumeArticol.setVisibility(View.GONE);
						textCodArticol.setVisibility(View.GONE);
						textUM.setVisibility(View.GONE);
						textStoc.setVisibility(View.GONE);
						textCant.setVisibility(View.GONE);
						labelCant.setVisibility(View.GONE);
						labelStoc.setVisibility(View.GONE);
						saveArtBtn.setVisibility(View.GONE);

						pretBtn.setVisibility(View.GONE);

						redBtnTable.setVisibility(View.GONE);
						spinnerUnitMas.setVisibility(View.GONE);
						layoutStocKA.setVisibility(View.GONE);

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

						performGetArticole();

					} else {
						Toast.makeText(getApplicationContext(), "Introduceti nume articol!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void performGetArticole() {

		try {

			String numeArticol = txtNumeArticol.getText().toString().trim();
			String tipCautare = "", tipArticol = "";

			if (tglButton.isChecked())
				tipCautare = "C";
			else
				tipCautare = "N";

			if (tglTipArtBtn.isChecked())
				tipArticol = "S";
			else
				tipArticol = "A";

			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("searchString", numeArticol);
			params.put("tipArticol", tipArticol);
			params.put("tipCautare", tipCautare);
			params.put("departament", selectedDepartamentAgent);

			opArticol.getArticoleDistributie(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("unchecked")
	public void addListenerBtnSaveArt() {
		saveArtBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					String localUnitMas = "";
					String alteValori = "", subCmp = "0";
					boolean altDepozit = false;

					if (textCant.getVisibility() != View.VISIBLE) {
						return;
					}

					if (textProcRed.getText().toString().trim().length() == 0) {
						if (tglProc.getText().equals(("%")))
							textProcRed.setText("0");

					}

					if (CreareComanda.articoleComanda.equals("")) {
						depozUnic = globalDepozSel.substring(2, 3);
						unitLogUnic = CreareComanda.filialaAlternativa;
					}

					if (!depozUnic.equals(globalDepozSel.substring(2, 3))) {
						altDepozit = true;
					}

					if (!CreareComanda.depozitUnic.equals("") && !CreareComanda.depozitUnic.equals(globalDepozSel.substring(2, 3))) {
						altDepozit = true;
					}

					if (!unitLogUnic.equals(CreareComanda.filialaAlternativa)) {
						Toast.makeText(getApplicationContext(), "Selectati articole dintr-o singura filiala!", Toast.LENGTH_LONG).show();

						return;
					}

					String cantArticol = textCant.getText().toString().trim();

					if (selectedCant != Double.parseDouble(cantArticol)) {

						Toast.makeText(getApplicationContext(), "Pretul nu corespunde cantitatii solicitate!", Toast.LENGTH_LONG).show();
						return;
					}

					if (Double.parseDouble(textCant.getText().toString().trim()) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", ""))) {
						Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
						return;
					}

					// verificare umvanz.

					localUnitMas = textUM.getText().toString().trim();

					if (listUmVanz.size() > 1) {

						artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
						localUnitMas = artMap.get("rowText");

						if (!selectedUnitMas.equals(localUnitMas)) {
							Toast.makeText(getApplicationContext(), "U.m. vanzare eronata!", Toast.LENGTH_LONG).show();

							return;
						}

					}

					// verificare procent discount
					double procRedFin = 0, valArticol = 0;
					procentAprob = 0;

					// exceptie material transport
					if (InfoStrings.isMatTransport(codArticol)) {
						initPrice = pretVanzare = finalPrice;
					}

					if (finalPrice == initPrice) // pretul din sap e pe
													// cantitate, daca se
													// modifica devine pe
													// unitate
						finalPrice = (finalPrice / globalCantArt) * valMultiplu;

					valArticol = (finalPrice / valMultiplu) * globalCantArt;

					if (initPrice != 0) {
						if (!tglProc.isChecked()) {
							if (textProcRed.getText().length() > 0)
								procRedFin = Double.parseDouble(textProcRed.getText().toString());
							else
								procRedFin = 0;
						} else
							procRedFin = (1 - finalPrice / (initPrice / globalCantArt * valMultiplu)) * 100;
					}

					if (procRedFin > 0)// procent_aprob se calculeaza doar daca
										// exista proc. de reducere dat de ag.
					{
						procentAprob = (1 - finalPrice / (pretVanzare / globalCantArt * valMultiplu)) * 100;
					}

					// sf. verificare

					if (finalPrice != 0) {

						tipAlert = " ";

						if (!UserInfo.getInstance().getTipAcces().equals("27")) {
							if (procentAprob > discMaxAV) {
								tipAlert = "SD";
							}
						}

						// pentru KA este nevoie de aprobarea SD-ului in cazul
						// in care
						// cantitatea comandata este mai mare decat jumatate din
						// stocul disponibil
						if (UserInfo.getInstance().getTipAcces().equals("27")) {
							if (Double.parseDouble(cantArticol) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", "")) / 2) {
								tipAlert = "SD";
							}
						}

						if (UserInfo.getInstance().getTipAcces().equals("27")) {// KA
							if (procentAprob > 0 && finalPrice < minimKAPrice) {

								if (!tipAlert.equals(""))
									tipAlert += ";" + "DV";
								else
									tipAlert = "DV";
							}

						} else {// agenti
							if (procentAprob > discMaxSD) {
								tipAlert = "DV";
							}
						}

						if (finalPrice < cmpArt) {
							subCmp = "1";
						}

						double procRedFact = 0; // factura de reducere
						if (listPrice != 0)
							procRedFact = (initPrice / globalCantArt * valMultiplu - finalPrice) / (listPrice / globalCantArt * valMultiplu) * 100;

						alteValori = String.valueOf(valArticol) + "!" + String.valueOf(listPrice) + "!" + String.valueOf(initPrice) + "!"
								+ String.valueOf(discMaxAV) + "!" + String.valueOf(discMaxSD) + "!" + codPromo + "!" + subCmp;

						NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
						nf.setMaximumFractionDigits(2);

						if (codArticol.length() == 18)
							codArticol = codArticol.substring(10, 18);

						ArticolComanda unArticol = new ArticolComanda();
						unArticol.setNumeArticol(numeArticol);
						unArticol.setCodArticol(codArticol);
						unArticol.setCantitate(Double.valueOf(cantArticol));
						unArticol.setDepozit(globalDepozSel);
						unArticol.setPretUnit(finalPrice);
						unArticol.setProcent(Double.valueOf(procRedFin));
						unArticol.setUm(localUnitMas);
						unArticol.setProcentFact(Double.valueOf(procRedFact));
						unArticol.setConditie(false);
						unArticol.setDiscClient(procDiscClient);
						unArticol.setProcAprob(procentAprob);
						unArticol.setMultiplu(valMultiplu);
						unArticol.setPret(valArticol);
						unArticol.setMoneda("RON");
						unArticol.setInfoArticol(infoArticol);
						unArticol.setCantUmb(Double.valueOf(cantUmb));
						unArticol.setUmb(Umb);
						unArticol.setAlteValori(alteValori);
						unArticol.setDepart(globalCodDepartSelectetItem);
						unArticol.setTipArt(tipArticol);
						unArticol.setPromotie(Integer.parseInt(codPromo));
						unArticol.setObservatii(tipAlert);
						unArticol.setDepartAprob(articolDBSelected.getDepartAprob());

						ListaArticoleComanda listaComanda = ListaArticoleComanda.getInstance();
						listaComanda.addArticolComanda(unArticol);

						if (!altDepozit) {
							if (CreareComanda.articoleComanda.indexOf(codArticol) == -1) // articolul
																							// nu
																							// e
																							// adaugat
																							// deja
								CreareComanda.articoleComanda += numeArticol + "#" + codArticol + "#" + cantArticol + "#" + String.valueOf(finalPrice) + "#"
										+ localUnitMas + "#" + globalDepozSel + "#" + nf.format(procRedFin) + "#" + tipAlert + "#" + codPromo + "#"
										+ nf.format(procRedFact) + "#" + nf.format(procDiscClient) + "#" + nf.format(procentAprob) + "#" + valMultiplu + "#"
										+ String.valueOf(valArticol) + "#" + infoArticol + "#" + Umb + "#" + cantUmb + "#" + alteValori + "#"
										+ globalCodDepartSelectetItem + "#" + tipArticol + "@@";

						} else {
							Toast.makeText(getApplicationContext(), "Comanda contine depozite diferite, articolul nu a fost adaugat! ", Toast.LENGTH_LONG)
									.show();

						}

						textNumeArticol.setText("");
						textCodArticol.setText("");
						textUM.setText("");
						textStoc.setText("");
						textCant.setText("");
						textPromo.setText("");
						txtNumeArticol.setText("");

						numeArticol = "";
						codArticol = "";
						tipArticol = "";
						umStoc = "";
						globalCodDepartSelectetItem = "";

						localUnitMas = "";
						procDiscClient = 0;
						initPrice = 0;
						finalPrice = 0;
						valArticol = 0;
						globalCantArt = 0;
						minimKAPrice = 0;
						cmpArt = 0;
						subCmp = "0";

						redBtnTable.setVisibility(View.GONE);
						labelStoc.setVisibility(View.GONE);
						labelCant.setVisibility(View.GONE);
						textCant.setVisibility(View.GONE);
						pretBtn.setVisibility(View.GONE);
						spinnerUnitMas.setVisibility(View.GONE);
						layoutStocKA.setVisibility(View.GONE);
						resultLayout.setVisibility(View.INVISIBLE);

						if (!tglProc.isChecked())
							tglProc.performClick();

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

					} else {

						Toast toast = Toast.makeText(getApplicationContext(), "Articolul nu are pret definit!", Toast.LENGTH_SHORT);
						toast.show();
					}

					textProcRed.setText("");

				} catch (Exception e) {

					Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	private void listArtStoc(String pretResponse) {

		resultLayout.setVisibility(View.VISIBLE);

		if (!pretResponse.equals("-1")) {

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			String[] tokenPret = pretResponse.split("#");

			textNumeArticol.setVisibility(View.VISIBLE);
			textCodArticol.setVisibility(View.VISIBLE);
			textUM.setVisibility(View.VISIBLE);
			textStoc.setVisibility(View.VISIBLE);
			textCant.setVisibility(View.VISIBLE);
			labelCant.setVisibility(View.VISIBLE);
			labelStoc.setVisibility(View.VISIBLE);
			pretBtn.setVisibility(View.VISIBLE);

			textUM.setText(tokenPret[1]);

			// verificare daca se afiseaza valoarea stocului (exceptia de la
			// BV90 pentr ag/sd)
			if (tokenPret[2].equals("0")) {
				textStoc.setVisibility(View.INVISIBLE);
			} else {
				textStoc.setVisibility(View.VISIBLE);
			}

			textStoc.setText(nf2.format(Double.valueOf(tokenPret[0])));

			// pentru KA se afiseaza stocul disponibil = stoc real / 2
			if (UserInfo.getInstance().getTipAcces().equals("27")) {
				layoutStocKA.setVisibility(View.VISIBLE);
				textUmKA.setText(tokenPret[1]);
				textStocKA.setText(nf2.format(Double.valueOf(tokenPret[0]) / 2));
			}

			umStoc = tokenPret[1];

			artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();

			String stocUM = artMap.get("rowText");

			if (!stocUM.equals(tokenPret[1]) && !tokenPret[1].trim().equals("")) // um
																					// vanz
																					// si
																					// um
																					// vanz
																					// difera
			{
				HashMap<String, String> tempUmVanz;
				tempUmVanz = new HashMap<String, String>();
				tempUmVanz.put("rowText", tokenPret[1]);

				listUmVanz.add(tempUmVanz);
				spinnerUnitMas.setAdapter(adapterUmVanz);
				spinnerUnitMas.setVisibility(View.VISIBLE);
			}

		} else {

			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

			textUM.setText("");
			textStoc.setText("");
		}

	}

	private void listArtPret(String pretResponse) {

		try {
			if (!pretResponse.equals("-1") && pretResponse.contains("#")) {

				String[] tokenPret = pretResponse.split("#");

				valMultiplu = 1;
				valMultiplu = Double.parseDouble(tokenPret[13].toString().trim());

				globalCantArt = Double.parseDouble(tokenPret[14]);

				cantUmb = tokenPret[14].toString();
				Umb = tokenPret[15].toString();

				cmpArt = nf2.parse(tokenPret[17]).doubleValue();

				saveArtBtn.setVisibility(View.VISIBLE);

				textPromo.setText("");

				nf2.setMinimumFractionDigits(3);
				nf2.setMaximumFractionDigits(3);

				codPromo = "-1";

				txtPretArt.setVisibility(View.VISIBLE);

				initPrice = Double.parseDouble(tokenPret[1]); // pret cu
																// discount pe
																// client
				listPrice = Double.parseDouble(tokenPret[8]); // pret de lista

				procDiscClient = 0;
				minimKAPrice = 0;
				if (UserInfo.getInstance().getTipAcces().equals("27")) {

					minimKAPrice = listPrice / globalCantArt * valMultiplu - (listPrice / globalCantArt * valMultiplu) * Double.valueOf(tokenPret[16]) / 100;

					if (listPrice > 0)
						procDiscClient = 100 - (initPrice / listPrice) * 100;

					layoutPretMaxKA.setVisibility(View.VISIBLE);
					textPretMinKA.setText(String.valueOf(nf2.format(minimKAPrice)));

				}

				if (globalDepozSel.substring(2, 3).equals("V")) {
					// pentru depozitele de vanzari se verifica cmp-ul

					if (initPrice / globalCantArt * valMultiplu < cmpArt) {

						Toast.makeText(getApplicationContext(), "Pret sub cmp!", Toast.LENGTH_LONG).show();

						if (layoutPretMaxKA.getVisibility() == View.VISIBLE)
							layoutPretMaxKA.setVisibility(View.INVISIBLE);

						return;
					}

				}

				finalPrice = initPrice;

				textProcRed.setText("");

				redBtnTable.setVisibility(View.VISIBLE);
				textProcRed.setVisibility(View.VISIBLE);
				procDisc.setVisibility(View.VISIBLE);
				textPretTVA.setVisibility(View.VISIBLE);
				textMultipluArt.setVisibility(View.VISIBLE);

				if (InfoStrings.isMatTransport(codArticol)) {
					txtPretArt.setVisibility(View.INVISIBLE);
				} else {
					txtPretArt.setVisibility(View.VISIBLE);
				}

				textMultipluArt.setText("Unit.pret: " + String.valueOf(valMultiplu) + " " + umStoc);

				txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
				txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));

				if (CreareComanda.canalDistrib.equals("10"))
					textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * 1.24)));
				else
					textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

				discMaxAV = Double.valueOf(tokenPret[10]);
				discMaxSD = Double.valueOf(tokenPret[11]);

				String[] condPret = tokenPret[9].toString().split(";");
				infoArticol = tokenPret[9].replace(',', '.');

				int ii = 0;
				String[] tokPret;
				String stringCondPret = "";
				Double valCondPret = 0.0;

				for (ii = 0; ii < condPret.length; ii++) {
					tokPret = condPret[ii].split(":");
					valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
					if (valCondPret != 0) {
						stringCondPret += tokPret[0] + addSpace(20 - tokPret[0].length()) + ":"
								+ addSpace(10 - String.valueOf(nf2.format(valCondPret)).length()) + nf2.format(valCondPret)
								+ System.getProperty("line.separator");

					}

				}

				pretVanzare = listPrice; // calcul procent aprobare

				textCondPret.setVisibility(View.VISIBLE);

				textCondPret.setText(stringCondPret);

				// calcul doar pentru AG
				if (!UserInfo.getInstance().getTipAcces().equals("27")) {
					if (listPrice > 0)
						procDiscClient = 100 - (initPrice / listPrice) * 100;
				}

				procDisc.setText(nf2.format(procDiscClient));
				txtPretArt.setEnabled(true);
				textProcRed.setFocusableInTouchMode(true);
				tglProc.setEnabled(true);

				// pentru totaluri negociate nu se modifica preturi
				if (CreareComanda.isTotalNegociat) {
					textProcRed.setFocusable(false);
					tglProc.setEnabled(false);
				}

				// se afiseaza direct pretul si nu procentul
				tglProc.setChecked(false);
				tglProc.performClick();

				if (noDiscount(tokenPret[3])) {
					txtPretArt.setEnabled(false);
					textProcRed.setFocusable(false);
					tglProc.setEnabled(false);
					codPromo = "1";

					if (Double.parseDouble(tokenPret[5]) != 0) {

						artPromoText = "";
						textPromo.setVisibility(View.VISIBLE);
						textPromo.setText("Articol cu promotie!");

						double pret1 = (Double.parseDouble(tokenPret[1]) / Double.parseDouble(tokenPret[0])) * valMultiplu;
						double pret2 = (Double.parseDouble(tokenPret[6]) / Double.parseDouble(tokenPret[5])) * valMultiplu;

						artPromoText = "Din cantitatea comandata " + tokenPret[0] + " " + tokenPret[2] + " au pretul de " + nf2.format(pret1) + " RON/"
								+ tokenPret[2] + " si " + tokenPret[5] + " " + tokenPret[7] + " au pretul de " + nf2.format(pret2) + " RON/" + tokenPret[7]
								+ ".";
					}

				} else {

					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

					// verificare articole promotii
					if (Double.parseDouble(tokenPret[5]) != 0) {
						artPromoText = "";
						textPromo.setVisibility(View.VISIBLE);

						// articolul din promotie are alt pret
						if (Double.parseDouble(tokenPret[6]) != 0) {

						} else // articolul din promotie este gratuit
						{
							codPromo = "2";

							// verificare cantitati articole gratuite
							// cant. art promotie se adauga la cant. ceruta
							if (Double.parseDouble(textCant.getText().toString().trim()) == Double.parseDouble(tokenPret[0])) {

								// verificare cod articol promotie
								// art. promo = art. din comanda
								if (codArticol.equals(tokenPret[4])) {
									artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + numeArticol + " gratuit. ";
								} else// art. promo diferit de art. din cmd.
								{
									artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + tokenPret[4] + " gratuit. ";

								}

							} else // cant art. promotie se scade din cant.
									// ceruta
							{

								artPromoText = "Din cantitatea comandata " + tokenPret[5] + " " + tokenPret[7] + " sunt gratis.";

							}

							textPromo.setText("Articol cu promotie");

						}

					}

				}

				// **la preturi zero se blocheaza modificarea
				if (Double.parseDouble(tokenPret[1].toString()) == 0) {
					txtPretArt.setEnabled(false);
				}

				if (!codPromo.equals("1")) {
					textProcRed.requestFocus();
				}

			} else {

				Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

			}

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private boolean noDiscount(String artPromo) {

		if (artPromo.toUpperCase(Locale.getDefault()).equals("X"))
			return true;
		else if ((CreareComanda.canalDistrib.equals("20") || globalCodDepartSelectetItem.equals("11"))
				&& !(globalDepozSel.equals("MAV1") && articolDBSelected.getDepart().equals("11") && !articolDBSelected.getDepartAprob().equals("00")))
			return true;

		return false;

	}

	public void showPromoWindow(String promoString) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(promoString).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).setTitle("Promotie!").setIcon(R.drawable.promotie96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

		articolDBSelected = articol;

		numeArticol = articol.getNume();
		codArticol = articol.getCod();
		tipArticol = articol.getTipAB();

		if (!tipArticol.trim().equals(""))
			numeArticol += " (" + tipArticol + ")";
		else
			tipArticol = " ";

		globalCodDepartSelectetItem = articol.getDepart();

		textNumeArticol.setText(numeArticol);
		textCodArticol.setText(codArticol);

		textUM.setText("");
		textStoc.setText("");
		textCant.setText("");

		String umVanz = articol.getUmVanz();
		listUmVanz.clear();
		spinnerUnitMas.setVisibility(View.GONE);
		HashMap<String, String> tempUmVanz;
		tempUmVanz = new HashMap<String, String>();
		tempUmVanz.put("rowText", umVanz);

		listUmVanz.add(tempUmVanz);
		spinnerUnitMas.setAdapter(adapterUmVanz);

		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol.setVisibility(View.INVISIBLE);
		saveArtBtn.setVisibility(View.GONE);

		redBtnTable.setVisibility(View.GONE);

		try {
			String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

			if (tokenDep[0].trim().length() == 2)
				globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
			else
				globalDepozSel = tokenDep[0].trim();

			performListArtStoc();

		} catch (Exception ex) {
			Log.e("Error", ex.toString());

		}

	}

	private void performListArtStoc() {
		try {

			if (this.getListView().getAdapter().getCount() == 0)
				return;

			HashMap<String, String> params = new HashMap<String, String>();

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			String varLocalUnitLog = "";

			if (globalDepozSel.equals("MAV1")) {
				if (CreareComanda.filialaAlternativa.equals("BV90"))
					varLocalUnitLog = "BV92";
				else
					varLocalUnitLog = CreareComanda.filialaAlternativa.substring(0, 2) + "2" + CreareComanda.filialaAlternativa.substring(3, 4);
			} else {
				varLocalUnitLog = CreareComanda.filialaAlternativa;
			}

			params.put("codArt", codArticol);
			params.put("filiala", varLocalUnitLog);

			params.put("depozit", globalDepozSel);
			params.put("depart", UserInfo.getInstance().getCodDepart());

			opArticol.getStocDepozit(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private String addSpace(int nrCars) {
		String retVal = "";

		for (int i = 0; i < nrCars; i++)
			retVal += " ";

		return retVal;
	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {

		switch (methodName) {

		case GET_STOC_DEPOZIT:
			listArtStoc((String) result);
			break;

		case GET_PRET:
			listArtPret((String) result);
			break;

		case GET_ARTICOLE_DISTRIBUTIE:
			populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
			break;

		default:
			break;

		}

	}

}
