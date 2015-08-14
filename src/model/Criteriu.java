package model;

import java.util.List;

import beans.BeanComandaCreata;

public interface Criteriu {
	public List<BeanComandaCreata> indeplinesteCriteriu(List<BeanComandaCreata> listaComenzi, String divizie);
}
