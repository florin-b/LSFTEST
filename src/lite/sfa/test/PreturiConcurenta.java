package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.GenericSpinnerSelection;
import listeners.OperatiiConcurentaListener;
import listeners.SpinnerSelectionListener;
import model.OperatiiConcurenta;
import model.OperatiiConcurentaImpl;
import model.UserInfo;
import utils.UtilsGeneral;
import adapters.AdapterArticolConcurenta;
import adapters.AfisPretConcurentaAdapter;
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
import beans.BeanArticolConcurenta;
import beans.BeanCompanieConcurenta;
import beans.BeanPretConcurenta;
import enums.EnumArticoleConcurenta;
import enums.EnumConcurenta;
import enums.EnumOperatiiConcurenta;

public class PreturiConcurenta extends Activity implements SpinnerSelectionListener, OperatiiConcurentaListener {

	private String selectedArtName = "", selectedArtCod = "", umVanz = "";
	private GenericSpinnerSelection itemSelected = new GenericSpinnerSelection();

	private TextView textNumeArticolSelectat;
	private TextView textCodArticolSelectat, labelUM;
	private EditText editPretArt;
	private LinearLayout artDetLayout;
	private ListView listViewArticoleConcurenta, listViewPreturi;

	private OperatiiConcurenta operatiiConcurenta;
	private Spinner spinnerCompetition, spinnerTipActulizare;
	private String competitionId = "-1";
	private String tipActualizare = "-1";

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

		operatiiConcurenta = new OperatiiConcurentaImpl(this);
		operatiiConcurenta.setListener(PreturiConcurenta.this);

		listViewPreturi = (ListView) findViewById(R.id.listIstoricPret);

		spinnerCompetition = (Spinner) findViewById(R.id.spinnerCompetition);
		setSpinnerCompetitionListener();

		operatiiConcurenta.getCompaniiConcurente(UtilsGeneral.newHashMapInstance());

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

		SimpleAdapter adapterCompetition = new SimpleAdapter(this, listCompetition, R.layout.generic_rowlayout, new String[] { "stringName", "stringId" },
				new int[] { R.id.textName, R.id.textId });

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

	private void setSpinnerCompetitionListener() {
		spinnerCompetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@SuppressWarnings("unchecked")
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long arg3) {

				if (pos > 0) {
					HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);
					competitionId = artMap.get("stringId");
					getArticoleConcurenta();
				} else
					competitionId = "-1";

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	protected void getArticoleConcurenta() {

		if (!competitionId.equals("-1") && !tipActualizare.equals("-1")) {

			artDetLayout.setVisibility(View.INVISIBLE);

			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("codConcurent", competitionId);
			params.put("tipActualizare", tipActualizare);
			operatiiConcurenta.getArticoleConcurentaBulk(params);

		}

	}

	private void fillArtTypeList() {

		spinnerTipActulizare = (Spinner) findViewById(R.id.spinnerArtTypeList);

		ArrayList<HashMap<String, String>> listTypes = new ArrayList<HashMap<String, String>>();

		SimpleAdapter adapterTypes = new SimpleAdapter(this, listTypes, R.layout.generic_rowlayout, new String[] { "stringName", "stringId" }, new int[] {
				R.id.textName, R.id.textId });

		HashMap<String, String> temp;

		for (EnumArticoleConcurenta comp : EnumArticoleConcurenta.values()) {
			temp = new HashMap<String, String>();
			temp.put("stringName", comp.getListName());
			temp.put("stringId", comp.getListCode());
			listTypes.add(temp);

		}

		spinnerTipActulizare.setAdapter(adapterTypes);
		setSpinnerTipActListener();

	}

	private void setSpinnerTipActListener() {
		spinnerTipActulizare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@SuppressWarnings("unchecked")
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long arg3) {

				if (pos > 0) {
					HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);
					tipActualizare = artMap.get("stringId");
					getArticoleConcurenta();
				} else
					tipActualizare = "-1";

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
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

			if (competitionId.length() > 0 && valPret.length() > 0) {
				HashMap<String, String> params = new HashMap<String, String>();

				params.put("codArt", selectedArtCod);
				params.put("codAgent", UserInfo.getInstance().getCod());
				params.put("concurent", competitionId);
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

	private void populateListArticoleConcurenta(List<BeanArticolConcurenta> listArticole) {
		AdapterArticolConcurenta adapterArticole = new AdapterArticolConcurenta(this, listArticole);
		listViewArticoleConcurenta.setAdapter(adapterArticole);

	}

	private void setListenerListConcurenta() {
		listViewArticoleConcurenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
				BeanArticolConcurenta articol = (BeanArticolConcurenta) parent.getAdapter().getItem(pos);
				showPanelAdaugaPret(articol);

			}
		});
	}

	private void showPanelAdaugaPret(BeanArticolConcurenta articol) {

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
		params.put("concurent", competitionId);
		operatiiConcurenta.getPretConcurenta(params);

	}

	public void selectedItem(View spinner, View parentView, String cod, String nume) {
		if (spinner.getId() == R.id.spinnerCompetition) {

			if (spinnerCompetition.getAdapter().getCount() == 1) {
				operatiiConcurenta.getCompaniiConcurente(UtilsGeneral.newHashMapInstance());
			} else {
				competitionId = cod;
				if (selectedArtCod.length() > 0)
					getPreturiConcurenta();
			}

		}

	}

	public void operationComplete(EnumOperatiiConcurenta numeComanda, Object result) {
		switch (numeComanda) {
		case GET_ARTICOLE_CONCURENTA_BULK:
			populateListArticoleConcurenta(operatiiConcurenta.deserializeArticoleConcurenta((String) result));
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
