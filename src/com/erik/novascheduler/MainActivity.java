package com.erik.novascheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private static final OnPageChangeListener OnPageChangeListener = null;

	public static Activity mainActivity;

	private mySpinnerAdapter mSpinnerAdapter;
	private LayoutParams layoutParams;
	private List<String> choises;
	private Spinner mSpinner;
	private Context mainContext;
	private int dayOfWeek;
	private TabsPagerAdapter mTabsPagerAdapter;
	private ViewPager mViewPager;
	private ActionBar actionBar;
	private String url = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=/sv-se&type=-1&id=&period=&week=&mode=2&printer=0&colors=32&head=0&clock=0&foot=0&day=0&width=&height=";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Initiera appinställningar
		WorkerClass.appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		WorkerClass.context = this;
		
		//Deklarera variabler
		mainActivity = this;
		mainContext = this;
		actionBar = getActionBar();
		
		//Få spinner att ligga till höger i ActionBar
		layoutParams = new ActionBar.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mSpinner = new Spinner(this);
		
		choises = new ArrayList<String>();
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		// TODO Auto-generated method stub
		for (int i = 0; i < 52; i++)
		{
			choises.add(("Vecka " + (i + 1)));
			
		}
		
		mSpinnerAdapter = new mySpinnerAdapter(mainContext, choises);
				
		dayOfWeek = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
		WorkerClass.week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
		
		
		if (dayOfWeek > 6 || dayOfWeek < 2)
		{
			dayOfWeek = 1;
			if (WorkerClass.week != 52)
			{
				WorkerClass.week += 1;
			}
			else {
				WorkerClass.week = 1;
			}
			
		}
		else 
		{
			dayOfWeek -= 1;
		}
		
		Log.i("NovaScheduler", "dayofWeek: " + dayOfWeek);
		Log.i("NovaScheduler", "WorkerClass.week: " + WorkerClass.week);
		
		//Max 5 stycken inladdade dagar åt gången
		mViewPager.setOffscreenPageLimit(5);
		
		mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mTabsPagerAdapter);
		
		
		
		myPageChangeListener mPageChangeListener = new myPageChangeListener();
		mPageChangeListener.mViewPager = mViewPager;
		mPageChangeListener.mTabsPagerAdapter = mTabsPagerAdapter;
			/*OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
			
				int position;
				
				public int getPosition()
				{
					return position;
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
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		};*/
		WorkerClass.mPageChangeListener = mPageChangeListener;
		mViewPager.setOnPageChangeListener(mPageChangeListener);
		
		OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				//mSpinner.setSelection(position);
				//((TextView) parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
				/*if (!DownloadTask.isActive)
				{
					if (!WorkerClass.firstTime)
					{
					DownloadImage((position + 1));
					mTabsPagerAdapter.notifyDataSetChanged();
					}
				
				if ((position + 1) == WorkerClass.week)
				{
					mViewPager.setCurrentItem((dayOfWeek*WorkerClass.week));
					//mViewPager.setCurrentItem(4);
				}
				else 
				{
					mViewPager.setCurrentItem(1*WorkerClass.week);
				}
				
				Log.i("NovaScheduler", ("OnItemSelectedListener, ID: " + position));
				}*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}

		};
		
		
		mSpinner.setAdapter(mSpinnerAdapter);
		mSpinner.setOnItemSelectedListener(onItemSelectedListener);
		WorkerClass.mSpinner = mSpinner;
		
		
		actionBar.setCustomView(mSpinner, layoutParams);
		actionBar.setDisplayShowCustomEnabled(true);
		
		
		mViewPager.setCurrentItem(((5 * WorkerClass.week)-(5-dayOfWeek)));
		Log.i("NovaScheduler", "viewPager Start position: " + (dayOfWeek * WorkerClass.week));
		
		//Ifall skola och/eller klass inte ställts in, gör detta först. Ladda annars in schema som vanligt.
		if (WorkerClass.appPreferences.getString("school_key", null) == null || WorkerClass.appPreferences.getString("freeTextBox_key", null) == null)
		{
			Context context = getApplicationContext();
			CharSequence sequence = "Skola och/eller klass behöver ställas in!";
			int duration = Toast.LENGTH_SHORT;
			
			Toast mToast = Toast.makeText (context, sequence, duration);
			mToast.show();
			
			startActivity(new Intent(mainContext, SettingsActivity.class));
		}
		else 
		{
			DownloadAndUpdate();
		}
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		android.os.Debug.stopMethodTracing();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			
			return true;
			
		case R.id.refresh:
			
			DownloadAndUpdate();
			mViewPager.setCurrentItem(((5 * WorkerClass.week)-(5-dayOfWeek)));
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void DownloadAndUpdate()
	{
		mViewPager.setCurrentItem(((5 * WorkerClass.week)-(5-dayOfWeek)));
		setActionBarTitle();
		
		if (dayOfWeek == 1)
		{
			try {
				//mSpinner.setSelection(WorkerClass.week-1);
				DownloadImage((WorkerClass.week));
			} catch (ArrayIndexOutOfBoundsException e) {
				//mSpinner.setSelection(0);
				DownloadImage(1);
			}
			
			
		}
		else 
		{
				//mSpinner.setSelection((new GregorianCalendar().get(Calendar.WEEK_OF_YEAR) - 1));
				DownloadImage();	
		}
	}
	
	public void DownloadImage()
	{
		if (!DownloadTask.isActive)
		{
			new DownloadTask(this, this, mTabsPagerAdapter, new GregorianCalendar().get(Calendar.WEEK_OF_YEAR)).execute(url);
		}
		else 
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (DownloadTask.isActive)
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					new DownloadTask(mainContext, mainActivity, mTabsPagerAdapter, new GregorianCalendar().get(Calendar.WEEK_OF_YEAR)).execute(url);
					
				}
			}).start();
		}
	}
	
	public void DownloadImage(int week)
	{
		final int thisWeek = week;
		if (!DownloadTask.isActive)
		{
			new DownloadTask(this, this, mTabsPagerAdapter, thisWeek).execute(url);
		}
		else 
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (DownloadTask.isActive)
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					new DownloadTask(mainContext, mainActivity, mTabsPagerAdapter, thisWeek).execute(url);
					
				}
			}).start();
		}
	}
	
	public void setActionBarTitle()
	{
		actionBar.setTitle(WorkerClass.appPreferences.getString("freeTextBox_key", null).toUpperCase(Locale.getDefault()));
	}
}
