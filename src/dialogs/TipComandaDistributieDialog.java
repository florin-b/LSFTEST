package dialogs;

import listeners.TipCmdDistribListener;
import lite.sfa.test.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import enums.TipCmdDistrib;

public class TipComandaDistributieDialog extends Dialog {

	private TipCmdDistribListener listener;
	private Context context;
	private TipCmdDistrib tipComanda = TipCmdDistrib.COMANDA_VANZARE;

	public TipComandaDistributieDialog(Context context) {
		super(context);
		this.context = context;

	}

	public void showDialog() {
		setUpLayout();
		this.show();

	}

	private void setUpLayout() {
		setContentView(R.layout.tipcomandadistributiedialog);
		setTitle("Selectati tipul de comanda");
		setCancelable(false);

		final RadioButton radioCV = (RadioButton) findViewById(R.id.radioCV);
		final RadioButton radioDL = (RadioButton) findViewById(R.id.radioDL);
		final RadioButton radioCC = (RadioButton) findViewById(R.id.radioCC);

		Button btnOkTipCmd = (Button) findViewById(R.id.btnOkTipCmd);
		btnOkTipCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (radioCV.isChecked())
					tipComanda = TipCmdDistrib.COMANDA_VANZARE;
				else if (radioDL.isChecked())
					tipComanda = TipCmdDistrib.DISPOZITIE_LIVRARE;
				else if (radioCC.isChecked())
					tipComanda = TipCmdDistrib.LIVRARE_CUSTODIE;				

				if (listener != null)
					listener.tipComandaSelected(tipComanda);
				dismiss();
			}
		});

	}

	public void setTipCmdDistribListener(TipCmdDistribListener listener) {
		this.listener = listener;
	}

}
