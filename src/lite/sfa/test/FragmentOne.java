/**
 * @author florinb
 *
 */
package lite.sfa.test;




import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentOne extends Fragment {

	TextView textView1;

	public FragmentOne() {

	}

	public static final FragmentOne newInstance() {

		FragmentOne f = new FragmentOne();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_one, null);

		return root;
	}

}