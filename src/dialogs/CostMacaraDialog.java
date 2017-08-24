package dialogs;

import java.text.NumberFormat;
import java.util.Locale;

import listeners.CostMacaraListener;
import lite.sfa.test.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CostMacaraDialog extends Dialog {

	private double costMacara;

	private CostMacaraListener listener;

	public CostMacaraDialog(Context context, double costMacara) {
		super(context);

		this.costMacara = costMacara;

		setContentView(R.layout.cost_macara_dialog);
		setTitle("Confirmare cost macara");

		setUpLayout();
	}

	void setUpLayout() {
		NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		StringBuilder strCost = new StringBuilder();
		strCost.append("Utilizarea macaralei presupune un cost suplimentar de ");
		strCost.append(nf2.format(costMacara));
		strCost.append(" RON. Sunteti de acord? ");

		TextView textCostMacara = (TextView) findViewById(R.id.textCost);
		textCostMacara.setText(strCost.toString());

		Button btnOkCmd = (Button) findViewById(R.id.btnOkCmd);
		btnOkCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (listener != null)
					listener.acceptaCostMacara(true, costMacara);

				dismiss();

			}
		});

		Button btnCancelCmd = (Button) findViewById(R.id.btnCancelCmd);
		btnCancelCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (listener != null)
					listener.acceptaCostMacara(false, 0);

				dismiss();

			}
		});

	}

	public void setCostMacaraListener(CostMacaraListener listener) {
		this.listener = listener;
	}

}
