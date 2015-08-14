package listeners;

import enums.EnumDaNuOpt;
import enums.EnumDialogConstraints;

public interface GenericDialogListener {
    void dialogResponse(EnumDialogConstraints constraint, EnumDaNuOpt response);

}
