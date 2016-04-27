package patterns;

import java.util.List;

import beans.BeanArticolConcurenta;

public interface CriteriuArticolConcurenta {
	public List<BeanArticolConcurenta> gasesteArticole(List<BeanArticolConcurenta> listArticole, String pattern);
}
