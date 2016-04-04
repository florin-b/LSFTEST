package beans;

import model.ArticolComanda;

public class ArticolSimulat extends ArticolComanda {

	private boolean isStocOK;
	private boolean isExceptie = true;

	public boolean isStocOK() {
		return isStocOK;
	}

	public void setStocOK(boolean isStocOK) {
		this.isStocOK = isStocOK;
	}

	public boolean isExceptie() {
		return isExceptie;
	}

	public void setExceptie(boolean isExceptie) {
		this.isExceptie = isExceptie;
	}

}
