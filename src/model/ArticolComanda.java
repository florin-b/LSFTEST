/**
 * @author florinb
 * clasa pentru definirea articolelor din comanda
 */
package model;

import java.util.Comparator;
import java.util.Locale;

public class ArticolComanda implements Comparable<ArticolComanda> {

	protected int nrCrt;
	protected String numeArticol;
	protected String codArticol;
	protected String depozit;
	protected double cantitate;
	protected String um;
	protected double pret;
	protected String moneda;
	protected double procent;
	protected String observatii;
	protected boolean conditie;
	protected int promotie;
	protected double procentFact;
	protected double pretUnit;
	protected double discClient;
	protected String tipAlert;
	protected double procAprob;
	protected double multiplu;
	protected String infoArticol;
	protected double cantUmb;
	protected String Umb;
	protected String alteValori;
	protected String depart;
	protected String tipArt;
	protected double taxaVerde;
	protected double pretUnitarPonderat;
	protected double pretUnitarClient;
	private String unitLogAlt;
	private String status;
	private double cmp;
	private String addCond;

	private double pretUnitarGed;
	private double marjaClient;
	private double marjaCorectata;
	private double reducerePonderata;
	private double marjaGed;
	private String tipAlertPret;
	private double adaosMinimArticol;
	private double adaosClientCorectat;
	private double adaosMinimAcceptat;
	private int ponderare;

	private double discountAg;
	private double discountSd;
	private double discountDv;
	private String permitSubCmp;

	private double coefCorectie;

	private double pretMediu;
	private double adaosMediu;
	private String unitMasPretMediu;
	private String departSintetic;

	private boolean hasConditii;
	private boolean isRespins;

	protected double deficit;

	private double valTransport;
	private double procTransport;

	public int getNrCrt() {
		return nrCrt;
	}

	public String getNumeArticol() {
		return numeArticol;
	}

	public String getCodArticol() {
		return codArticol;
	}

	public String getDepozit() {
		return depozit;
	}

	public double getCantitate() {
		return cantitate;
	}

	public String getUm() {
		return um;
	}

	public double getPret() {
		return pret;
	}

	public String getMoneda() {
		return moneda;
	}

	public double getProcent() {
		return procent;
	}

	public String getObservatii() {
		return observatii;
	}

	public boolean getConditie() {
		return conditie;
	}

	public int getPromotie() {
		return promotie;
	}

	public double getProcentFact() {
		return procentFact;
	}

	public double getPretUnit() {
		return pretUnit;
	}

	public double getDiscClient() {
		return discClient;
	}

	public String getTipAlert() {
		if (tipAlert == null)
			return " ";
		return tipAlert;
	}

	public double getProcAprob() {
		return procAprob;
	}

	public double getMultiplu() {
		return multiplu;
	}

	public String getInfoArticol() {
		return infoArticol;
	}

	public double getCantUmb() {
		return cantUmb;
	}

	public String getUmb() {
		return Umb;
	}

	public String getAlteValori() {
		return alteValori;
	}

	public String getDepart() {
		return depart;
	}

	public String getTipArt() {
		return tipArt;
	}

	public void setNrCrt(int nrCrt) {
		this.nrCrt = nrCrt;
	}

	public void setNumeArticol(String numeArticol) {
		this.numeArticol = numeArticol;
	}

	public void setCodArticol(String codArticol) {
		this.codArticol = codArticol;
	}

	public void setDepozit(String depozit) {
		this.depozit = depozit;
	}

	public void setCantitate(double cantitate) {
		this.cantitate = cantitate;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public void setPret(double pret) {
		this.pret = pret;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public void setProcent(double procent) {
		this.procent = procent;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public void setConditie(boolean conditie) {
		this.conditie = conditie;
	}

	public void setPromotie(int promotie) {
		this.promotie = promotie;
	}

	public void setProcentFact(double procentFact) {
		this.procentFact = procentFact;
	}

	public void setPretUnit(double pretUnit) {
		this.pretUnit = pretUnit;
	}

	public void setDiscClient(double discClient) {
		this.discClient = discClient;
	}

	public void setTipAlert(String tipAlert) {
		this.tipAlert = tipAlert;
	}

	public void setProcAprob(double procAprob) {
		this.procAprob = procAprob;
	}

	public void setMultiplu(double multiplu) {
		this.multiplu = multiplu;
	}

	public void setInfoArticol(String infoArticol) {
		this.infoArticol = infoArticol;
	}

	public void setCantUmb(double cantUmb) {
		this.cantUmb = cantUmb;
	}

	public void setUmb(String Umb) {
		this.Umb = Umb;
	}

	public void setAlteValori(String alteValori) {
		this.alteValori = alteValori;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public void setTipArt(String tipArt) {
		this.tipArt = tipArt;
	}

	public double getTaxaVerde() {
		return taxaVerde;
	}

	public void setTaxaVerde(double taxaVerde) {
		this.taxaVerde = taxaVerde;
	}

	public double getPretUnitarPonderat() {
		return pretUnitarPonderat;
	}

	public void setPretUnitarPonderat(double pretUnitarPonderat) {
		this.pretUnitarPonderat = pretUnitarPonderat;
	}

	public double getPretUnitarClient() {
		return pretUnitarClient;
	}

	public void setPretUnitarClient(double pretUnitarClient) {
		this.pretUnitarClient = pretUnitarClient;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUnitLogAlt() {
		return unitLogAlt;
	}

	public void setUnitLogAlt(String unitLogAlt) {
		this.unitLogAlt = unitLogAlt;
	}

	public double getCmp() {
		return cmp;
	}

	public void setCmp(double cmpArt) {
		this.cmp = cmpArt;
	}

	public String getAddCond() {
		return addCond;
	}

	public void setAddCond(String addCond) {
		this.addCond = addCond;
	}

	public int compareTo(ArticolComanda compareArtCom) {

		String comparaDepart = (compareArtCom).getDepart();
		return this.depart.compareTo(comparaDepart);

	}

	public double getPretUnitarGed() {
		return pretUnitarGed;
	}

	public void setPretUnitarGed(double pretUnitarGed) {
		this.pretUnitarGed = pretUnitarGed;
	}

	public double getMarjaClient() {
		return marjaClient;
	}

	public void setMarjaClient(double marjaClient) {
		this.marjaClient = marjaClient;
	}

	public double getMarjaCorectata() {
		return marjaCorectata;
	}

	public void setMarjaCorectata(double marjaCorectata) {
		this.marjaCorectata = marjaCorectata;
	}

	public double getReducerePonderata() {
		return reducerePonderata;
	}

	public void setReducerePonderata(double reducerePonderata) {
		this.reducerePonderata = reducerePonderata;
	}

	public double getMarjaGed() {
		return marjaGed;
	}

	public void setMarjaGed(double marjaGed) {
		this.marjaGed = marjaGed;
	}

	public String getTipAlertPret() {
		return tipAlertPret;
	}

	public void setTipAlertPret(String tipAlertPret) {
		this.tipAlertPret = tipAlertPret;
	}

	public double getAdaosMinimArticol() {
		return adaosMinimArticol;
	}

	public void setAdaosMinimArticol(double adaosMinimArticol) {
		this.adaosMinimArticol = adaosMinimArticol;
	}

	public double getAdaosClientCorectat() {
		return adaosClientCorectat;
	}

	public void setAdaosClientCorectat(double adaosClientCorectat) {
		this.adaosClientCorectat = adaosClientCorectat;
	}

	public double getAdaosMinimAcceptat() {
		return adaosMinimAcceptat;
	}

	public void setAdaosMinimAcceptat(double adaosMinimAcceptat) {
		this.adaosMinimAcceptat = adaosMinimAcceptat;
	}

	public int getPonderare() {
		return ponderare;
	}

	public void setPonderare(int ponderare) {
		this.ponderare = ponderare;
	}

	public double getDiscountAg() {
		return discountAg;
	}

	public void setDiscountAg(double discountAg) {
		this.discountAg = discountAg;
	}

	public double getDiscountSd() {
		return discountSd;
	}

	public void setDiscountSd(double discountSd) {
		this.discountSd = discountSd;
	}

	public double getDiscountDv() {
		return discountDv;
	}

	public void setDiscountDv(double discountDv) {
		this.discountDv = discountDv;
	}

	public String getPermitSubCmp() {
		return permitSubCmp;
	}

	public void setPermitSubCmp(String permitSubCmp) {
		this.permitSubCmp = permitSubCmp;
	}

	public double getCoefCorectie() {
		return coefCorectie;
	}

	public double getPretMediu() {
		return pretMediu;
	}

	public void setPretMediu(double pretMediu) {
		this.pretMediu = pretMediu;
	}

	public double getAdaosMediu() {
		return adaosMediu;
	}

	public void setAdaosMediu(double adaosMediu) {
		this.adaosMediu = adaosMediu;
	}

	public String getUnitMasPretMediu() {
		return unitMasPretMediu;
	}

	public void setUnitMasPretMediu(String unitMasPretMediu) {
		this.unitMasPretMediu = unitMasPretMediu;
	}

	public void setCoefCorectie(double coefCorectie) {
		this.coefCorectie = coefCorectie;
	}

	public String getDepartSintetic() {
		return departSintetic;
	}

	public void setDepartSintetic(String departSintetic) {
		this.departSintetic = departSintetic;
	}

	public boolean hasConditii() {
		return hasConditii;
	}

	public void setConditii(boolean isConditieAcceptata) {
		this.hasConditii = isConditieAcceptata;
	}

	public boolean isRespins() {
		return isRespins;
	}

	public void setRespins(boolean isRespins) {
		this.isRespins = isRespins;
	}

	public double getDeficit() {
		return deficit;
	}

	public void setDeficit(double deficit) {
		this.deficit = deficit;
	}

	public double getValTransport() {
		return valTransport;
	}

	public void setValTransport(double valTransport) {
		this.valTransport = valTransport;
	}

	public double getProcTransport() {
		return procTransport;
	}

	public void setProcTransport(double procTransport) {
		this.procTransport = procTransport;
	}

	@Override
	public String toString() {
		return "ArticolComanda [numeArticol=" + numeArticol + ", codArticol=" + codArticol + ", ponderare=" + ponderare + "]";
	}

	public static Comparator<ArticolComanda> DepartComparator = new Comparator<ArticolComanda>() {

		public int compare(ArticolComanda depart1, ArticolComanda depart2) {

			String departName1 = "";
			String departName2 = "";

			if (depart1 != null)
				departName1 = depart1.getDepart().toUpperCase(Locale.getDefault());

			if (depart2 != null)
				departName2 = depart2.getDepart().toUpperCase(Locale.getDefault());

			// descrescator
			return departName2.compareTo(departName1);

			// crescator
			// return departName1.compareTo(departName2);

		}

	};

}
