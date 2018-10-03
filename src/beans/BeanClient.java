package beans;

public class BeanClient {

	private String numeClient;
	private String codClient;
	private String tipClient;
	private String agenti;

	public BeanClient() {

	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getTipClient() {
		return tipClient;
	}

	public void setTipClient(String tipClient) {
		this.tipClient = tipClient;
	}

	@Override
	public String toString() {
		return numeClient;
	}

	public String getAgenti() {
		return agenti;
	}

	public void setAgenti(String agenti) {
		this.agenti = agenti;
	}



}
