package com.erik.novascheduler;

import android.support.v4.view.ViewPager;
import android.util.Log;

public class myPageChangeListener implements ViewPager.OnPageChangeListener {

	
	public ViewPager mViewPager;
	public TabsPagerAdapter mTabsPagerAdapter;
	private static int position;
	
	public int getCurrentPosition()
	{
		return this.position;
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
		
		WorkerClass.position = position;
		this.position = position;
		
		if (position == 0)
		{
			mViewPager.setCurrentItem(mTabsPagerAdapter.NUM_PAGES, false);
			WorkerClass.position = mTabsPagerAdapter.NUM_PAGES;
		}
		if (position == mTabsPagerAdapter.NUM_PAGES + 1)
		{
			mViewPager.setCurrentItem(1, false);
			WorkerClass.position = 1;
		}
		
		WorkerClass.mPageChangeListener = this;
	}

}
