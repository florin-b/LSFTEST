package adapters;

import java.util.List;

import lite.sfa.test.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import beans.BeanDocumentRetur;

public class DocumenteReturAdapter extends BaseAdapter {

	Context context;
	List<BeanDocumentRetur> listDocumente;
	int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public DocumenteReturAdapter(Context context, List<BeanDocumentRetur> listDocumente) {
		this.context = context;
		this.listDocumente = listDocumente;
	}

	static class ViewHolder {
		TextView textNumarDocument, textDataDocument;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.document_retur_item, parent, false);
			viewHolder.textNumarDocument = (TextView) convertView.findViewById(R.id.textNumarDocument);
			viewHolder.textDataDocument = (TextView) convertView.findViewById(R.id.textDataDocument);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanDocumentRetur docRetur = getItem(position);

		viewHolder.textNumarDocument.setText(docRetur.getNumar());
		viewHolder.textDataDocument.setText(docRetur.getData());

		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	@Override
	public int getCount() {
		return listDocumente.size();
	}

	@Override
	public BeanDocumentRetur getItem(int position) {
		return listDocumente.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
