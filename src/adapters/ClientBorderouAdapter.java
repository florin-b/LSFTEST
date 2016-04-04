package adapters;

import java.util.List;

import lite.sfa.test.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import beans.BeanClientBorderou;
import beans.BeanComandaDeschisa;

public class ClientBorderouAdapter extends BaseAdapter {

	private Context context;
	private List<BeanClientBorderou> listClienti;
	private BeanComandaDeschisa comandaCurenta;

	public ClientBorderouAdapter(Context context, List<BeanClientBorderou> listClienti) {
		this.context = context;
		this.listClienti = listClienti;
	}

	public void setComandaCurenta(BeanComandaDeschisa comandaCurenta) {
		this.comandaCurenta = comandaCurenta;
	}

	static class ViewHolder {
		public TextView textPozitie, textNumeClient, textStareLivrare;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.client_borderou, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textPozitie = (TextView) convertView.findViewById(R.id.textPozitie);
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textStareLivrare = (TextView) convertView.findViewById(R.id.textStareLivrare);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanClientBorderou comanda = getItem(position);

		viewHolder.textPozitie.setText(String.valueOf(comanda.getPozitie()) + ".");
		if (comandaCurenta.getCodClient().equals(comanda.getCodClient()))
			viewHolder.textNumeClient.setText(comandaCurenta.getNumeClient());
		else
			viewHolder.textNumeClient.setText("Client");

		viewHolder.textStareLivrare.setText(comanda.getStareLivrare());

		if (position % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;

	}

	@Override
	public int getCount() {
		return listClienti.size();
	}

	@Override
	public BeanClientBorderou getItem(int position) {
		return listClienti.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
