package com.erik.novascheduler;

import com.erik.novascheduler.ScheduleFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
	
	private CharSequence[] pageTitles = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag"};
	public int NUM_PAGES = 260;
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int position) {
    	try {
    		int index = position;
    		if (position == 0) {
				index = NUM_PAGES - 1;
			} else if (position == NUM_PAGES + 1) {
				index = 0;
			}
    		ScheduleFragment fragment = ScheduleFragment.newInstace(index);
    		//mPageReferenceMap.put(position, fragment);
    		return fragment;
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return NUM_PAGES + 2;
    }
    
    public int getPosition (Object object)
    {
    	return this.getItemPosition(object);
    }
    
    @Override
    public int getItemPosition (Object object)
    {
    	if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update();
        }
        //don't return POSITION_NONE, avoid fragment recreation. 
        return super.getItemPosition(object);
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
}
