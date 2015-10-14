package com.erik.novascheduler;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends ActionBarActivity implements multiInterface {

	public static Activity mainActivity;

    public final String APP_NAME = "NovaScheduler";

    private Toolbar toolbar;
    private SharedPreferences appPreferences;

    private String freeTextBox, currentFreeTextBox;
    private int schoolID;
    private double heightPercent;

    private FloatingActionButton addButton;

    private List<String> choises;
    private Spinner mSpinner;
    private mySpinnerAdapter mSpinnerAdapter;

    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;

    private PagerFragment pagerFragment;

    private boolean spinnerByCode;
    private boolean firstRun;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private ArrayList<schemaDrawerItem> leftSliderData;

    private final String PREFS_KEY = "prefs_url";
    private final String URL_KEY = "url_key";
    private ArrayList<String> URLList;



	private String url = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=/sv-se&type=-1&id=&period=&week=&mode=2&printer=0&colors=32&head=0&clock=0&foot=0&day=&width=&height=";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Log.i("NovaScheduler", "OnCreate()");

        leftSliderData = new ArrayList<schemaDrawerItem>();
        initView();

        //toolbar.setTitle("");
        toolbar = (Toolbar)findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);

        initDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        drawerToggle.syncState();

        spinnerByCode = false;
        firstRun = false;

		//Initiera appinställningar
		//appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appPreferences = getSharedPreferences(PREFS_KEY, 0);
		
		//Deklarera variabler
		mainActivity = this;



		//Ifall skola och/eller klass inte ställts in, gör detta först. Ladda annars in schema som vanligt.
		//if (appPreferences.getString("school_key", null) == null || appPreferences.getString("freeTextBox_key", null) == null)
        if (appPreferences.getString(URL_KEY,null) == null)
		{
            firstRun = true;
			Context context = getApplicationContext();
			CharSequence sequence = "Skola och/eller klass behöver ställas in!";
			int duration = Toast.LENGTH_SHORT;
			
			Toast mToast = Toast.makeText (context, sequence, duration);
			mToast.show();

            FragmentManager fm = getSupportFragmentManager();
            newScheduleFragment fragment = newScheduleFragment.newInstance();
            fragment.show(fm, "new_schedule");

            /*Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent,0);*/
		}
		else 
		{
            initEverything();
            prepareDefaultSchedule();
		}

	}

    private void initView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new NavigationDrawerAdapter(getApplicationContext(), this, R.layout.navigation_drawer_item, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();

                getSupportActionBar().setTitle(currentFreeTextBox);
                //getSupportActionBar().setDisplayShowCustomEnabled(true);
                mSpinner.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);


            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();

                getSupportActionBar().setTitle(R.string.app_name);
                mSpinner.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                //getSupportActionBar().setDisplayShowCustomEnabled(false);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        leftDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFragment(navigationDrawerAdapter.getItem(position), position);

                leftDrawerList.setItemChecked(position, true);
            }
        });

        leftDrawerList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(APP_NAME, "Long clicked");
                //navigationDrawerAdapter.remove(navigationDrawerAdapter.getItem(position));
                editSchedule(navigationDrawerAdapter.getItem(position));
                return true;
            }
        });
    }

    public void setFragment(schemaDrawerItem item, int position)
    {
        currentFreeTextBox = item.getFreeTextBox();
        pagerFragment = PagerFragment.newInstance(item.getURL());

        leftDrawerList.setItemChecked(position, true);
        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle(item.getFreeTextBox());

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pagerFragment).commit();
    }

    public void setPlaceHolderFragment()
    {
        currentFreeTextBox = "";
        pagerFragment = PagerFragment.newInstance("");

        drawerLayout.closeDrawers();
        getSupportActionBar().setTitle("");

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pagerFragment).commit();
    }

    public void initEverything()
    {

        addButton = (FloatingActionButton)findViewById(R.id.addButton);
        mSpinner = (Spinner)findViewById(R.id.toolbar_spinner);

        choises = new ArrayList<String>();

        for (int i = 0; i < 52; i++)
        {
            choises.add(("Vecka " + (i + 1)));

        }

        mSpinnerAdapter = new mySpinnerAdapter(getSupportActionBar().getThemedContext(), choises);

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub

                Log.i("NovaScheduler", "onItemSelected, position: " + position);

                Log.i(APP_NAME, "onItemSelected, getTag(): " + mSpinner.getTag());
                if (mSpinner.getTag() == "user") {
                    pagerFragment.setPosition((5 * (position + 1) - 4));
                    return;
                }

                mSpinner.setTag("user");

                //mPageChangeListener.updateSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        };


        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSchedule();
            }
        });

        heightPercent = 95;

        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.noschedule)
                .showImageForEmptyUri(R.drawable.noschedule).preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bm) {
                        Bitmap resizedBitmap = null;

                        try {

                            int width = (bm.getWidth() - 5);
                            int height = bm.getHeight();

                            //int startwidth = (width/5)*position;
                            //int scaleWidth = (int)(width/amount);
                            int startwidth = 0;
                            int scaleHeight = (int)((heightPercent / 100) * height);

                            //resizedBitmap = Bitmap.createBitmap(cropImage(bm, (width), (height)), startwidth, height-scaleHeight, scaleWidth, scaleHeight, null, false);
                            resizedBitmap = Bitmap.createBitmap(cropImage(bm, (width), (height)), startwidth, height-scaleHeight, width, scaleHeight, null, false);
                        }
                        catch (NullPointerException e)
                        {
                            resizedBitmap = null;
                        }
                        if (resizedBitmap != bm)
                        {
                            bm.recycle();
                        }
                        return resizedBitmap;
                    }
                }).build();

        config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).threadPriority(1).threadPoolSize(1).build();
        ImageLoader.getInstance().init(config);


        //Lägg till i schemaDrawerItem.java så att URL konverteras från url;default=boolean till endast url

    }

    private static Bitmap cropImage (Bitmap imgBitmap, int x, int y)
    {
        int xSize = x;
        int ySize = y;

        Bitmap result = null;

        if (imgBitmap != null)
        {
            result = Bitmap.createBitmap(imgBitmap, 0, (imgBitmap.getHeight() - ySize), xSize, ySize, null, false);

        }
        return result;
    }

    public void prepareDefaultSchedule()
    {
        SharedPreferences appPreferences = getSharedPreferences(PREFS_KEY, 0);
        URLList = new ArrayList<String>();
        URLList.addAll(Arrays.asList(appPreferences.getString(URL_KEY, null).split("\\|")));

        Log.i(APP_NAME, "Stored URLs: " + appPreferences.getString(URL_KEY, null));
        String defaultURL = schemaDrawerItem.getDefaultURL(URLList);
        Log.i(APP_NAME, "defaultURL: " + defaultURL);

        pagerFragment = PagerFragment.newInstance(defaultURL);

        leftSliderData.addAll(schemaDrawerItem.getDrawerList(appPreferences.getString(URL_KEY, null)));
        sortLeftSliderData();

        //navigationDrawerAdapter.addAll(addList);
        navigationDrawerAdapter.notifyDataSetChanged();

        setFragment(new schemaDrawerItem(defaultURL), navigationDrawerAdapter.getPosition(new schemaDrawerItem(defaultURL)));
        //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,pagerFragment).commit();

        /*schoolID = Integer.parseInt(appPreferences.getString("school_key", null).trim());
        freeTextBox = appPreferences.getString("freeTextBox_key", null);

        Log.i("NovaScheduler", "freeTextBox: " + freeTextBox);
        Log.i("NovaScheduler", "New GregorianCalendarWeek: " + new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));*/
    }

    @Override
    protected void onStart()
    {
        super.onStart();

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(GravityCompat.START);

        //menu.findItem(R.id.new_schedule).setVisible(drawerOpen);
        menu.findItem(R.id.refresh).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
            /*Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent,0);*/
			//startActivity(new Intent(this, SettingsActivity.class));

			return true;
			
		case R.id.refresh:
			
			pagerFragment.DownloadAndUpdate();
			
			return true;

            case R.id.todays_schedule:

            pagerFragment.DownloadTodaysSchedule();

            return true;

        /*case R.id.new_schedule:

            newSchedule();*/

		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!firstRun)
        {
            try
            {
                appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                schoolID = Integer.parseInt(appPreferences.getString("school_key", null).trim());
                freeTextBox = appPreferences.getString("freeTextBox_key", null);

                Log.i("NovaScheduler", "old freeTextBox: " + pagerFragment.getFreeTextBox() + ". New freeTextBox: " + freeTextBox);
                Log.i("NovaScheduler", "old schoolID: " + pagerFragment.getSchoolID() + ". New schoolID: " + schoolID);

                if (pagerFragment.getSchoolID() != schoolID || !freeTextBox.equals(pagerFragment.getFreeTextBox()))
                {
                    pagerFragment = PagerFragment.newInstance(url);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pagerFragment).commit();
                    Log.i("NovaScheduler", "Loading new schedule");
                }
                else {
                    Log.i("NovaScheduler", "Not loading new schedule...");
                }
            }
            catch (Exception e)
            {
                Log.e("NovaScheduler", "OnActivityResult, something went wrong... " + e.toString());
            }

        }
        else
        {
            Log.i("NovaScheduler", "firstRun");
            initEverything();
        }

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateSpinner(int week) {

        Log.i("NovaScheduler", "updateSpinner()");
        if (mSpinner != null) {
            mSpinner.setTag("code");
            mSpinner.setSelection(week);
        }
    }

    private void newSchedule()
    {
        FragmentManager fm = getSupportFragmentManager();
        newScheduleFragment fragment = newScheduleFragment.newInstance();
        fragment.show(fm, "new_schedule");

    }

    private void editSchedule(schemaDrawerItem item)
    {
        FragmentManager fm = getSupportFragmentManager();
        newScheduleFragment fragment = newScheduleFragment.newInstance(item, this);
        fragment.show(fm,"edit_schedule");
    }


    @Override
    public void onDialogCompleted(String url, String oldURL) {

        if (firstRun)
        {
            initEverything();
        }

        Log.i(APP_NAME, "onDialogCompleted, newURL: " + url);
        Log.i(APP_NAME, "onDialogCompleted, oldURL: " + oldURL);

        int addPosition;

        schemaDrawerItem itemToAdd = null;
        Log.i(APP_NAME, "onDialogCompleted, leftSliderData.size() = " + leftSliderData.size());
        if (!leftSliderData.isEmpty()) {
            addPosition = leftSliderData.size();
        }
        else
        {
            addPosition = 0;
        }

        if (!url.equals("")) {

            itemToAdd = new schemaDrawerItem(url);

            if (itemToAdd.isDefault())
            {
                for (int i = 0; i < leftSliderData.size(); i++)
                {
                    leftSliderData.get(i).setDefault(false);
                }
            }

            drawerLayout.closeDrawers();
            leftSliderData.add(itemToAdd);
        }

        if (!oldURL.trim().matches(""))
        {
            int removeposition = 0;
            for (int i = 0; i < leftSliderData.size(); i++)
            {
                if (leftSliderData.get(i).getURL().equals(oldURL))
                {
                    removeposition = i;
                    addPosition -= 1;
                    break;
                }
            }
            Log.i(APP_NAME, "onDialogCompleted, removeposition = " + removeposition);
            leftSliderData.remove(removeposition);

        }

        sortLeftSliderData();
        navigationDrawerAdapter.notifyDataSetChanged();

        if (!url.equals("")) {
            setFragment(itemToAdd, addPosition);
        }
        else
        {
            Log.i(APP_NAME, "onDialogCompleted, setPlaceHolderFragment()");
            setPlaceHolderFragment();
        }


    }

    public void sortLeftSliderData()
    {
        Collections.sort(leftSliderData, new Comparator<schemaDrawerItem>() {
            @Override
            public int compare(schemaDrawerItem item1, schemaDrawerItem item2) {
                return item1.getFreeTextBox().compareTo(item2.getFreeTextBox());
            }
        });
    }
}
