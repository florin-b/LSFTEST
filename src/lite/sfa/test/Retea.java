/**
 * @author florinb
 *
 */
package lite.sfa.test;




import model.UserInfo;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Retea extends Activity {

	Button quitBtn;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private TextView conType, params, imeiNr, telNr;
	String tipAcces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.retea);

		conType = (TextView) findViewById(R.id.conType);
		params = (TextView) findViewById(R.id.params);
		imeiNr = (TextView) findViewById(R.id.imeiNr);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Retea");
		actionBar.setDisplayHomeAsUpEnabled(true);

		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = conMan
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = conMan
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			try {

				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();

				conType.setText("Wi-fi");
				params.setText(wifiInfo.toString());

			} catch (Exception ex) {
				Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			}

		} else if (mobile.isAvailable()) {
			TelephonyManager tempManager;
			tempManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

			String operatorname = tempManager.getNetworkOperatorName();
			String phonenumber = tempManager.getLine1Number();

			conType.setText("Mobile 3G");
			params.setText(operatorname + " " + phonenumber);

		} else {
			conType.setText("Fara retea");
			params.setText("");
		}

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		imeiNr.setText(telephonyManager.getDeviceId().toString());

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

	

	

}