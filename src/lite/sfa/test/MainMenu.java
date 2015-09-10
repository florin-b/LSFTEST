/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import model.UserInfo;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainMenu extends Activity {

	public GridView gridview;

	public String[] btnNamesAgents = { "Utilizator", "Creare comanda", "Modificare comanda", "Afisare comanda", "Creare cmd GED", "Comenzi simulate",
			"Cmz.blocate limita credit", "Creare CLP", "Afisare CLP", "Creare DL", "Afisare DL", "Retur paleti", "Stare retur paleti", "Vanzari",
			"Neincasate", "Stocuri", "Preturi", "Urmarire sablon", "Info client", "Clienti inactivi", "Clienti semiactivi", "Adrese clienti",
			"Info venituri", "Despre", "Iesire" };

	public int[] btnImageAgents = new int[] { R.drawable.id_icon, R.drawable.new_icon, R.drawable.modif_icon, R.drawable.preview_icon,
			R.drawable.blue_basket_icon, R.drawable.simulate, R.drawable.cmd_bloc, R.drawable.clp, R.drawable.afis_clp, R.drawable.box_orange_48,
			R.drawable.box_yellow_48, R.drawable.retur_marfa, R.drawable.status_retur_48, R.drawable.vanzari, R.drawable.neincasate,
			R.drawable.stoc_icon, R.drawable.dollar_icon, R.drawable.chart_icon, R.drawable.client_info, R.drawable.clienti_inactivi,
			R.drawable.clienti_inactivi, R.drawable.location_icon, R.drawable.line_chart_icon, R.drawable.despre_icon, R.drawable.exit_icon,
			R.drawable.blank };

	public String[] btnNamesSD = { "Utilizator", "Creare comanda", "Modificare comanda", "Afisare comanda", "Creare cmd GED",
			"Cmz.blocate limita credit", "Creare CLP", "Aprobare CLP", "Afisare CLP", "Creare DL", "Aprobare DL", "Afisare DL", "Retur paleti",
			"Stare retur paleti", "Vanzari", "Neincasate", "Stocuri", "Preturi", "Preturi concurenta", "Articole avarie", "Creare sablon",
			"Afisare sablon", "Urmarire sablon", "Aprobare comanda", "Comenzi conditionate", "Info client", "Clienti inactivi", "Clienti semiactivi",
			"Adrese clienti", "Info venituri", "Despre", "Iesire" };

	public int[] btnImageSD = new int[] { R.drawable.id_icon, R.drawable.new_icon, R.drawable.modif_icon, R.drawable.preview_icon,
			R.drawable.blue_basket_icon, R.drawable.cmd_bloc, R.drawable.clp, R.drawable.aprob_clp, R.drawable.afis_clp, R.drawable.box_orange_48,
			R.drawable.box_green_48, R.drawable.box_yellow_48, R.drawable.retur_marfa, R.drawable.status_retur_48, R.drawable.vanzari,
			R.drawable.neincasate, R.drawable.stoc_icon, R.drawable.dollar_icon, R.drawable.concurenta, R.drawable.znecesar1_icon,
			R.drawable.reduceri_icon, R.drawable.viewreduceri_icon, R.drawable.chart_icon, R.drawable.agree_icon, R.drawable.constraints,
			R.drawable.client_info, R.drawable.clienti_inactivi, R.drawable.clienti_inactivi, R.drawable.location_icon, R.drawable.line_chart_icon,
			R.drawable.despre_icon, R.drawable.exit_icon, R.drawable.blank };

	public String[] btnNamesDV = { "Utilizator", "Aprobare comanda", "Comenzi conditionate", "Afisare comanda", "Aprobare CLP", "Afisare CLP",
			"Cmz.blocate limita credit", "Vanzari", "Neincasate", "Stocuri", "Preturi", "Preturi concurenta", "Articole avarie", "Afisare sablon",
			"Urmarire sablon", "Info client", "Clienti inactivi", "Clienti semiactivi", "Adrese clienti", "Info venituri", "Despre", "Iesire" };

	public int[] btnImageDV = new int[] { R.drawable.id_icon, R.drawable.agree_icon, R.drawable.constraints, R.drawable.preview_icon,
			R.drawable.aprob_clp, R.drawable.afis_clp, R.drawable.cmd_bloc, R.drawable.vanzari, R.drawable.neincasate, R.drawable.stoc_icon,
			R.drawable.dollar_icon, R.drawable.concurenta, R.drawable.znecesar1_icon, R.drawable.viewreduceri_icon, R.drawable.chart_icon,
			R.drawable.client_info, R.drawable.clienti_inactivi, R.drawable.clienti_inactivi, R.drawable.location_icon, R.drawable.line_chart_icon,
			R.drawable.despre_icon, R.drawable.exit_icon, R.drawable.blank };

	public String[] btnNamesKA = { "Utilizator", "Creare comanda", "Modificare comanda", "Afisare comanda", "Cmz.blocate limita credit",
			"Creare CLP", "Afisare CLP", "Creare DL", "Afisare DL", "Obiective", "Retur paleti", "Stare retur paleti", "Vanzari", "Neincasate",
			"Stocuri", "Preturi", "Info client", "Adrese clienti", "Despre", "Iesire" };

	public int[] btnImageKA = new int[] { R.drawable.id_icon, R.drawable.new_icon, R.drawable.modif_icon, R.drawable.preview_icon,
			R.drawable.cmd_bloc, R.drawable.clp, R.drawable.afis_clp, R.drawable.box_orange_48, R.drawable.box_yellow_48, R.drawable.colosseum,
			R.drawable.retur_marfa, R.drawable.status_retur_48, R.drawable.vanzari, R.drawable.neincasate, R.drawable.stoc_icon,
			R.drawable.dollar_icon, R.drawable.client_info, R.drawable.location_icon, R.drawable.despre_icon, R.drawable.exit_icon, R.drawable.blank };

	public String[] btnNamesDK = { "Utilizator", "Afisare comanda", "Cmz.blocate limita credit", "Vanzari", "Neincasate", "Stocuri", "Preturi",
			"Info client", "Adrese clienti", "Despre", "Iesire" };

	public int[] btnImageDK = new int[] { R.drawable.id_icon, R.drawable.preview_icon, R.drawable.cmd_bloc, R.drawable.vanzari,
			R.drawable.neincasate, R.drawable.stoc_icon, R.drawable.dollar_icon, R.drawable.client_info, R.drawable.location_icon,
			R.drawable.despre_icon, R.drawable.exit_icon, R.drawable.blank };

	public String[] btnNamesCVA = { "Utilizator", "Creare cmd GED", "Modificare comanda", "Afisare comanda", "Comenzi simulate", "Creare CLP",
			"Afisare CLP", "Retur paleti", "Stare retur paleti", "Vanzari", "Neincasate", "Stocuri", "Preturi", "Info client", "Despre", "Iesire" };

	public int[] btnImageCVA = new int[] { R.drawable.id_icon, R.drawable.blue_basket_icon, R.drawable.modif_icon, R.drawable.preview_icon,
			R.drawable.simulate, R.drawable.clp, R.drawable.afis_clp, R.drawable.retur_marfa, R.drawable.status_retur_48, R.drawable.vanzari,
			R.drawable.neincasate, R.drawable.stoc_icon, R.drawable.dollar_icon, R.drawable.client_info, R.drawable.despre_icon,
			R.drawable.exit_icon, R.drawable.blank };

	public String[] btnNamesCONSGED = { "Utilizator", "Creare cmd GED", "Afisare comanda", "Comenzi simulate", "Retur paleti", "Stare retur paleti",
			"Vanzari", "Neincasate", "Stocuri", "Preturi", "Preturi concurenta", "Info client", "Despre", "Iesire" };

	public int[] btnImageCONSGED = new int[] { R.drawable.id_icon, R.drawable.blue_basket_icon, R.drawable.preview_icon, R.drawable.simulate,
			R.drawable.retur_marfa, R.drawable.status_retur_48, R.drawable.vanzari, R.drawable.neincasate, R.drawable.stoc_icon,
			R.drawable.dollar_icon, R.drawable.concurenta, R.drawable.client_info, R.drawable.despre_icon, R.drawable.exit_icon };

	public String[] btnNamesKA3 = { "Utilizator", "Creare comanda", "Afisare comanda", "Comenzi simulate", "Retur paleti", "Vanzari", "Neincasate",
			"Stocuri", "Preturi", "Info client", "Despre", "Iesire" };

	public int[] btnImageKA3 = new int[] { R.drawable.id_icon, R.drawable.new_icon, R.drawable.modif_icon, R.drawable.preview_icon,
			R.drawable.cmd_bloc, R.drawable.simulate, R.drawable.clp, R.drawable.afis_clp, R.drawable.retur_marfa, R.drawable.vanzari,
			R.drawable.neincasate, R.drawable.stoc_icon, R.drawable.dollar_icon, R.drawable.client_info, R.drawable.despre_icon,
			R.drawable.exit_icon, R.drawable.blank };

	private static final String URL = "http://10.1.0.58/androidwebservices/TESTService.asmx";
	String name = "", filiala = "";
	public String cod;
	public String numeDepart, codDepart, unitLog, tipAcces, parent;
	public FTPClient mFTPClient = null;
	private String buildVer = "0";
	private static final String DATABASE_NAME = "/download/AndroidLRTest";

	private Button modifCmdBtn, blocLimCredBtn, cmdCondBtn, aprobClp, aprobDl, aprobRetur;
	private Animation animation;
	private Timer timerCheckCmdCond = null;
	private Handler checkCondHandler = new Handler();

	private checkUpdate check = null;
	long timeMinimize = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.mainscreen);

		checkStaticVars();

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ButtonAdapter(this));

		animation = new AlphaAnimation(1, 0);

		animation.setDuration(800);
		animation.setInterpolator(new LinearInterpolator());

		animation.setRepeatCount(Animation.INFINITE);

		animation.setRepeatMode(Animation.REVERSE);

		// verificare update doar la logon
		if (UserInfo.getInstance().getParentScreen().equals("logon")) {

			PackageInfo pInfo = null;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (Exception e) {
				Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_LONG).show();

			}

			buildVer = String.valueOf(pInfo.versionCode);

			try {

				check = new checkUpdate(this);
				check.execute("dummy");

			} catch (Exception e) {
				Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_LONG).show();
			}

		}

	}// sf. OnCreate

	@Override
	public void onResume() {
		super.onResume();

		// restartare la inactivitate
		if (timeMinimize > 0) {
			long difference = System.currentTimeMillis() - timeMinimize;

			int days = (int) (difference / (1000 * 60 * 60 * 24));
			int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));

			if (hours > 10) {
				Intent nextScreen = new Intent(MainMenu.this, LogonScreen.class);
				startActivity(nextScreen);

				finish();
			}

		}

		checkStaticVars();

		if (check == null) {

			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
					|| UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
					|| UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("17")
					|| UserInfo.getInstance().getTipAcces().equals("18")) {

				if (timerCheckCmdCond == null) {
					timerCheckCmdCond = new Timer();
					timerCheckCmdCond.schedule(new UpdateTimeTask(), 1000, 30000);

				}

			}
		}

	}

	public class ButtonAdapter extends BaseAdapter {
		private Context mContext;

		public ButtonAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return getNrBtns(); // nr. butoane
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		public View getView(int position, View convertView, ViewGroup parent) {

			Button btn;
			Typeface font = Typeface.SERIF;

			if (convertView == null) {

				btn = new Button(mContext);
				btn.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 120));

			} else {
				btn = (Button) convertView;
			}

			btn.setText(getBtnName(position));

			btn.setCompoundDrawablesWithIntrinsicBounds(0, getBtnImage(position), 0, 0);

			btn.setId(position);
			btn.setTextSize(16);
			btn.setTextColor(android.graphics.Color.rgb(0, 104, 122));
			btn.setTypeface(font);
			btn.setOnClickListener(new MyOnClickListener(position));

			btn.setBackgroundResource(R.drawable.grid_button_style);

			return btn;

		}

		private void checkTimer() {
			if (timerCheckCmdCond != null) {
				timerCheckCmdCond.cancel();
				timerCheckCmdCond.purge();
				timerCheckCmdCond = null;
			}
		}

		class MyOnClickListener implements OnClickListener {
			private final int position;

			public MyOnClickListener(int position) {
				this.position = position;
			}

			public void onClick(View v) {

				String selectedBtnName = getBtnName(position);

				checkTimer();

				// info
				if (selectedBtnName.equalsIgnoreCase("Utilizator")) {

					Intent nextScreen = new Intent(MainMenu.this, User.class);
					startActivity(nextScreen);
					finish();

				}

				// creare comanda
				if (selectedBtnName.equalsIgnoreCase("Creare comanda")) {

					Class<?> className;
					if (UserInfo.getInstance().getTipUserSap().equals("KA3"))
						className = CreareComandaGed.class;
					else
						className = CreareComanda.class;

					Intent nextScreen = new Intent(MainMenu.this, className);
					startActivity(nextScreen);
					finish();

				}

				// modificare comanda
				if (selectedBtnName.equalsIgnoreCase("Modificare comanda")) {

					Intent nextScreen = new Intent(MainMenu.this, ModificareComanda.class);
					v.clearAnimation();
					startActivity(nextScreen);
					finish();

				}

				// afisare comanda
				if (selectedBtnName.equalsIgnoreCase("Afisare comanda")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisComanda.class);
					startActivity(nextScreen);
					finish();

				}

				// comenzi blocate pt. limita credit
				if (selectedBtnName.equalsIgnoreCase("Cmz.blocate limita credit")) {

					Intent nextScreen = new Intent(MainMenu.this, ComenziBlocateLimCredit.class);
					startActivity(nextScreen);
					finish();

				}

				// comenzi ged {
				if (selectedBtnName.equalsIgnoreCase("Creare cmd GED")) {

					Intent nextScreen = new Intent(MainMenu.this, CreareComandaGed.class);
					startActivity(nextScreen);
					finish();

				}

				// comenzi simulate
				if (selectedBtnName.equalsIgnoreCase("Comenzi simulate")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisComenziSimulate.class);
					startActivity(nextScreen);
					finish();

				}

				// creare clp
				if (selectedBtnName.equalsIgnoreCase("Creare CLP")) {

					Intent nextScreen = new Intent(MainMenu.this, CreareClp.class);
					startActivity(nextScreen);
					finish();

				}

				// aprobare clp
				if (selectedBtnName.equalsIgnoreCase("Aprobare CLP")) {

					Intent nextScreen = new Intent(MainMenu.this, AprobareClpActivity.class);
					startActivity(nextScreen);
					finish();

				}

				// afisare clp
				if (selectedBtnName.equalsIgnoreCase("Afisare CLP")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisareClpActivity.class);
					startActivity(nextScreen);
					finish();

				}

				// creare dl
				if (selectedBtnName.equalsIgnoreCase("Creare DL")) {

					Intent nextScreen = new Intent(MainMenu.this, CreareDispozitiiLivrare.class);
					startActivity(nextScreen);
					finish();

				}

				// aprobare dl
				if (selectedBtnName.equalsIgnoreCase("Aprobare DL")) {

					Intent nextScreen = new Intent(MainMenu.this, AprobareDispozitiiLivrare.class);
					startActivity(nextScreen);
					finish();

				}

				// afisare dl
				if (selectedBtnName.equalsIgnoreCase("Afisare DL")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisareDispozitiiLivrareActivity.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Retur paleti")) {

					Intent nextScreen = new Intent(MainMenu.this, ReturMarfa.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Stare retur paleti")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisareReturMarfa.class);
					startActivity(nextScreen);
					finish();

				}

				// obiective
				if (selectedBtnName.equalsIgnoreCase("Obiective")) {

					Intent nextScreen = new Intent(MainMenu.this, ObiectiveKA.class);
					startActivity(nextScreen);
					finish();

				}

				// vanzari
				if (selectedBtnName.equalsIgnoreCase("Vanzari")) {

					Intent nextScreen = new Intent(MainMenu.this, VanzariAgentiActivity.class);
					startActivity(nextScreen);
					finish();

				}

				// neincasate
				if (selectedBtnName.equalsIgnoreCase("Neincasate")) {

					Intent nextScreen = new Intent(MainMenu.this, Neincasate.class);
					startActivity(nextScreen);
					finish();

				}

				// stocuri
				if (selectedBtnName.equalsIgnoreCase("Stocuri")) {

					Intent nextScreen = new Intent(MainMenu.this, Stocuri.class);
					startActivity(nextScreen);
					finish();

				}

				// preturi
				if (selectedBtnName.equalsIgnoreCase("Preturi")) {

					Intent nextScreen = new Intent(MainMenu.this, PreturiActivity.class);
					startActivity(nextScreen);
					finish();

				}

				// preturi
				if (selectedBtnName.equalsIgnoreCase("Articole avarie")) {

					Intent nextScreen = new Intent(MainMenu.this, NecesarArticoleActivity.class);
					startActivity(nextScreen);
					finish();

				}

				// preturi concurenta
				if (selectedBtnName.equalsIgnoreCase("Preturi concurenta")) {

					Intent nextScreen = new Intent(MainMenu.this, PreturiConcurenta.class);
					startActivity(nextScreen);
					finish();

				}

				// creare reduceri ulterioare
				if (selectedBtnName.equalsIgnoreCase("Creare sablon")) {

					Intent nextScreen = new Intent(MainMenu.this, ReduceriUlterioare.class);
					startActivity(nextScreen);
					finish();

				}

				// afisare reduceri ulterioare
				if (selectedBtnName.equalsIgnoreCase("Afisare sablon")) {

					Intent nextScreen = new Intent(MainMenu.this, AfisReduceri.class);
					startActivity(nextScreen);
					finish();

				}

				// afisare grad realizare reduceri
				if (selectedBtnName.equalsIgnoreCase("Urmarire sablon")) {

					Intent nextScreen = new Intent(MainMenu.this, GradReduceri.class);
					startActivity(nextScreen);
					finish();

				}

				// aprobare comenzi
				if (selectedBtnName.equalsIgnoreCase("Aprobare comanda")) {

					Intent nextScreen = new Intent(MainMenu.this, AprobareComanda.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Info client")) {

					Intent nextScreen = new Intent(MainMenu.this, InfoClient.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Clienti inactivi")) {

					Intent nextScreen = new Intent(MainMenu.this, ClientiInactivi.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Clienti semiactivi")) {

					Intent nextScreen = new Intent(MainMenu.this, ClientiSemiactivi.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Adrese clienti")) {

					Intent nextScreen = new Intent(MainMenu.this, AdreseGpsClienti.class);
					startActivity(nextScreen);
					finish();

				}

				if (selectedBtnName.equalsIgnoreCase("Info venituri")) {

					Intent nextScreen = new Intent(MainMenu.this, InfoVenituri.class);
					startActivity(nextScreen);
					finish();

				}

				// retea
				if (selectedBtnName.equalsIgnoreCase("Retea")) {

					Intent nextScreen = new Intent(MainMenu.this, Retea.class);
					startActivity(nextScreen);
					finish();

				}

				// sincronizare articole
				if (selectedBtnName.equalsIgnoreCase("Sincronizare articole")) {

					Intent nextScreen = new Intent(MainMenu.this, SincArticole.class);
					startActivity(nextScreen);
					finish();

				}

				// sincronizare articole
				if (selectedBtnName.equalsIgnoreCase("Comenzi conditionate")) {

					Intent nextScreen = new Intent(MainMenu.this, ComenziConditionate.class);
					startActivity(nextScreen);
					finish();

				}

				// about
				if (selectedBtnName.equalsIgnoreCase("Despre")) {

					Intent nextScreen = new Intent(MainMenu.this, Update.class);
					startActivity(nextScreen);
					finish();

				}

				// exit
				if (selectedBtnName.equalsIgnoreCase("Iesire")) {

					System.exit(0);

				}

			}
		}

	}

	private class checkUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private checkUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {

			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Verificare versiune aplicatie...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			mFTPClient = new FTPClient();

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/LiteReportsVerTest.txt";

					FileOutputStream desFile2 = new FileOutputStream("sdcard/download/LiteReportsVerTest.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

					desFile2.close();

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();

			} finally {
				if (mFTPClient.isConnected()) {
					{
						try {
							mFTPClient.logout();
							mFTPClient.disconnect();
						} catch (IOException f) {
							errMessage = f.getMessage();
							Toast.makeText(MainMenu.this, errMessage, Toast.LENGTH_LONG).show();
						}
					}
				}
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
					Toast toast = Toast.makeText(MainMenu.this, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					validateUpdate();
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}

		}

	}

	public void validateUpdate() {

		try {

			File fVer = new File(Environment.getExternalStorageDirectory() + "/download/LiteReportsVerTest.txt");
			FileInputStream fileIS = new FileInputStream(fVer);
			BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = buf.readLine();
			String[] tokenVer = readString.split("#");
			fileIS.close();

			if (!tokenVer[2].equals("0")) // 1 - fisierul este gata pentru
											// update, 0 - inca nu
			{

				if (Float.parseFloat(buildVer) < Float.parseFloat(tokenVer[3])) {
					// exista update
					try {
						downloadUpdate download = new downloadUpdate(this);
						download.execute("dummy");
					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				} else {
					if (isUpdateble()) {

						if (timerCheckCmdCond == null) {
							timerCheckCmdCond = new Timer();
							timerCheckCmdCond.schedule(new UpdateTimeTask(), 1000, 30000);

						}

					}
				}

			} else {

			}

			check = null;

		} catch (Exception ex) {
			Toast.makeText(MainMenu.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	boolean isUpdateble() {
		return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
				|| UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("17")
				|| UserInfo.getInstance().getTipAcces().equals("18");
	}

	private class downloadUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private downloadUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Descarcare...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			mFTPClient = new FTPClient();

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/LiteSFATest.apk";
					FileOutputStream desFile1 = new FileOutputStream("sdcard/download/LiteSFATest.apk");
					mFTPClient.retrieveFile(sourceFile, desFile1);

					sourceFile = "/Update/LiteSFA/LiteReportsVerTEST.txt";
					FileOutputStream desFile2 = new FileOutputStream("sdcard/download/LiteReportsVerTest.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

					desFile1.close();
					desFile2.close();

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {
				if (mFTPClient.isConnected()) {
					{
						try {
							mFTPClient.logout();
							mFTPClient.disconnect();
						} catch (IOException f) {
							errMessage = f.getMessage();
						}
					}
				}
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
					Toast toast = Toast.makeText(MainMenu.this, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					startInstall();
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	public void startInstall() {

		String fileUrl = "/download/LiteSFATest.apk";
		String file = android.os.Environment.getExternalStorageDirectory().getPath() + fileUrl;
		File f = new File(file);

		if (f.exists()) {
			// stergere bd veche
			try {

				String fileDB = android.os.Environment.getExternalStorageDirectory().getPath() + DATABASE_NAME;
				this.getApplicationContext().deleteDatabase(fileDB);

			} catch (Exception ex) {
				Log.e("Error", ex.toString());
			}
			// sf. bd

			// start install
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "LiteSFATest.apk")),
					"application/vnd.android.package-archive");
			startActivity(intent);
			finish();
		} else {
			Toast toast = Toast.makeText(MainMenu.this, "Fisier corupt, repetati operatiunea!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	// verificare comenzi ce au conditii si comenzi ce necesita aprobare
	private class checkCmdCond extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private checkCmdCond(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {

			if (!isFinishing()) {
				this.dialog = new ProgressDialog(mContext);
				this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				this.dialog.setMessage("Verificare comanda conditii...");
				this.dialog.setCancelable(false);
				this.dialog.show();
			}
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject("http://SFATest.org/", "checkCmdCond");
				NumberFormat nf3 = new DecimalFormat("00000000");
				String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();
				String paramDepart = UserInfo.getInstance().getCodDepart();

				request.addProperty("agCode", fullCode);

				if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) { // ag,
					// ka,

					paramDepart = "-1";

				} else {

					if (UserInfo.getInstance().getTipAcces().equals("18") || UserInfo.getInstance().getTipAcces().equals("17")) // ged
					{
						paramDepart = "11";
					} else {
						paramDepart = UserInfo.getInstance().getCodDepart();
					}
				}

				request.addProperty("depart", paramDepart);
				request.addProperty("filiala", UserInfo.getInstance().getUnitLog());
				request.addProperty("tipUser", UserInfo.getInstance().getTipUser());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 15000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call("http://SFATest.org/checkCmdCond", envelope, headerList);
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
					Toast toast = Toast.makeText(MainMenu.this, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					startModifCmdBtnAnimation(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}

		}

	}

	public void startModifCmdBtnAnimation(String result) {

		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
				|| UserInfo.getInstance().getTipAcces().equals("35") || UserInfo.getInstance().getTipAcces().equals("17")
				|| UserInfo.getInstance().getTipAcces().equals("18")) // userul
		// este
		// agent
		// sau
		// ka,
		// se
		// verifica
		// doar comenzile ce au conditii si
		// cele blocate pt. limita de credit
		{
			String[] tipRes = result.split("#");

			// butonul "Modificare comanda"
			this.modifCmdBtn = (Button) gridview.findViewById(getBtnPosByName("Modificare comanda"));

			// butonul "Comenzi blocate limita credit"
			this.blocLimCredBtn = (Button) gridview.findViewById(getBtnPosByName("Cmz.blocate limita credit"));

			if (tipRes[0].equals("1")) {
				this.modifCmdBtn.startAnimation(animation);
			} else {
				this.modifCmdBtn.clearAnimation();
			}

			if (tipRes[1].equals("1")) {
				this.blocLimCredBtn.startAnimation(animation);
			} else {
				this.blocLimCredBtn.clearAnimation();
			}

		}
		if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("12")
				|| UserInfo.getInstance().getTipAcces().equals("14")) // sd,
		// sm
		// comenzile
		// ce
		// necesita
		// aprobare
		{
			String[] tipRes = result.split("#");

			// butonul "Aprobare comanda"
			this.modifCmdBtn = (Button) gridview.findViewById(getBtnPosByName("Aprobare comanda"));

			this.blocLimCredBtn = (Button) gridview.findViewById(getBtnPosByName("Cmz.blocate limita credit"));

			// butonul "Comenzi conditionate"
			this.cmdCondBtn = (Button) gridview.findViewById(getBtnPosByName("Comenzi conditionate"));

			if (tipRes[0].equals("1")) {
				this.modifCmdBtn.startAnimation(animation);
			} else {
				this.modifCmdBtn.clearAnimation();
			}

			if (tipRes[1].equals("1")) {
				this.blocLimCredBtn.startAnimation(animation);
			} else {
				this.blocLimCredBtn.clearAnimation();
			}

			if (tipRes[2].equals("1")) {
				this.cmdCondBtn.startAnimation(animation);
			} else {
				this.cmdCondBtn.clearAnimation();
			}

			// cereri CLP -> SD, DV
			if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("12")
					|| UserInfo.getInstance().getTipAcces().equals("14")) {

				this.aprobClp = (Button) gridview.findViewById(getBtnPosByName("Aprobare CLP"));

				if (tipRes[3].equals("1")) {
					this.aprobClp.startAnimation(animation);
				} else {
					this.aprobClp.clearAnimation();
				}

			}

			// cereri DL -> SD
			if (UserInfo.getInstance().getTipAcces().equals("10")) {

				this.aprobDl = (Button) gridview.findViewById(getBtnPosByName("Aprobare DL"));

				if (tipRes[4].equals("1")) {
					this.aprobDl.startAnimation(animation);
				} else {
					this.aprobDl.clearAnimation();
				}

			}

			// cereri comenzi retur -> SD
			if (UserInfo.getInstance().getTipAcces().equals("10")) {

				this.aprobRetur = (Button) gridview.findViewById(getBtnPosByName("Stare retur paleti"));

				if (tipRes[5].equals("1")) {
					this.aprobRetur.startAnimation(animation);
				} else {
					this.aprobRetur.clearAnimation();
				}

			}

		}

	}

	public void checkCmdCond() {
		try {

			checkCmdCond cmdCond = new checkCmdCond(this);
			cmdCond.execute("dummy");

		} catch (Exception e) {
			Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	class UpdateTimeTask extends TimerTask {
		public void run() {

			checkCondHandler.post(new Runnable() {
				public void run() {
					checkCmdCond();
				}
			});

		}
	}

	@Override
	public void onBackPressed() {

	}

	private void checkStaticVars() {
		// pentru in idle mare variabilele statice se sterg si setarile locale
		// se reseteaza

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

	public void onUserLeaveHint() { //

		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
				|| UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("27")) // ag,
		// sd,
		// dv,
		// dd,
		// ka
		{

			if (timerCheckCmdCond != null) {
				timerCheckCmdCond.cancel();
				timerCheckCmdCond.purge();
				timerCheckCmdCond = null;
			}

		}

		timeMinimize = System.currentTimeMillis();

	}

	private int getNrBtns() {
		int nrBtns = -1;

		if (UserInfo.getInstance().getTipUser().equals("AV")) {
			nrBtns = btnNamesAgents.length;
		}

		if (UserInfo.getInstance().getTipUser().equals("SD")) {
			nrBtns = btnNamesSD.length;
		}

		if (UserInfo.getInstance().getTipUser().equals("DV")) {
			nrBtns = btnNamesDV.length;
		}

		if (UserInfo.getInstance().getTipUser().equals("KA") && !UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			nrBtns = btnNamesKA.length;
		}

		if (UserInfo.getInstance().getTipUser().equals("DK")) {
			nrBtns = btnNamesDK.length;
		}

		if ((UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM"))
				&& !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			nrBtns = btnNamesCVA.length;
		}

		if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			nrBtns = btnNamesKA3.length;
		}

		if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			nrBtns = btnNamesCONSGED.length;
		}

		return nrBtns;

	}

	private String getBtnName(int btnPos) {
		String btnName = "";

		if (UserInfo.getInstance().getTipUser().equals("AV")) {
			btnName = btnNamesAgents[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("SD")) {
			btnName = btnNamesSD[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("DV")) {
			btnName = btnNamesDV[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("KA") && !UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			btnName = btnNamesKA[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("DK")) {
			btnName = btnNamesDK[btnPos];
		}

		if ((UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM"))
				&& !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			btnName = btnNamesCVA[btnPos];
		}

		if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			btnName = btnNamesKA3[btnPos];
		}

		if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			btnName = btnNamesCONSGED[btnPos];
		}

		return btnName;

	}

	private int getBtnImage(int btnPos) {

		int btnImg = 0;

		if (UserInfo.getInstance().getTipUser().equals("AV")) {
			btnImg = btnImageAgents[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("SD")) {
			btnImg = btnImageSD[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("DV")) {
			btnImg = btnImageDV[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("KA") && !UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			btnImg = btnImageKA[btnPos];
		}

		if (UserInfo.getInstance().getTipUser().equals("DK")) {
			btnImg = btnImageDK[btnPos];
		}

		if ((UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM"))
				&& !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			btnImg = btnImageCVA[btnPos];
		}

		if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			btnImg = btnImageKA3[btnPos];
		}

		if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
			btnImg = btnImageCONSGED[btnPos];
		}

		return btnImg;

	}

	private int getBtnPosByName(String btnName) {
		int position = 0;

		if (UserInfo.getInstance().getTipUser().equals("AV")) {

			for (int i = 0; i < btnNamesAgents.length; i++) {
				if (btnNamesAgents[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUser().equals("SD")) {

			for (int i = 0; i < btnNamesSD.length; i++) {
				if (btnNamesSD[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUser().equals("DV")) {

			for (int i = 0; i < btnNamesDV.length; i++) {
				if (btnNamesDV[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUser().equals("KA") && !UserInfo.getInstance().getTipUserSap().equals("KA3")) {

			for (int i = 0; i < btnNamesKA.length; i++) {
				if (btnNamesKA[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUser().equals("DK")) {

			for (int i = 0; i < btnNamesDK.length; i++) {
				if (btnNamesDK[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if ((UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM"))
				&& !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {

			for (int i = 0; i < btnNamesCVA.length; i++) {
				if (btnNamesCVA[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {

			for (int i = 0; i < btnNamesCVA.length; i++) {
				if (btnNamesKA3[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {

			for (int i = 0; i < btnNamesCONSGED.length; i++) {
				if (btnNamesCONSGED[i].equalsIgnoreCase(btnName)) {
					position = i;
					break;
				}
			}
		}

		return position;

	}

}// sf. class
