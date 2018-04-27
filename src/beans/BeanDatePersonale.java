package beans;

public class BeanDatePersonale {

	private String cnp;
	private String nume;
	private String codjudet;
	private String localitate;
	private String strada;

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCodjudet() {
		return codjudet;
	}

	public void setCodjudet(String codjudet) {
		this.codjudet = codjudet;
	}

	public String getLocalitate() {
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	@Override
	public String toString() {
		return "BeanDatePersonale [cnp=" + cnp + ", nume=" + nume + ", codjudet=" + codjudet + ", localitate=" + localitate + ", strada=" + strada + "]";
	}

}
