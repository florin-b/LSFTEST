/**
 * @author florinb
 *
 */
package model;

import java.lang.reflect.Field;

import lite.sfa.test.ScrollerCustomDuration;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

public class ViewPagerCustomDuration extends ViewPager {

	public ViewPagerCustomDuration(Context context) {
		super(context);
		postInitViewPager();
	}

	public ViewPagerCustomDuration(Context context, AttributeSet attrs) {
		super(context, attrs);
		postInitViewPager();
	}

	private ScrollerCustomDuration mScroller = null;

	private void postInitViewPager() {
		try {
			Class<?> viewpager = ViewPager.class;
			Field scroller = viewpager.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			Field interpolator = viewpager.getDeclaredField("sInterpolator");
			interpolator.setAccessible(true);

			mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
			scroller.set(this, mScroller);
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
	}

	public void setScrollDurationFactor(double scrollFactor) {
		mScroller.setScrollDurationFactor(scrollFactor);
	}

}
