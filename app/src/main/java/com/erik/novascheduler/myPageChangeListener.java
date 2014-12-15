package com.erik.novascheduler;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Spinner;

public class myPageChangeListener implements ViewPager.OnPageChangeListener {

	
	public ViewPager mViewPager;
	public TabsPagerAdapter mTabsPagerAdapter;
    private final int NUM_PAGES;
    public boolean spinnerByCode;
    private final Spinner mSpinner;

    public myPageChangeListener(int pages, Spinner mSpinner)
    {
        this.NUM_PAGES = pages - 1;
        this.mSpinner = mSpinner;
        Log.i("NovaScheduler", "myPageChangeListener, NUM_PAGES = " + this.NUM_PAGES);
    }
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        Log.i("NovaScheduler", "OnPageSelected Position: " + position);

        if (position == 0) {
            mViewPager.setCurrentItem((mTabsPagerAdapter.NUM_PAGES), false);
            //mTabsPagerAdapter.notifyDataSetChanged();
        }
        if (position == mTabsPagerAdapter.NUM_PAGES + 1) {
            mViewPager.setCurrentItem(1, false);

        }

        if ((position != 0) && (position != NUM_PAGES))
        {
            this.spinnerByCode = true;

            if (position % 5 == 0)
            {
                mSpinner.setSelection((position/5)-1);
            }
            else {
                mSpinner.setSelection((position/5));
            }
        }
	}

}
