package patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import beans.BeanArticolConcurenta;

public class CriteriuArticolConcImpl implements CriteriuArticolConcurenta {

	@Override
	public List<BeanArticolConcurenta> gasesteArticole(List<BeanArticolConcurenta> listArticole, String pattern) {

		List<BeanArticolConcurenta> resultList = new ArrayList<BeanArticolConcurenta>();
		Iterator<BeanArticolConcurenta> iterator = listArticole.iterator();

		while (iterator.hasNext()) {
			BeanArticolConcurenta articol = iterator.next();

			if (articol.getCod().contains(pattern) || articol.getNume().toUpperCase().contains(pattern.toUpperCase()))
				resultList.add(articol);

		}

		return resultList;
	}

}
