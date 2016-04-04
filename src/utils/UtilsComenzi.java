package utils;

public class UtilsComenzi {

	public static String getStareComanda(int codStare) {
		String numeStare = "";

		switch (codStare) {
		case 2:
			numeStare = "Borderou tiparit";
			break;

		case 3:
			numeStare = "Inceput incarcare";
			break;

		case 4:
			numeStare = "Terminat incarcare";
			break;

		case 6:
			numeStare = "Plecat in cursa";
			break;

		default:
			break;
		}

		return numeStare;
	}

}
