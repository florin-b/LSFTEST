package dialogs;

import java.util.HashMap;
import java.util.List;

import listeners.CautaClientDialogListener;
import listeners.OperatiiClientListener;
import lite.sfa.test.R;
import model.OperatiiClient;
import model.UserInfo;
import utils.ScreenUtils;
import utils.UtilsGeneral;
import adapters.CautareClientiAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import beans.BeanClient;
import enums.EnumClienti;

public class CautaClientDialog extends Dialog implements OperatiiClientListener {

	private ImageButton cancelButton;
	private Button btnCautaClient;
	private EditText textNumeClient;
	private OperatiiClient opClient;
	private ListView listClientiObiective;
	private CautaClientDialogListener listener;
	private Context context;
	private boolean isMeserias;
	private boolean isClientObiectivKA;
	private boolean isInstitPublica;

	public CautaClientDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.select_client_obiective_dialog);
		setTitle("Selectie client");
		setCancelable(true);

		opClient = new OperatiiClient(getContext());
		opClient.setOperatiiClientListener(this);

		setUpLayout();

	}

	private void setUpLayout() {

		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		btnCautaClient = (Button) findViewById(R.id.btnCautaClient);
		setListenerCautaClient();

		textNumeClient = (EditText) findViewById(R.id.textNumeClient);
		textNumeClient.setHint("Nume client");
		listClientiObiective = (ListView) findViewById(R.id.listClientiObiective);
		setListenerListClienti();

	}

	private void setListenerCancelButton() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private void setListenerCautaClient() {
		btnCautaClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ScreenUtils.hideSoftKeyboard(context, textNumeClient);
				if (isMeserias()) {
					cautaMeserias();
				} else if (isInstitPublica()) {
					cautaInstitPub();
				} else {
					cautaClient();
				}

			}
		});
	}

	private void setListenerListClienti() {
		listClientiObiective.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BeanClient client = (BeanClient) listClientiObiective.getItemAtPosition(arg2);
				if (listener != null) {
					listener.clientSelected(client);
					dismiss();
				}

			}
		});
	}

	public void setClientSelectedListener(CautaClientDialogListener listener) {
		this.listener = listener;
	}

	private void cautaClient() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		String localUnitLog = UserInfo.getInstance().getUnitLog();

		if (isClientObiectivKA)
			localUnitLog = "NN10";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("depart", "00");
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", localUnitLog);

		opClient.getListClienti(params);
	}

	private void cautaMeserias() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		opClient.getListMeseriasi(params);
	}

	private void cautaInstitPub() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		opClient.getClientiInstitPub(params);
	}

	private void afisListClienti(List<BeanClient> listClienti) {
		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(getContext(), listClienti);
		listClientiObiective.setAdapter(adapterClienti);
	}

	public void operationComplete(EnumClienti methodName, Object result) {
		switch (methodName) {
		case GET_LISTA_CLIENTI:
		case GET_LISTA_MESERIASI:
		case GET_CLIENTI_INST_PUB:
			afisListClienti(opClient.deserializeListClienti((String) result));
			break;
		default:
			break;
		}

	}

	public boolean isMeserias() {
		return isMeserias;
	}

	public void setMeserias(boolean isMeserias) {
		this.isMeserias = isMeserias;
	}

	public boolean isClientObiectivKA() {
		return isClientObiectivKA;
	}

	public void setClientObiectivKA(boolean isClientObiectivKA) {
		this.isClientObiectivKA = isClientObiectivKA;
	}

	public boolean isInstitPublica() {
		return isInstitPublica;
	}

	public void setInstitPublica(boolean isInstitPublica) {
		this.isInstitPublica = isInstitPublica;
	}

}
