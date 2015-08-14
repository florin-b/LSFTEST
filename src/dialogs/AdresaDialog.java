package dialogs;

import java.util.ArrayList;
import java.util.List;

import listeners.AdresaDialogListener;
import lite.sfa.test.R;
import lite.sfa.test.R.id;
import lite.sfa.test.R.layout;
import utils.UtilsGeneral;
import adapters.AdapterListJudete;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import beans.BeanAdresaGenerica;
import beans.BeanJudet;

public class AdresaDialog extends Dialog {

	private Spinner spinnerJudete;
	private EditText textOras, textStrada;
	private ImageButton saveButton, cancelButton;
	private AdresaDialogListener adresaListener;
	private boolean showStradaField;

	public AdresaDialog(Context context, boolean showStradaField) {
		super(context);
		this.showStradaField = showStradaField;

		setContentView(R.layout.adresa_dialog);
		setTitle("Adresa");
		setCancelable(true);

		setUpLayout();
	}

	private void setUpLayout() {

		spinnerJudete = (Spinner) findViewById(R.id.spinnerJudete);

		AdapterListJudete adapter = new AdapterListJudete(getContext(), listJudete());
		spinnerJudete.setAdapter(adapter);

		textOras = (EditText) findViewById(R.id.textOras);
		textOras.setHint("Completati orasul");
		textStrada = (EditText) findViewById(R.id.textStrada);
		textStrada.setHint("Completati strada");

		if (!showStradaField)
			textStrada.setVisibility(View.INVISIBLE);

		saveButton = (ImageButton) findViewById(R.id.btnSave);
		cancelButton = (ImageButton) findViewById(R.id.btnCancel);

		setSaveButtonListener();
		setCancelButtonListener();

	}

	private List<BeanJudet> listJudete() {
		List<BeanJudet> listJudete = new ArrayList<BeanJudet>();

		BeanJudet judet = null;
		for (int i = 0; i < UtilsGeneral.numeJudete.length; i++) {
			judet = new BeanJudet();
			judet.setCodJudet(UtilsGeneral.codJudete[i]);
			judet.setNumeJudet(UtilsGeneral.numeJudete[i]);
			listJudete.add(judet);
		}

		return listJudete;
	}

	private void setSaveButtonListener() {
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isAdresaValid()) {
					if (adresaListener != null) {
						adresaListener.adresaSelected(getAdresa());
						dismiss();
					}
				}

			}
		});
	}

	private BeanAdresaGenerica getAdresa() {
		BeanAdresaGenerica adresa = new BeanAdresaGenerica();

		BeanJudet judet = (BeanJudet) spinnerJudete.getSelectedItem();
		adresa.setCodJudet(judet.getCodJudet());
		adresa.setNumeJudet(judet.getNumeJudet());
		adresa.setOras(textOras.getText().toString().replace("/", " ").trim());
		adresa.setStrada(textStrada.getText().toString().replace("/", " ").trim());

		return adresa;

	}

	private void setCancelButtonListener() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setAdresaDialogListener(AdresaDialogListener adresaListener) {
		this.adresaListener = adresaListener;
	}

	private boolean isAdresaValid() {
		if (spinnerJudete.getSelectedItemPosition() == 0) {
			Toast.makeText(getContext(), "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (textOras.getText().toString().trim().length() == 0) {
			Toast.makeText(getContext(), "Completati orasul", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
