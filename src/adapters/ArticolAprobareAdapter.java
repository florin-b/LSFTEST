package adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import lite.sfa.test.R;
import model.ArticolComanda;
import model.UserInfo;
import utils.UtilsGeneral;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.ValoriComanda;

public class ArticolAprobareAdapter extends BaseAdapter {

	Context context;
	List<ArticolComanda> listArticole;
	int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };
	double procentCmp, multiplu;

	private double valoareCmp;

	private NumberFormat nf3;
	private NumberFormat nf2;
	private NumberFormat nf4;

	public ArticolAprobareAdapter(Context context, List<ArticolComanda> listArticole) {
		this.context = context;
		this.listArticole = listArticole;

		nf3 = new DecimalFormat("#0.000");
		nf2 = new DecimalFormat("#0.00");
		nf4 = new DecimalFormat("#0.0000");

	}

	public static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textUmArt, textPretArt, textMonedaArt, textDepozit, textStatusArt, textProcRed, textAddCond,
				textCmp, textProcCmp, textDisClient, textProcAprob, textMultipAprob, textInfoArticol, textPretSpecial, textIstoricPret;

		LinearLayout layoutIstoricPret;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % colors.length;
		String unitPret = "RON";

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.customrowaprobarecmd, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textUmArt = (TextView) convertView.findViewById(R.id.textUmArt);
			viewHolder.textPretArt = (TextView) convertView.findViewById(R.id.textPretArt);
			viewHolder.textMonedaArt = (TextView) convertView.findViewById(R.id.textMonedaArt);
			viewHolder.textDepozit = (TextView) convertView.findViewById(R.id.textDepozit);
			viewHolder.textStatusArt = (TextView) convertView.findViewById(R.id.textStatusArt);
			viewHolder.textProcRed = (TextView) convertView.findViewById(R.id.textProcRed);
			viewHolder.textAddCond = (TextView) convertView.findViewById(R.id.textAddCond);
			viewHolder.textCmp = (TextView) convertView.findViewById(R.id.textCmp);
			viewHolder.textProcCmp = (TextView) convertView.findViewById(R.id.textProcCmp);
			viewHolder.textDisClient = (TextView) convertView.findViewById(R.id.textDisClient);
			viewHolder.textProcAprob = (TextView) convertView.findViewById(R.id.textProcAprob);
			viewHolder.textMultipAprob = (TextView) convertView.findViewById(R.id.textMultipAprob);
			viewHolder.textInfoArticol = (TextView) convertView.findViewById(R.id.textInfoArticol);
			viewHolder.textPretSpecial = (TextView) convertView.findViewById(R.id.textPretSpecial);
			viewHolder.textIstoricPret = (TextView) convertView.findViewById(R.id.textIstoricPret);
			viewHolder.layoutIstoricPret = (LinearLayout) convertView.findViewById(R.id.layoutIstoricPret);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolComanda articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeArt.setText(articol.getNumeArticol());
		viewHolder.textCodArt.setText(articol.getCodArticol());
		viewHolder.textCantArt.setText(nf3.format(articol.getCantitate()));

		if (!articol.getUmb().equals(articol.getUm())) {
			unitPret = "RON/" + System.getProperty("line.separator") + articol.getUmb();
		}

		viewHolder.textUmArt.setText(articol.getUm());

		if (articol.getDepart().equals("02"))
			viewHolder.textPretArt.setText(nf4.format(articol.getPretUnit()));
		else
			viewHolder.textPretArt.setText(nf2.format(articol.getPretUnit()));

		viewHolder.textMonedaArt.setText(unitPret);
		viewHolder.textDepozit.setText(articol.getDepozit());
		viewHolder.textStatusArt.setText(UtilsGeneral.getDescStatusArt(articol.getStatus()));
		viewHolder.textProcRed.setText(nf2.format(articol.getProcent()));
		viewHolder.textProcAprob.setText(nf2.format(articol.getProcAprob()));

		viewHolder.textAddCond.setText(articol.getAddCond());
		viewHolder.textCmp.setText(nf4.format(articol.getCmp() > 0 ? articol.getCmp() : 0));

		multiplu = articol.getMultiplu() == 0 ? 1 : articol.getMultiplu();
		multiplu = 1;

		valoareCmp = articol.getCmp();

		if (isDV()) {
			if (valoareCmp > 0) {
				if (UserInfo.getInstance().getCodDepart().equals("07")) {
					procentCmp = (articol.getPretUnit() / (valoareCmp * multiplu) - 1) * 100;
				} else
					procentCmp = (1 - (valoareCmp * multiplu) / articol.getPretUnit()) * 100;
			} else
				procentCmp = 0.0;
		} else {
			valoareCmp = 0.0;
			procentCmp = 0.0;
		}

		viewHolder.textProcCmp.setText(nf2.format(procentCmp));
		viewHolder.textDisClient.setText(nf2.format(articol.getDiscClient()) + "%");
		viewHolder.textMultipAprob.setText(nf2.format(articol.getMultiplu()));
		viewHolder.textInfoArticol.setText(articol.getInfoArticol());

		if (articol.getIstoricPret() != null && !articol.getIstoricPret().trim().isEmpty()) {
			viewHolder.layoutIstoricPret.setVisibility(View.VISIBLE);
			viewHolder.textIstoricPret.setText(articol.getIstoricPret());
		} else
			viewHolder.layoutIstoricPret.setVisibility(View.GONE);

		if (isPretSpecial(articol.getInfoArticol()))
			viewHolder.textPretSpecial.setText("(*)");

		if (colorPos % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	private boolean isDV() {
		return UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14");
	}

	private boolean isPretSpecial(String infoArticol) {
		return infoArticol.contains("Disc cant cumulata") || infoArticol.contains("Pret promotional") || infoArticol.contains("Discount treapta 3")
				|| infoArticol.contains("Promotie cantitativa") || infoArticol.contains("Discount multiplu");
	}

	public ValoriComanda getValoriComanda(List<ArticolComanda> articol) {

		double marja = 0;
		ValoriComanda valoriComanda = new ValoriComanda();

		for (ArticolComanda art : articol) {

			if (art.getTipArt().equals("B")) {
				valoriComanda.setPondereB(art.getPret() + valoriComanda.getPondereB());
			}

			valoriComanda.setTotal(art.getPret() + valoriComanda.getTotal());

			if (art.getCmp() > 0) {
				marja = (art.getPretUnit() - art.getCmp()) * Double.valueOf(art.getCantUmb());
				valoriComanda.setMarja(marja + valoriComanda.getMarja());
			}
		}

		return valoriComanda;
	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolComanda getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
