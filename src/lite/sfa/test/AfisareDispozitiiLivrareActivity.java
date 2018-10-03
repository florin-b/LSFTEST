/**
 * @author florinb
 * 
 */
package lite.sfa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.DlDAOListener;
import model.DlDAO;
import model.HandleJSONData;
import model.UserInfo;
import utils.UtilsDates;
import adapters.ArticoleDLExpAdapter;
import adapters.DLExpirateAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.ArticolCLP;
import beans.DLExpirat;
import beans.StatusIntervalLivrare;
import dialogs.SelectDateDialog;
import enums.EnumDlDAO;

public class AfisareDispozitiiLivrareActivity extends Activity implements DlDAOListener {

	Spinner spinnerCmdClp;

	public static String selectedCmd = "", selectedCmdSap = "";
	private ListView listViewArtCmdClp;

	private TextView textAdrLivr, textPersContact, textTelefon, textOras, textJudet, textDataLivrare, textTipMarfa, textMasa, textTipCamion, textTipIncarcare,
			textAprobatOC, textTipPlata, textObservatii, textTransport;

	private LinearLayout layoutCmdCondHead;

	private static int restrictiiAfisare = 0, intervalAfisare = 0;

	DlDAO operatiiComenzi;
	private RadioGroup radioStari;
	private Button btnDataLivrare;
	private Button btnSalveaza;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afisare_dispozitii_livrare);

		operatiiComenzi = new DlDAO(this);
		operatiiComenzi.setDlDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Modificare DL");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCmdClp = (Spinner) findViewById(R.id.spinnerCmdClp);

		spinnerCmdClp.setVisibility(View.INVISIBLE);
		addSpinnerListener();

		listViewArtCmdClp = (ListView) findViewById(R.id.listArtCmdClp);

		listViewArtCmdClp.setVisibility(View.INVISIBLE);

		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

		textTipMarfa = (TextView) findViewById(R.id.textTipMarfa);
		textMasa = (TextView) findViewById(R.id.textMasa);
		textTipCamion = (TextView) findViewById(R.id.textTipCamion);
		textTipIncarcare = (TextView) findViewById(R.id.textTipIncarcare);

		layoutCmdCondHead = (LinearLayout) findViewById(R.id.layoutCmdCondHead);
		layoutCmdCondHead.setVisibility(View.INVISIBLE);

		textAprobatOC = (TextView) findViewById(R.id.textAprobatOC);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textObservatii = (TextView) findViewById(R.id.textObservatii);
		textTransport = (TextView) findViewById(R.id.textTransport);

		radioStari = (RadioGroup) findViewById(R.id.radioStari);
		setRadioStariListener();

		btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
		setListenerDataLivrare();

		btnSalveaza = (Button) findViewById(R.id.btnSalveaza);
		setListenerBtnSalveaza();

		performGetComenziClp();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			final CharSequence[] constrCmd = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };
			AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			AlertDialog alertConstr;
			builderConstr.setTitle("Afiseaza cererile create");
			builderConstr.setCancelable(true);
			builderConstr.setSingleChoiceItems(constrCmd, intervalAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					intervalAfisare = which;

					performGetComenziClp();
					dialog.cancel();

				}
			});

			alertConstr = builderConstr.create();
			alertConstr.setCancelable(true);
			alertConstr.show();

			return true;

		case android.R.id.home:

			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void performGetComenziClp() {

		HashMap<String, String> params = new HashMap<String, String>();

		String localTipUser = "NN";

		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {
			localTipUser = "AV";
		}

		if (UserInfo.getInstance().getTipAcces().equals("10"))
			localTipUser = "SD";

		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipUser", localTipUser);
		params.put("codUser", UserInfo.getInstance().getCod());

		operatiiComenzi.getDLExpirate(params);

	}

	void populateCmdList(String cmdList) {

		HandleJSONData objDocList = new HandleJSONData(this, cmdList);
		ArrayList<DLExpirat> listDocumente = objDocList.decodeDLExpirat();

		DLExpirateAdapter adapterComenziClp = new DLExpirateAdapter(this, listDocumente);
		spinnerCmdClp.setAdapter(adapterComenziClp);

		if (listDocumente.size() > 0) {
			spinnerCmdClp.setVisibility(View.VISIBLE);
			listViewArtCmdClp.setVisibility(View.VISIBLE);
			layoutCmdCondHead.setVisibility(View.VISIBLE);

		} else {
			layoutCmdCondHead.setVisibility(View.INVISIBLE);
			spinnerCmdClp.setVisibility(View.INVISIBLE);
			listViewArtCmdClp.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	private void performArtCmd() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		operatiiComenzi.getArticoleDLExpirat(params);

	}

	private void populateArtCmdList(List<ArticolCLP> listArticole) {

		ArticoleDLExpAdapter adapterArticole = new ArticoleDLExpAdapter(this, listArticole);
		listViewArtCmdClp.setAdapter(adapterArticole);

	}

	private void setRadioStariListener() {
		radioStari.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.modifData) {
					btnDataLivrare.setVisibility(View.VISIBLE);
					btnSalveaza.setVisibility(View.INVISIBLE);
				} else if (checkedId == R.id.modifLivrat) {
					btnDataLivrare.setVisibility(View.INVISIBLE);
					btnSalveaza.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void setListenerDataLivrare() {
		btnDataLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Locale.setDefault(new Locale("ro"));

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SelectDateDialog datePickerDialog = new SelectDateDialog(AfisareDispozitiiLivrareActivity.this, datePickerListener, year, month, day);
				datePickerDialog.setTitle("Data livrare");

				datePickerDialog.show();

			}
		});
	}

	private void setListenerBtnSalveaza() {

		btnSalveaza.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// private RadioButton radioModifData, radioModifLivrat;

				if (((RadioButton) findViewById(R.id.modifData)).isChecked())
					setDLDataLivrare();
				else if (((RadioButton) findViewById(R.id.modifLivrat)).isChecked())
					setDLFinalizata();
			}
		});

	}

	private void setDLFinalizata() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		params.put("codAgent", UserInfo.getInstance().getCod());
		operatiiComenzi.setDLFinalizata(params);
	}

	private void setDLDataLivrare() {

		String[] dataLivrare = textDataLivrare.getText().toString().split("\\.");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		params.put("dataLivrare", dataLivrare[2] + dataLivrare[1] + dataLivrare[0]);
		operatiiComenzi.setDLDataLivrare(params);
	}

	private void dlFinalizata(String result) {
		if (result.equals("0")) {
			Toast.makeText(getApplicationContext(), "Operatie reusita", Toast.LENGTH_LONG).show();
			performGetComenziClp();

		} else {
			Toast.makeText(getApplicationContext(), "Operatie esuata", Toast.LENGTH_LONG).show();
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				StatusIntervalLivrare statusInterval = UtilsDates.getStatusIntervalLivrare(calendar.getTime());

				if (statusInterval.isValid()) {
					textDataLivrare.setText(displayFormat.format(calendar.getTime()));
					btnSalveaza.setVisibility(View.VISIBLE);
				} else
					Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	};

	private void clearAllData() {

		selectedCmd = "";
		selectedCmdSap = "";
		restrictiiAfisare = 0;
		intervalAfisare = 0;

	}

	@Override
	public void onBackPressed() {

		clearAllData();
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();
		return;
	}

	void addSpinnerListener() {
		spinnerCmdClp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				DLExpirat documentClp = ((DLExpirat) parent.getAdapter().getItem(position));
				textDataLivrare.setText(documentClp.getDataLivrare());
				selectedCmd = documentClp.getNrDocument();
				selectedCmdSap = documentClp.getNrDocumentSap();

				performArtCmd();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void operationDlComplete(EnumDlDAO methodName, Object result) {
		switch (methodName) {
		case GET_DL_EXPIRATE:
			populateCmdList((String) result);
			break;
		case GET_ARTICOLE_DL_EXPIRAT:
			populateArtCmdList(operatiiComenzi.decodeArticoleDLExpirat((String) result));
			break;
		case SET_DL_FINALIZATA:
		case SET_DL_DATALIVRARE:
			dlFinalizata((String) result);
			break;
		default:
			break;

		}

	}
}
