package enums;

public enum EnumMeniuObiectiv {
	CREARE(0), MODIFICARE(1), URMARIRE(2);

	private int codMeniu;

	EnumMeniuObiectiv(int codMeniu) {
		this.codMeniu = codMeniu;
	}

	public int getCodMeniu() {
		return codMeniu;
	}

}
