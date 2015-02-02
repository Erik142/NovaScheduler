package com.erik.novascheduler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFragment extends Fragment implements myPageChangeListener.PageChangeInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "url";

    // TODO: Rename and change types of parameters
    private String mURL;

    public final String APP_NAME = "NovaScheduler";

    private myPageChangeListener mPageChangeListener;
    private SharedPreferences appPreferences;
    private ActionBar.LayoutParams layoutParams;
    private int dayOfWeek, week, schoolID;
    private TabsPagerAdapter mTabsPagerAdapter;
    private ViewPager mViewPager;

    private String freeTextBox;
    private Toolbar toolbar;
    private View spinnerContainer;

    private schemaDrawerItem drawerItem;

    public boolean active = false;


    private FragmentActivity mContext;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url schedule URL Parameter.
     * @return A new instance of fragment PagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerFragment newInstance(String url) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        //spinnerContainer = inflater.inflate(R.layout.toolbar_spinner, toolbar, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = (Toolbar)view.findViewById(R.id.ToolBar);

        mViewPager = (ViewPager)view.findViewById(R.id.pager);

        //prepareForDownload();
        initDayWeek();
        initVars();
        DownloadTodaysSchedule();

        //drawerItem = new schemaDrawerItem(mURL);
        //Log.i(APP_NAME, "school: " + drawerItem.getSchool(getActivity()));
        //Log.i(APP_NAME, "freetextbox: " + drawerItem.getFreeTextBox());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mContext = (FragmentActivity)activity;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void DownloadAndUpdate()
    {
        //mViewPager.setCurrentItem(((5 * week)-(5-dayOfWeek)));
        mListener.setActionBarTitle();
        mTabsPagerAdapter.notifyDataSetChanged();
    }

    public void DownloadTodaysSchedule()
    {
        mViewPager.setCurrentItem(((5 * week)-(5-dayOfWeek)));
        //mListener.updateSpinner(week-1,true);
        mListener.setActionBarTitle();
        //mTabsPagerAdapter.notifyDataSetChanged();
    }

    public void setPosition(int position)
    {
        mViewPager.setCurrentItem(position);
    }

    public void setPosition(int position, boolean animate)
    {
        mViewPager.setCurrentItem(position,animate);
    }

    public void initDayWeek()
    {
        dayOfWeek = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
        week = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);


        Log.i(APP_NAME, "År: " + (new GregorianCalendar().get(Calendar.YEAR)));


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

        Log.i(APP_NAME, "dayofWeek: " + dayOfWeek);
        Log.i(APP_NAME, "week: " + week);
        Log.i(APP_NAME, "viewPager Start position: " + ((5 * week)-(5-dayOfWeek)));
    }

    public void initVars()
    {

        //Få spinner att ligga till höger i ActionBar
        layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.END);

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);



        //getSupportActionBar().setDisplayShowTitleEnabled(true);


        //mSpinner = new Spinner(this);




        mTabsPagerAdapter = new TabsPagerAdapter(getChildFragmentManager(), mURL);

        mPageChangeListener = new myPageChangeListener(mTabsPagerAdapter.getCount(), mTabsPagerAdapter, this);
        //mPageChangeListener.mTabsPagerAdapter = mTabsPagerAdapter;

        mViewPager.setAdapter(mTabsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);  //Max 5 stycken inladdade dagar åt gången
        mViewPager.setOnPageChangeListener(mPageChangeListener);

        //getActionBar().setCustomView(mSpinner, layoutParams);
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);



    }

    public void prepareForDownload()
    {
        int width = 0;
        int height = 0;
        schoolID = 0;
        freeTextBox = null;

        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        Display display = getActivity().getWindowManager().getDefaultDisplay();
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


        Log.i(APP_NAME, "Screen width: " + Integer.toString(width));

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

        mURL = mURL.replace("&schoolid=", ("&schoolid=" + schoolID));
        mURL = mURL.replace("&id=", ("&id=" + freeTextBox));
        mURL = mURL.replace("&width=", ("&width=" + width));
        mURL = mURL.replace("&height=", "&height=" + height);
        mURL = mURL.replace("Å", "%C3%85");
        mURL = mURL.replace("Ä", "%C3%84");
        mURL = mURL.replace("Ö", "%C3%96");

        Log.i(APP_NAME, ("URL: " + mURL));

        Log.i(APP_NAME, "freeTextBox: " + freeTextBox);
        Log.i(APP_NAME, "New GregorianCalendarWeek: " + new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));
    }

    public void updateURL()
    {
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        int oldSchoolID = schoolID;
        String oldFreeTextBox = freeTextBox;

        try {

            schoolID = Integer.parseInt(appPreferences.getString("school_key", null).trim());
            freeTextBox = appPreferences.getString("freeTextBox_key", null);

            Log.i(APP_NAME, "new freeTextBox: " + freeTextBox);
            Log.i(APP_NAME, "new schoolID: " + schoolID);

            if (schoolID != oldSchoolID || !freeTextBox.equals(oldFreeTextBox))
            {
                if (oldFreeTextBox != null)
                {

                    mURL = mURL.replace(("&id=" + oldFreeTextBox), ("&id=" + freeTextBox));
                }
                else
                {

                    mURL = mURL.replace("&id=", ("&id=" + freeTextBox));
                }

                if (oldSchoolID != 0)
                {
                    mURL = mURL.replace(("&schoolid=" + oldSchoolID), ("&schoolid=" + schoolID));
                }
                else
                {
                    mURL = mURL.replace("&schoolid=", ("&schoolid=" + schoolID));
                }
            }

            Log.i(APP_NAME, "new url: " + mURL);

            mTabsPagerAdapter.updateURL(mURL);
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

    public String getFreeTextBox()
    {
        return freeTextBox;
    }

    public int getSchoolID()
    {
        return schoolID;
    }

    public boolean getStatus()
    {
        return active;
    }

    @Override
    public void updateSpinner(int week, boolean spinnerByCode) {
        mListener.updateSpinner(week,spinnerByCode);
    }

    @Override
    public void setViewPagerPosition(int position, boolean animate) {
        setPosition(position,animate);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void setActionBarTitle();
        public void updateSpinner(int week, boolean spinnerByCode);
    }

}
