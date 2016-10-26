package patterns;

import java.util.ArrayList;
import java.util.List;

import beans.BeanComandaCreata;

public class CriteriuComenziSimulate {

	public List<BeanComandaCreata> getCmdByClient(List<BeanComandaCreata> listComenzi, String numeClient) {

		List<BeanComandaCreata> comenziGasite = new ArrayList<BeanComandaCreata>();

		for (BeanComandaCreata comanda : listComenzi)
			if (comanda.getNumeClient().equals(numeClient))
				comenziGasite.add(comanda);

		return comenziGasite;
	}

}
