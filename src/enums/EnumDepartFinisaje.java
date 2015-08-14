package enums;

public enum EnumDepartFinisaje {

	Acoperis("09"), Instalatii("08"), Electrice("05"), Interioare("00");

	private String codDepart;

	private EnumDepartFinisaje(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getCodDepart() {
		return codDepart;
	}

}
