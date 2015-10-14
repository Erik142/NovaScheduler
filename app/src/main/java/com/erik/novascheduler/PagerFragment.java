package com.erik.novascheduler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
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

import java.util.Calendar;
import java.util.GregorianCalendar;


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
            try {
                mURL = getArguments().getString(ARG_URL).split(";")[0];
            }
            catch (Exception e)
            {
                mURL = "";
            }
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
        mTabsPagerAdapter.notifyDataSetChanged();
    }

    public void DownloadTodaysSchedule()
    {
        mViewPager.setCurrentItem(((5 * week)-(5-dayOfWeek)));
        mListener.updateSpinner(week - 1);

    }

    public void setPosition(int position)
    {
        mViewPager.setCurrentItem(position);
    }

    public void setPosition(int position, boolean animate)
    {
        mViewPager.setCurrentItem(position, animate);
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
        Log.i(APP_NAME, "viewPager Start position: " + ((5 * week) - (5 - dayOfWeek)));
    }

    public void initVars()
    {

        //Få spinner att ligga till höger i ActionBar
        layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.END);


        mTabsPagerAdapter = new TabsPagerAdapter(getChildFragmentManager(), mURL);

        mPageChangeListener = new myPageChangeListener(mTabsPagerAdapter.getCount(), mTabsPagerAdapter, this);

        mViewPager.setAdapter(mTabsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);  //Max 5 stycken inladdade dagar åt gången
        mViewPager.addOnPageChangeListener(mPageChangeListener);

    }

    public String getFreeTextBox()
    {
        return freeTextBox;
    }

    public int getSchoolID()
    {
        return schoolID;
    }

    @Override
    public void updateSpinner(int week) {
        mListener.updateSpinner(week);
    }

    @Override
    public void setViewPagerPosition(int position, boolean animate) {
        setPosition(position,animate);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void updateSpinner(int week);
    }

}
