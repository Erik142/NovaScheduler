package com.erik.novascheduler;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Spinner;

public class myPageChangeListener implements ViewPager.OnPageChangeListener {

	
	public ViewPager mViewPager;
	public TabsPagerAdapter mTabsPagerAdapter;
	private static int position;
    private int NUM_PAGES;
    public boolean spinnerByCode;
    private Spinner mSpinner;

    public myPageChangeListener(int pages, Spinner mSpinner)
    {
        this.NUM_PAGES = pages - 1;
        this.mSpinner = mSpinner;
        Log.i("NovaScheduler", "myPageChangeListener, NUM_PAGES = " + this.NUM_PAGES);
    }

	public void setPosition(int position)
	{
		this.position = position;
	}
	
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

        this.position = position;

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
                //Log.i("NovaScheduler", "Apply bitmap, week: " + week);
            }
            else {

                mSpinner.setSelection((position/5));
                //Log.i("NovaScheduler", "Apply bitmap, week: " + week);
            }
        }





        /*if (position >= 0 && position <=10)
        {
            WorkerClass.week = (position / 5) + 1;
            Log.i("NovaScheduler", "Apply bitmap, week: " + WorkerClass.week);
        }
        else if (position % 5 == 0)
        {
            WorkerClass.week = (position / 5);
            Log.i("NovaScheduler", "Apply bitmap, week: " + WorkerClass.week);
        }
        else {
            WorkerClass.week = position / 5;
            Log.i("NovaScheduler", "Apply bitmap, week: " + WorkerClass.week);
        }

        mTabsPagerAdapter.notifyDataSetChanged();*/
	}

}
