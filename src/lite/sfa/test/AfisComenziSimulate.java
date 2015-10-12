/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import listeners.AsyncTaskListener;
import listeners.ComenziDAOListener;
import listeners.CustomSpinnerClass;
import listeners.CustomSpinnerListener;
import model.ComenziDAO;
import model.ConnectionStrings;
import model.InfoStrings;
import model.UserInfo;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.BeanComandaCreata;
import enums.EnumComenziDAO;

public class AfisComenziSimulate extends Activity implements AsyncTaskListener, CustomSpinnerListener,
		ComenziDAOListener {

	Button creazaCmdSimBtn, stergeCmdSimBtn;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private SimpleAdapter adapter;

	private Spinner spinnerCmd;
	private SimpleAdapter adapterComenzi, adapterArtCond;
	private static ArrayList<HashMap<String, String>> listComenzi = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "", selectedCmdSAP = "";

	private String selectedClientCode = "-1";
	private String cmdNr = null, tipSelCmd = "";
	private TextView textTipPlata, textAdrLivr, textTotalCmd, labelTotal, textOrasModif, textJudetModif;
	private TextView textPersContact, textTelefon, textCantar, textTransport, labelCantar, labelFactRed, textFactRed;

	public static String codClientVar = "";
	public static String numeClientVar = "";
	public static String articoleComanda = "", numeArtSelContextMenu = "", codArtSelContextMenu = "";
	public static double totalComanda = 0, stocArtCond = 0;
	public static String dateLivrare = "", tipOpCmd = "-1";

	private boolean touched = false;
	private Dialog dialogSelClient;
	public static String tipAcces;
	ListView listArtModif, listViewArtCond;

	private String selectedClient = "";
	private Integer selectedClientIndex = -1;

	private String selectedAgent = "-1", selectedFiliala = "-1";

	private boolean totiClientiiRadioBtnSelected = true;

	private HashMap<String, String> artMap = null;

	private CustomSpinnerClass spinnerListener = new CustomSpinnerClass();
	ComenziDAO comenzi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afiscomenzisimulate);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Comenzi simulate");
		actionBar.setDisplayHomeAsUpEnabled(true);

		comenzi = ComenziDAO.getInstance(this);
		comenzi.setComenziDAOListener(this);

		checkStaticVars();

		spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);

		adapterComenzi = new SimpleAdapter(this, listComenzi, R.layout.comsimulatecustomview, new String[] { "idCmd",
				"codClient", "numeClient", "data", "suma", "stare", "tipCmd", "ul", "cmdSap" }, new int[] {
				R.id.textIdCmd, R.id.textCodClient, R.id.textClient, R.id.textData, R.id.textSuma, R.id.textStare,
				R.id.textTipCmd, R.id.textUL, R.id.textCmdSAP });

		spinnerCmd.setOnItemSelectedListener(spinnerListener);
		spinnerListener.setListener(this);

		spinnerCmd.setVisibility(View.INVISIBLE);
		spinnerCmd.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				touched = true;
				return false;
			}

		});

		spinnerCmd.setAdapter(adapterComenzi);

		listArtModif = (ListView) findViewById(R.id.listArtModif);

		this.creazaCmdSimBtn = (Button) findViewById(R.id.creazaCmdSimBtn);
		this.creazaCmdSimBtn.setVisibility(View.INVISIBLE);
		addListenerCreazaCmdSimBtn();

		this.stergeCmdSimBtn = (Button) findViewById(R.id.stergeCmdSimBtn);
		this.stergeCmdSimBtn.setVisibility(View.INVISIBLE);
		addListenerStergeCmdSimBtn();

		adapter = new SimpleAdapter(this, list1, R.layout.comsimulatecustomrowview, new String[] { "nrCrt", "numeArt",
				"codArt", "cantArt", "umArt", "pretArt", "monedaArt", "depozit", "status", "procent", "procFact",
				"zDis", "tipAlert", "procAprob" }, new int[] { R.id.textNrCrt, R.id.textNumeArt, R.id.textCodArt,
				R.id.textCantArt, R.id.textUmArt, R.id.textPretArt, R.id.textMonedaArt, R.id.textDepozit,
				R.id.textStatusArt, R.id.textProcRed, R.id.textProcFact, R.id.textZDIS, R.id.textAlertUsr,
				R.id.textProcAprobModif });

		listArtModif.setAdapter(adapter);
		listArtModif.setVisibility(View.INVISIBLE);

		addListenerListArtModif();
		registerForContextMenu(listArtModif);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textCantar = (TextView) findViewById(R.id.textCantar);
		textTransport = (TextView) findViewById(R.id.textTransport);

		textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);
		labelTotal = (TextView) findViewById(R.id.labelTotal);
		labelCantar = (TextView) findViewById(R.id.labelCantar);
		textFactRed = (TextView) findViewById(R.id.textFactRed);
		labelFactRed = (TextView) findViewById(R.id.labelFactRed);

		textOrasModif = (TextView) findViewById(R.id.textOrasModif);
		textJudetModif = (TextView) findViewById(R.id.textJudetModif);

		labelTotal.setVisibility(View.GONE);
		textTotalCmd.setVisibility(View.GONE);

		labelCantar.setVisibility(View.GONE);
		textFactRed.setVisibility(View.GONE);
		labelFactRed.setVisibility(View.GONE);

		textCantar.setText("");
		textFactRed.setText("");

		try {

			performGetComenzi();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		checkStaticVars();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {

		MenuItem mnu1 = menu.add(0, 0, 0, "Clienti");

		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:

			if (0 == listComenzi.size()) {
				return false;
			}

			dialogSelClient = new Dialog(AfisComenziSimulate.this);
			dialogSelClient.setContentView(R.layout.selclientdialogafiscmd);
			dialogSelClient.setTitle("Selectie client");

			final Spinner spinnerClienti = (Spinner) dialogSelClient.findViewById(R.id.spinnerSelClient);
			spinnerClienti.setVisibility(View.INVISIBLE);

			final ArrayList<HashMap<String, String>> listClienti = new ArrayList<HashMap<String, String>>();
			final SimpleAdapter adapterClienti = new SimpleAdapter(this, listClienti, R.layout.rowlayoutagenti,
					new String[] { "numeClient", "codClient" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });

			final RadioGroup radioGroupClnt = (RadioGroup) dialogSelClient.findViewById(R.id.radioGroup1);

			radioGroupClnt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

				public void onCheckedChanged(RadioGroup group, int checkedId) {

					RadioButton checkedRadioButton = (RadioButton) radioGroupClnt.findViewById(checkedId);

					String selectedRadioBtn = checkedRadioButton.getText().toString();

					if (selectedRadioBtn.equals("Toti clientii")) {

						totiClientiiRadioBtnSelected = true;
						selectedClient = "";
						selectedClientIndex = -1;
						listClienti.clear();
						spinnerClienti.setVisibility(View.INVISIBLE);

					}
					if (selectedRadioBtn.equals("Alege un client")) {

						totiClientiiRadioBtnSelected = false;
						if (0 == listClienti.size()) {
							getListClienti();
						}

						spinnerClienti.setVisibility(View.VISIBLE);

					}

				}

				// lista clienti pentru selectie comenzi
				public void getListClienti() {

					try {
						GetListClienti clienti = new GetListClienti(dialogSelClient.getContext());
						clienti.execute("dummy");
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				final class GetListClienti extends AsyncTask<String, Void, String> {
					String errMessage = "";
					Context mContext;
					private ProgressDialog dialog;

					private GetListClienti(Context context) {
						super();
						this.mContext = context;
					}

					protected void onPreExecute() {
						this.dialog = new ProgressDialog(mContext);
						this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						this.dialog.setMessage("Incarcare lista clienti...");
						this.dialog.setCancelable(false);
						this.dialog.show();
					}

					@Override
					protected String doInBackground(String... url) {
						String response = "";
						try {

							SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(),
									"getListClientiGED");

							NumberFormat nf3 = new DecimalFormat("00000000");
							String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

							String unitLogParam = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2"
									+ UserInfo.getInstance().getUnitLog().substring(3, 4);

							String tipAgent = " ";

							if (UserInfo.getInstance().getTipAcces().equals("17"))
								tipAgent = "CV";

							request.addProperty("codAgent", fullCode);
							request.addProperty("tipAgent", tipAgent);
							request.addProperty("filiala", unitLogParam);

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
							envelope.dotNet = true;
							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance()
									.getUrl(), 60000);
							List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
							headerList.add(new HeaderProperty("Authorization", "Basic "
									+ org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
							androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace()
									+ "getListClientiGED", envelope, headerList);
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
							if (null != dialog) {
								dialog.dismiss();
								dialog = null;
							}

							if (errMessage.length() > 0) {
								Toast toast = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT);
								toast.show();
							} else {
								populateClientiList(result);
							}
						} catch (Exception e) {
							Log.e("Error", e.toString());
						}
					}

				}

				void populateClientiList(String clientiList) {

					if (!clientiList.equals("-1") && clientiList.length() > 0) {

						listClienti.clear();

						HashMap<String, String> temp;
						String[] tokenLinie = clientiList.split("@@");
						String[] tokenClient;
						String client = "";

						for (int i = 0; i < tokenLinie.length; i++) {
							temp = new HashMap<String, String>(100, 0.75f);
							client = tokenLinie[i];
							tokenClient = client.split("#");

							temp.put("numeClient", tokenClient[1]);
							temp.put("codClient", tokenClient[0]);

							listClienti.add(temp);
						}

						spinnerClienti.setAdapter(adapterClienti);

						if (selectedClientIndex != -1)
							spinnerClienti.setSelection(selectedClientIndex);

					} else {
						Toast.makeText(getApplicationContext(), "Nu exista clienti!", Toast.LENGTH_SHORT).show();
					}

				}

			});

			RadioButton rb1 = (RadioButton) dialogSelClient.findViewById(R.id.radio1);
			RadioButton rb2 = (RadioButton) dialogSelClient.findViewById(R.id.radio2);

			if (totiClientiiRadioBtnSelected) {
				rb1.toggle();
			} else {
				rb2.toggle();
			}

			Button btnOkClnt = (Button) dialogSelClient.findViewById(R.id.btnOkClnt);
			btnOkClnt.setOnClickListener(new View.OnClickListener() {

				@SuppressWarnings("unchecked")
				public void onClick(View v) {

					if (listClienti.size() > 0) {
						artMap = (HashMap<String, String>) spinnerClienti.getSelectedItem();

						selectedClientIndex = spinnerClienti.getSelectedItemPosition();
						selectedClient = artMap.get("codClient");
					}

					performGetComenzi();
					dialogSelClient.dismiss();

				}
			});

			dialogSelClient.show();

			return true;

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			list1.clear();
			listComenzi.clear();

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	public void addListenerCreazaCmdSimBtn() {
		creazaCmdSimBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (tipSelCmd.equals("21")) {
					Toast.makeText(getApplicationContext(), "Doar comenzile cu rezervare de stoc", Toast.LENGTH_LONG)
							.show();
				}
				if (tipSelCmd.equals("20")) // transformare in comanda ferma
				{
					showCreateCmdConfirmationAlert();
				}

			}
		});

	}

	public void showCreateCmdConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Creati comanda?").setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						tipOpCmd = "9";
						opereazaComanda();

					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void opereazaComanda() {

		try {
			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

			params.put("nrCmd", selectedCmd);
			params.put("nrCmdSAP", selectedCmdSAP);
			params.put("tipOp", tipOpCmd);
			params.put("codUser", fullCode);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "operatiiComenzi", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void showDeleteConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Stergeti comanda?").setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						tipOpCmd = "3";
						opereazaComanda();

					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void addListenerStergeCmdSimBtn() {
		stergeCmdSimBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				showDeleteConfirmationAlert();

			}
		});

	}

	private void refreshCommandList() {

		try {

			performGetComenzi();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("unchecked")
	private void getArtSelCmd() {

		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");
		labelTotal.setVisibility(View.INVISIBLE);
		textTotalCmd.setVisibility(View.INVISIBLE);
		textTotalCmd.setText("0.00");

		// detalii comanda
		try {

			artMap = (HashMap<String, String>) spinnerCmd.getSelectedItem();

			cmdNr = artMap.get("idCmd").substring(0, artMap.get("idCmd").length() - 1);
			selectedCmdSAP = artMap.get("cmdSAP");

			tipSelCmd = artMap.get("tipCmd");

			selectedCmd = cmdNr;

			totalComanda = Double.parseDouble(artMap.get("suma").replace(",", ""));

			textTotalCmd.setText(String.format("%.02f", totalComanda));

			selectedClientCode = artMap.get("codClient");

			codClientVar = selectedClientCode;
			numeClientVar = artMap.get("numeClient");

			performArtCmd();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		textTipPlata.setVisibility(View.VISIBLE);
		textAdrLivr.setVisibility(View.VISIBLE);
		textPersContact.setVisibility(View.VISIBLE);
		textTelefon.setVisibility(View.VISIBLE);
		textCantar.setVisibility(View.VISIBLE);
		textTransport.setVisibility(View.VISIBLE);

		touched = false;

	}

	public void performGetComenzi() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "CV";

			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("9"))
				tipUser = "CV";

			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("codUser", UserInfo.getInstance().getCod());
			params.put("tipCmd", "4"); // comenzi simulate
			params.put("depart", "11");
			params.put("tipUser", tipUser);
			params.put("codClient", selectedClient);

			comenzi.getListComenzi(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void performArtCmd() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("nrCmd", selectedCmd);
			params.put("afisCond", "1");

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getCmdArt", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateArtCmdList(String articoleComanda) {

		list1.clear();
		adapter.notifyDataSetChanged();

		if (!articoleComanda.equals("-1") && articoleComanda.length() > 0) {
			HashMap<String, String> temp;
			String[] tokenMain = articoleComanda.split("@@");
			String[] tokenAntet = tokenMain[0].split("#");
			String tipPlata = "", tipTransport = "", cantar = "", factRed = "", tipAlert = "";

			listArtModif.setVisibility(View.VISIBLE);

			// tip plata
			if (tokenAntet[4].equals("B")) {
				tipPlata = "Bilet la ordin";
			}
			if (tokenAntet[4].equals("C")) {
				tipPlata = "Cec";
			}
			if (tokenAntet[4].equals("E")) {
				tipPlata = "Plata in numerar";
			}
			if (tokenAntet[4].equals("L")) {
				tipPlata = "Plata interna buget-trezorerie";
			}
			if (tokenAntet[4].equals("O")) {
				tipPlata = "Ordin de plata";
			}
			if (tokenAntet[4].equals("U")) {
				tipPlata = "Plata interna-alte institutii";
			}
			if (tokenAntet[4].equals("W")) {
				tipPlata = "Plata in strainatate-banci";
			}
			if (!tipPlata.equals(""))
				textTipPlata.setText(tipPlata);

			if (tokenAntet[3].equals("TCLI")) {
				tipTransport = "Transport client";
			}
			if (tokenAntet[3].equals("TRAP")) {
				tipTransport = "Transport propriu";
			}
			if (tokenAntet[3].equals("TERT")) {
				tipTransport = "Transport terti";
			}

			if (!tipTransport.equals(""))
				textTransport.setText(tipTransport);

			if (tokenAntet[5].equals("1")) {
				cantar = "DA";
			}
			if (tokenAntet[5].equals("0")) {
				cantar = "NU";
			}

			if (!cantar.equals(""))
				textCantar.setText(cantar);

			if (tokenAntet[6].equals("X")) {
				factRed = "DA";
			}
			if (tokenAntet[6].equals(" ")) {
				factRed = "NU";
			}

			if (!factRed.equals(""))
				textFactRed.setText(factRed);

			textAdrLivr.setText(tokenAntet[2]);
			textPersContact.setText(tokenAntet[0]);
			textTelefon.setText(tokenAntet[1]);
			textOrasModif.setText(tokenAntet[7]);
			textJudetModif.setText(InfoStrings.numeJudet(tokenAntet[8]));

			int nrArt = Integer.parseInt(tokenAntet[9]); // nr. articole comanda

			String[] tokenArt;
			String client = "";
			String statusArt = " ", procAprob = "";

			for (int i = 1; i <= nrArt; i++) {

				statusArt = "";
				tipAlert = " ";
				temp = new HashMap<String, String>();
				client = tokenMain[i];
				tokenArt = client.split("#");

				temp.put("nrCrt", String.valueOf(i) + ".");
				temp.put("numeArt", tokenArt[2]);
				temp.put("codArt", tokenArt[1]);
				temp.put("cantArt", String.format("%.02f", Float.parseFloat(tokenArt[3])).toString());
				temp.put("umArt", tokenArt[6]);
				temp.put("pretArt", String.format("%.02f", Float.parseFloat(tokenArt[5])).toString());
				temp.put("procent", String.format("%.02f", Float.parseFloat(tokenArt[15])).toString());
				temp.put("monedaArt", "RON");
				temp.put("depozit", tokenArt[4]);

				if (tokenArt[0].equals("9")) {
					statusArt = "Stoc insuficient";
				}
				if (tokenArt[0].equals("19")) {
					statusArt = "Articol fara pret";
				}
				if (tokenArt[0].equals("16")) {
					statusArt = "Articol modificat";
				}
				if (tokenArt[0].equals("17")) {
					statusArt = "Articol adaugat";
				}
				if (tokenArt[0].equals("18")) {
					statusArt = "Articol sters";
				}

				if (!statusArt.equals("")) {
					temp.put("status", statusArt);
				} else {
					temp.put("status", " ");
				}

				if (UserInfo.getInstance().getTipAcces().equals("9"))// userul
																		// este
				// agent
				{
					if (Double.parseDouble(tokenArt[15]) > Double.parseDouble(tokenArt[12])) {
						tipAlert = "SD";
					}
				}

				if (Double.parseDouble(tokenArt[15]) > Double.parseDouble(tokenArt[13])) {
					tipAlert = "DV";
				}

				list1.add(temp);

			}// sf. for

			listArtModif.setAdapter(adapter);

			labelTotal.setVisibility(View.VISIBLE);
			textTotalCmd.setVisibility(View.VISIBLE);

			labelCantar.setVisibility(View.VISIBLE);
			labelFactRed.setVisibility(View.VISIBLE);
			textFactRed.setVisibility(View.VISIBLE);

		}

	}

	protected void populateCmdList1(String cmdList) {

		if (!cmdList.equals("-1") && cmdList.length() > 0) {

			creazaCmdSimBtn.setVisibility(View.VISIBLE);
			stergeCmdSimBtn.setVisibility(View.VISIBLE);

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			list1.clear();
			listComenzi.clear();
			spinnerCmd.setVisibility(View.VISIBLE);

			HashMap<String, String> temp;
			String[] tokenLinie = cmdList.split("@@");
			String[] tokenClient;
			String client = "", numeAgent = "";
			String stareCmd = "";
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");
				temp.put("idCmd", tokenClient[0] + ".");
				temp.put("numeClient", tokenClient[1]);
				temp.put("data", tokenClient[2]);
				temp.put("suma", nf2.format(Double.parseDouble(tokenClient[3])));
				temp.put("codClient", tokenClient[5]);
				temp.put("tipCmd", tokenClient[7]);
				temp.put("cmdSAP", tokenClient[6]);

				numeAgent = " ";
				if (UserInfo.getInstance().getTipAcces().equals("10"))
					numeAgent = tokenClient[13];

				temp.put("ul", numeAgent);

				stareCmd = InfoStrings.statusAprobCmd(Integer.parseInt(tokenClient[7]));

				temp.put("stare", stareCmd);

				listComenzi.add(temp);

			}

			spinnerCmd.setAdapter(adapterComenzi);

		} else {
			creazaCmdSimBtn.setVisibility(View.INVISIBLE);
			stergeCmdSimBtn.setVisibility(View.INVISIBLE);

			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	protected void populateCmdList(List<BeanComandaCreata> cmdList) {

		if (cmdList.size() > 0) {

			creazaCmdSimBtn.setVisibility(View.VISIBLE);
			stergeCmdSimBtn.setVisibility(View.VISIBLE);

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			list1.clear();
			listComenzi.clear();
			spinnerCmd.setVisibility(View.VISIBLE);

			HashMap<String, String> temp;

			String client = "", numeAgent = "";
			String stareCmd = "";

			Iterator<BeanComandaCreata> iterator = cmdList.iterator();
			BeanComandaCreata comanda;

			while (iterator.hasNext()) {
				temp = new HashMap<String, String>();

				comanda = iterator.next();

				temp.put("idCmd", comanda.getId());
				temp.put("numeClient", comanda.getNumeClient());
				temp.put("data", comanda.getData());
				temp.put("suma", nf2.format(Double.parseDouble(comanda.getSuma())));
				temp.put("codClient", comanda.getCodClient());
				temp.put("tipCmd", comanda.getStare());
				temp.put("cmdSAP", comanda.getCmdSap());

				numeAgent = " ";
				if (UserInfo.getInstance().getTipAcces().equals("10"))
					numeAgent = comanda.getNumeAgent();

				temp.put("ul", numeAgent);

				stareCmd = comanda.getStare();

				temp.put("stare", stareCmd);

				listComenzi.add(temp);

			}

			spinnerCmd.setAdapter(adapterComenzi);

		} else {
			creazaCmdSimBtn.setVisibility(View.INVISIBLE);
			stergeCmdSimBtn.setVisibility(View.INVISIBLE);

			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerListArtModif() {
		listArtModif.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				return false;

			}
		});
	}

	private void clearAllData() {
		// curatenie ecran
		list1.clear();
		adapter.notifyDataSetChanged();

		listComenzi.clear();
		adapterComenzi.notifyDataSetChanged();

		textTotalCmd.setText("");
		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");
		textOrasModif.setText("");
		textJudetModif.setText("");

		textTotalCmd.setVisibility(View.GONE);
		textTipPlata.setVisibility(View.GONE);
		textAdrLivr.setVisibility(View.GONE);
		textPersContact.setVisibility(View.GONE);
		textTelefon.setVisibility(View.GONE);
		textCantar.setVisibility(View.GONE);
		textTransport.setVisibility(View.GONE);

		labelTotal.setVisibility(View.GONE);
		labelCantar.setVisibility(View.GONE);
		labelFactRed.setVisibility(View.GONE);
		textFactRed.setVisibility(View.GONE);

		// sf.

		// reset variabile
		numeClientVar = "";
		articoleComanda = "";
		dateLivrare = "";

		articoleComanda = "";

		numeClientVar = "";
		codClientVar = "";
		totalComanda = 0;

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

	@Override
	public void onBackPressed() {
		UserInfo.getInstance().setParentScreen("");
		list1.clear();
		listComenzi.clear();

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();

		return;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("operatiiComenzi")) {
			Toast toast = Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_SHORT);
			toast.show();
			refreshCommandList();
		}

		if (methodName.equals("getCmdArt")) {
			populateArtCmdList((String) result);
		}

	}

	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map, int position) {

		if (spinnerId == R.id.spinnerCmd) {
			if (touched || list1.size() == 0) {
				getArtSelCmd();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateCmdList((List<BeanComandaCreata>) result);
			break;
		default:
			break;

		}

	}

}