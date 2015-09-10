package adapters;

import lite.sfa.test.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import enums.EnumStadiuSubantrep;

public class AdapterStadiuSubantrepObiectiv extends BaseAdapter {

	private Context context;

	public static class ViewHolder {
		TextView textStatus;
		ImageView imageStatus;
	}

	public AdapterStadiuSubantrepObiectiv(Context context) {
		this.context = context;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_stadiu, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textStatus = (TextView) convertView.findViewById(R.id.textStatus);
			viewHolder.imageStatus = (ImageView) convertView.findViewById(R.id.imageStatus);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textStatus.setText(EnumStadiuSubantrep.getNumeStadiu(position));

		switch (position) {
		case 0:
			viewHolder.imageStatus.setBackgroundResource(R.drawable.red_button_32);
			break;
		case 1:
			viewHolder.imageStatus.setBackgroundResource(R.drawable.green_button_32);
			break;
		}

		return convertView;
	}

	public int getCount() {
		return EnumStadiuSubantrep.values().length;

	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

}