package lite.sfa.test.voce.view;

import java.util.ArrayList;
import java.util.Arrays;

import lite.sfa.test.R;
import lite.sfa.test.TestVoice;
import utils.UtilsGeneral;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HelperCautaArticolVoce {

	private TestVoice caller;

	private Spinner spinnerDepozite;

	public HelperCautaArticolVoce(TestVoice caller) {
		this.caller = caller;
	}

	public void setupLayoutArticole() {
		spinnerDepozite = (Spinner) caller.findViewById(R.id.spinnerDepozite);
		setupDepozite();

	}

	private void setupDepozite() {
		ArrayList<String> arrayListDepozite = new ArrayList<String>();
		arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteDistributie()));
		ArrayAdapter<String> adapterSpinnerDepozite = new ArrayAdapter<String>(caller, android.R.layout.simple_spinner_item, arrayListDepozite);

		adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDepozite.setAdapter(adapterSpinnerDepozite);
	}

}
