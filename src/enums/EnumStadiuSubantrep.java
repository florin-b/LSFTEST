package enums;

public enum EnumStadiuSubantrep {

	TERMINAT("Terminat", 0), IN_CONSTRUCTIE("In constructie", 1);

	private String numeStadiu;
	private int codStadiu;

	EnumStadiuSubantrep(String numeStadiu, int codStadiu) {
		this.numeStadiu = numeStadiu;
		this.codStadiu = codStadiu;
	}

	public String getNumeStadiu() {
		return numeStadiu;
	}

	public int getCodStadiu() {
		return codStadiu;
	}

	public static String getNumeStadiu(int codStadiu) {

		for (EnumStadiuSubantrep enumSt : EnumStadiuSubantrep.values())
			if (enumSt.codStadiu == codStadiu)
				return enumSt.numeStadiu;

		return "";
	}

	public static EnumStadiuSubantrep getStadiu(int codStadiu) {
		for (EnumStadiuSubantrep enumSt : EnumStadiuSubantrep.values())
			if (enumSt.codStadiu == codStadiu)
				return enumSt;

		return null;
	}

}
