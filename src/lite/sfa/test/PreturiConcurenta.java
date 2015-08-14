package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.GenericSpinnerSelection;
import listeners.OperatiiConcurentaListener;
import listeners.SpinnerSelectionListener;
import model.OperatiiArticol;
import model.OperatiiArticolFactory;
import model.OperatiiConcurenta;
import model.OperatiiConcurentaImpl;
import model.UserInfo;
import utils.ScreenUtils;
import utils.UtilsGeneral;
import adapters.AfisPretConcurentaAdapter;
import adapters.CautareArticoleAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import beans.ArticolDB;
import beans.BeanCompanieConcurenta;
import beans.BeanPretConcurenta;
import enums.EnumArticoleConcurenta;
import enums.EnumConcurenta;
import enums.EnumOperatiiConcurenta;

public class PreturiConcurenta extends Activity implements SpinnerSelectionListener, OperatiiConcurentaListener {

	private String selectedArtName = "", selectedArtCod = "", codConcurenta = "", tipLista = "", umVanz = "";
	private GenericSpinnerSelection itemSelected = new GenericSpinnerSelection();

	private Button articoleBtn;
	private ToggleButton tglButton, tglTipArtBtn;
	private EditText txtNumeArticol;
	private TextView textNumeArticolSelectat;
	private TextView textCodArticolSelectat, labelUM;
	private EditText editPretArt;
	private LinearLayout artDetLayout;
	private ListView listViewArticoleConcurenta, listViewPreturi;
	private OperatiiArticol operatiiArticol;
	private OperatiiConcurenta operatiiConcurenta;
	private Spinner spinnerCompetition, spinnerTipActulizare;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		InitUISetup();

	}

	private void InitUISetup() {
		setTheme(R.style.LRTheme);
		setContentView(R.layout.preturi_concurenta);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Preturi concurenta");
		actionBar.setDisplayHomeAsUpEnabled(true);

		txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
		txtNumeArticol.setHint("Introduceti cod articol");

		operatiiArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);

		operatiiConcurenta = new OperatiiConcurentaImpl(this);
		operatiiConcurenta.setListener(PreturiConcurenta.this);

		tglButton = (ToggleButton) findViewById(R.id.togglebutton);
		addListenerToggle();
		tglButton.setChecked(true);

		tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
		addListenerTglTipArtBtn();

		articoleBtn = (Button) findViewById(R.id.articoleBtn);
		addListenerBtnArticole();

		listViewPreturi = (ListView) findViewById(R.id.listIstoricPret);

		spinnerCompetition = (Spinner) findViewById(R.id.spinnerCompetition);
		spinnerCompetition.setOnItemSelectedListener(itemSelected);

		fillCompetitionList(null);
		fillArtTypeList();

		addBtnPret();

		listViewArticoleConcurenta = (ListView) findViewById(R.id.listArticole);
		setListenerListConcurenta();

		textNumeArticolSelectat = (TextView) findViewById(R.id.textNumeArticolSel);
		textCodArticolSelectat = (TextView) findViewById(R.id.textCodArticolSel);
		labelUM = (TextView) findViewById(R.id.labelUM);

		artDetLayout = (LinearLayout) findViewById(R.id.artDetLayout);
		artDetLayout.setVisibility(View.INVISIBLE);

		itemSelected.setItemSelectionListener(this);

	}

	private void fillCompetitionList(List<BeanCompanieConcurenta> listConcurenta) {

		ArrayList<HashMap<String, String>> listCompetition = new ArrayList<HashMap<String, String>>();

		SimpleAdapter adapterCompetition = new SimpleAdapter(this, listCompetition, R.layout.generic_rowlayout, new String[] { "stringName",
				"stringId" }, new int[] { R.id.textName, R.id.textId });

		HashMap<String, String> temp;

		for (EnumConcurenta comp : EnumConcurenta.values()) {
			temp = new HashMap<String, String>();
			temp.put("stringName", comp.getName());
			temp.put("stringId", comp.getCode());
			listCompetition.add(temp);

		}

		if (listConcurenta != null) {
			for (BeanCompanieConcurenta comp : listConcurenta) {
				temp = new HashMap<String, String>();
				temp.put("stringName", comp.getNume());
				temp.put("stringId", comp.getCod());
				listCompetition.add(temp);

			}
		}

		spinnerCompetition.setAdapter(adapterCompetition);

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

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ScreenUtils.hideSoftKeyboard(getApplicationContext(), txtNumeArticol);
				if (isInputValid()) {
					getArticoleConcurenta();
				}

			}
		});

	}

	private boolean isInputValid() {
		if (txtNumeArticol.getText().toString().length() == 0) {
			Toast.makeText(PreturiConcurenta.this, "Cod sau nume articol invalid", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (spinnerCompetition.getSelectedItemPosition() == 0) {
			Toast.makeText(PreturiConcurenta.this, "Selectati un concurent", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (spinnerTipActulizare.getSelectedItemPosition() == 0) {
			Toast.makeText(PreturiConcurenta.this, "Selectati un tip de actualizare", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	protected void getArticoleConcurenta() {

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
			params.put("tipActualizare", tipLista);

			operatiiConcurenta.getArticoleConcurenta(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void fillArtTypeList() {

		spinnerTipActulizare = (Spinner) findViewById(R.id.spinnerArtTypeList);

		ArrayList<HashMap<String, String>> listTypes = new ArrayList<HashMap<String, String>>();

		SimpleAdapter adapterTypes = new SimpleAdapter(this, listTypes, R.layout.generic_rowlayout, new String[] { "stringName", "stringId" },
				new int[] { R.id.textName, R.id.textId });

		HashMap<String, String> temp;

		for (EnumArticoleConcurenta comp : EnumArticoleConcurenta.values()) {
			temp = new HashMap<String, String>();
			temp.put("stringName", comp.getListName());
			temp.put("stringId", comp.getListCode());
			listTypes.add(temp);

		}

		spinnerTipActulizare.setAdapter(adapterTypes);
		spinnerTipActulizare.setOnItemSelectedListener(itemSelected);

	}

	private void addBtnPret() {
		Button addPretBtn = (Button) findViewById(R.id.addPretBtn);
		addPretBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveNewPret();

			}
		});
	}

	private void saveNewPret() {

		if (selectedArtCod != null) {

			editPretArt = (EditText) findViewById(R.id.editPretArt);

			String valPret = editPretArt.getText().toString().trim();

			if (codConcurenta.length() > 0 && valPret.length() > 0) {
				HashMap<String, String> params = new HashMap<String, String>();

				params.put("codArt", selectedArtCod);
				params.put("codAgent", UserInfo.getInstance().getCod());
				params.put("concurent", codConcurenta);
				params.put("valoare", editPretArt.getText().toString());

				operatiiConcurenta.addPretConcurenta(params);

			}
		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	private void populateListPret(List<BeanPretConcurenta> listArticole) {
		AfisPretConcurentaAdapter adapter = new AfisPretConcurentaAdapter(this, listArticole);
		listViewPreturi.setAdapter(adapter);
	}

	private void populateListArticoleConcurenta(List<ArticolDB> listArticole) {
		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, listArticole);
		listViewArticoleConcurenta.setAdapter(adapterArticole);

	}

	private void setListenerListConcurenta() {
		listViewArticoleConcurenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
				ArticolDB articol = (ArticolDB) parent.getAdapter().getItem(pos);
				showPanelAdaugaPret(articol);

			}
		});
	}

	private void showPanelAdaugaPret(ArticolDB articol) {

		if (artDetLayout.getVisibility() == View.INVISIBLE)
			artDetLayout.setVisibility(View.VISIBLE);

		selectedArtName = articol.getNume();
		selectedArtCod = articol.getCod();
		umVanz = articol.getUmVanz();

		textNumeArticolSelectat.setText(selectedArtName);
		textCodArticolSelectat.setText(selectedArtCod);
		labelUM.setText("/" + umVanz);

		getPreturiConcurenta();

	}

	private void getPreturiConcurenta() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codArt", selectedArtCod);
		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("concurent", codConcurenta);
		operatiiConcurenta.getPretConcurenta(params);

	}

	public void selectedItem(View spinner, View parentView, String cod, String nume) {
		if (spinner.getId() == R.id.spinnerCompetition) {

			if (spinnerCompetition.getAdapter().getCount() == 1) {
				operatiiConcurenta.getCompaniiConcurente(UtilsGeneral.newHashMapInstance());
			} else {
				codConcurenta = cod;
				if (selectedArtCod.length() > 0)
					getPreturiConcurenta();
			}

		}

		if (spinner.getId() == R.id.spinnerArtTypeList) {
			tipLista = cod;
		}

	}

	public void operationComplete(EnumOperatiiConcurenta numeComanda, Object result) {
		switch (numeComanda) {
		case GET_ARTICOLE_CONCURENTA:
			populateListArticoleConcurenta(operatiiArticol.deserializeArticoleVanzare((String) result));
			break;
		case GET_COMPANII_CONCURENTE:
			fillCompetitionList(operatiiConcurenta.deserializeCompConcurente((String) result));
			break;
		case GET_PRET_CONCURENTA:
			populateListPret(operatiiConcurenta.deserializePreturiConcurenta((String) result));
			break;
		case ADD_PRET_CONCURENTA:
			editPretArt.setText("");
			getPreturiConcurenta();
			break;
		default:
			break;
		}

	}

}
