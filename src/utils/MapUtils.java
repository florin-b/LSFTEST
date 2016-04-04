package utils;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Geocoder;
import beans.Address;

import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

	public static LatLng geocodeAddress(Address address, Context context) throws Exception {

		LatLng coords = null;

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreet() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getStreet());
			strAddress.append(",");
		}

		if (address.getNumber() != null && !address.getNumber().equals("")) {
			strAddress.append(address.getNumber());
			strAddress.append(",");
		}

		if (address.getSector() != null && !address.getSector().equals("")) {
			strAddress.append(address.getSector());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().equals("")) {
			strAddress.append(address.getCity());
			strAddress.append(",");
		}

		strAddress.append(address.getCountry());

		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		List<android.location.Address> addresses = geoCoder.getFromLocationName(strAddress.toString(), 1);

		if (addresses.size() > 0) {

			latitude = addresses.get(0).getLatitude();
			longitude = addresses.get(0).getLongitude();

		}

		coords = new LatLng(latitude, longitude);

		return coords;
	}

}
