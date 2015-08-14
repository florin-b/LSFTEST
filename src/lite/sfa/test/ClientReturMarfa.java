package lite.sfa.test;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import beans.BeanClient;
import enums.EnumClienti;

public class ClientReturMarfa extends Fragment implements
        OperatiiClientListener {

    OperatiiClient opClient;
    EditText textNumeClient;
    ListView clientiList;
    TextView selectIcon;

    ClientReturListener clientListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater
                .inflate(R.layout.client_retur_marfa, container, false);

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

    public static ClientReturMarfa newInstance() {
        ClientReturMarfa frg = new ClientReturMarfa();
        Bundle bdl = new Bundle();
        frg.setArguments(bdl);
        return frg;
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
        clientiList.setAdapter(new CautareClientiAdapter(getActivity(),
                new ArrayList<BeanClient>()));
        selectIcon.setVisibility(View.INVISIBLE);
    }

    private void addListenerClientiList() {
        clientiList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                BeanClient client = (BeanClient) arg0.getAdapter()
                        .getItem(arg2);
                if (client != null) {
                    clientListener.clientSelected(client.getCodClient(),
                            client.getNumeClient());

                }
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private boolean hasText(EditText editText) {
        return editText.getText().toString().trim().length() > 0 ? true : false;
    }

    private void populateListViewClient(List<BeanClient> listClienti) {

        if (listClienti.size() > 0) {
            selectIcon.setVisibility(View.VISIBLE);
            CautareClientiAdapter adapterClienti = new CautareClientiAdapter(
                    getActivity(), listClienti);
            clientiList.setAdapter(adapterClienti);
        }

    }

    public void operationComplete(EnumClienti methodName, Object result) {

        switch (methodName) {
        case GET_LISTA_CLIENTI:
        case GET_LISTA_CLIENTI_CV:
            populateListViewClient(opClient
                    .deserializeListClienti((String) result));
            break;
        default:
            break;
        }

    }

}
