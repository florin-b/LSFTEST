package beans;

import java.util.List;

public class BeanConditii {

	private BeanConditiiHeader header;
	private List<BeanConditiiArticole> articole;

	public BeanConditii() {

	}

	public BeanConditiiHeader getHeader() {
		return header;
	}

	public void setHeader(BeanConditiiHeader header) {
		this.header = header;
	}

	public List<BeanConditiiArticole> getArticole() {
		return articole;
	}

	public void setArticole(List<BeanConditiiArticole> articole) {
		this.articole = articole;
	}

}
