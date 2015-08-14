package lite.sfa.test;

import java.util.ArrayList;
import java.util.List;

import model.ViewPagerCustomDuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Obiective2 extends Fragment {

	ViewPagerCustomDuration pager;
	ViewPager viewPager;
	private CreareObiectiveGeneral obiectiveGeneral;
	private CreareObiectiveFundatie obiectiveFundatie;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.creare_obiective, container, false);
		super.onCreate(savedInstanceState);

		obiectiveGeneral = CreareObiectiveGeneral.newInstance();
		obiectiveFundatie = CreareObiectiveFundatie.newInstance();

		List<Fragment> fragments = getFragments();
		viewPager = (ViewPager) v.findViewById(R.id.returviewpager);
		final ObiectivePagerAdapter returAdapter = new ObiectivePagerAdapter(getFragmentManager(), fragments);

		viewPager.setAdapter(returAdapter);
		viewPager.setOffscreenPageLimit(4);

		setViewPagerListener();

		return v;

	}

	private void setViewPagerListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int pageNumber) {
				if (pageNumber == 1) {
					obiectiveFundatie.setupScreen();
				}

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	class ObiectivePagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public ObiectivePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		public int getCount() {
			return this.fragments.size();
		}
	}

	private List<Fragment> getFragments() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(obiectiveGeneral);
		fragmentList.add(obiectiveFundatie);

		return fragmentList;

	}

}
