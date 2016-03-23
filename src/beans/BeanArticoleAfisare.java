package beans;

import java.util.ArrayList;

import model.ArticolComanda;

public class BeanArticoleAfisare {

	private DateLivrareAfisare dateLivrare;
	private ArrayList<ArticolComanda> listArticole;
	private ArrayList<ArticolSimulat> articoleSimulate;
	private BeanConditii conditii;

	public BeanArticoleAfisare() {

	}

	public DateLivrareAfisare getDateLivrare() {
		return dateLivrare;
	}

	public void setDateLivrare(DateLivrareAfisare dateLivrare) {
		this.dateLivrare = dateLivrare;
	}

	public ArrayList<ArticolComanda> getListArticole() {
		return listArticole;
	}

	public void setListArticole(ArrayList<ArticolComanda> listArticole) {
		this.listArticole = listArticole;
	}

	public BeanConditii getConditii() {
		return conditii;
	}

	public void setConditii(BeanConditii conditii) {
		this.conditii = conditii;
	}

	public ArrayList<ArticolSimulat> getArticoleSimulate() {
		return articoleSimulate;
	}

	public void setArticoleSimulate(ArrayList<ArticolSimulat> articoleSimulate) {
		this.articoleSimulate = articoleSimulate;
	}
	
	

}
