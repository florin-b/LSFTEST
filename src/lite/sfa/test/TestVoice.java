package lite.sfa.test;

import helpers.HelperCreareComandaVoice;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import listeners.IVoiceRecognitionHandling;
import listeners.OperatiiArticolListener;
import listeners.OperatiiClientListener;
import listeners.RecogListener;
import listeners.VoiceRecognitionListener;
import lite.sfa.test.voce.adapters.ClientAdapter;
import lite.sfa.test.voce.view.HelperCautaArticolVoce;
import model.OperatiiArticol;
import model.OperatiiArticolFactory;
import model.OperatiiClient;
import model.UserInfo;
import utils.UtilsFormatting;
import utils.UtilsGeneral;
import adapters.CautareArticoleAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import beans.ArticolDB;
import beans.BeanClient;
import beans.DetaliiClient;
import enums.EnumArticoleDAO;
import enums.EnumClienti;

public class TestVoice extends Activity implements IVoiceRecognitionHandling, VoiceRecognitionListener, OperatiiClientListener, OperatiiArticolListener {

	private TextView istoricVoce;
	private SpeechRecognizer speech = null;
	private Intent recognizerIntent;

	private ProgressBar progressBarAction;
	private boolean isMicOn;

	private SpeechRecognizer recog;
	private Runnable readyRecognizeSpeech;
	private Handler handler = new Handler();

	private Button testButton;
	private ScrollView scrollView;
	private TextView headerLabelClient, labelArticole, labelLivrare;
	private Button cautaClientButton;
	private EditText txtNumeClient;
	private OperatiiClient opClient;
	private ListView listResClienti;
	private OperatiiArticol opArticol;

	private BeanClient selectedClient;
	private DetaliiClient detaliiClient;

	private TextView textNumeClient, textCodClient, labelSelClient;
	private Button salveazaClientButton;
	private Button stergeClientButton;

	private LinearLayout layoutCautaClient, layoutAfisClient, layoutDetClient;
	private LinearLayout layoutSelectieArticole, layoutCautaArticol;

	private Button cautaArticolButton, articoleBtn;
	private ListView listSelArticol;

	private TextView textNumeArticol;
	private TextView textStoc;
	private Button pretArticol;

	private String depozitArticol;

	private String activeModule = "";
	private ArticolDB selectedArticol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_voice);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Creare comanda");
		actionBar.setDisplayHomeAsUpEnabled(true);

		opClient = new OperatiiClient(this);
		opClient.setOperatiiClientListener(this);

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
		opArticol.setListener(this);

		readyRecognizeSpeech = new Runnable() {
			@Override
			public void run() {
				startRecognizeSpeech();
			}
		};

		scrollView = (ScrollView) findViewById(R.id.mainScroll);

		headerLabelClient = (TextView) findViewById(R.id.headerLabelClient);
		labelArticole = (TextView) findViewById(R.id.labelArticole);
		labelLivrare = (TextView) findViewById(R.id.labelLivrare);

		testButton = (Button) findViewById(R.id.testButton);
		setListenerTestButton();

		setupLayout();

		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.action_voice, null);

		istoricVoce = (TextView) mCustomView.findViewById(R.id.istoricVoce);
		istoricVoce.setVisibility(View.INVISIBLE);

		TextView numeModul = (TextView) mCustomView.findViewById(R.id.numeModul);
		numeModul.setText("Creare comanda");

		progressBarAction = (ProgressBar) mCustomView.findViewById(R.id.progressBarAction);
		progressBarAction.setVisibility(View.INVISIBLE);
		progressBarAction.setMax(10);

		final ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isMicOn = !isMicOn;

				if (isMicOn) {

					imageButton.setImageResource(R.drawable.mic_on);

					progressBarAction.setVisibility(View.VISIBLE);

					istoricVoce.setVisibility(View.VISIBLE);
					startRecognizeSpeech();

				} else {
					imageButton.setImageResource(R.drawable.mic_off);

					progressBarAction.setIndeterminate(false);
					progressBarAction.setVisibility(View.INVISIBLE);

					istoricVoce.setVisibility(View.INVISIBLE);
					stopRecognizeSpeech();

				}

			}
		});

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

	}

	private void setupLayout() {

		layoutCautaClient = (LinearLayout) findViewById(R.id.layoutCautaClient);
		layoutAfisClient = (LinearLayout) findViewById(R.id.layoutAfisClient);
		layoutDetClient = (LinearLayout) findViewById(R.id.layoutDetClient);

		layoutCautaArticol = (LinearLayout) findViewById(R.id.layoutCautaArticol);
		layoutCautaArticol.setVisibility(View.GONE);

		if (selectedClient != null) {
			layoutAfisClient.setVisibility(View.VISIBLE);
			layoutCautaClient.setVisibility(View.GONE);
		} else {
			layoutAfisClient.setVisibility(View.GONE);
			layoutCautaClient.setVisibility(View.VISIBLE);
		}

		listSelArticol = (ListView) findViewById(R.id.listResArticole);

		listSelArticol.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		listSelArticol.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

				selectedArticol = (ArticolDB) parent.getAdapter().getItem(pos);

				getStocArticol();

			}
		});

		txtNumeClient = (EditText) findViewById(R.id.txtNumeClient);
		listResClienti = (ListView) findViewById(R.id.listResClienti);

		labelSelClient = (TextView) findViewById(R.id.labelSelClient);
		labelSelClient.setVisibility(View.INVISIBLE);

		listResClienti.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		listResClienti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

				selectedClient = (BeanClient) parent.getAdapter().getItem(pos);

				// listResClienti.getChildAt(pos).startAnimation(getAnimation());

				HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
				params.put("codClient", selectedClient.getCodClient());
				params.put("depart", getExceptiiDepartament());

				opClient.getDetaliiClient(params);

			}
		});

		cautaClientButton = (Button) findViewById(R.id.cautaClientButton);
		setListenerCautaClient();

		textNumeClient = (TextView) findViewById(R.id.textNumeClient);
		textCodClient = (TextView) findViewById(R.id.textCodClient);

		salveazaClientButton = (Button) findViewById(R.id.salveazaClientButton);
		setListenerSalveazaClient();

		stergeClientButton = (Button) findViewById(R.id.stergeClientButton);
		setListenerStergeClient();

		cautaArticolButton = (Button) findViewById(R.id.cautaArticolButton);
		cautaArticolButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutCautaArticol.setVisibility(View.VISIBLE);
				cautaArticolButton.setVisibility(View.GONE);

			}
		});

		HelperCautaArticolVoce helperArticole = new HelperCautaArticolVoce(this);
		helperArticole.setupLayoutArticole();

		articoleBtn = (Button) findViewById(R.id.articoleBtn);
		setListenerArticoleBtn();

		textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
		textStoc = (TextView) findViewById(R.id.textStoc);

		pretArticol = (Button) findViewById(R.id.pretArticol);
		pretArticol.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPretArticol();
			}
		});

		if (selectedClient == null)
			afisLayoutCautaClient();

	}

	private void getPretArticol() {

		HashMap<String, String> params = new HashMap<String, String>();

		String depSel = "";
		String uLog = UserInfo.getInstance().getUnitLog();
		String tipUser = "";

		String codArticol = selectedArticol.getCod();

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		depSel = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipAcces().equals("9")) {
			tipUser = "AV";
		}
		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipUser = "SD";
		}
		if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")) {
			tipUser = "DV";
		}
		if (UserInfo.getInstance().getTipAcces().equals("27"))
			tipUser = "KA";

		params.put("client", selectedClient.getCodClient());
		params.put("articol", codArticol);
		params.put("cantitate", ((EditText) findViewById(R.id.textCantitate)).getText().toString().trim());
		params.put("depart", depSel);
		params.put("um", selectedArticol.getUmVanz10());
		params.put("ul", uLog);
		params.put("tipUser", tipUser);
		params.put("depoz", depozitArticol);
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("canalDistrib", "10");
		params.put("filialaAlternativa", uLog);

		opArticol.getPret(params);

	}

	private void setListenerArticoleBtn() {
		articoleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getArticole();
			}
		});
	}

	private void getStocArticol() {

		textNumeArticol.setText(selectedArticol.getCod() + " " + selectedArticol.getNume());
		getStoc();

	}

	private void getArticole() {

		String numeArticol = ((EditText) findViewById(R.id.txtNumeArt)).getText().toString().trim();
		String tipCautare = "";

		RadioButton radioCod = (RadioButton) findViewById(R.id.radio_cod);

		if (radioCod.isChecked())
			tipCautare = "C";
		else
			tipCautare = "N";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("searchString", numeArticol);
		params.put("tipArticol", "A");
		params.put("tipCautare", tipCautare);
		params.put("departament", UserInfo.getInstance().getCodDepart());
		params.put("filiala", UserInfo.getInstance().getUnitLog());

		opArticol.getArticoleDistributie(params);

	}

	private void getStoc() {

		HashMap<String, String> params = new HashMap<String, String>();

		String codArticol = selectedArticol.getCod();

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		String[] tokenDep = ((Spinner) findViewById(R.id.spinnerDepozite)).getSelectedItem().toString().split("-");

		if (tokenDep[0].trim().length() == 2)
			depozitArticol = selectedArticol.getDepart().substring(0, 2) + tokenDep[0].trim();
		else
			depozitArticol = tokenDep[0].trim();

		params.put("codArt", codArticol);
		params.put("filiala", UserInfo.getInstance().getUnitLog());

		params.put("depozit", depozitArticol);
		params.put("depart", UserInfo.getInstance().getCodDepart());

		opArticol.getStocDepozit(params);

	}

	private void setListenerSalveazaClient() {

		salveazaClientButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				afisLayoutSelectClient();

			}
		});

	}

	private void setListenerStergeClient() {

		stergeClientButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				afisLayoutCautaClient();

			}
		});

	}

	private void afisLayoutCautaClient() {
		detaliiClient = null;
		selectedClient = null;

		txtNumeClient.setText("");
		layoutCautaClient.setVisibility(View.VISIBLE);
		layoutAfisClient.setVisibility(View.GONE);
		layoutDetClient.setVisibility(View.INVISIBLE);
		listResClienti.setVisibility(View.INVISIBLE);
		labelSelClient.setVisibility(View.INVISIBLE);
		salveazaClientButton.setVisibility(View.INVISIBLE);

	}

	private void setListenerCautaClient() {
		cautaClientButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

				if (numeClient.isEmpty())
					return;

				HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
				params.put("numeClient", numeClient);
				params.put("depart", getExceptiiDepartament());
				params.put("departAg", UserInfo.getInstance().getCodDepart());
				params.put("unitLog", UserInfo.getInstance().getUnitLog());

				opClient.getListClienti(params);

			}
		});
	}

	private String getExceptiiDepartament() {
		String depSel = UserInfo.getInstance().getCodDepart();
		if (CreareComanda.canalDistrib.equals("20"))
			depSel = "11";

		if (!CreareComanda.canalDistrib.equals("20") && UserInfo.getInstance().getTipUser().equals("KA")) {
			depSel = "00";
		}

		return depSel;
	}

	private void setListenerTestButton() {
		testButton.setOnClickListener(new OnClickListener() {

			int i = 0;

			@Override
			public void onClick(View v) {

				scrollView.post(new Runnable() {
					public void run() {
						// scrollView.smoothScrollTo(0,
						// headerLabelClient.getTop());

						// headerLabelClient.startAnimation(getAnimation());

						// HelperCreareComandaVoice.runCommand("nume",
						// TestVoice.this);

						if (i == 0)
							HelperCreareComandaVoice.runCommand("nume", TestVoice.this);

						if (i == 1)
							HelperCreareComandaVoice.runCommand("real", TestVoice.this);

						if (i == 2)
							HelperCreareComandaVoice.runCommand("cauta", TestVoice.this);

						if (i == 3)
							HelperCreareComandaVoice.runCommand("selecteaza", TestVoice.this);

						if (i == 4)
							HelperCreareComandaVoice.runCommand("1", TestVoice.this);

						if (i == 5)
							HelperCreareComandaVoice.runCommand("salveaza", TestVoice.this);

						if (i == 6)
							HelperCreareComandaVoice.runCommand("sterge", TestVoice.this);

						i++;

						if (i == 7)
							i = 0;

					}
				});

			}
		});
	}

	@Override
	public void startRecognizeSpeech() {
		handler.removeCallbacks(readyRecognizeSpeech);

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, new Locale("ro"));
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

		recog = SpeechRecognizer.createSpeechRecognizer(this);

		RecogListener recogListener = new RecogListener(this);
		recogListener.setVoiceRecognitionListener(this);

		recog.setRecognitionListener(recogListener);

		recog.startListening(intent);

		istoricVoce.setVisibility(View.VISIBLE);

	}

	public void stopRecognizeSpeech() {

		if (recog != null) {
			recog.destroy();
			recog = null;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			returnToHome();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void returnToHome() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private void interpretCommand(String command) {

		if (command.equalsIgnoreCase("client") || command.equalsIgnoreCase("klient")) {
			scrollView.smoothScrollTo(0, findViewById(R.id.layoutSelectieClient).getTop());
			headerLabelClient.startAnimation(getAnimation());
			activeModule = "client";

		} else if (command.equalsIgnoreCase("articole")) {
			scrollView.smoothScrollTo(0, findViewById(R.id.layoutSelectieArticole).getTop());
			labelArticole.startAnimation(getAnimation());
		} else if (command.equalsIgnoreCase("livrare")) {
			scrollView.smoothScrollTo(0, findViewById(R.id.labelLivrare).getTop());
			labelLivrare.startAnimation(getAnimation());
		}

		if (activeModule.equals("client") && !command.equalsIgnoreCase("client") && !command.equalsIgnoreCase("klient"))
			HelperCreareComandaVoice.runCommand(command, this);

	}

	@Override
	public Runnable getRecognizeSpeechThread() {
		return readyRecognizeSpeech;
	}

	@Override
	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onResults(String results) {
		istoricVoce.setText(results);
		interpretCommand(UtilsFormatting.flattenToAscii(results));

	}

	@Override
	public void onRmsChanged(float voiceLevel) {
		progressBarAction.setProgress((int) voiceLevel);

	}

	private Animation getAnimation() {
		Animation animation = new AlphaAnimation(1, 0);
		animation.setDuration(200);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(2);
		animation.setRepeatMode(Animation.REVERSE);

		return animation;
	}

	public void populateListViewClient(List<BeanClient> listClienti) {

		listResClienti.setVisibility(View.VISIBLE);
		labelSelClient.setVisibility(View.VISIBLE);

		listResClienti.setAdapter(new ClientAdapter(this, listClienti));
		listResClienti.setVisibility(View.VISIBLE);

	}

	private void listClientDetails(DetaliiClient detaliiClient) {

		layoutDetClient.setVisibility(View.VISIBLE);

		this.detaliiClient = detaliiClient;
		textNumeClient.setText(selectedClient.getNumeClient());
		textCodClient.setText(selectedClient.getCodClient());
		((TextView) findViewById(R.id.textAdrClient)).setText(detaliiClient.getOras() + " " + detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());

		((TextView) findViewById(R.id.textLimitaCredit)).setText(detaliiClient.getLimitaCredit());
		((TextView) findViewById(R.id.textRestCredit)).setText(detaliiClient.getRestCredit());
		((TextView) findViewById(R.id.tipClient)).setText(detaliiClient.getTipClient());

		if (detaliiClient.getStare().equals("X")) {
			((TextView) findViewById(R.id.clientBlocat)).setText("Blocat : " + detaliiClient.getMotivBlocare());
			salveazaClientButton.setVisibility(View.INVISIBLE);
		} else
			salveazaClientButton.setVisibility(View.VISIBLE);

		((TextView) findViewById(R.id.textFilialaClient)).setText(detaliiClient.getFiliala());
	}

	private void afisLayoutSelectClient() {
		layoutCautaClient.setVisibility(View.GONE);
		layoutAfisClient.setVisibility(View.VISIBLE);

		((TextView) findViewById(R.id.afisClient)).setText(selectedClient.getNumeClient());
		((TextView) findViewById(R.id.afisLimitaCredit)).setText(detaliiClient.getLimitaCredit());
		((TextView) findViewById(R.id.afisRestCredit)).setText(detaliiClient.getRestCredit());

	}

	private void populateListViewArticol(List<ArticolDB> resultsList) {

		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
		listSelArticol.setAdapter(adapterArticole);

	}

	private void populateStocArticole(String result) {

		if (!result.contains("#") || result.equals("-1"))
			return;

		String[] tokenStoc = result.split("#");

		textStoc.setText(tokenStoc[0]);
		((TextView) findViewById(R.id.textUm)).setText(tokenStoc[1]);

	}

	private void populatePretArticol(String result) {
		if (!result.contains("#") || result.equals("-1"))
			return;

		String[] tokenPret = result.split("#");

		((EditText) findViewById(R.id.textValoare)).setText(tokenPret[1]);

	}

	@Override
	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_LISTA_CLIENTI:
			populateListViewClient(opClient.deserializeListClienti((String) result));

			break;
		case GET_DETALII_CLIENT:
			listClientDetails(opClient.deserializeDetaliiClient((String) result));
			break;
		default:
			break;
		}

	}

	@Override
	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {
		case GET_ARTICOLE_DISTRIBUTIE:
			populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
			break;

		case GET_STOC_DEPOZIT:
			populateStocArticole((String) result);
			break;
		case GET_PRET:
			populatePretArticol((String) result);

			break;
		default:
			break;
		}

	}

}
