package dialogs;

import listeners.MapListener;
import lite.sfa.test.R;
import utils.MapUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import beans.Address;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapAddressDialog extends Dialog {

	private Button btnCloseDialog;
	private GoogleMap map;
	private Context context;
	private FragmentManager fm;
	private Address address;
	private MapListener mapListener;
	private TextView textLabel;

	private static final int DETAIL_MEDIUM = 15;
	private static final int DETAIL_HIGH = 18;

	public MapAddressDialog(Address address, Context context, FragmentManager fm) {
		super(context);
		this.context = context;
		this.address = address;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.adresa_harta);
		setTitle("Pozitionare adresa");
		setCancelable(true);
		this.fm = fm;
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		setupLayout();
	}

	private void setupLayout() {

		try {

			textLabel = (TextView) findViewById(R.id.textLabel);

			int detailLevel = DETAIL_MEDIUM;

			if (address.getStreet() != null && address.getStreet().trim().length() > 1)
				detailLevel = DETAIL_HIGH;

			LatLng coord = null;
			coord = MapUtils.geocodeAddress(address, context);

			
			if (coord.latitude == 0) {
				textLabel.setText("Adresa inexistenta.");
				removeMap();
			} else {
			
				map = ((MapFragment) fm.findFragmentById(R.id.map)).getMap();
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				map.getUiSettings().setZoomControlsEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, detailLevel));
				setMapListener();
			}

		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		btnCloseDialog = (Button) findViewById(R.id.btnCloseDialog);
		setListenerCloseDialog();

	}

	private void setMapListener() {
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				map.clear();
				map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				map.addMarker(markerOptions);

				if (mapListener != null)
					mapListener.addressSelected(latLng);

			}
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		removeMap();

	}

	private void removeMap() {
		MapFragment f = (MapFragment) fm.findFragmentById(R.id.map);
		if (f != null)
			fm.beginTransaction().remove(f).commit();

	}

	private void setListenerCloseDialog() {
		btnCloseDialog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setMapListener(MapListener listener) {
		this.mapListener = listener;
	}

}
