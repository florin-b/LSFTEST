/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import listeners.AsyncTaskListener;
import model.Constants;
import model.InfoStrings;
import model.UserInfo;
import utils.UtilsGeneral;
import utils.UtilsUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogonScreen extends Activity implements AsyncTaskListener {

	private EditText etUsername;
	private EditText etPassword;
	private TextView lblResult;

	private static final String METHOD_NAME = "userLogon";

	public static String codInitDepart = "00";
	public static String numeInitDepart = "NEDE", globalMyIP = "0.0.0.0";

	public static Double discMaxAV = 0.0, discMaxSD = 0.0, discMaxDV = 0.0;

	MySwitch slideToUnLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		System.setProperty("http.keepAlive", "false");

		// formatare data si numere = en
		String languageToLoad = "en";
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		//

		setTheme(R.style.LiteTheme);
		setContentView(R.layout.main);

		if (isDebugMode(this)) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath()
					.build());
		}

		slideToUnLock = (MySwitch) findViewById(R.id.switchLogon);
		slideToUnLock.toggle();
		slideToUnLock.disableClick();
		slideToUnLock.fixate(false);
		slideToUnLock.requestFocus();

		addListenerSlider();

		etUsername = (EditText) findViewById(R.id.txtUserName);
		etPassword = (EditText) findViewById(R.id.txtPassword);
		lblResult = (TextView) findViewById(R.id.result);

		etUsername.setHint("Utilizator");
		etPassword.setHint("Parola");

		etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		addListenerUserName();
		addListenerPassword();

		etUsername.setText("androsd");
		etPassword.setText("112");

		
		
		etUsername.setText("SIONITA");
		etPassword.setText("R5JNA3");
/*	
		etUsername.setText("VRATA"); //
		etPassword.setText("TG2dR7");

		etUsername.setText("luciad"); // SMR
		etPassword.setText("1LuciA5");

		etUsername.setText("TDUMITRU"); // CVR
		etPassword.setText("d6hPY9");

		
	
		etUsername.setText("IBUGACIU1"); //
		etPassword.setText("j8ZPTD");

		etUsername.setText("GMACOVEI"); //
		etPassword.setText("6Nx9br");

		etUsername.setText("CGRIGORAS1"); //
		etPassword.setText("267Jvj");
		
*/		

		etUsername.setText("LTROFIN");
		etPassword.setText("tXyv4W");
		
		globalMyIP = getIPConnection();

		checkBundleExtra();

	}

	private void checkBundleExtra() {

		if (getIntent().hasExtra("UserInfo")) {
			UtilsUser.deserializeUserInfo(getIntent().getExtras().getString("UserInfo"), getApplicationContext());
			startMainMenuActivity();
		}
	}

	private void addListenerSlider() {
		slideToUnLock.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (buttonView.isChecked()) {

				} else {
					if (etUsername.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati utilizatorul!", Toast.LENGTH_SHORT).show();
						slideToUnLock.setChecked(true);
						return;

					}
					if (etPassword.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati parola!", Toast.LENGTH_SHORT).show();
						slideToUnLock.setChecked(true);
						return;
					}
					if (etUsername.getText().toString().trim().length() > 0 && etPassword.getText().toString().trim().length() > 0) {

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);

						performLoginThread();

					}

				}

			}
		});
	}

	@SuppressWarnings("deprecation")
	private String getIPConnection() {
		String myIP = "0.0.0.0";

		try {

			ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isAvailable()) {

				WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
				WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
				int ipAddress = myWifiInfo.getIpAddress();

				myIP = android.text.format.Formatter.formatIpAddress(ipAddress);

			} else if (mobile.isAvailable()) {

				myIP = GetLocalIpAddress();
			} else {
				myIP = "0.0.0.0";
			}
		} catch (Exception ex) {
			Log.e("Error", ex.toString());
		}

		return myIP;

	}

	private String GetLocalIpAddress() {
		String retVal = "0.0.0.0";
		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						retVal = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("Error", ex.toString());
			retVal = "IP Error";
		}

		return retVal;
	}

	public static boolean isDebugMode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return true;
	}

	public void addListenerUserName() {

		etUsername.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (etUsername.getText().toString().compareTo("Utilizator") == 0) {
					etUsername.setText("");
				}

				return false;
			}
		});

	}

	public void addListenerPassword() {

		etPassword.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (etPassword.getText().toString().compareTo("Parola") == 0) {
					etPassword.setText("");
				}

				return false;
			}
		});

	}

	public void performLoginThread() {
		// start login thread
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String userN = etUsername.getText().toString().trim();
			String passN = etPassword.getText().toString().trim();

			params.put("userId", userN);
			params.put("userPass", passN);
			params.put("ipAdr", globalMyIP);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onUserLeaveHint() {
		finish();
	}

	private void startMainMenuActivity() {
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	public void validateLogin(String result) {
		if (!result.equals("-1") && result.length() > 0) {
			String[] token = result.split("#");

			if (token[0].equals("0")) {
				lblResult.setText("Cont inexistent!");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("1")) {
				lblResult.setText("Cont blocat 60 de minute.");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("2")) {
				lblResult.setText("Parola incorecta!");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("3")) {

				if (token[5].equals("8") || token[5].equals("9") || token[5].equals("10") || token[5].equals("14") || token[5].equals("12")
						|| token[5].equals("27") || token[5].equals("35") || token[5].equals("17") || token[5].equals("18") || token[5].equals("32")
						|| token[5].equals("41") || token[5].equals("43") || token[5].equals("44") || token[5].equals("45") || token[5].equals("39")
						|| token[5].equals("51") || token[5].equals("50") || token[5].equals("62")) {

					UserInfo uInfo = UserInfo.getInstance();

					String tempAgCod = token[4].toString();

					if (tempAgCod.equalsIgnoreCase("-1")) {
						lblResult.setText("Utilizator nedefinit!");
						slideToUnLock.setChecked(true);
						return;
					}

					for (int i = 0; i < 8 - token[4].length(); i++) {
						tempAgCod = "0" + tempAgCod;
					}

					// coeficienti consilieri si sm
					if (token[5].equals("17") || token[5].equals("18") || token[10].equals("KA3")) {
						String[] coeficientiCV = token[6].split("!");
						uInfo.setComisionCV(Double.valueOf(coeficientiCV[0]));
						uInfo.setCoefCorectie(coeficientiCV[1]);

					}

					discMaxSD = 0.0;
					discMaxDV = 0.0;

					uInfo.setNume(token[3]);
					uInfo.setFiliala(token[2]);
					uInfo.setCod(tempAgCod);
					uInfo.setNumeDepart(token[1]);
					uInfo.setCodDepart(UtilsGeneral.getDepart(token[1]));
					uInfo.setUnitLog(UtilsGeneral.getFiliala(token[2]));
					uInfo.setInitUnitLog(UtilsGeneral.getFiliala(token[2]));
					uInfo.setTipAcces(token[5].toString());

					if (token[10].equalsIgnoreCase("w") || token[10].equalsIgnoreCase("cvw") || token[10].equalsIgnoreCase("smw")) {
						uInfo.setTipUser("WOOD");
						uInfo.setTipUserSap("WOOD");
					} else {
						uInfo.setTipUser(InfoStrings.getTipUser(token[5]));
						uInfo.setTipUserSap(token[10]);
					}

					uInfo.setParentScreen("logon");
					uInfo.setFilialeDV(token[9]);
					uInfo.setAltaFiliala(false);
					uInfo.setUserSite(token[8]);
					uInfo.setDepartExtra(token[7]);

					uInfo.setExtraFiliale(token[11]);

					if (uInfo.getTipAcces().equals("27") || uInfo.getTipAcces().equals("35") || uInfo.getTipAcces().equals("17")
							|| uInfo.getTipAcces().equals("18")) {

						uInfo.setCodDepart("01");
						uInfo.setNumeDepart("LEMN");
					}

					uInfo.setFilHome(Boolean.valueOf(token[12]));

					uInfo.setFtpIP(token[13]);
					uInfo.setInitDivizie(token[14]);
					uInfo.setCodDepart(UtilsGeneral.getDepart(token[1]));

					if (uInfo.getTipUserSap().equals(Constants.tipSuperAv) || uInfo.getTipUserSap().equals(Constants.tipInfoAv) || UtilsUser.isSMR()
							|| UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD())
						uInfo.setCodSuperUser(uInfo.getCod());

					startMainMenuActivity();

				} else {
					lblResult.setText("Acces interzis!");
					slideToUnLock.setChecked(true);
				}
			}
			if (token[0].equals("4")) {
				lblResult.setText("Cont inactiv!");
				slideToUnLock.setChecked(true);

			}
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Autentificare esuata!", Toast.LENGTH_SHORT);
			toast.show();
			slideToUnLock.setChecked(true);
		}

	}

	public final void onTaskComplete(String methodName, Object result) {
		if (methodName.equals(METHOD_NAME)) {
			validateLogin((String) result);
		}

	}

}
