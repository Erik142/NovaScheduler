package com.erik.novascheduler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public final String APP_NAME = "NovaScheduler";

	private final CharSequence[] pageTitles = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag"};
	public final int NUM_PAGES;
    private String url;
	
    public TabsPagerAdapter(FragmentManager fm, String url) {

        super(fm);

        this.url = url;

        Log.i(APP_NAME, "Skottår: " + isLeapYear());
        if (isLeapYear())
        {
            this.NUM_PAGES = 261;
        }
        else
        {
            this.NUM_PAGES = 260;
        }
    }



    @Override
    public Fragment getItem(int position) {

    		return ScheduleFragment.newInstace(position, NUM_PAGES, url);
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return NUM_PAGES + 2;
    }

    @Override
    public int getItemPosition (Object object)
    {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(url);
        }

        ScheduleFragment f = (ScheduleFragment)object;
        int position = f.getPosition();

        if (position > 0 && position < NUM_PAGES)
        {
            return position;
        }
        else
        {
            return POSITION_NONE;
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position)
    {
    	if (position == 0)
    	{
    		return pageTitles[4];
    	}
    	else if (position == NUM_PAGES + 1)
    	{
    		return pageTitles[0];
    	}
    	else {
    		return pageTitles[(position - 1)%5];
		}
    }

    private boolean isLeapYear()
    {
        if ((new GregorianCalendar().get(Calendar.YEAR) % 4) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void updateURL(String newUrl)
    {
        this.url = newUrl;
        Log.i(APP_NAME, "TabsPagerAdapter, updated url: " + url);
    }
}
