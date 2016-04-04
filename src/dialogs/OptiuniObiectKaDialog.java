package dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.DialogObiectiveKAListener;
import listeners.ObiectiveListener;
import listeners.OperatiiAgentListener;
import lite.sfa.test.R;
import model.Agent;
import model.OperatiiAgent;
import model.OperatiiObiective;
import model.UserInfo;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import enums.EnumFilialeKA;
import enums.EnumOperatiiObiective;
import enums.EnumTipInterval;
import enums.EnumTipUser;

public class OptiuniObiectKaDialog extends Dialog implements OperatiiAgentListener, ObiectiveListener {

	private Spinner spinnerFiliale, spinnerAgenti, spinnerInterval;
	private OperatiiAgent operatiiAgent;
	private String selectedFiliala, selectedAgent = "00";
	private int selectedInterval;
	private ImageButton buttonOk, buttonCancel;
	private OperatiiObiective operatiiObiective;
	private DialogObiectiveKAListener listener;

	public OptiuniObiectKaDialog(Context context) {
		super(context);

		setContentView(R.layout.optiuni_ka_dialog);
		setTitle("Selectii");
		setCancelable(true);

		operatiiAgent = OperatiiAgent.getInstance();
		operatiiAgent.setOperatiiAgentListener(this);

		operatiiObiective = new OperatiiObiective(getContext());

		setupLayout();
	}

	private void setupLayout() {
		spinnerFiliale = (Spinner) findViewById(R.id.spinnerFiliale);
		setupSpinnerFiliale();

		spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);
		spinnerInterval.setVisibility(View.INVISIBLE);
		setupSpinnerInterval();

		spinnerAgenti = (Spinner) findViewById(R.id.spinnerAgenti);
		spinnerAgenti.setVisibility(View.INVISIBLE);

		if (isUserDV())
			spinnerAgenti.setVisibility(View.GONE);

		buttonOk = (ImageButton) findViewById(R.id.btnOk);
		setListenerBtnOk();
		buttonCancel = (ImageButton) findViewById(R.id.btnCancel);
		setListenerBtnCancel();

	}

	private void setListenerBtnCancel() {
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});

	}

	private void setListenerBtnOk() {
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isInputValid()) {
					getListObiective();
				}

				dismiss();

			}
		});

	}

	private void getListObiective() {

		String depart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipUser().equals("DK"))
			depart = "00";

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", selectedAgent);
		params.put("filiala", selectedFiliala);
		params.put("tip", "-1");
		params.put("interval", String.valueOf(selectedInterval));
		params.put("depart", depart);

		operatiiObiective.setObiectiveListener(this);
		operatiiObiective.getListObiective(params);
	}

	private boolean isInputValid() {
		if (spinnerFiliale.getSelectedItemPosition() == 0) {
			Toast.makeText(getContext(), "Selectati filiala ", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (isUserDK()) {
			if (spinnerAgenti.getSelectedItemPosition() == 0) {
				Toast.makeText(getContext(), "Selectati agentul ", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		if (spinnerInterval.getSelectedItemPosition() == 0) {
			Toast.makeText(getContext(), "Selectati intervalul ", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void setupSpinnerInterval() {
		spinnerInterval.setAdapter(new ArrayAdapter<EnumTipInterval>(getContext(), android.R.layout.simple_list_item_1, EnumTipInterval.values()));
		spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedInterval = ((EnumTipInterval) spinnerInterval.getSelectedItem()).getCod();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void setupSpinnerFiliale() {
		spinnerFiliale.setAdapter(new ArrayAdapter<EnumFilialeKA>(getContext(), android.R.layout.simple_list_item_1, EnumFilialeKA.values()));
		spinnerFiliale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					selectedFiliala = ((EnumFilialeKA) spinnerFiliale.getSelectedItem()).getCod();
					spinnerInterval.setVisibility(View.VISIBLE);
					getListaAgenti(selectedFiliala);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private boolean isUserDK() {
		return UserInfo.getInstance().getTipUser().equals(EnumTipUser.DK.getTipAcces());
	}

	private boolean isUserDV() {
		return UserInfo.getInstance().getTipUser().equals(EnumTipUser.DV.getTipAcces());
	}

	private void getListaAgenti(String filiala) {
		if (isUserDK())
			operatiiAgent.getListaAgenti(filiala, getDepartUser(), getContext(), true);
	}

	private String getDepartUser() {
		String codDepart = "00";

		if (UserInfo.getInstance().getTipUser().equals(EnumTipUser.DV.getTipAcces()))
			codDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipUser().equals(EnumTipUser.DK.getTipAcces()))
			codDepart = "10";

		return codDepart;

	}

	private void populateSpinnerAgenti(List<Agent> listAgenti) {

		spinnerAgenti.setVisibility(View.VISIBLE);
		spinnerAgenti.setAdapter(new ArrayAdapter<Agent>(getContext(), android.R.layout.simple_list_item_1, listAgenti));
		spinnerAgenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedAgent = ((Agent) spinnerAgenti.getSelectedItem()).getCod();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateSpinnerAgenti(operatiiAgent.getListObjAgenti());

	}

	public void setDialogListener(DialogObiectiveKAListener listener) {
		this.listener = listener;
	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_LIST_OBIECTIVE:
			if (listener != null)
				listener.selectionComplete(operatiiObiective.deserializeListaObiective((String) result));

			break;
		default:
			break;
		}

	}

}
