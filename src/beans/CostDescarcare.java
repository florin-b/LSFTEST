package beans;

import java.util.List;

public class CostDescarcare {

	private List<ArticolDescarcare> articoleDescarcare;
	private boolean sePermite;

	public List<ArticolDescarcare> getArticoleDescarcare() {
		return articoleDescarcare;
	}

	public void setArticoleDescarcare(List<ArticolDescarcare> articoleDescarcare) {
		this.articoleDescarcare = articoleDescarcare;
	}

	public boolean getSePermite() {
		return sePermite;
	}

	public void setSePermite(boolean sePermite) {
		this.sePermite = sePermite;
	}

	public double getValoareDescarcare() {

		double valDesc = 0;

		if (articoleDescarcare == null)
			return 0;

		for (ArticolDescarcare artDesc : articoleDescarcare) {
			valDesc += Double.valueOf(artDesc.getValoare() * artDesc.getCantitate());
		}

		return valDesc;

	}

	public double getValoareMinDescarcare() {
		double valMinDesc = 0;

		if (articoleDescarcare == null)
			return 0;

		for (ArticolDescarcare artDesc : articoleDescarcare) {
			valMinDesc += Double.valueOf(artDesc.getValoareMin() * artDesc.getCantitate());
		}

		return valMinDesc;
	}

}
