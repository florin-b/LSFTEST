package dialogs;

import java.util.ArrayList;
import java.util.List;

import listeners.MapListener;
import lite.sfa.test.R;
import main.ZoneBucuresti;
import utils.MapUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import beans.Address;
import beans.GeocodeAddress;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import enums.EnumZona;

public class MapAddressDialog extends Dialog {

	private Button btnCloseDialog;
	private GoogleMap map;
	private Context context;
	private FragmentManager fm;
	private Address address;
	private MapListener mapListener;
	private TextView textLabel;
	private LatLng coords;

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

	}

	@Override
	public void show() {
		setupLayout();
		super.show();

	}

	private void setupLayout() {

		try {

			textLabel = (TextView) findViewById(R.id.textLabel);

			int detailLevel = DETAIL_MEDIUM;

			if (address.getStreet() != null && address.getStreet().trim().length() > 1)
				detailLevel = DETAIL_HIGH;

			GeocodeAddress geoAddress = MapUtils.geocodeAddress(address, context);

			LatLng coord = geoAddress.getCoordinates();

			if (coord.latitude == 0) {
				textLabel.setText("Adresa inexistenta.");
				removeMap();
			} else {

				map = ((MapFragment) fm.findFragmentById(R.id.map)).getMap();
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				map.getUiSettings().setZoomControlsEnabled(true);
				addMapMarker(map);

				addZoneBucuresti(map);
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
				map.addMarker(markerOptions);
				addZoneBucuresti(map);
				map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

				android.location.Address locAddress = MapUtils.getAddressFromCoordinate(latLng, context);

				if (mapListener != null)
					mapListener.addressSelected(latLng, locAddress);

			}
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		removeMap();

	}

	private List<LatLng> getGoogleCoords(List<beans.LatLng> brutCoords) {
		List<LatLng> listCoords = new ArrayList<LatLng>();

		for (beans.LatLng coord : brutCoords) {
			LatLng oneCoord = new LatLng(coord.getLat(), coord.getLng());
			listCoords.add(oneCoord);

		}

		return listCoords;

	}

	public void setCoords(LatLng coords) {
		this.coords = coords;
	}

	private void addMapMarker(GoogleMap map) {
		if (coords != null && coords.latitude > 0) {
			MarkerOptions marker = new MarkerOptions();

			marker.position(coords);
			map.clear();
			map.addMarker(marker);
		}

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

	public void addZoneBucuresti(GoogleMap map) {

		if (!address.getCity().equalsIgnoreCase("bucuresti"))
			return;

		List<beans.LatLng> zoneA = ZoneBucuresti.getZona(EnumZona.ZONA_A);

		PolygonOptions polyOptionsA = new PolygonOptions();
		polyOptionsA.addAll(getGoogleCoords(zoneA));
		polyOptionsA.strokeColor(Color.RED);
		polyOptionsA.strokeWidth(3);

		List<beans.LatLng> zoneB1 = ZoneBucuresti.getZona(EnumZona.ZONA_B1);
		PolygonOptions polyOptionsB1 = new PolygonOptions();
		polyOptionsB1.addAll(getGoogleCoords(zoneB1));
		polyOptionsB1.strokeColor(Color.BLACK);
		polyOptionsB1.strokeWidth(3);

		List<beans.LatLng> zoneB2 = ZoneBucuresti.getZona(EnumZona.ZONA_B2);
		PolygonOptions polyOptionsB2 = new PolygonOptions();
		polyOptionsB2.addAll(getGoogleCoords(zoneB2));
		polyOptionsB2.strokeColor(Color.BLACK);
		polyOptionsB2.strokeWidth(3);

		map.addPolygon(polyOptionsA);
		map.addPolygon(polyOptionsB1);
		map.addPolygon(polyOptionsB2);
	}

}
