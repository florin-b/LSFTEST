/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.util.HashMap;
import java.util.List;

import listeners.ComenziDAOListener;
import listeners.SelectAgentDialogListener;
import model.Agent;
import model.ComenziDAO;
import model.UserInfo;
import utils.MapUtils;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.ClientBorderouAdapter;
import adapters.ComandaDeschisaAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import beans.Address;
import beans.BeanClientBorderou;
import beans.BeanComandaDeschisa;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dialogs.SelectAgentDialog;
import enums.EnumComenziDAO;

public class StareComenzi extends Activity implements ComenziDAOListener, SelectAgentDialogListener {

	private Spinner spinnerComenzi;
	private ComenziDAO operatiiComenzi;
	private ListView listClientiBorderou;
	private BeanComandaDeschisa comandaCurenta;
	private android.app.Fragment mapFragment;
	private Button hartaButton;
	private GoogleMap googleMap;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.stare_comanda);

		actionBar = getActionBar();
		actionBar.setTitle("Stare comenzi");
		actionBar.setDisplayHomeAsUpEnabled(true);

		hartaButton = (Button) findViewById(R.id.hartaButton);
		hartaButton.setVisibility(View.INVISIBLE);
		setListenerHartaButton();

		mapFragment = getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getView().setVisibility(View.INVISIBLE);

		spinnerComenzi = (Spinner) findViewById(R.id.spinnerComenzi);
		spinnerComenzi.setVisibility(View.INVISIBLE);
		setSpinnerComenziListener();

		listClientiBorderou = (ListView) findViewById(R.id.listClientiBorderou);
		listClientiBorderou.setVisibility(View.INVISIBLE);

		operatiiComenzi = ComenziDAO.getInstance(this);
		operatiiComenzi.setComenziDAOListener(this);

		if (!isSDorSM())
			getComenziDeschise(UserInfo.getInstance().getCod());

	}

	private void getComenziDeschise(String codAgent) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		operatiiComenzi.getComenziDeschise(params);

	}

	private boolean isSDorSM() {
		return UserInfo.getInstance().getTipUserSap().equals("SD") || UserInfo.getInstance().getTipUserSap().equals("SM") || UtilsUser.isSMNou();
	}

	private void displayComenziDeschise(List<BeanComandaDeschisa> listComenzi) {

		if (listComenzi.size() > 0) {
			ComandaDeschisaAdapter adapter = new ComandaDeschisaAdapter(this, listComenzi);
			spinnerComenzi.setVisibility(View.VISIBLE);
			spinnerComenzi.setAdapter(adapter);
		} else {
			Toast.makeText(getApplicationContext(), "Nu exista comenzi", Toast.LENGTH_SHORT).show();
			spinnerComenzi.setVisibility(View.GONE);
			listClientiBorderou.setVisibility(View.GONE);
			mapFragment.getView().setVisibility(View.GONE);
			hartaButton.setVisibility(View.GONE);
		}

	}

	private void displayClientiBorderou(List<BeanClientBorderou> listClienti) {
		if (listClienti.size() > 0) {
			ClientBorderouAdapter adapter = new ClientBorderouAdapter(this, listClienti);
			adapter.setComandaCurenta(comandaCurenta);
			listClientiBorderou.setVisibility(View.VISIBLE);
			listClientiBorderou.setAdapter(adapter);

			if (comandaCurenta.getCodStareComanda() >= 6)
				hartaButton.setVisibility(View.VISIBLE);
			else
				hartaButton.setVisibility(View.INVISIBLE);

			mapFragment.getView().setVisibility(View.INVISIBLE);

		}
	}

	private void setListenerHartaButton() {
		hartaButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showMap();

			}
		});
	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Agenti");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (isSDorSM())
			CreateMenu(menu);
		return true;
	}

	private void initMap() {
		if (mapFragment.getView().getVisibility() == View.INVISIBLE) {
			mapFragment.getView().setVisibility(View.VISIBLE);

		}

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
	}

	private void showMap() {
		initMap();
		getPozitieMasina();

	}

	private void getPozitieMasina() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrMasina", comandaCurenta.getNrMasina());
		operatiiComenzi.getPozitieMasina(params);

	}

	private void showMapPozitieMasina(String coords) {

		if (!coords.equals("-1")) {

			Address address = new Address();
			address.setSector(UtilsGeneral.getNumeJudet(comandaCurenta.getCodJudet()));
			address.setCity(comandaCurenta.getLocalitate());
			address.setStreet(comandaCurenta.getStrada());

			LatLng coordClient = null;
			try {
				coordClient = MapUtils.geocodeAddress(address, this).getCoordinates();
			} catch (Exception e) {

			}

			String[] localCoords = coords.split("#");

			googleMap.clear();

			LatLng coordMasina = new LatLng(Double.valueOf(localCoords[0]), Double.valueOf(localCoords[1]));

			if (coordClient.latitude > 0) {
				MarkerOptions markerClient = new MarkerOptions();
				markerClient.position(coordClient);
				markerClient.icon(BitmapDescriptorFactory.fromResource(R.drawable.customer));
				googleMap.addMarker(markerClient);
			}

			if (coordMasina.latitude > 0) {
				MarkerOptions markerMasina = new MarkerOptions();
				markerMasina.position(coordMasina);
				markerMasina.icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery));
				googleMap.addMarker(markerMasina);
			}

			if (coordMasina.latitude > 0)
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordMasina, 8));
			else if (coordClient.latitude > 0)
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordClient, 8));

		}

	}

	private void setSpinnerComenziListener() {
		spinnerComenzi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				comandaCurenta = (BeanComandaDeschisa) parent.getAdapter().getItem(position);
				getClientiBorderou();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void getClientiBorderou() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codBorderou", comandaCurenta.getCodBorderou());

		operatiiComenzi.getClientiBorderou(params);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			showSelectAgentDialog();
			break;

		case android.R.id.home:
			returnToMainMenu();
			return true;

		}
		return false;
	}

	private void showSelectAgentDialog() {
		SelectAgentDialog selectAgent = new SelectAgentDialog(StareComenzi.this);
		selectAgent.setAgentDialogListener(this);
		selectAgent.show();
	}

	private void returnToMainMenu() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	@Override
	public void onBackPressed() {
		returnToMainMenu();
		return;
	}

	@Override
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_COMENZI_DESCHISE:
			displayComenziDeschise(operatiiComenzi.deserializeComenziDeschise((String) result));
			break;
		case GET_CLIENTI_BORD:
			displayClientiBorderou(operatiiComenzi.deserializeClientiBorderou((String) result));
			break;
		case GET_POZITIE_MASINA:
			showMapPozitieMasina((String) result);
			break;
		default:
			break;

		}

	}

	@Override
	public void agentSelected(Agent agent) {
		getComenziDeschise(agent.getCod());
		actionBar.setTitle("Stare comenzi" + " - " + agent.getNume());

	}

}