package lite.sfa.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.DocumentReturListener;
import listeners.ListaArtReturListener;
import listeners.ListaDocReturListener;
import listeners.OperatiiReturListener;
import model.ClientReturListener;
import model.OperatiiReturMarfa;
import model.UserInfo;
import model.ViewPagerCustomDuration;
import utils.UtilsGeneral;
import utils.UtilsUser;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import beans.BeanDocumentRetur;
import enums.EnumRetur;

public class ReturMarfa extends FragmentActivity implements ClientReturListener, OperatiiReturListener, DocumentReturListener {

	ViewPagerCustomDuration pager;
	ViewPager viewPager;
	static String value;
	OperatiiReturMarfa opRetur;
	ListaDocReturListener docReturListener;
	ListaArtReturListener artReturListener;

	ClientReturMarfa clientiReturMarfa;
	DocumenteReturMarfa documenteReturMarfa;
	DateLivrareReturMarfa dateLivrareReturMarfa;
	ArticoleReturMarfa articoleReturMarfa;
	private String numeClient, codClient;
	private String nrDocument;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.retur_marfa);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		opRetur = new OperatiiReturMarfa(this);
		opRetur.setOperatiiReturListener(this);

		clientiReturMarfa = ClientReturMarfa.newInstance();
		documenteReturMarfa = DocumenteReturMarfa.newInstance();
		dateLivrareReturMarfa = DateLivrareReturMarfa.newInstance();
		articoleReturMarfa = ArticoleReturMarfa.newInstance();

		docReturListener = documenteReturMarfa;
		artReturListener = articoleReturMarfa;

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Retur paleti");
		actionBar.setDisplayHomeAsUpEnabled(true);

		List<Fragment> fragments = getFragments();
		viewPager = (ViewPager) findViewById(R.id.returviewpager);
		final ReturMarfaPagerAdapter returAdapter = new ReturMarfaPagerAdapter(getSupportFragmentManager(), fragments);

		viewPager.setAdapter(returAdapter);
		viewPager.setOffscreenPageLimit(4);

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		mnu1.setIcon(R.drawable.arrow_left);

		MenuItem mnu2 = menu.add(0, 1, 1, "");
		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		mnu2.setIcon(R.drawable.arrow_right);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			return true;
		case 1:
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			return true;

		case android.R.id.home:
			returnToHome();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private List<Fragment> getFragments() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(clientiReturMarfa);
		fragmentList.add(documenteReturMarfa);
		fragmentList.add(dateLivrareReturMarfa);
		fragmentList.add(articoleReturMarfa);

		return fragmentList;

	}

	class ReturMarfaPagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public ReturMarfaPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		public int getCount() {
			return this.fragments.size();
		}
	}

	private void returnToHome() {
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	public void clientSelected(String codClient, String numeClient) {
		this.numeClient = numeClient;
		this.codClient = codClient;

		String codDepart = UserInfo.getInstance().getCodDepart();
		if (UtilsUser.isUserGed())
			codDepart = "11";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codClient", codClient);
		params.put("codDepartament", codDepart);
		params.put("unitLog", UserInfo.getInstance().getUnitLog());
		opRetur.getDocumenteClient(params);
	}

	public void documentSelected(String nrDocument) {
		this.nrDocument = nrDocument;
		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("nrDocument", nrDocument);
		opRetur.getArticoleDocument(params);

	}

	private void displayDocumenteRetur(List<BeanDocumentRetur> listaDocumente) {
		if (listaDocumente.size() > 0) {
			docReturListener.setListDocRetur(numeClient, listaDocumente);
			viewPager.setCurrentItem(1, true);
		} else {
			Toast.makeText(this, "Nu exista documente", Toast.LENGTH_SHORT).show();
		}
	}

	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(this, MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	public void operationReturComplete(EnumRetur methodName, Object result) {
		switch (methodName) {
		case GET_LISTA_DOCUMENTE:
			displayDocumenteRetur(opRetur.deserializeListDocumente((String) result));
			break;
		case GET_ARTICOLE_DOCUMENT:
			artReturListener.setListArtRetur(nrDocument, opRetur.deserializeListArticole((String) result), codClient, numeClient);
			dateLivrareReturMarfa.setListAdreseLivrare(opRetur.getListAdrese());
			dateLivrareReturMarfa.setPersoaneContact(opRetur.getListPersoane());
			viewPager.setCurrentItem(2, true);
			break;
		default:
			break;
		}

	}
}
