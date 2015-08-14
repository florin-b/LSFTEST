/**
 * @author florinb
 *
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInfo {

	private String nume = "";
	private String filiala = "";
	private String cod = "";
	private String numeDepart = "";
	private String codDepart = "";
	private String unitLog = "";
	private String initUnitLog = "";
	private String tipAcces = "";
	private String parentScreen = "";
	private String filialeDV = "";
	private boolean altaFiliala = false;
	private String tipUser = "";
	private String departExtra;
	private String tipUserSap = "";
	private List<String> extraFiliale;

	private double comisionCV = 0;
	private String coefCorectie = "";
	private String userSite = "";

	private static UserInfo instance = new UserInfo();

	private UserInfo() {
	}

	public static UserInfo getInstance() {
		return instance;
	}

	public String getNume() {
		return this.nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNumeDepart() {
		return numeDepart;
	}

	public void setNumeDepart(String numeDepart) {
		this.numeDepart = numeDepart;
	}

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getTipAcces() {
		return tipAcces;
	}

	public void setTipAcces(String tipAcces) {
		this.tipAcces = tipAcces;
	}

	public String getParentScreen() {
		return parentScreen;
	}

	public void setParentScreen(String parentScreen) {
		this.parentScreen = parentScreen;
	}

	public String getFilialeDV() {
		return filialeDV;
	}

	public void setFilialeDV(String filialeDV) {
		this.filialeDV = filialeDV;
	}

	public boolean isAltaFiliala() {
		return altaFiliala;
	}

	public void setAltaFiliala(boolean altaFiliala) {
		this.altaFiliala = altaFiliala;
	}

	public double getComisionCV() {
		return comisionCV;
	}

	public void setComisionCV(double comisionCV) {
		this.comisionCV = comisionCV;
	}

	public String getCoefCorectie() {
		return coefCorectie;
	}

	public void setCoefCorectie(String coefCorectie) {
		this.coefCorectie = coefCorectie;
	}

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	public String getTipUser() {
		return tipUser;
	}

	public void setTipUser(String tipUser) {
		this.tipUser = tipUser;
	}

	public String getDepartExtra() {
		return departExtra;
	}

	public void setDepartExtra(String departExtra) {
		this.departExtra = departExtra;
	}

	public static void setInstance(UserInfo instance) {
		UserInfo.instance = instance;
	}

	public String getTipUserSap() {
		return tipUserSap;
	}

	public void setTipUserSap(String tipUserSap) {
		this.tipUserSap = tipUserSap;
	}

	public List<String> getExtraFiliale() {
		return extraFiliale;
	}

	public String getInitUnitLog() {
		return initUnitLog;
	}

	public void setInitUnitLog(String initUnitLog) {
		this.initUnitLog = initUnitLog;
	}

	public void setExtraFiliale(String extraFiliale) {

		Scanner scanner = new Scanner(extraFiliale.trim());
		scanner.useDelimiter(",");

		List<String> listExtraFiliale = new ArrayList<String>();

		while (scanner.hasNext()) {
			listExtraFiliale.add(scanner.next());
		}

		this.extraFiliale = listExtraFiliale;
	}

}
