package beans;

public class BeanFurnizorProduse {

	private String numeFurnizorProduse;
	private String codFurnizorProduse;

	public BeanFurnizorProduse() {

	}

	public BeanFurnizorProduse(String numeFurnizorProduse, String codFurnizorProduse) {
		super();
		this.numeFurnizorProduse = numeFurnizorProduse;
		this.codFurnizorProduse = codFurnizorProduse;
	}

	public String getNumeFurnizorProduse() {
		return numeFurnizorProduse;
	}

	public void setNumeFurnizorProduse(String numeFurnizorProduse) {
		this.numeFurnizorProduse = numeFurnizorProduse;
	}

	public String getCodFurnizorProduse() {
		return codFurnizorProduse;
	}

	public void setCodFurnizorProduse(String codFurnizorProduse) {
		this.codFurnizorProduse = codFurnizorProduse;
	}

}
