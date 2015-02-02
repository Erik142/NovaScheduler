package com.erik.novascheduler;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Spinner;

public class myPageChangeListener implements ViewPager.OnPageChangeListener {

    public final String APP_NAME = "NovaScheduler";

	private TabsPagerAdapter mTabsPagerAdapter;
    private final int NUM_PAGES;
    public boolean spinnerByCode;
    //private final Spinner mSpinner;
    private boolean isActive = false;
    private PageChangeInterface PCInterface;

    public myPageChangeListener(int pages, TabsPagerAdapter tpa, PageChangeInterface PCI)
    {
        this.PCInterface = PCI;
        this.mTabsPagerAdapter = tpa;
        this.NUM_PAGES = pages - 1;
        Log.i(APP_NAME, "myPageChangeListener, NUM_PAGES = " + this.NUM_PAGES);
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
        Log.i(APP_NAME, "OnPageSelected Position: " + position);

        this.isActive = true;

        if (position == 0) {
            PCInterface.setViewPagerPosition((mTabsPagerAdapter.NUM_PAGES), false);
            this.spinnerByCode = true;
            //mSpinner.setSelection(51);
            PCInterface.updateSpinner(51, true);
            //this.spinnerByCode = false;
            //mTabsPagerAdapter.notifyDataSetChanged();
        }
        if (position == mTabsPagerAdapter.NUM_PAGES + 1) {
            PCInterface.setViewPagerPosition(1, false);
            this.spinnerByCode = true;
            //mSpinner.setSelection(0);
            PCInterface.updateSpinner(0, true);
            //this.spinnerByCode = false;

        }

        if ((position > 0) && (position < NUM_PAGES - 1))
        {


            if ((position % 5) == 0)
            {
                this.spinnerByCode = true;
                //mSpinner.setSelection((position/5)-1);
                PCInterface.updateSpinner((position/5)-1, true);
            }
            else if ((position % 5) == 1) {
                this.spinnerByCode = true;
                //mSpinner.setSelection((position/5));
                PCInterface.updateSpinner((position/5), true);
            }
        }
        //this.spinnerByCode = false;
        this.isActive = false;
	}

    public boolean operationActive()
    {
        return this.isActive;
    }


    public interface PageChangeInterface {
        public void updateSpinner(int week, boolean spinnerByCode);
        public void setViewPagerPosition(int position, boolean animate);

    }

}
