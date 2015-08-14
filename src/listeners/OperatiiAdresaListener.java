package listeners;

import enums.EnumLocalitate;
import enums.EnumOperatiiAdresa;

public interface OperatiiAdresaListener {
	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate);
}
