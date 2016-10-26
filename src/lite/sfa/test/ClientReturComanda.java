package lite.sfa.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import listeners.OperatiiClientListener;
import model.ClientReturListener;
import model.OperatiiClient;
import model.UserInfo;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.CautareClientiAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import beans.BeanClient;
import enums.EnumClienti;

public class ClientReturComanda extends Fragment implements OperatiiClientListener {

	private OperatiiClient opClient;
	private EditText textNumeClient;
	private ListView clientiList;
	private TextView selectIcon;
	private Spinner spinnerLuna, spinnerAn;

	private ClientReturListener clientListener;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.client_retur_comanda, container, false);

		opClient = new OperatiiClient(getActivity());
		opClient.setOperatiiClientListener(this);

		textNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
		textNumeClient.setHint("Introduceti nume client");

		Button searchClient = (Button) v.findViewById(R.id.clientBtn);
		addListenerClient(searchClient);

		clientiList = (ListView) v.findViewById(R.id.listClienti);
		addListenerClientiList();

		selectIcon = (TextView) v.findViewById(R.id.selectIcon);
		selectIcon.setVisibility(View.INVISIBLE);

		spinnerLuna = (Spinner) v.findViewById(R.id.spinnerLuna);
		spinnerAn = (Spinner) v.findViewById(R.id.spinnerAn);

		loadDataSpinners();

		return v;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			clientListener = (ClientReturListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	public static ClientReturComanda newInstance() {
		ClientReturComanda frg = new ClientReturComanda();
		Bundle bdl = new Bundle();
		frg.setArguments(bdl);
		return frg;
	}

	private void loadDataSpinners() {

		String[] months = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie",
				"Decembrie" };

		ArrayAdapter<String> adapterLuna = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, months);
		spinnerLuna.setAdapter(adapterLuna);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		spinnerLuna.setSelection(cal.get(Calendar.MONTH));

		int year = cal.get(Calendar.YEAR);

		String[] years = new String[2];
		years[0] = String.valueOf(year - 1);
		years[1] = String.valueOf(year);

		ArrayAdapter<String> adapterAn = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, years);
		spinnerAn.setAdapter(adapterAn);

		spinnerAn.setSelection(1);

	}

	private void addListenerClient(Button clientButton) {
		clientButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getListClienti();
			}
		});
	}

	void getListClienti() {

		if (hasText(textNumeClient)) {

			clearScreen();
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("numeClient", textNumeClient.getText().toString().trim());
			params.put("depart", "00");
			params.put("departAg", UserInfo.getInstance().getCodDepart());
			params.put("unitLog", UserInfo.getInstance().getUnitLog());
			if (UtilsUser.isUserGed())
				opClient.getListClientiCV(params);
			else
				opClient.getListClienti(params);
			hideSoftKeyboard();

		}

	}

	private void clearScreen() {
		clientiList.setAdapter(new CautareClientiAdapter(getActivity(), new ArrayList<BeanClient>()));
		selectIcon.setVisibility(View.INVISIBLE);
	}

	private void addListenerClientiList() {
		clientiList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				BeanClient client = (BeanClient) arg0.getAdapter().getItem(arg2);
				if (client != null) {
					clientListener.clientSelected(client.getCodClient(), client.getNumeClient(), getIntervalSel());

				}
			}
		});
	}

	private String getIntervalSel() {
		String lunaSel = String.format("%02d", spinnerLuna.getSelectedItemPosition() + 1);
		String anSel = spinnerAn.getSelectedItem().toString();

		return anSel + lunaSel;
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private boolean hasText(EditText editText) {
		return editText.getText().toString().trim().length() > 0 ? true : false;
	}

	private void populateListViewClient(List<BeanClient> listClienti) {

		if (listClienti.size() > 0) {
			selectIcon.setVisibility(View.VISIBLE);
			CautareClientiAdapter adapterClienti = new CautareClientiAdapter(getActivity(), listClienti);
			clientiList.setAdapter(adapterClienti);
		}

	}

	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_LISTA_CLIENTI:
		case GET_LISTA_CLIENTI_CV:
			populateListViewClient(opClient.deserializeListClienti((String) result));
			break;
		default:
			break;
		}

	}

}
