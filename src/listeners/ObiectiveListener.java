package listeners;

import enums.EnumOperatiiObiective;

public interface ObiectiveListener {
	void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result);
}
