/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.ConnectionStrings;
import model.UserInfo;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SincArticole extends Activity {

	Button buttonInstall;
	public Button buttonUpdate;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";

	private String resArts = "";
	String tipAcces;
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.sincarticole);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Sincronizare articole");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		this.buttonUpdate = (Button) findViewById(R.id.buttonStartSync);
		addListenerUpdate();

		
	}

	public SincArticole() {

	}

	@Override
	public void onResume() {
		super.onResume();
		checkStaticVars();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");

			Intent nextScreen = new Intent(getApplicationContext(),
					MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	public void sincArtExtern() {
		performCreateDatabase();
		performGetArt();
	}

	public void addListenerUpdate() {
		buttonUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				performCreateDatabase();
				performGetArt();

			}
		});

	}

	public void performCreateDatabase() {

		try {

			String dbUrl = ConnectionStrings.getInstance().getDatabaseName();

			String file = android.os.Environment.getExternalStorageDirectory()
					.getPath() + dbUrl;
			this.getApplicationContext().deleteDatabase(file);

			File f = new File(file);

			if (!f.exists()) {
				SQLiteDatabase db = openOrCreateDatabase(dbUrl,
						SQLiteDatabase.CREATE_IF_NECESSARY, null);

				// articole
				db.execSQL(" create table if not exists articoledef  "
						+ " (id text ,"
						+ " nume text, sintetic text, codnivel1 text, umvanz_d text, umvanz_g text, tip_art text)");

				// grupe nivel 1
				db.execSQL(" create table if not exists nivel1def  "
						+ " (codnivel1 text ,numenivel1 text, sintetic text )");

				// articole complementare
				db.execSQL(" create table if not exists artcomplement  "
						+ " (depart text, sintetic text, sintetic_comp text, nivel1 text, nivel1_comp)");

				// sintetice
				db.execSQL(" create table if not exists sinteticedef  "
						+ " (id integer , nume text, cod_depart text )");

				db.close();

			}

		} catch (SQLiteException e) {

			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}

	}

	public void performGetArt() {

		try {

			syncArticole articol = new syncArticole(this);
			articol.execute(("dummy"));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}

	}

	private class syncArticole extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private syncArticole(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Citire articole...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(
						ConnectionStrings.getInstance().getNamespace(), "syncArtAndroid");

				String localNumeDepart = UserInfo.getInstance().getNumeDepart();
				String localCodDepart = UserInfo.getInstance().getCodDepart();

				if (UserInfo.getInstance().getTipAcces().equals("27")
						|| UserInfo.getInstance().getTipAcces().equals("35")) // KA,
															// DKA

				{
					localNumeDepart = "NEDE";
					localCodDepart = "00";
				}

				if (UserInfo.getInstance().getTipAcces().equals("17")
						|| UserInfo.getInstance().getTipAcces().equals("18")) // consilier
															// sau
															// sm

				{
					localNumeDepart = "NEDE";
					localCodDepart = "11";
				}

				request.addProperty("depart", localNumeDepart);
				request.addProperty("codDepart", localCodDepart);
				request.addProperty("filiala", UserInfo.getInstance().getUnitLog().substring(0, 2));

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						ConnectionStrings.getInstance().getUrl(), 600000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin"
								.getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace()
						+ "syncArtAndroid", envelope, headerList);
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
					Toast toast = Toast.makeText(getApplicationContext(),
							errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					if (result.contains("#")) {
						resArts = result;
						writeArtToTable();
					} else {
						Toast.makeText(getApplicationContext(),
								"Sincronizare esuata!", Toast.LENGTH_SHORT)
								.show();
					}
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	public class loadDatabase extends AsyncTask<String, String, String> {

		String errMessage = "";

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... url) {

			try {

				SQLiteDatabase checkDB;
				checkDB = SQLiteDatabase.openDatabase(
						ConnectionStrings.getInstance().getDatabaseName(), null,
						SQLiteDatabase.OPEN_READWRITE);
				checkDB.execSQL("delete  from articoledef; ");
				checkDB.execSQL("delete  from nivel1def; ");
				checkDB.execSQL("delete  from artcomplement; ");
				checkDB.execSQL("delete  from sinteticedef; ");

				String[] artDef = resArts.split("!");
				String[] tokenLinie = artDef[0].split("@@");
				String[] tokenClient;
				String client = "";

				mProgressDialog.setMax(tokenLinie.length);

				for (int i = 0; i < tokenLinie.length; i++) {

					client = tokenLinie[i];
					tokenClient = client.split("#");

					checkDB.execSQL("INSERT INTO articoledef "
							+ " (id, nume, sintetic, codnivel1, umvanz_d, umvanz_g, tip_art)"
							+ " VALUES ('" + tokenClient[0] + "', '"
							+ tokenClient[1].replace("'", "").trim() + "', "
							+ " '" + tokenClient[2] + "', '" + tokenClient[3]
							+ "', '" + tokenClient[4] + "', '" + tokenClient[5]
							+ "', '" + tokenClient[6] + "');");

					publishProgress("" + i);

				}

				tokenLinie = artDef[1].split("@@");
				client = "";

				mProgressDialog.setMax(tokenLinie.length);

				for (int i = 0; i < tokenLinie.length; i++) {

					client = tokenLinie[i];
					tokenClient = client.split("#");

					checkDB.execSQL("INSERT INTO nivel1def "
							+ " ( codnivel1, numenivel1, sintetic)"
							+ " VALUES ('" + tokenClient[0] + "', '"
							+ tokenClient[1].replace("'", "").trim() + "','"
							+ tokenClient[2] + "' );");

					publishProgress("" + i);

				}

				tokenLinie = artDef[2].split("@@");
				client = "";

				mProgressDialog.setMax(tokenLinie.length);

				for (int i = 0; i < tokenLinie.length; i++) {

					client = tokenLinie[i];
					tokenClient = client.split("#");

					checkDB.execSQL("INSERT INTO artcomplement "
							+ " ( depart, sintetic, sintetic_comp, nivel1, nivel1_comp)"
							+ " VALUES ('" + tokenClient[0] + "', '"
							+ tokenClient[1].replace("'", "").trim() + "','"
							+ tokenClient[2] + "','" + tokenClient[3] + "','"
							+ tokenClient[4] + "' );");

					publishProgress("" + i);

				}

				tokenLinie = artDef[3].split("@@");
				client = "";

				mProgressDialog.setMax(tokenLinie.length);

				for (int i = 0; i < tokenLinie.length; i++) {

					client = tokenLinie[i];

					if (client.contains("#")) {

						tokenClient = client.split("#");

						checkDB.execSQL("INSERT INTO sinteticedef "
								+ " ( id, nume, cod_depart )" + " VALUES ('"
								+ tokenClient[0] + "', '"
								+ tokenClient[1].replace("'", "").trim()
								+ "', '" + tokenClient[2] + "' );");
					}

					publishProgress("" + i);

				}

				checkDB.close();

			} catch (Exception e) {
				errMessage = e.getMessage();
			}

			return null;
		}

		public void onProgressUpdate(String... progress) {
			System.out.println(String.valueOf(progress));
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
			mProgressDialog.setCancelable(false);

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO

			removeDialog(DIALOG_DOWNLOAD_PROGRESS);

			if (errMessage != null) {
				if (errMessage.length() > 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Load database:" + errMessage, Toast.LENGTH_LONG);
					toast.show();
				} else {

					

				}
			}
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Scriere date...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.show();
			mProgressDialog.setCancelable(false);
			return mProgressDialog;
		default:
			return null;
		}
	}

	public void writeArtToTable() {
		try {
			loadDatabase load = new loadDatabase();
			load.execute(("dummy"));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void checkStaticVars() {
		// pentru in idle mare variabilele statice se sterg si setarile locale
		// se reseteaza

		// resetare locale la idle
		String locLang = getBaseContext().getResources().getConfiguration().locale
				.getLanguage();

		if (!locLang.toLowerCase(Locale.getDefault()).equals("en")) {

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
					.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
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
	public void onUserLeaveHint() { //

		finish();

	}


}
