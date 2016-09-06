package lite.sfa.test;

import java.util.HashMap;

import listeners.OperatiiVenitListener;
import listeners.SelectAgentDialogListener;
import model.Agent;
import model.CalculVenit;
import model.CalculVenitImpl;
import model.UserInfo;
import adapters.AdapterVenitTCF;
import adapters.AdapterVenitTPR;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import beans.VenitAG;
import dialogs.SelectAgentDialog;
import enums.EnumOperatiiVenit;

public class RealizareTarget extends Activity implements OperatiiVenitListener, SelectAgentDialogListener {

	private ActionBar actionBar;
	private CalculVenit calculVenit;
	private ListView listTPR, listTCF;
	private TextView textAntetTcf, textAntetTpr;
	private LinearLayout layoutAntetTcf, layoutAntetTpr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.realizare_target);

		actionBar = getActionBar();
		actionBar.setTitle("Realizare target");
		actionBar.setDisplayHomeAsUpEnabled(true);

		setupLayout();

		calculVenit = new CalculVenitImpl(this);
		calculVenit.setOperatiiVenitListener(RealizareTarget.this);

		if (!isSD())
			getCalculVenit(UserInfo.getInstance().getCod());

	}

	private boolean isSD() {
		return UserInfo.getInstance().getTipUserSap().equals("SD");
	}

	private void setupLayout() {
		listTPR = (ListView) findViewById(R.id.listTPR);
		listTCF = (ListView) findViewById(R.id.listTCF);

		textAntetTcf = (TextView) findViewById(R.id.textAntetTcf);
		textAntetTpr = (TextView) findViewById(R.id.textAntetTpr);

		textAntetTcf.setVisibility(View.INVISIBLE);
		textAntetTpr.setVisibility(View.INVISIBLE);

		layoutAntetTcf = (LinearLayout) findViewById(R.id.layoutAntetTcf);
		layoutAntetTpr = (LinearLayout) findViewById(R.id.layoutAntetTpr);

		layoutAntetTcf.setVisibility(View.INVISIBLE);
		layoutAntetTpr.setVisibility(View.INVISIBLE);

	}

	private void getCalculVenit(String codAgent) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		params.put("departament", UserInfo.getInstance().getCodDepart());
		params.put("filiala", UserInfo.getInstance().getUnitLog());

		calculVenit.getVenitTPR_TCF(params);

	}

	private void afiseazaVenit(Object result) {
		VenitAG venitAg = calculVenit.getVenit(result);

		AdapterVenitTPR adapterTPR = new AdapterVenitTPR(this);
		adapterTPR.setListTPR(venitAg.getVenitTpr());

		AdapterVenitTCF adapterTCF = new AdapterVenitTCF(this);
		adapterTCF.setListTCF(venitAg.getVenitTcf());

		listTPR.setAdapter(adapterTPR);
		listTCF.setAdapter(adapterTCF);

		if (venitAg.getVenitTcf().size() > 0 || venitAg.getVenitTpr().size() > 0)
			setUIVisibility(true);
		else {
			setUIVisibility(false);
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
		}

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Agenti");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (isSD())
			CreateMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			showSelectAgentDialog();
			break;

		case android.R.id.home:
			returnToMainMenu();
			return true;

		}
		return false;
	}

	private void setUIVisibility(boolean isVisible) {
		if (isVisible) {
			textAntetTcf.setVisibility(View.VISIBLE);
			textAntetTpr.setVisibility(View.VISIBLE);

			layoutAntetTcf.setVisibility(View.VISIBLE);
			layoutAntetTpr.setVisibility(View.VISIBLE);
		} else {
			textAntetTcf.setVisibility(View.INVISIBLE);
			textAntetTpr.setVisibility(View.INVISIBLE);

			layoutAntetTcf.setVisibility(View.INVISIBLE);
			layoutAntetTpr.setVisibility(View.INVISIBLE);
		}
	}

	private void showSelectAgentDialog() {
		SelectAgentDialog selectAgent = new SelectAgentDialog(this);
		selectAgent.setAgentDialogListener(this);
		selectAgent.show();
	}

	@Override
	public void onBackPressed() {
		returnToMainMenu();
		return;
	}

	private void returnToMainMenu() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	@Override
	public void operatiiVenitComplete(EnumOperatiiVenit numeComanda, Object result) {
		afiseazaVenit(result);

	}

	@Override
	public void agentSelected(Agent agent) {
		actionBar.setTitle("Realizare target" + " - " + agent.getNume());
		getCalculVenit(agent.getCod());

	}

}
