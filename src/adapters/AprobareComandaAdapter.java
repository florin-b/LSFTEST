package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import beans.Aprobare;
import lite.sfa.test.R;

public class AprobareComandaAdapter extends ArrayAdapter<Aprobare> {

	public ArrayList<Aprobare> aprobariNecesareArray;
	public Context context;

	public AprobareComandaAdapter(Context context2, ArrayList<Aprobare> listaAprobari) {
		super(context2, 0, listaAprobari);
		this.context = context2;
		this.aprobariNecesareArray = listaAprobari;

	}

	static class LayoutHandler {
		TextView TIP, NUME, TELEFON;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutHandler layoutHandler;
		if (row == null) {

			LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = layoutInflater.inflate(R.layout.aprobcom, parent, false);
			layoutHandler = new LayoutHandler();

			layoutHandler.TIP = (TextView) row.findViewById(R.id.tip);
			layoutHandler.NUME = (TextView) row.findViewById(R.id.nume);
			layoutHandler.TELEFON = (TextView) row.findViewById(R.id.telefon);

		} else {
			layoutHandler = (LayoutHandler) row.getTag();
		}

		Aprobare aprobari = (Aprobare) this.getItem(position);

		layoutHandler.TIP.setText("("+ aprobari.getTip() + ")");
		layoutHandler.NUME.setText(aprobari.getNume());
		layoutHandler.TELEFON.setText(aprobari.getNrTelefon());
		row.setTag(layoutHandler);

		return row;
	}

}
