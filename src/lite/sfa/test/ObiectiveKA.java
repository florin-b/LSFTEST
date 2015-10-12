package lite.sfa.test;

import java.util.HashMap;

import listeners.ObiectiveListener;
import listeners.SelectObiectivListener;
import model.OperatiiObiective;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import beans.BeanObiectivAfisare;
import beans.BeanObiectiveGenerale;
import dialogs.SelectObiectivDialog;
import enums.EnumMeniuObiectiv;
import enums.EnumOperatiiObiective;

public class ObiectiveKA extends FragmentActivity implements SelectObiectivListener, ObiectiveListener {

	private OperatiiObiective operatiiObiective;
	private static EnumMeniuObiectiv meniuObiectiv;
	private SelectObiectivDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.obiective_new);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Obiective");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiObiective = new OperatiiObiective(this);
		operatiiObiective.setObiectiveListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_obiective, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		dialog = new SelectObiectivDialog(this);
		dialog.setObiectivSelectedListener(this);

		FragmentTransaction tx = null;
		BeanObiectiveGenerale.getInstance().clearInstanceData();
		int itemId = item.getItemId();
		if (itemId == R.id.menuCreare) {
			meniuObiectiv = EnumMeniuObiectiv.CREARE;
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "lite.sfa.test.Obiective2"));
			tx.commit();
		} else if (itemId == R.id.menuModificare) {
			meniuObiectiv = EnumMeniuObiectiv.MODIFICARE;
			dialog.setTipOperatie(meniuObiectiv);
			dialog.show();
		} else if (itemId == R.id.menuUrmarire) {
			meniuObiectiv = EnumMeniuObiectiv.URMARIRE;
			dialog.setTipOperatie(meniuObiectiv);
			dialog.show();
		} else if (itemId == android.R.id.home) {
			returnToHome();
		}

		return false;
	}

	private void returnToHome() {
		BeanObiectiveGenerale.getInstance().clearInstanceData();
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	public void onBackPressed() {
		returnToHome();
		return;
	}

	private void getDetaliiModificare(String idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv);
		operatiiObiective.getDetaliiObiectiv(params);

	}

	private void getDetaliiUrmarire(String idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv);
		operatiiObiective.getClientiObiectiv(params);
	}

	public static EnumMeniuObiectiv getSelectedMeniuObiectiv() {
		return meniuObiectiv;
	}

	public void obiectivSelected(BeanObiectivAfisare obiectiv) {
		if (meniuObiectiv == EnumMeniuObiectiv.MODIFICARE)
			getDetaliiModificare(obiectiv.getId());

		if (meniuObiectiv == EnumMeniuObiectiv.URMARIRE) {
			BeanObiectiveGenerale.getInstance().setNumeObiectiv(obiectiv.getNume());
			BeanObiectiveGenerale.getInstance().setId(obiectiv.getId());
			getDetaliiUrmarire(obiectiv.getId());

		}

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		FragmentTransaction tx;
		switch (numeComanda) {
		case GET_DETALII_OBIECTIV:
			operatiiObiective.deserializeDetaliiObiectiv((String) result);
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "lite.sfa.test.Obiective2"));
			tx.commit();
			break;
		case GET_CLIENTI_OBIECTIV:
			BeanObiectiveGenerale.getInstance().setListConstructori(operatiiObiective.deserializeListConstructori((String) result));
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "lite.sfa.test.UrmarireObiective"));
			tx.commit();
			break;
		default:
			break;
		}

	}

}
