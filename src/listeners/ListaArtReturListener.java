package listeners;

import java.util.List;

import beans.BeanArticolRetur;

public interface ListaArtReturListener {
	public void setListArtRetur(String nrDocument, List<BeanArticolRetur> listArticole, String codClient, String numeClient);
}
