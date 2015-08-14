/**
 * @author florinb
 * 
 */
package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;

import listeners.AsyncTaskListener;
import model.HandleJSONData;
import model.UserInfo;
import adapters.ClientiSemiactiviAdapter;
import android.app.Fragment;
import android.app.LauncherActivity.ListItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import beans.BeanClientSemiactiv;

public class AfisClientiSemiactivi extends Fragment implements AsyncTaskListener {

	private String reportParams = "0";
	static ListView listViewClSemiactivi;

	public static ArrayList<HashMap<String, String>> arrayListClInactivi = null;
	ClientiSemiactiviAdapter adapterClSemiactivi;

	LinearLayout layoutClSemiactivi, layoutArts, layoutArtDoc;
	Integer listViewSelPos = -1;
	String selectedDocument = "-1";

	public static String[] rapHeader = { "nrCrt", "numeClient", "codClient", "numeAgent", "stare" };

	public static String upload = " ";
	public static String GalleryImage;
	private ArrayList<ListItem> myItems = new ArrayList<ListItem>();

	public static final AfisClientiSemiactivi newInstance() {

		AfisClientiSemiactivi f = new AfisClientiSemiactivi();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.afis_clienti_semiactivi, container, false);

		try {

			listViewClSemiactivi = (ListView) v.findViewById(R.id.listClientiInactivi);

			this.layoutArtDoc = (LinearLayout) v.findViewById(R.id.layout_artvmd_vanz);

			arrayListClInactivi = new ArrayList<HashMap<String, String>>();

			this.layoutClSemiactivi = (LinearLayout) v.findViewById(R.id.layoutHeaderClInactivi);
			this.layoutClSemiactivi.setVisibility(View.VISIBLE);

			if (ClientiSemiactivi.selectedFiliala.equals("-1")) {
				Toast.makeText(getActivity(), "Selectati agentul sau filiala!", Toast.LENGTH_SHORT).show();
			} else {

				performGetRaportData();

			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

		return v;

	}

	private void performGetRaportData() {

		String selectedDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			selectedDepart = "10";
		}

		if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("27")) {
			selectedDepart = "11";
		}

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codAgent", ClientiSemiactivi.selectedAgent);
		params.put("codDepart", selectedDepart);
		params.put("filiala", ClientiSemiactivi.selectedFiliala);

		AsyncTaskListener contextListener = (AsyncTaskListener) AfisClientiSemiactivi.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getClientiSemiactivi", params);
		call.getCallResultsFromFragment();

	}

	private void fillRaportData(String reportData) {

		try {

			HandleJSONData objClientList = new HandleJSONData(getActivity(), reportData);
			ArrayList<BeanClientSemiactiv> clientiArray = objClientList.decodeJSONClientiSemiactivi();

			if (clientiArray.size() > 0) {
				adapterClSemiactivi = new ClientiSemiactiviAdapter(getActivity(), clientiArray);
				listViewClSemiactivi.setAdapter(adapterClSemiactivi);

			} else {
				Toast.makeText(getActivity(), "Nu exista date!", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getClientiSemiactivi")) {
			fillRaportData((String) result);
		}

	}

}
