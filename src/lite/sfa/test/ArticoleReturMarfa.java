package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import listeners.ListaArtReturListener;
import listeners.OperatiiReturListener;
import model.OperatiiReturMarfa;
import model.UserInfo;
import utils.UtilsGeneral;
import adapters.ArticoleReturAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import beans.BeanArticolRetur;
import beans.BeanComandaRetur;
import enums.EnumRetur;

public class ArticoleReturMarfa extends Fragment implements ListaArtReturListener, OperatiiReturListener {

	TextView textDocument;
	ListView listArticoleRetur;
	TextView textNumeArticol, textCodArticol, textCantArticol, textUmArticol;
	Button saveArtRetur, cancelArtRetur, saveReturBtn;
	EditText textReturArticol;
	List<BeanArticolRetur> listArticole;
	BeanArticolRetur selectedArticol;
	LinearLayout layoutRetur;

	String[] tipTransport = { "Selectati tip transport", "TRAP - Transport Arabesque", "TCLI - Transport client", "TERT - Transport curier" };
	ProgressBar saveReturProgress;
	private int progressVal = 0;
	private Timer myTimer;
	private Handler myHandler = new Handler();
	private String nrDocument, codClient, numeClient;
	private OperatiiReturMarfa opRetur;
	TextView selectIcon;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.articole_retur_marfa, container, false);

		opRetur = new OperatiiReturMarfa(getActivity());
		opRetur.setOperatiiReturListener(this);

		layoutRetur = (LinearLayout) v.findViewById(R.id.layoutRetur);
		layoutRetur.setVisibility(View.GONE);

		textDocument = (TextView) v.findViewById(R.id.textDocument);
		listArticoleRetur = (ListView) v.findViewById(R.id.listArticoleRetur);
		addListenerListArticole();

		textNumeArticol = (TextView) v.findViewById(R.id.textNumeArticol);
		textCodArticol = (TextView) v.findViewById(R.id.textCodArticol);
		textCantArticol = (TextView) v.findViewById(R.id.textCantArticol);
		textUmArticol = (TextView) v.findViewById(R.id.textUmArticol);

		saveArtRetur = (Button) v.findViewById(R.id.saveArtRetur);
		addListenerSaveArt();

		cancelArtRetur = (Button) v.findViewById(R.id.cancelArtRetur);
		addListenerCancelArt();

		textReturArticol = (EditText) v.findViewById(R.id.textReturArticol);

		saveReturBtn = (Button) v.findViewById(R.id.saveReturBtn);
		saveReturBtn.setVisibility(View.INVISIBLE);
		addListenerSaveReturBtn();

		selectIcon = (TextView) v.findViewById(R.id.selectIcon);
		selectIcon.setVisibility(View.INVISIBLE);

		saveReturProgress = (ProgressBar) v.findViewById(R.id.progress_bar_retur);
		saveReturProgress.setVisibility(View.INVISIBLE);

		return v;
	}

	public static ArticoleReturMarfa newInstance() {
		ArticoleReturMarfa frg = new ArticoleReturMarfa();
		Bundle bdl = new Bundle();
		frg.setArguments(bdl);
		return frg;
	}

	private void addListenerSaveArt() {
		saveArtRetur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isCantValid()) {
					selectedArticol.setCantitateRetur(Double.valueOf(textReturArticol.getText().toString()));
					updateListArticole(selectedArticol);
					showPanel("selectItem");
				}
			}
		});
	}

	private void addListenerCancelArt() {
		cancelArtRetur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showPanel("selectItem");
			}
		});
	}

	private void addListenerSaveReturBtn() {
		saveReturBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isValidInput()) {

						saveReturProgress.setVisibility(View.VISIBLE);
						saveReturProgress.setProgress(0);
						progressVal = 0;
						myTimer = new Timer();
						myTimer.schedule(new UpdateProgress(), 40, 15);
					}
					return true;
				case MotionEvent.ACTION_UP:
					if (saveReturProgress.getVisibility() == View.VISIBLE) {
						myTimer.cancel();
						saveReturProgress.setVisibility(View.INVISIBLE);
						return true;
					}
				}
				return false;
			}
		});
	}

	private boolean isValidInput() {
		return isDateReturValide() && hasArticolReturCant();
	}

	private boolean isDateReturValide() {

		if (DateLivrareReturMarfa.dataRetur.length() == 0) {
			Toast.makeText(getActivity(), "Selectati data retur", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (DateLivrareReturMarfa.tipTransport.length() == 0) {
			Toast.makeText(getActivity(), "Selectati tipul de transport", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (DateLivrareReturMarfa.motivRetur.length() == 0) {
			Toast.makeText(getActivity(), "Selectati motivul de retur", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (DateLivrareReturMarfa.adresaCodJudet.length() == 0) {
			Toast.makeText(getActivity(), "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (DateLivrareReturMarfa.adresaOras.length() == 0) {
			Toast.makeText(getActivity(), "Selectati orasul", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean hasArticolReturCant() {
		boolean retCant = false;

		for (int i = 0; i < listArticole.size(); i++)
			if (listArticole.get(i).getCantitateRetur() > 0)
				retCant = true;

		if (!retCant)
			Toast.makeText(getActivity(), "Adaugati o cantitate de retur", Toast.LENGTH_SHORT).show();
		return retCant;
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (saveReturProgress.getProgress() == 50) {
				myHandler.post(new Runnable() {
					public void run() {

						showWarningDialog();

						// performSaveRetur();
					}
				});
				myTimer.cancel();
			} else {
				saveReturProgress.setProgress(progressVal);
			}
		}
	}

	private void performSaveRetur() {

		BeanComandaRetur comandaRetur = new BeanComandaRetur();
		comandaRetur.setNrDocument(nrDocument);
		comandaRetur.setDataLivrare(DateLivrareReturMarfa.dataRetur);
		comandaRetur.setTipTransport(DateLivrareReturMarfa.tipTransport);
		comandaRetur.setCodAgent(UserInfo.getInstance().getCod());
		comandaRetur.setTipAgent(UserInfo.getInstance().getTipUser());
		comandaRetur.setMotivRespingere(DateLivrareReturMarfa.motivRetur);
		comandaRetur.setNumePersContact(DateLivrareReturMarfa.numePersContact);
		comandaRetur.setTelPersContact(DateLivrareReturMarfa.telPersContact);
		comandaRetur.setAdresaCodJudet(DateLivrareReturMarfa.adresaCodJudet);
		comandaRetur.setAdresaOras(DateLivrareReturMarfa.adresaOras);
		comandaRetur.setAdresaStrada(DateLivrareReturMarfa.adresaStrada);
		comandaRetur.setAdresaCodAdresa(DateLivrareReturMarfa.adresaCodAdresa);
		comandaRetur.setListArticole(opRetur.serializeListArticole(listArticole));
		comandaRetur.setObservatii(DateLivrareReturMarfa.observatii);
		comandaRetur.setCodClient(codClient);
		comandaRetur.setNumeClient(numeClient);

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("dateRetur", opRetur.serializeComandaRetur(comandaRetur));

		opRetur.saveComandaRetur(params);

	}

	private boolean isCantValid() {
		if (textReturArticol.getText().toString().trim().length() == 0) {
			Toast.makeText(getActivity(), "Cantitate invalida", Toast.LENGTH_LONG).show();
			return false;
		}

		double cantitate = Double.valueOf(textCantArticol.getText().toString());
		double retur = Double.valueOf(textReturArticol.getText().toString().trim());

		if (retur > cantitate) {
			Toast.makeText(getActivity(), "Cantitate invalia", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	private void updateListArticole(BeanArticolRetur articol) {

		int artPosition = 0;
		for (int i = 0; i < listArticole.size(); i++) {
			if (articol == listArticole.get(i)) {
				artPosition = i;
				break;
			}
		}

		listArticole.set(artPosition, articol);
		populateListArticole(listArticole);
		listArticoleRetur.setSelection(artPosition);
		clearArtFields();

	}

	public void showWarningDialog() {

		StringBuilder alertMsg = new StringBuilder();
		alertMsg.append("\n");
		alertMsg.append("Te rugam sa te asiguri ca ai discutat cu clientul pentru pregatirea numarului si tipurilor de paleti ce trebuiesc returnati!");
		alertMsg.append("\n");
		alertMsg.append("\n");
		alertMsg.append("Este responsabilitatea ta! ");
		alertMsg.append("\n");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(alertMsg.toString()).setCancelable(false).setPositiveButton("Salveaza", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				performSaveRetur();
			}
		}).setNegativeButton("Anuleaza", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).setTitle("Atentie!").setIcon(R.drawable.warning96).setCancelable(false);

		AlertDialog alert = builder.create();
		alert.show();

	}

	private void clearArtFields() {
		textNumeArticol.setText("");
		textCodArticol.setText("");
		textCantArticol.setText("");
		textUmArticol.setText("");
		textReturArticol.setText("");
	}

	private void addListenerListArticole() {
		listArticoleRetur.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BeanArticolRetur articol = (BeanArticolRetur) arg0.getAdapter().getItem(arg2);
				selectedArticol = articol;
				showArticolData(articol);
				showPanel("cantitateRetur");

			}
		});
	}

	private void showPanel(String panel) {
		if (panel.equals("cantitateRetur")) {
			layoutRetur.setVisibility(View.VISIBLE);
			selectIcon.setVisibility(View.GONE);
		}

		if (panel.equals("selectItem")) {
			layoutRetur.setVisibility(View.GONE);
			selectIcon.setVisibility(View.VISIBLE);
		}

	}

	private void emptyScreen() {
		layoutRetur.setVisibility(View.GONE);
		selectIcon.setVisibility(View.GONE);
		saveReturBtn.setVisibility(View.INVISIBLE);
		populateListArticole(new ArrayList<BeanArticolRetur>());

	}

	private void showArticolData(BeanArticolRetur articol) {
		textNumeArticol.setText(articol.getNume());
		textCodArticol.setText(articol.getCod());
		textCantArticol.setText(String.valueOf(articol.getCantitate()));
		textUmArticol.setText(articol.getUm());

		if (articol.getCantitateRetur() > 0)
			textReturArticol.setText(String.valueOf(articol.getCantitateRetur()));

	}

	@Override
	public void setListArtRetur(String nrDocument, List<BeanArticolRetur> listArticole, String codClient, String numeClient) {
		this.listArticole = listArticole;
		this.nrDocument = nrDocument;
		this.codClient = codClient;
		this.numeClient = numeClient;

		saveReturBtn.setVisibility(View.VISIBLE);
		showPanel("selectItem");
		textDocument.setText("Articole");
		populateListArticole(listArticole);
	}

	private void populateListArticole(List<BeanArticolRetur> listArticole) {
		ArticoleReturAdapter adapter = new ArticoleReturAdapter(getActivity(), listArticole);
		listArticoleRetur.setAdapter(adapter);
	}

	private void showCmdStatus(String response) {
		if (response.equals("0")) {
			Toast.makeText(getActivity(), "Date salvate", Toast.LENGTH_SHORT).show();
			emptyScreen();
		} else {
			Toast.makeText(getActivity(), "Eroare: " + response, Toast.LENGTH_LONG).show();
		}

	}

	public void operationReturComplete(EnumRetur methodName, Object result) {
		switch (methodName) {
		case SAVE_COMANDA_RETUR:
			showCmdStatus((String) result);
			break;
		default:
			break;
		}

	}

}
