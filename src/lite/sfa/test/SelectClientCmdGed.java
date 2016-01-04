/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import listeners.CautaClientDialogListener;
import listeners.OperatiiClientListener;
import model.DateLivrare;
import model.InfoStrings;
import model.ListaArticoleComandaGed;
import model.OperatiiClient;
import model.UserInfo;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.CautareClientiAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import beans.BeanClient;
import beans.DetaliiClient;
import dialogs.CautaClientDialog;
import enums.EnumClienti;

public class SelectClientCmdGed extends Activity implements OperatiiClientListener, CautaClientDialogListener {

	Button cautaClientBtn, saveClntBtn;
	String filiala = "", nume = "", cod = "";
	String clientResponse = "";
	String codClient = "";
	String numeClient = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "";

	private EditText txtNumeClientGed, txtNumeClientDistrib, txtCNPClient, txtCodJ;

	RadioButton radioClDistrib, radioClPF, radioClPJ, radioCmdNormala, radioCmdSimulata, radioRezervStocDa, radioRezervStocNu;
	LinearLayout layoutRezervStocLabel, layoutRezervStocBtn, layoutDetaliiClientDistrib;
	private OperatiiClient operatiiClient;

	public SimpleAdapter adapterClienti;
	private LinearLayout layoutLabelJ, layoutTextJ;
	private LinearLayout layoutClientPersoana, layoutClientDistrib;
	private ListView listViewClienti;
	private BeanClient selectedClient;
	private TextView textNumeClientDistrib, textCodClientDistrib, textAdrClient, textLimitaCredit, textRestCredit, tipClient, clientBlocat, filialaClient;

	private RadioButton radioClMeserias;
	private NumberFormat numberFormat;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectclientcmd_ged_header);

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		operatiiClient = new OperatiiClient(this);
		operatiiClient.setOperatiiClientListener(this);

		layoutClientPersoana = (LinearLayout) findViewById(R.id.layoutClientPersoana);
		layoutClientPersoana.setVisibility(View.GONE);

		layoutClientDistrib = (LinearLayout) findViewById(R.id.layoutClientDistrib);
		layoutClientDistrib.setVisibility(View.VISIBLE);

		layoutDetaliiClientDistrib = (LinearLayout) findViewById(R.id.detaliiClientDistrib);
		layoutDetaliiClientDistrib.setVisibility(View.GONE);

		textNumeClientDistrib = (TextView) findViewById(R.id.textNumeClientDistrib);
		textCodClientDistrib = (TextView) findViewById(R.id.textCodClientDistrib);
		textAdrClient = (TextView) findViewById(R.id.textAdrClient);
		textLimitaCredit = (TextView) findViewById(R.id.textLimitaCredit);
		textRestCredit = (TextView) findViewById(R.id.textRestCredit);
		tipClient = (TextView) findViewById(R.id.tipClient);
		clientBlocat = (TextView) findViewById(R.id.clientBlocat);
		filialaClient = (TextView) findViewById(R.id.filClient);

		this.saveClntBtn = (Button) findViewById(R.id.saveClntBtn);
		addListenerSave();

		cautaClientBtn = (Button) findViewById(R.id.cautaClientBtn);
		setListenerCautaClientBtn();

		listViewClienti = (ListView) findViewById(R.id.listClienti);
		setListViewClientiListener();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Selectie client");
		actionBar.setDisplayHomeAsUpEnabled(true);

		txtNumeClientGed = (EditText) findViewById(R.id.txtNumeClient);

		txtNumeClientDistrib = (EditText) findViewById(R.id.txtNumeClientDistrib);

		txtCNPClient = (EditText) findViewById(R.id.txtCNPClient);
		txtCodJ = (EditText) findViewById(R.id.txtCodJ);

		layoutLabelJ = (LinearLayout) findViewById(R.id.layoutLabelJ);
		layoutLabelJ.setVisibility(View.GONE);
		layoutTextJ = (LinearLayout) findViewById(R.id.layoutTextJ);
		layoutTextJ.setVisibility(View.GONE);

		radioClDistrib = (RadioButton) findViewById(R.id.radioClDistrib);
		radioClPJ = (RadioButton) findViewById(R.id.radioClPJ);
		radioClPF = (RadioButton) findViewById(R.id.radioClPF);
		radioClMeserias = (RadioButton) findViewById(R.id.radioClMeserias);

		setVisibilityRadioClMeserias(radioClMeserias);

		addListenerRadioClDistrib();
		addListenerRadioCLPF();
		addListenerRadioCLPJ();
		addListenerRadioMeseriasi();

		radioClDistrib.setChecked(false);
		radioClDistrib.setVisibility(View.GONE);
		radioClPF.setChecked(true);

		if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {
			radioClDistrib.setChecked(true);
			radioClDistrib.setVisibility(View.VISIBLE);
		}

		radioCmdNormala = (RadioButton) findViewById(R.id.radioCmdNormala);
		addListenerRadioCmdNormala();

		radioCmdSimulata = (RadioButton) findViewById(R.id.radioCmdSimulata);
		addListenerRadioCmdSimulata();

		radioRezervStocDa = (RadioButton) findViewById(R.id.radioRezervStocDa);

		radioRezervStocNu = (RadioButton) findViewById(R.id.radioRezervStocNu);

		layoutRezervStocLabel = (LinearLayout) findViewById(R.id.layoutRezervStocLabel);
		layoutRezervStocLabel.setVisibility(View.INVISIBLE);

		layoutRezervStocBtn = (LinearLayout) findViewById(R.id.layoutRezervStocBtn);
		layoutRezervStocBtn.setVisibility(View.INVISIBLE);

	}

	private void setListenerCautaClientBtn() {
		cautaClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isNumeClientValid())
					getListaClienti();

			}
		});
	}

	private boolean isNumeClientValid() {
		if (txtNumeClientDistrib.getText().toString().trim().length() > 0) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "Introduceti nume client!", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private void setVisibilityRadioClMeserias(RadioButton radioClMeserias) {
		if (UserInfo.getInstance().getTipUserSap().contains("CAG"))
			radioClMeserias.setVisibility(View.VISIBLE);
		else
			radioClMeserias.setVisibility(View.INVISIBLE);

	}

	private void getListaClienti() {
		String numeClient = txtNumeClientDistrib.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("depart", "00");
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		operatiiClient.getListClienti(params);

		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private void addListenerRadioClDistrib() {
		radioClDistrib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					layoutClientPersoana.setVisibility(View.GONE);
					layoutClientDistrib.setVisibility(View.VISIBLE);

					clearDateLivrare();

				} else {
					layoutClientPersoana.setVisibility(View.VISIBLE);
					layoutClientDistrib.setVisibility(View.GONE);
				}

			}
		});

	}

	private void addListenerRadioCLPJ() {
		radioClPJ.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					layoutLabelJ.setVisibility(View.VISIBLE);
					layoutTextJ.setVisibility(View.VISIBLE);
					setTextNumeClientEnabled(true);
					clearDateLivrare();
				}

			}
		});
	}

	private void addListenerRadioCLPF() {

		radioClPF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					layoutLabelJ.setVisibility(View.GONE);
					layoutTextJ.setVisibility(View.GONE);

					setTextNumeClientEnabled(true);
					clearDateLivrare();
				}

			}
		});

	}

	private void addListenerRadioMeseriasi() {

		radioClMeserias.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				layoutLabelJ.setVisibility(View.GONE);
				layoutTextJ.setVisibility(View.GONE);

				setTextNumeClientEnabled(false);

				CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
				clientDialog.setMeserias(true);
				clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
				clientDialog.show();

				clearDateLivrare();

			}
		});

	}

	private void setTextNumeClientEnabled(boolean isEnabled) {

		txtNumeClientGed.setText("");
		txtCNPClient.setText("");

		if (!isEnabled) {
			txtNumeClientGed.setFocusable(false);
			txtCNPClient.setFocusable(false);
		} else {
			txtNumeClientGed.setFocusableInTouchMode(true);
			txtCNPClient.setFocusableInTouchMode(true);
			txtNumeClientGed.requestFocus();
		}

	}

	private void clearDateLivrare() {
		if (ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() == 0)
			DateLivrare.getInstance().resetAll();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void addListenerRadioCmdNormala() {
		radioCmdNormala.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					CreareComandaGed.tipComanda = "N";
					layoutRezervStocLabel.setVisibility(View.INVISIBLE);
					layoutRezervStocBtn.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	public void addListenerRadioCmdSimulata() {
		radioCmdSimulata.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					CreareComandaGed.tipComanda = "S";
					layoutRezervStocLabel.setVisibility(View.VISIBLE);
					layoutRezervStocBtn.setVisibility(View.VISIBLE);
					radioRezervStocDa.setChecked(true);
				}

			}
		});
	}

	public void addListenerSave() {
		saveClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!radioClDistrib.isChecked()) {
					if (txtNumeClientGed.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati numele clientului!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (txtCNPClient.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati CNP / CUI client!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (radioClPF.isChecked()) {
						CreareComandaGed.tipClient = "PF";
						DateLivrare.getInstance().setTipPersClient("PF");

						if (UtilsUser.isConsWood())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PF");
						else
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
					}

					if (radioClPJ.isChecked()) {
						CreareComandaGed.tipClient = "PJ";
						DateLivrare.getInstance().setTipPersClient("PJ");

						if (UtilsUser.isConsWood())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PJ");
						else
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PJ");
					}

					if (radioCmdNormala.isChecked())
						CreareComandaGed.tipComanda = "N";

					if (radioCmdSimulata.isChecked())
						CreareComandaGed.tipComanda = "S";

					if (radioRezervStocDa.isChecked())
						CreareComandaGed.rezervStoc = true;

					if (radioRezervStocNu.isChecked())
						CreareComandaGed.rezervStoc = false;

					CreareComandaGed.numeClientVar = txtNumeClientGed.getText().toString().trim();
					CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

					if (layoutTextJ.getVisibility() == View.VISIBLE)
						CreareComandaGed.codJ = txtCodJ.getText().toString().trim();
				}

				if (radioClDistrib.isChecked()) {

					if (selectedClient == null) {
						Toast.makeText(getApplicationContext(), "Completati numele clientului!", Toast.LENGTH_SHORT).show();
						return;
					}

					CreareComandaGed.tipComanda = "N";
					CreareComandaGed.tipClient = "D";
					DateLivrare.getInstance().setTipPersClient("D");
					CreareComandaGed.numeClientVar = selectedClient.getNumeClient();
					CreareComandaGed.codClientVar = selectedClient.getCodClient();
					CreareComandaGed.cnpClient = " ";
					CreareComandaGed.codJ = " ";
					CreareComandaGed.rezervStoc = false;

				}

				finish();

			}
		});

	}

	private void setListViewClientiListener() {
		listViewClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedClient = (BeanClient) arg0.getAdapter().getItem(arg2);
				getDetaliiClient();

			}
		});
	}

	private void getDetaliiClient() {
		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codClient", selectedClient.getCodClient());
		params.put("depart", "00");
		operatiiClient.getDetaliiClient(params);
	}

	private void populateListViewClient(List<BeanClient> listClienti) {
		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(this, listClienti);
		listViewClienti.setAdapter(adapterClienti);
	}

	private void listClientDetails(DetaliiClient detaliiClient) {

		layoutDetaliiClientDistrib.setVisibility(View.VISIBLE);

		textNumeClientDistrib.setText(selectedClient.getNumeClient());
		textCodClientDistrib.setText(selectedClient.getCodClient());

		textAdrClient.setText(detaliiClient.getOras() + " " + detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());

		textLimitaCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getLimitaCredit())));
		textRestCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getRestCredit())));
		tipClient.setText(detaliiClient.getTipClient());
		DateLivrare.getInstance().setTermenPlata(detaliiClient.getTermenPlata());

		filialaClient.setText(detaliiClient.getFiliala());

		if (detaliiClient.getStare().equals("X")) {
			clientBlocat.setVisibility(View.VISIBLE);
			clientBlocat.setText("Blocat : " + detaliiClient.getMotivBlocare());
			saveClntBtn.setVisibility(View.INVISIBLE);
		} else {
			clientBlocat.setText("");
			tipClient.setText(detaliiClient.getTipClient());
			saveClntBtn.setVisibility(View.VISIBLE);
		}

	}

	public void onBackPressed() {
		finish();
		return;
	}

	public void operationComplete(EnumClienti methodName, Object result) {
		switch (methodName) {
		case GET_LISTA_CLIENTI:
			populateListViewClient(operatiiClient.deserializeListClienti((String) result));
			break;
		case GET_DETALII_CLIENT:
			listClientDetails(operatiiClient.deserializeDetaliiClient((String) result));
			break;
		default:
			break;
		}

	}

	public void clientSelected(BeanClient client) {
		txtNumeClientGed.setText(client.getNumeClient());
		txtCNPClient.setText(client.getCodClient());
		CreareComandaGed.codClientVar = client.getCodClient();
		CreareComandaGed.tipClient = client.getTipClient();
	}
}