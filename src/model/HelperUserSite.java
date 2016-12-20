package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.HelperSiteListener;
import lite.sfa.test.AsyncTaskWSCall;
import android.content.Context;
import beans.DepoziteUl;
import enums.EnumOperatiiSite;

public class HelperUserSite implements AsyncTaskListener {

	private EnumOperatiiSite numeComanda;
	private HashMap<String, String> params;
	private Context context;
	private HelperSiteListener listener;

	public HelperUserSite(Context context) {
		this.context = context;
	}

	public void getDepoziteUl(HashMap<String, String> params) {
		numeComanda = EnumOperatiiSite.GET_DEPOZ_UL;
		this.params = params;

		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeOp(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setHelperSiteListener(HelperSiteListener listener) {
		this.listener = listener;
	}

	public List<String> deserListDepoziteUl(String depozite) {

		List<String> listDepozite = new ArrayList<String>();

		if (depozite != null && depozite.length() > 0 && depozite.contains(",")) {
			listDepozite = Arrays.asList(depozite.split(","));

		}

		return listDepozite;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.helperSiteComplete(methodName, result);
		}
	}

	public void setDepoziteUl(String ul) {
		DepoziteUl.getInstance().setListDepozite(deserListDepoziteUl(ul));

	}

	public static String getDepozitMavSite() {

		for (String depoz : DepoziteUl.getInstance().getListDepozite()) {
			if (!depoz.equals("BV90") && !depoz.equals(UserInfo.getInstance().getUnitLog()))
				return depoz;
		}

		return UserInfo.getInstance().getUnitLog();
	}

}
