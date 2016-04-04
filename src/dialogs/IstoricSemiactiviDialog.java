package dialogs;

import java.util.List;

import lite.sfa.test.R;
import adapters.IstoricSemiactiviAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import beans.BeanIstoricSemiactiv;

public class IstoricSemiactiviDialog extends Dialog {

	private Context context;
	private List<BeanIstoricSemiactiv> listIstoric;
	private Button okButton;
	private ListView listViewIstoric;

	public IstoricSemiactiviDialog(Context context, List<BeanIstoricSemiactiv> listIstoric, String numeClient) {
		super(context);
		this.context = context;
		this.listIstoric = listIstoric;

		setContentView(R.layout.afis_istoric_semiactiv);
		setTitle("Istoric client " + numeClient);
		setCancelable(true);

		setUpLayout();

	}

	private void setUpLayout() {

		listViewIstoric = (ListView) findViewById(R.id.listIstoric);
		IstoricSemiactiviAdapter adapter = new IstoricSemiactiviAdapter(context, listIstoric);
		listViewIstoric.setAdapter(adapter);

		okButton = (Button) findViewById(R.id.btnOk);
		addOkButtonListener();
	}

	public void dismissCustomDialog() {
		this.dismiss();
	}

	private void addOkButtonListener() {
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismissCustomDialog();

			}
		});

	}

}
