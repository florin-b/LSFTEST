package listeners;

import enums.EnumMotiveSuspendareObKA;
import enums.EnumStadiuObiectivKA;
import enums.EnumStadiuSubantrep;

public interface StadiuDialogListener {
	void stadiuSelected(EnumStadiuSubantrep stadiuObiectiv);
	void stadiuSelected(EnumStadiuObiectivKA stadiuObiectiv, EnumMotiveSuspendareObKA motivSuspendare);

}
