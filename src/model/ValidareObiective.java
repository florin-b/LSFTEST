package model;

import android.content.Context;
import android.widget.Toast;
import beans.BeanObiectiveGenerale;

public class ValidareObiective {

	public boolean valideaza(Context context) {
		BeanObiectiveGenerale obiectiv = BeanObiectiveGenerale.getInstance();

		if (obiectiv.getNumeObiectiv().toString().trim().length() == 0) {
			Toast.makeText(context, "Completati numele obiectivului", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (obiectiv.getAdresaSer().toString().trim().length() == 0) {
			Toast.makeText(context, "Completati adresa obiectivului", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (obiectiv.getNumeBeneficiar().toString().trim().length() == 0) {
			Toast.makeText(context, "Completati beneficiarul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (obiectiv.getNumeAntreprenorGeneral().toString().trim().length() == 0) {
			Toast.makeText(context, "Completati antreprenorul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (obiectiv.getValoareObiectiv().toString().trim().length() == 0) {
			Toast.makeText(context, "Completati valoarea obiectivului", Toast.LENGTH_SHORT).show();
			return false;
		}

		/*
		if (obiectiv.getListConstructori().size() == 0) {
			Toast.makeText(context, "Adaugati cel putin un client", Toast.LENGTH_SHORT).show();
			return false;
		}
		*/

		return true;
	}

}
