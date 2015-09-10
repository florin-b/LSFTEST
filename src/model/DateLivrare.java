/**
 * @author florinb
 *
 */
package model;

import beans.DateLivrareAfisare;

public class DateLivrare {

	private static DateLivrare instance = new DateLivrare();

	private DateLivrare() {
	}

	public static DateLivrare getInstance() {
		return instance;
	}

	private String codJudet = "";
	private String numeJudet = "";
	private String Oras = "";
	private String Strada = "";
	private String persContact = "";
	private String nrTel = "";
	private String redSeparat = "R";
	private String Cantar = "";
	private String tipPlata = "";
	private String Transport = "";
	private String dateLivrare = "";
	private String termenPlata = "";
	private String obsLivrare = "";
	private Integer dataLivrare = 0;
	private boolean adrLivrNoua = false;
	private String tipDocInsotitor = "1"; // 1 = factura, 2 = aviz
	private String obsPlata = " ";
	private String addrNumber = " ";
	private String valoareIncasare = "0";
	private boolean isValIncModif = false;
	private String mail = " ";
	private String totalComanda;
	private String unitLog;
	private String codAgent;
	private String factRed;
	private String tipPersClient;
	private double valTransport;
	private double valTransportSAP;
	private String adresaD;
	private String orasD;
	private String codJudetD;
	private boolean masinaMacara;
	private boolean altaAdresa;
	private String numeClient;
	private String cnpClient;
	private String idObiectiv = " ";
	private boolean isAdresaObiectiv;

	private DateLivrareAfisare dateLivrareAfisare;

	public DateLivrareAfisare getDateLivrareAfisare() {
		return dateLivrareAfisare;
	}

	public void setDateLivrareAfisare(DateLivrareAfisare dateLivrareAfisare) {
		this.codJudet = dateLivrareAfisare.getCodJudet();
		this.numeJudet = dateLivrareAfisare.getNumeJudet();
		this.Oras = dateLivrareAfisare.getOras();
		this.Strada = dateLivrareAfisare.getDateLivrare();
		this.persContact = dateLivrareAfisare.getPersContact();
		this.nrTel = dateLivrareAfisare.getNrTel();
		this.redSeparat = dateLivrareAfisare.getRedSeparat();
		this.Cantar = dateLivrareAfisare.getCantar();
		this.tipPlata = dateLivrareAfisare.getTipPlata();
		this.Transport = dateLivrareAfisare.getTransport();
		this.dateLivrare = dateLivrareAfisare.getDateLivrare();
		this.termenPlata = dateLivrareAfisare.getTermenPlata();
		this.obsLivrare = dateLivrareAfisare.getObsLivrare();
		this.dataLivrare = dateLivrareAfisare.getDataLivrare();
		this.adrLivrNoua = dateLivrareAfisare.isAdrLivrNoua();
		this.tipDocInsotitor = dateLivrareAfisare.getTipDocInsotitor();
		this.obsPlata = dateLivrareAfisare.getObsPlata();
		this.addrNumber = dateLivrareAfisare.getAddrNumber();
		this.valoareIncasare = dateLivrareAfisare.getValoareIncasare();
		this.isValIncModif = dateLivrareAfisare.isValIncModif();
		this.mail = dateLivrareAfisare.getMail();
		this.totalComanda = dateLivrareAfisare.getTotalComanda();
		this.unitLog = dateLivrareAfisare.getUnitLog();
		this.codAgent = dateLivrareAfisare.getCodAgent();
		this.factRed = dateLivrareAfisare.getFactRed();
		this.redSeparat = dateLivrareAfisare.getFactRed();
		this.tipPersClient = dateLivrareAfisare.getTipPersClient();
		this.valTransport = dateLivrareAfisare.getValTransport();
		this.valTransportSAP = dateLivrareAfisare.getValTransportSAP();
		this.adresaD = dateLivrareAfisare.getAdresaD();
		this.orasD = dateLivrareAfisare.getOrasD();
		this.codJudetD = dateLivrareAfisare.getCodJudetD();
		this.masinaMacara = dateLivrareAfisare.getMacara().equalsIgnoreCase("X") ? true : false;

		this.numeClient = dateLivrareAfisare.getNumeClient();
		this.cnpClient = dateLivrareAfisare.getCnpClient();
		this.idObiectiv = dateLivrareAfisare.getIdObiectiv();
		this.isAdresaObiectiv = dateLivrareAfisare.isAdresaObiectiv();

	}

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getNumeJudet() {
		return numeJudet;
	}

	public void setNumeJudet(String numeJudet) {
		this.numeJudet = numeJudet;
	}

	public String getOras() {
		return Oras;
	}

	public void setOras(String oras) {
		Oras = oras;
	}

	public String getStrada() {
		return Strada;
	}

	public void setStrada(String strada) {
		Strada = strada;
	}

	public String getPersContact() {
		return persContact;
	}

	public void setPersContact(String persContact) {
		this.persContact = persContact;
	}

	public String getNrTel() {
		return nrTel;
	}

	public void setNrTel(String nrTel) {
		this.nrTel = nrTel;
	}

	public String getRedSeparat() {
		return redSeparat;
	}

	public void setRedSeparat(String redSeparat) {
		this.redSeparat = redSeparat;
	}

	public String getCantar() {
		return Cantar;
	}

	public void setCantar(String cantar) {
		Cantar = cantar;
	}

	public String getTipPlata() {
		return tipPlata;
	}

	public void setTipPlata(String tipPlata) {
		this.tipPlata = tipPlata;
	}

	public String getTransport() {
		return Transport;
	}

	public void setTransport(String transport) {
		Transport = transport;
	}

	public String getDateLivrare() {
		return dateLivrare;
	}

	public void setDateLivrare(String dateLivrare) {
		this.dateLivrare = dateLivrare;
	}

	public String getTermenPlata() {
		return termenPlata;
	}

	public void setTermenPlata(String termenPlata) {
		this.termenPlata = termenPlata;
	}

	public String getObsLivrare() {
		return obsLivrare;
	}

	public void setObsLivrare(String obsLivrare) {
		this.obsLivrare = obsLivrare;
	}

	public Integer getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(Integer dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public boolean isAdrLivrNoua() {
		return adrLivrNoua;
	}

	public void setAdrLivrNoua(boolean adrLivrNoua) {
		this.adrLivrNoua = adrLivrNoua;
	}

	public String getTipDocInsotitor() {
		return tipDocInsotitor;
	}

	public void setTipDocInsotitor(String tipDocInsotitor) {
		this.tipDocInsotitor = tipDocInsotitor;
	}

	public String getObsPlata() {
		return obsPlata;
	}

	public void setObsPlata(String obsPlata) {
		this.obsPlata = obsPlata;
	}

	public String getAddrNumber() {
		return addrNumber;
	}

	public void setAddrNumber(String addrNumber) {
		this.addrNumber = addrNumber;
	}

	public String getValoareIncasare() {
		return valoareIncasare;
	}

	public void setValoareIncasare(String valoareIncasare) {
		this.valoareIncasare = valoareIncasare;
	}

	public boolean isValIncModif() {
		return isValIncModif;
	}

	public void setValIncModif(boolean isValIncModif) {
		this.isValIncModif = isValIncModif;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTotalComanda() {
		if (totalComanda == null)
			return "0";
		return totalComanda;
	}

	public void setTotalComanda(String totalComanda) {
		this.totalComanda = totalComanda;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public String getFactRed() {
		return factRed;
	}

	public void setFactRed(String factRed) {
		this.factRed = factRed;
	}

	public double getValTransport() {
		return valTransport;
	}

	public void setValTransport(double valTransport) {
		this.valTransport = valTransport;
	}

	public double getValTransportSAP() {
		return valTransportSAP;
	}

	public void setValTransportSAP(double valTransportSAP) {
		this.valTransportSAP = valTransportSAP;
	}

	public String getTipPersClient() {
		return tipPersClient;
	}

	public void setTipPersClient(String tipPersClient) {
		this.tipPersClient = tipPersClient;
	}

	public String getAdresaD() {
		if (adresaD == null)
			return " ";
		return adresaD;
	}

	public void setAdresaD(String adresaD) {
		this.adresaD = adresaD;
	}

	public String getOrasD() {
		if (orasD == null)
			return " ";
		return orasD;
	}

	public void setOrasD(String orasD) {
		this.orasD = orasD;
	}

	public String getCodJudetD() {

		if (codJudetD == null)
			return " ";
		return codJudetD;
	}

	public void setCodJudetD(String codJudetD) {
		this.codJudetD = codJudetD;
	}

	public boolean isMasinaMacara() {
		return masinaMacara;
	}

	public void setMasinaMacara(boolean masinaMacara) {
		this.masinaMacara = masinaMacara;
	}

	public boolean isAltaAdresa() {
		return altaAdresa;
	}

	public void setAltaAdresa(boolean altaAdresa) {
		this.altaAdresa = altaAdresa;
	}

	public String getNumeClient() {
		if (numeClient == null)
			return " ";
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCnpClient() {
		if (cnpClient == null)
			return " ";
		return cnpClient;
	}

	public void setCnpClient(String cnpClient) {
		this.cnpClient = cnpClient;
	}

	public String getIdObiectiv() {
		return idObiectiv;
	}

	public void setIdObiectiv(String idObiectiv) {
		this.idObiectiv = idObiectiv;
	}

	public boolean isAdresaObiectiv() {
		return isAdresaObiectiv;
	}

	public void setAdresaObiectiv(boolean isAdresaObiectiv) {
		this.isAdresaObiectiv = isAdresaObiectiv;
	}

	public void resetAll() {
		codJudet = "";
		numeJudet = "";
		Oras = "";
		Strada = "";
		persContact = "";
		nrTel = "";
		redSeparat = "R";
		Cantar = "";
		tipPlata = "";
		Transport = "";
		dateLivrare = "";
		termenPlata = "";
		obsLivrare = "";
		dataLivrare = 0;
		adrLivrNoua = false;
		tipDocInsotitor = "1";
		obsPlata = " ";
		addrNumber = " ";
		valoareIncasare = "0";
		isValIncModif = false;
		mail = " ";
		tipPersClient = "";
		valTransport = 0;
		valTransportSAP = 0;
		masinaMacara = false;
		altaAdresa = false;
		adresaD = " ";
		orasD = " ";
		codJudetD = " ";
		idObiectiv = " ";
		isAdresaObiectiv = false;

	}

}