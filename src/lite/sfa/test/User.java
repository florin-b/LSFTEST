/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import model.InfoStrings;
import model.UserInfo;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import enums.EnumFiliale;

public class User extends Activity {

	Button buttonUpdate, buttonInstall;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private TextView numeUser, filUser, codU, tipA, labelFiliala;
	String tipAcces;
	private Spinner spinnerFiliala, spinnerDepart;
	private static ArrayList<HashMap<String, String>> listFiliala = null, listDepart = null;
	public SimpleAdapter adapterFiliala, adapterDepart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);

		setContentView(R.layout.user_activity);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Utilizator");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		numeUser = (TextView) findViewById(R.id.numeUser);
		numeUser.setText(UserInfo.getInstance().getNume());

		filUser = (TextView) findViewById(R.id.filiala);
		filUser.setText(UserInfo.getInstance().getFiliala());

		codU = (TextView) findViewById(R.id.codUser);
		codU.setText(UserInfo.getInstance().getCod());

		tipA = (TextView) findViewById(R.id.tipAcces);
		tipA.setText(getTipAccDesc(UserInfo.getInstance().getTipAcces()));

		spinnerFiliala = (Spinner) findViewById(R.id.spinnerFiliala);
		spinnerFiliala.setVisibility(View.GONE);

		spinnerDepart = (Spinner) findViewById(R.id.spinnerDepart);
		spinnerDepart.setEnabled(false);

		labelFiliala = (TextView) findViewById(R.id.labelFiliala);
		labelFiliala.setVisibility(View.GONE);

		// exceptie pentru agenti si sd din BUC
		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")) {
			if (UserInfo.getInstance().getUnitLog().equals("BU10") || UserInfo.getInstance().isAltaFiliala()) {
				if (UserInfo.getInstance().getCodDepart().equals("01")
						|| UserInfo.getInstance().getCodDepart().equals("02")) {

					listFiliala = new ArrayList<HashMap<String, String>>();
					adapterFiliala = new SimpleAdapter(this, listFiliala, R.layout.simplerowlayout_1,
							new String[] { "rowText" }, new int[] { R.id.textRowName });

					HashMap<String, String> temp;
					temp = new HashMap<String, String>();
					temp.put("rowText", "BU10");
					listFiliala.add(temp);

					temp = new HashMap<String, String>();
					temp.put("rowText", "BU13");
					listFiliala.add(temp);

					spinnerFiliala.setAdapter(adapterFiliala);
					filUser.setText("GLINA");

					if (UserInfo.getInstance().getUnitLog().equals("BU13")) {
						spinnerFiliala.setSelection(1);
						filUser.setText("ANDRONACHE");
					}

					spinnerFiliala.setVisibility(View.VISIBLE);
					labelFiliala.setVisibility(View.VISIBLE);

					spinnerFiliala.setOnItemSelectedListener(new onSelectedFiliala());

				}

			}
		}

		// user site
		if (UserInfo.getInstance().getUserSite().equals("X")) {

			// afisare filiale BUC
			listFiliala = new ArrayList<HashMap<String, String>>();
			adapterFiliala = new SimpleAdapter(this, listFiliala, R.layout.simplerowlayout_1,
					new String[] { "rowText" }, new int[] { R.id.textRowName });

			HashMap<String, String> temp;

			for (EnumFiliale enumF : EnumFiliale.values()) {
				temp = new HashMap<String, String>();
				temp.put("rowText", enumF.getCod());
				listFiliala.add(temp);
			}

			spinnerFiliala.setAdapter(adapterFiliala);
			spinnerFiliala.setVisibility(View.VISIBLE);
			labelFiliala.setVisibility(View.VISIBLE);
			spinnerFiliala.setOnItemSelectedListener(new onSelectedFiliala());
			spinnerFiliala.setSelection(EnumFiliale.getItemPosition(UserInfo.getInstance().getUnitLog()));

		}

		if ((UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("17") || UserInfo
				.getInstance().getTipAcces().equals("18"))
				&& !UserInfo.getInstance().getUserSite().equals("X"))// KA,
		// cons
		// sau
		// sm
		{

			// pentru cei din BUC posibilitate selectie filiale BUC
			if (UserInfo.getInstance().getUnitLog().contains("BU")) {
				// afisare filiale BUC
				listFiliala = new ArrayList<HashMap<String, String>>();
				adapterFiliala = new SimpleAdapter(this, listFiliala, R.layout.simplerowlayout_1,
						new String[] { "rowText" }, new int[] { R.id.textRowName });

				HashMap<String, String> temp;
				temp = new HashMap<String, String>();
				temp.put("rowText", "BU10");
				listFiliala.add(temp);

				temp = new HashMap<String, String>();
				temp.put("rowText", "BU11");
				listFiliala.add(temp);

				temp = new HashMap<String, String>();
				temp.put("rowText", "BU12");
				listFiliala.add(temp);

				temp = new HashMap<String, String>();
				temp.put("rowText", "BU13");
				listFiliala.add(temp);

				spinnerFiliala.setAdapter(adapterFiliala);

				spinnerFiliala.setVisibility(View.VISIBLE);
				labelFiliala.setVisibility(View.VISIBLE);

				spinnerFiliala.setOnItemSelectedListener(new onSelectedFiliala());

				if (UserInfo.getInstance().getUnitLog().equals("BU10")) {
					spinnerFiliala.setSelection(0);
				}

				if (UserInfo.getInstance().getUnitLog().equals("BU11")) {
					spinnerFiliala.setSelection(1);
				}

				if (UserInfo.getInstance().getUnitLog().equals("BU12")) {
					spinnerFiliala.setSelection(2);
				}

				if (UserInfo.getInstance().getUnitLog().equals("BU13")) {
					spinnerFiliala.setSelection(3);
				}

			}// sf. if BUC

			spinnerDepart.setEnabled(true);

			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")) // cons
			// sau
			// sm
			// -
			// nu
			// se
			// afiseaza
			// depart
			{
				spinnerDepart.setVisibility(View.INVISIBLE);
			}

		} // sf. KA

		if (UserInfo.getInstance().getExtraFiliale().size() > 0) {
			addExtraFiliale();
		}

		listDepart = new ArrayList<HashMap<String, String>>();
		adapterDepart = new SimpleAdapter(this, listDepart, R.layout.simplerowlayout_2, new String[] { "rowText",
				"rowDesc" }, new int[] { R.id.textRowName, R.id.textRowId });

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("rowText", "01");
		temp.put("rowDesc", "Lemnoase");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "02");
		temp.put("rowDesc", "Feronerie");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "03");
		temp.put("rowDesc", "Parchet");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "04");
		temp.put("rowDesc", "Materiale grele");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "05");
		temp.put("rowDesc", "Electrice");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "06");
		temp.put("rowDesc", "Gips");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "07");
		temp.put("rowDesc", "Chimice");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "08");
		temp.put("rowDesc", "Instalatii");
		listDepart.add(temp);

		temp = new HashMap<String, String>();
		temp.put("rowText", "09");
		temp.put("rowDesc", "Hidroizolatii");
		listDepart.add(temp);

		spinnerDepart.setAdapter(adapterDepart);

		spinnerDepart.setSelection(0);

		if (UserInfo.getInstance().getCodDepart().equals("01")) {
			spinnerDepart.setSelection(0);
		}

		if (UserInfo.getInstance().getCodDepart().equals("02")) {
			spinnerDepart.setSelection(1);
		}

		if (UserInfo.getInstance().getCodDepart().equals("03")) {
			spinnerDepart.setSelection(2);
		}

		if (UserInfo.getInstance().getCodDepart().equals("04")) {
			spinnerDepart.setSelection(3);
		}

		if (UserInfo.getInstance().getCodDepart().equals("05")) {
			spinnerDepart.setSelection(4);
		}

		if (UserInfo.getInstance().getCodDepart().equals("06")) {
			spinnerDepart.setSelection(5);
		}

		if (UserInfo.getInstance().getCodDepart().equals("07")) {
			spinnerDepart.setSelection(6);
		}

		if (UserInfo.getInstance().getCodDepart().equals("08")) {
			spinnerDepart.setSelection(7);
		}

		if (UserInfo.getInstance().getCodDepart().equals("09")) {
			spinnerDepart.setSelection(8);
		}

		spinnerDepart.setOnItemSelectedListener(new onSelectedDepart());

		String languageToLoad = "ro";
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources()
				.updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

	}

	// captare evenimente spinner filiale
	public class onSelectedFiliala implements OnItemSelectedListener {

		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
			HashMap<String, String> map = (HashMap<String, String>) adapterFiliala.getItem(pos);

			String localSelectedFil = map.get("rowText");

			String filSel = EnumFiliale.getNumeFiliala(localSelectedFil).toUpperCase();

			UserInfo.getInstance().setUnitLog(localSelectedFil);
			UserInfo.getInstance().setFiliala(filSel);
			filUser.setText(UserInfo.getInstance().getFiliala());
			UserInfo.getInstance().setAltaFiliala(true);

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO
		}
	}

	private boolean isUserExceptieBZ_HD() {
		return (UserInfo.getInstance().getUnitLog().equals("GL10")
				|| UserInfo.getInstance().getUnitLog().equals("BZ10")
				|| UserInfo.getInstance().getUnitLog().equals("TM10") || UserInfo.getInstance().getUnitLog()
				.equals("HD10"))
				&& UserInfo.getInstance().getCodDepart().contains("04")
				&& (UserInfo.getInstance().getTipUserSap().equals("AV") || UserInfo.getInstance().getTipUserSap()
						.equals("SD"));
	}

	// captare evenimente spinner departament
	public class onSelectedDepart implements OnItemSelectedListener {

		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			HashMap<String, String> map = (HashMap<String, String>) adapterDepart.getItem(pos);

			UserInfo.getInstance().setCodDepart(map.get("rowText").toString());
			UserInfo.getInstance().setNumeDepart(InfoStrings.getNumeDepart(UserInfo.getInstance().getCodDepart()));

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO
		}
	}

	private String getTipAccDesc(String codAcc) {
		String retVal = "Nedefinit";

		if (codAcc.equals("8")) {
			retVal = "Administrator";
		} else if (codAcc.equals("9")) {
			retVal = "Agent";
		} else if (codAcc.equals("10")) {
			retVal = "Sef departament";
		}

		else if (codAcc.equals("14")) {
			retVal = "Director vanzari";
		}

		else if (codAcc.equals("12")) {
			retVal = "Director departament";
		}

		else if (codAcc.equals("27")) {
			retVal = "Keyaccount";
		}

		else if (codAcc.equals("35")) {
			retVal = "Director keyaccount";
		}

		else if (codAcc.equals("17")) {
			retVal = "Consilier";
		}

		else if (codAcc.equals("18")) {
			retVal = "Sef de magazin";
		}

		return retVal;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void checkStaticVars() {
		// pentru in idle mare variabilele statice se sterg si setarile locale
		// se reseteaza

		// resetare locale la idle
		String locLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

		if (!locLang.toLowerCase(Locale.ENGLISH).equals("en")) {

			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		}

		// restart app la idle
		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}

	private void addExtraFiliale() {
		listFiliala = new ArrayList<HashMap<String, String>>();
		adapterFiliala = new SimpleAdapter(this, listFiliala, R.layout.simplerowlayout_1, new String[] { "rowText" },
				new int[] { R.id.textRowName });

		HashMap<String, String> temp;

		temp = new HashMap<String, String>();
		temp.put("rowText", UserInfo.getInstance().getInitUnitLog());
		listFiliala.add(temp);

		int selectedItem = 0, i = 1;

		for (String filiala : UserInfo.getInstance().getExtraFiliale()) {
			temp = new HashMap<String, String>();
			temp.put("rowText", filiala);
			listFiliala.add(temp);

			if (UserInfo.getInstance().getUnitLog().equals(filiala))
				selectedItem = i;

			i++;
		}

		spinnerFiliala.setAdapter(adapterFiliala);
		spinnerFiliala.setVisibility(View.VISIBLE);
		labelFiliala.setVisibility(View.VISIBLE);
		spinnerFiliala.setOnItemSelectedListener(new onSelectedFiliala());
		spinnerFiliala.setSelection(selectedItem);
	}

	public void onResume() {

		super.onResume();
		checkStaticVars();
	}

	@Override
	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();
		return;
	}

}