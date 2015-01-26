package com.erik.novascheduler;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity {

	public static Activity mainActivity;
    public static boolean active = false;

    private myPageChangeListener mPageChangeListener;
    private SharedPreferences appPreferences;
	private mySpinnerAdapter mSpinnerAdapter;
	private LayoutParams layoutParams;
	private List<String> choises;
	private Spinner mSpinner;
	private int dayOfWeek, week, schoolID;
	private TabsPagerAdapter mTabsPagerAdapter;
	private ViewPager mViewPager;
	private String url = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=/sv-se&type=-1&id=&period=&week=&mode=2&printer=0&colors=32&head=0&clock=0&foot=0&day=&width=&height=";
    private String freeTextBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Log.i("NovaScheduler", "OnCreate()");
		
		//Initiera appinställningar
		appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Deklarera variabler
		mainActivity = this;
		
		//Ifall skola och/eller klass inte ställts in, gör detta först. Ladda annars in schema som vanligt.
		if (appPreferences.getString("school_key", null) == null || appPreferences.getString("freeTextBox_key", null) == null)
		{
			Context context = getApplicationContext();
			CharSequence sequence = "Skola och/eller klass behöver ställas in!";
			int duration = Toast.LENGTH_SHORT;
			
			Toast mToast = Toast.makeText (context, sequence, duration);
			mToast.show();
			
			startActivity(new Intent(this, SettingsActivity.class));
		}
		else 
		{
            initDayWeek();
            prepareForDownload();   //Initiera url för schema
            initVars();             //Initiera variabler
            DownloadTodaysSchedule();    //Sätt rätt position samt titel på ActionBar
            active = true;
		}

	}

    @Override
    protected void onStart()
    {
        super.onStart();
        initDayWeek();
        if (mTabsPagerAdapter != null)
        {
            mTabsPagerAdapter.notifyDataSetChanged();
            DownloadTodaysSchedule();
        }
        Log.i("NovaScheduler", "OnStart()");
    }
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
        Log.i("NovaScheduler", "OnDestroy()");
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
			
			return true;

            case R.id.todays_schedule:

            DownloadTodaysSchedule();

            return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void DownloadAndUpdate()
	{
		//mViewPager.setCurrentItem(((5 * week)-(5-dayOfWeek)));
		setActionBarTitle();
        mTabsPagerAdapter.notifyDataSetChanged();
	}

    public void DownloadTodaysSchedule()
    {
        mViewPager.setCurrentItem(((5 * week)-(5-dayOfWeek)));
        setActionBarTitle();
        mTabsPagerAdapter.notifyDataSetChanged();
    }
	
	public void setActionBarTitle()
	{
		getActionBar().setTitle(appPreferences.getString("freeTextBox_key", null).toUpperCase(Locale.getDefault()));
	}

    public void initDayWeek()
    {
        dayOfWeek = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
        week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);


        Log.i("NovaScheduler", "År: " + (new GregorianCalendar().get(Calendar.YEAR)));


        if (dayOfWeek > 6 || dayOfWeek < 2)
        {
            dayOfWeek = 1;
            if (week != 52)
            {
                week += 1;
            }
            else {
                week = 1;
            }

        }
        else
        {
            dayOfWeek -= 1;
        }

        Log.i("NovaScheduler", "dayofWeek: " + dayOfWeek);
        Log.i("NovaScheduler", "week: " + week);
        Log.i("NovaScheduler", "viewPager Start position: " + ((5 * week)-(5-dayOfWeek)));
    }

    public void initVars()
    {
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Få spinner att ligga till höger i ActionBar
        layoutParams = new ActionBar.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        mSpinner = new Spinner(this);

        choises = new ArrayList<String>();
        mViewPager = (ViewPager)findViewById(R.id.pager);

        for (int i = 0; i < 52; i++)
        {
            choises.add(("Vecka " + (i + 1)));

        }

        mSpinnerAdapter = new mySpinnerAdapter(this, choises);



        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), url);

        mPageChangeListener = new myPageChangeListener(mTabsPagerAdapter.getCount(), mSpinner);
        mPageChangeListener.mViewPager = mViewPager;
        mPageChangeListener.mTabsPagerAdapter = mTabsPagerAdapter;

        mViewPager.setAdapter(mTabsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);  //Max 5 stycken inladdade dagar åt gången
        mViewPager.setOnPageChangeListener(mPageChangeListener);

        OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub

                Log.i("NovaScheduler", "onItemSelected, position: " + position);

                mPageChangeListener.updateSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        };


        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);

        getActionBar().setCustomView(mSpinner, layoutParams);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);

    }

    public void prepareForDownload()
    {
        int width = 0;
        int height = 0;
        schoolID = 0;
        freeTextBox = null;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //width = size.x * 5;
        width = size.x;

        if (size.x >= 1080)
        {
            width = (width/3);
        }
        else
        {
            width = width/2;
        }


        Log.i("NovaScheduler", "Screen width: " + Integer.toString(width));

        if (size.y >= 1920)
        {
            height = (int)((size.y/3)*(1-(1.8/10.6)));
        }
        else
        {
            height = (int)((size.y/2)*(1-(1.8/10.6)));
        }




        try {

            schoolID = Integer.parseInt(appPreferences.getString("school_key", null).trim());
            freeTextBox = appPreferences.getString("freeTextBox_key", null);


        } catch (NumberFormatException e) {
            // TODO: handle exception
            if (e.equals(schoolID))
            {
                schoolID = 0;
            }
            if (e.equals(freeTextBox))
            {
                freeTextBox = null;
            }
        }

        freeTextBox = freeTextBox.replace(" ", "");

        url = url.replace("&schoolid=", ("&schoolid=" + schoolID));
        url = url.replace("&id=", ("&id=" + freeTextBox));
        url = url.replace("&width=", ("&width=" + width));
        url = url.replace("&height=", "&height=" + height);
        url = url.replace("Å", "%C3%85");
        url = url.replace("Ä", "%C3%84");
        url = url.replace("Ö", "%C3%96");

        Log.i("NovaScheduler", ("URL: " + url));

        Log.i("NovaScheduler", "freeTextBox: " + freeTextBox);
        Log.i("NovaScheduler", "New GregorianCalendarWeek: " + new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));
    }

    public void updateURL()
    {
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int oldSchoolID = schoolID;
        String oldFreeTextBox = freeTextBox;

        try {

            schoolID = Integer.parseInt(appPreferences.getString("school_key", null).trim());
            freeTextBox = appPreferences.getString("freeTextBox_key", null);

            Log.i("NovaScheduler", "new freeTextBox: " + freeTextBox);
            Log.i("NovaScheduler", "new schoolID: " + schoolID);

            if (schoolID != oldSchoolID || !freeTextBox.equals(oldFreeTextBox))
            {
                if (oldFreeTextBox != null)
                {

                    url = url.replace(("&id=" + oldFreeTextBox), ("&id=" + freeTextBox));
                }
                else
                {

                    url = url.replace("&id=", ("&id=" + freeTextBox));
                }

                if (oldSchoolID != 0)
                {
                    url = url.replace(("&schoolid=" + oldSchoolID), ("&schoolid=" + schoolID));
                }
                else
                {
                    url = url.replace("&schoolid=", ("&schoolid=" + schoolID));
                }
            }

        Log.i("NovaScheduler", "new url: " + url);

        mTabsPagerAdapter.updateURL(url);
        mTabsPagerAdapter.notifyDataSetChanged();


        } catch (NumberFormatException e) {
            // TODO: handle exception
            if (e.equals(schoolID))
            {
                schoolID = 0;
            }
            if (e.equals(freeTextBox))
            {
                freeTextBox = null;
            }
        }
    }

    public boolean getStatus()
    {
        return active;
    }
}
