package utils;

import model.UserInfo;



public class UtilsUser {

	public static boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	public static boolean isCV() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
	}

	public static boolean isDVCV() {
		return UserInfo.getInstance().getTipUser().equals("DV") || UserInfo.getInstance().getCodDepart().equals("00");
	}

	public static boolean isAV() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD");
	}

	public static boolean hasObiective() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("KA");
	}

	public static boolean isANYDV() {
		return UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("35");
	}

	public static boolean isSDBUCURESTI() {
		return UserInfo.getInstance().getTipAcces().equals("10") && UserInfo.getInstance().getUnitLog().substring(0, 2).equals("BU");
	}

	public static boolean isUserGed() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
	}

	public static boolean isAgentOrSD() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().contains("AV") || UserInfo.getInstance().getTipUserSap().toUpperCase().equals("SD");

	}

	public static boolean isConsWood() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().contains("WOOD");
	}

	public static boolean isDV_WOOD() {
		if (isANYDV()) {

			String filDV = UserInfo.getInstance().getFilialeDV();

			if (filDV.contains(";")) {
				String[] filiale = UserInfo.getInstance().getFilialeDV().split(";");

				if (filiale.length > 0 && filiale[0].substring(2, 3).equals("4"))
					return true;
			}

		}

		return false;
	}

	public static boolean isDV_CONS() {
		String codDvCons = "00001027,00012326";

		if (codDvCons.contains(UserInfo.getInstance().getCod()))
			return true;

		return false;

	}

	public static boolean isMacaraVisible() {

		if (isAgentOrSD()) {
			if (UserInfo.getInstance().getCodDepart().contains("04") || UserInfo.getInstance().getCodDepart().contains("07")
					|| UserInfo.getInstance().getCodDepart().contains("09"))
				return true;
			else
				return false;
		} else if (isKA()) {
			return true;
		} else if (isCV()) {
			return true;
		}

		return false;
	}

}
