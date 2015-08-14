/**
 * @author florinb
 *
 */
package lite.sfa.test;




import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentMainKA extends Fragment {

	TextView textView1;

	public static Fragment newInstance(Context context) {
		FragmentMainKA f = new FragmentMainKA();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_main_ka, null);

		return root;
	}

}