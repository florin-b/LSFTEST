package beans;

import com.google.android.gms.maps.model.LatLng;

public class GeocodeAddress {

	private LatLng coordinates;
	private boolean geoStatus;

	public LatLng getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}

	public boolean isAdresaValida() {
		return geoStatus;
	}

	public void setGeoStatus(boolean geoStatus) {
		this.geoStatus = geoStatus;
	}

}
