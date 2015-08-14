package dialogs;

import android.app.DatePickerDialog;
import android.content.Context;

public class SelectDateDialog extends DatePickerDialog {

	public SelectDateDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);

	}

}
