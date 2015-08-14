/**
 * @author florinb
 *
 */
package adapters;

import java.util.HashMap;
import java.util.List;

import lite.sfa.test.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NeincasateAdapter extends SimpleAdapter {

	Context context;

	static class ViewHolder {

		public TextView textEmpty, textNrCrt, textReferinta, textEmitere,
				textScadenta, textAcoperit, textTipPlata, textScadentaBO,
				textValoare, textIncasat, textRest;

		public ImageView image;
	}

	public NeincasateAdapter(Context context,
			List<HashMap<String, String>> items, int resource, String[] from,
			int[] to) {

		super(context, items, resource, from, to);
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		boolean isBold = false;

		if (v == null) {
			LayoutInflater vi = ((Activity) context).getLayoutInflater();
			v = vi.inflate(R.layout.custom_art_row_neincasate, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.textEmpty = (TextView) v.findViewById(R.id.emptyView);

			viewHolder.textNrCrt = (TextView) v.findViewById(R.id.textNrCrt);
			viewHolder.textReferinta = (TextView) v
					.findViewById(R.id.textReferinta);
			viewHolder.textEmitere = (TextView) v
					.findViewById(R.id.textEmitere);
			viewHolder.textScadenta = (TextView) v
					.findViewById(R.id.textScadenta);
			viewHolder.textAcoperit = (TextView) v
					.findViewById(R.id.textAcoperit);
			viewHolder.textTipPlata = (TextView) v
					.findViewById(R.id.textTipPlata);
			viewHolder.textScadentaBO = (TextView) v
					.findViewById(R.id.textScadentaBO);

			viewHolder.textValoare = (TextView) v
					.findViewById(R.id.textValoare);
			viewHolder.textIncasat = (TextView) v
					.findViewById(R.id.textIncasat);
			viewHolder.textRest = (TextView) v.findViewById(R.id.textRest);

			v.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) v.getTag();

		HashMap<String, String> artMap = (HashMap<String, String>) this
				.getItem(position);

		holder.textEmpty.setText("");
		holder.textEmpty.setHeight(40);

		String tokNewVal = artMap.get("nrCrt").substring(0,
				artMap.get("nrCrt").length() - 1);
		if (!tokNewVal.trim().equals("")) {
			isBold = false;
			holder.textNrCrt.setText(tokNewVal + ".");
		} else {
			isBold = true;
			holder.textNrCrt.setText(" ");
		}

		tokNewVal = artMap.get("referinta");
		holder.textReferinta.setText(tokNewVal);
		if (isBold) {
			holder.textReferinta.setTypeface(null, Typeface.BOLD);
		} else {
			holder.textReferinta.setTypeface(null, Typeface.NORMAL);
		}

		tokNewVal = artMap.get("emitere");
		holder.textEmitere.setText(tokNewVal);

		tokNewVal = artMap.get("scadenta");
		holder.textScadenta.setText(tokNewVal);

		tokNewVal = artMap.get("acoperit");
		holder.textAcoperit.setText(tokNewVal);

		tokNewVal = artMap.get("tipPlata");
		holder.textTipPlata.setText(tokNewVal);

		tokNewVal = artMap.get("scadentaBO");
		holder.textScadentaBO.setText(tokNewVal);

		tokNewVal = artMap.get("valoare");
		holder.textValoare.setText(tokNewVal);
		if (isBold) {
			holder.textValoare.setTypeface(null, Typeface.BOLD);
		} else {
			holder.textValoare.setTypeface(null, Typeface.NORMAL);
		}

		tokNewVal = artMap.get("incasat");
		holder.textIncasat.setText(tokNewVal);
		if (isBold)
			holder.textIncasat.setTypeface(null, Typeface.BOLD);
		else
			holder.textIncasat.setTypeface(null, Typeface.NORMAL);

		tokNewVal = artMap.get("rest");
		holder.textRest.setText(tokNewVal);
		if (isBold)
			holder.textRest.setTypeface(null, Typeface.BOLD);
		else
			holder.textRest.setTypeface(null, Typeface.NORMAL);

		return v;

	}

}
