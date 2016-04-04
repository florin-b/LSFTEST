package adapters;

import java.util.List;

import lite.sfa.test.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import beans.BeanArticolConcurenta;

public class AdapterArticolConcurenta extends BaseAdapter {

	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };
	Context context;
	List<BeanArticolConcurenta> listArticole;

	static class ViewHolder {
		public TextView textNumeArticol, textCodArticol, textTipArt, textData, textValoare;
	}

	public AdapterArticolConcurenta(Context context, List<BeanArticolConcurenta> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		String strValoare = "";
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_concurenta, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
			viewHolder.textCodArticol = (TextView) convertView.findViewById(R.id.textCodArticol);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textValoare = (TextView) convertView.findViewById(R.id.textValoare);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanArticolConcurenta articol = getItem(position);
		viewHolder.textNumeArticol.setText(articol.getNume());
		viewHolder.textCodArticol.setText(articol.getCod());
		viewHolder.textData.setText(articol.getDataValoare());

		if (articol.getValoare().trim().length() > 0)
			strValoare = articol.getValoare() + " RON/" + articol.getUmVanz();
		else
			strValoare = "";
		viewHolder.textValoare.setText(strValoare);

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public BeanArticolConcurenta getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
