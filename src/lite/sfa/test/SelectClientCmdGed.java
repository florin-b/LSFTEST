/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import listeners.CautaClientDialogListener;
import listeners.DatePersListener;
import listeners.OperatiiClientListener;
import model.DateLivrare;
import model.InfoStrings;
import model.ListaArticoleComandaGed;
import model.OperatiiClient;
import model.UserInfo;
import utils.UtilsCheck;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.CautareClientiAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import beans.BeanDatePersonale;
import beans.DetaliiClient;
import beans.PlatitorTva;
import dialogs.CautaClientDialog;
import dialogs.DatePersClientDialog;
import enums.EnumClienti;

public class SelectClientCmdGed extends Activity implements OperatiiClientListener, CautaClientDialogListener, DatePersListener {

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
	private TextView textNumeClientDistrib, textCodClientDistrib, textAdrClient, textLimitaCredit, textRestCredit, textTipClient, clientBlocat, filialaClient;

	private RadioButton radioClMeserias;
	private NumberFormat numberFormat;
	private CheckBox checkPlatTva, checkFacturaPF;
	private Button clientBtn, verificaID, verificaTva;
	private TextView textClientParavan, labelIDClient;

	private Button cautaClientPFBtn;

	private enum EnumTipClient {
		MESERIAS, PARAVAN;
	}

	private EnumTipClient tipClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectclientcmd_ged_header);

		tipClient = EnumTipClient.MESERIAS;

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		operatiiClient = new OperatiiClient(this);
		operatiiClient.setOperatiiClientListener(this);

		checkFacturaPF = (CheckBox) findViewById(R.id.checkFacturaPF);
		setListenerFacturaPF();

		checkPlatTva = (CheckBox) findViewById(R.id.checkPlatTva);
		checkPlatTva.setVisibility(View.INVISIBLE);

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
		textTipClient = (TextView) findViewById(R.id.tipClient);
		clientBlocat = (TextView) findViewById(R.id.clientBlocat);
		filialaClient = (TextView) findViewById(R.id.filClient);

		this.saveClntBtn = (Button) findViewById(R.id.saveClntBtn);
		addListenerSave();

		cautaClientBtn = (Button) findViewById(R.id.cautaClientBtn);
		setListenerCautaClientBtn();

		cautaClientPFBtn = (Button) findViewById(R.id.cautaClientPFBtn);
		setListenerCautaClientPFBtn();

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

		labelIDClient = (TextView) findViewById(R.id.labelIdClient);
		labelIDClient.setText("CNP");

		verificaID = (Button) findViewById(R.id.verificaId);
		setListenerVerificaID();

		verificaTva = (Button) findViewById(R.id.verificaTva);
		verificaTva.setVisibility(View.GONE);
		setListenerVerificaTva();

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

		textClientParavan = (TextView) findViewById(R.id.textClientParavan);

		clientBtn = (Button) findViewById(R.id.clientBtn);
		addListenerClientBtn();

	}

	private void setListenerVerificaTva() {
		verificaTva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!txtCNPClient.getText().toString().isEmpty()) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("cuiClient", txtCNPClient.getText().toString().trim());
					operatiiClient.getStarePlatitorTva(params);
				}

			}
		});
	}

	private void setListenerFacturaPF() {
		checkFacturaPF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkFacturaPF.isChecked())
					checkFacturaPF.setText("Se emite factura");
				else
					checkFacturaPF.setText("Nu se emite factura");

			}
		});
	}

	private void setListenerVerificaID() {

		verificaID.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkCNP(true);

			}
		});

	}

	private boolean checkCNP(boolean showValidMessage) {

		if (UtilsCheck.isCnpValid(txtCNPClient.getText().toString().trim())) {

			if (showValidMessage)
				Toast.makeText(getApplicationContext(), "CNP valid", Toast.LENGTH_SHORT).show();

			return true;
		} else {

			if (showValidMessage)
				Toast.makeText(getApplicationContext(), "CNP invalid", Toast.LENGTH_SHORT).show();
			return false;
		}

	}

	private void setListenerCautaClientBtn() {
		cautaClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isNumeClientValid())
					getListaClienti();

			}
		});
	}

	private void setListenerCautaClientPFBtn() {
		cautaClientPFBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String textClient = txtNumeClientGed.getText().toString().trim();

				if (!textClient.isEmpty()) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("numeClient", textClient);
					params.put("tipClient", getTipClient());
					operatiiClient.getCnpClient(params);
				}

			}
		});
	}

	private String getTipClient() {
		String tipClient = " ";
		if (radioClPF.isChecked())
			tipClient = "PF";
		else if (radioClPJ.isChecked())
			tipClient = "PJ";

		return tipClient;
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

	private void addListenerClientBtn() {
		clientBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				tipClient = EnumTipClient.PARAVAN;
				CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
				clientDialog.setMeserias(false);
				clientDialog.setClientObiectivKA(false);
				clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
				clientDialog.show();
			}
		});
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

	private void updateStareTva(PlatitorTva platitorTva) {

		String stare = "";

		if (platitorTva.isPlatitor()) {
			checkPlatTva.setChecked(true);
		} else {
			checkPlatTva.setChecked(false);
			stare = " nu";
		}

		String message = "";

		if (platitorTva.getErrMessage().length() > 0) {
			message = platitorTva.getErrMessage();
			txtNumeClientGed.setText("");
			txtCodJ.setText("");
		} else {
			message = platitorTva.getNumeClient() + stare + " este platitor de tva.";
			txtNumeClientGed.setText(platitorTva.getNumeClient());
			txtCodJ.setText(platitorTva.getNrInreg());

		}

		Toast.makeText(this, message, Toast.LENGTH_LONG).show();

	}

	private void addListenerRadioClDistrib() {
		radioClDistrib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					layoutClientPersoana.setVisibility(View.GONE);
					layoutClientDistrib.setVisibility(View.VISIBLE);
					verificaID.setVisibility(View.GONE);
					verificaTva.setVisibility(View.GONE);
					labelIDClient.setText("CUI");
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
					checkPlatTva.setChecked(true);
					checkPlatTva.setVisibility(View.VISIBLE);
					verificaID.setVisibility(View.GONE);
					checkFacturaPF.setVisibility(View.GONE);
					verificaTva.setVisibility(View.VISIBLE);
					labelIDClient.setText("CUI");
					txtCodJ.setText("");
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
					checkPlatTva.setVisibility(View.INVISIBLE);
					verificaID.setVisibility(View.VISIBLE);
					verificaTva.setVisibility(View.GONE);
					checkFacturaPF.setVisibility(View.VISIBLE);
					labelIDClient.setText("CNP");
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
				checkPlatTva.setVisibility(View.INVISIBLE);

				verificaID.setVisibility(View.GONE);
				checkFacturaPF.setVisibility(View.GONE);
				labelIDClient.setText("COD");

				setTextNumeClientEnabled(false);

				tipClient = EnumTipClient.MESERIAS;
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

					if (!radioClPF.isChecked() && txtCNPClient.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati CUI client!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (radioClPF.isChecked() && checkFacturaPF.isChecked() && !checkCNP(false) && !UtilsUser.isUserSite()) {
						Toast.makeText(getApplicationContext(), "Pentru emiterea facturii cnp-ul este obligatoriu.", Toast.LENGTH_SHORT).show();
						return;
					}

					if (radioClPF.isChecked() && UtilsUser.isUserSite() && hasCnp() && !checkCNP(false)) {
						Toast.makeText(getApplicationContext(), "CNP invalid", Toast.LENGTH_SHORT).show();
						return;
					}

					if (radioClPF.isChecked()) {
						CreareComandaGed.tipClient = "PF";
						DateLivrare.getInstance().setTipPersClient("PF");

						if (!checkFacturaPF.isChecked()) {

							if (UtilsUser.isConsWood()) {
								CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood_faraFact(UserInfo.getInstance().getUnitLog(), "PF");
							} else {
								if (UtilsUser.isUserExceptieCONSGED())
									CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED_faraFactura(UserInfo.getInstance().getUnitLog(),
											"PF");
								else
									CreareComandaGed.codClientVar = InfoStrings.getClientGed_FaraFactura(UserInfo.getInstance().getUnitLog());

							}

							DateLivrare.getInstance().setFacturaCmd(false);

						} else {

							if (UtilsUser.isUserSite()) {

								if (hasCnp())
									CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
								else
									CreareComandaGed.codClientVar = InfoStrings.getClientCVO_cuFact_faraCnp(UserInfo.getInstance().getUnitLog(), "PF");

							}

							else {
								if (UtilsUser.isConsWood())
									CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PF");
								else {
									if (UtilsUser.isUserExceptieCONSGED())
										CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PF");
									else
										CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
								}
							}

							DateLivrare.getInstance().setFacturaCmd(true);

						}

						CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

					}

					if (radioClPJ.isChecked()) {
						CreareComandaGed.tipClient = "PJ";
						DateLivrare.getInstance().setTipPersClient("PJ");

						CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

						if (UtilsUser.isConsWood())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PJ");
						else {
							if (checkPlatTva.isChecked()) {

								CreareComandaGed.cnpClient = "RO" + txtCNPClient.getText().toString().trim();

								if (UtilsUser.isUserExceptieCONSGED())
									CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PJ");
								else
									CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PJ");
							} else {
								if (UtilsUser.isUserExceptieCONSGED())
									CreareComandaGed.codClientVar = InfoStrings.gedPJNeplatitorTVA_CONSGED(UserInfo.getInstance().getUnitLog());
								else
									CreareComandaGed.codClientVar = InfoStrings.gedPJNeplatitorTVA(UserInfo.getInstance().getUnitLog());
							}
						}
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

	private boolean hasCnp() {
		return !txtCNPClient.getText().toString().isEmpty();
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

	private void afisDatePersSelectDialog(String strDatePersonale) {
		List<BeanDatePersonale> listDatePers = operatiiClient.deserializeDatePersonale(strDatePersonale);

		if (listDatePers.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
		} else {
			DatePersClientDialog datePersDialog = new DatePersClientDialog(this, listDatePers);
			datePersDialog.setDatePersListener(this);
			datePersDialog.show();
		}

	}

	private void listClientDetails(DetaliiClient detaliiClient) {

		layoutDetaliiClientDistrib.setVisibility(View.VISIBLE);

		textNumeClientDistrib.setText(selectedClient.getNumeClient());
		textCodClientDistrib.setText(selectedClient.getCodClient());

		textAdrClient.setText(detaliiClient.getOras() + " " + detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());

		textLimitaCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getLimitaCredit())));
		textRestCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getRestCredit())));
		textTipClient.setText(detaliiClient.getTipClient());
		DateLivrare.getInstance().setTermenPlata(detaliiClient.getTermenPlata());

		filialaClient.setText(detaliiClient.getFiliala());

		if (detaliiClient.getStare().equals("X")) {
			clientBlocat.setVisibility(View.VISIBLE);
			clientBlocat.setText("Blocat : " + detaliiClient.getMotivBlocare());
			saveClntBtn.setVisibility(View.INVISIBLE);
		} else {
			clientBlocat.setText("");
			textTipClient.setText(detaliiClient.getTipClient());
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
		case GET_STARE_TVA:
			updateStareTva(operatiiClient.deserializePlatitorTva((String) result));
			break;
		case GET_CNP_CLIENT:
			afisDatePersSelectDialog((String) result);
			break;
		default:
			break;
		}

	}

	public void clientSelected(BeanClient client) {
		if (tipClient == EnumTipClient.MESERIAS) {
			txtNumeClientGed.setText(client.getNumeClient());
			txtCNPClient.setText(client.getCodClient());
			CreareComandaGed.codClientVar = client.getCodClient();
			CreareComandaGed.tipClient = client.getTipClient();
		} else {
			CreareComandaGed.codClientParavan = client.getCodClient();
			CreareComandaGed.numeClientParavan = client.getNumeClient();
			textClientParavan.setText(CreareComandaGed.numeClientParavan);
		}
	}

	private void populateDatePersonale(BeanDatePersonale datePersonale) {

		txtNumeClientGed.setText(datePersonale.getNume());
		txtCNPClient.setText(datePersonale.getCnp());

		DateLivrare.getInstance().setCodJudet(datePersonale.getCodjudet());
		DateLivrare.getInstance().setOras(datePersonale.getLocalitate());
		DateLivrare.getInstance().setStrada(datePersonale.getStrada());

	}

	@Override
	public void datePersSelected(Object result) {
		populateDatePersonale((BeanDatePersonale) result);

	}
}