package beans;

public class BeanAdresaLivrare {

	private String oras;
	private String strada;
	private String nrStrada;
	private String codJudet;
	private String codAdresa;
	private String tonaj;
	private String coords;

	public BeanAdresaLivrare() {

	}

	public String getOras() {
		return oras;
	}

	public void setOras(String oras) {
		this.oras = oras;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public String getNrStrada() {
		return nrStrada;
	}

	public void setNrStrada(String nrStrada) {
		this.nrStrada = nrStrada;
	}

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public String getTonaj() {
		return tonaj;
	}

	public void setTonaj(String tonaj) {
		this.tonaj = tonaj;
	}

	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return "BeanAdresaLivrare [oras=" + oras + ", strada=" + strada + ", nrStrada=" + nrStrada + ", codJudet=" + codJudet + ", codAdresa=" + codAdresa
				+ ", tonaj=" + tonaj + ", coords=" + coords + "]";
	}

}
