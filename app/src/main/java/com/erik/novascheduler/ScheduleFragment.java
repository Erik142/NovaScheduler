package com.erik.novascheduler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;


public class ScheduleFragment extends Fragment implements UpdateableFragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";

    public final String APP_NAME = "NovaScheduler";
	
	private ImageView mImageView;
	private int argPosition, NUM_PAGES;
    private String url;

    public int getPosition()
    {
        return argPosition;
    }

	public static ScheduleFragment newInstace(int position, int NUM_PAGES, String url)
	{
		ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("NUM_PAGES", NUM_PAGES);
        args.putString("url", url);
        fragment.setArguments(args);
        
        return fragment;
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		argPosition = getArguments() != null ? getArguments().getInt("position") : 1;
        NUM_PAGES = getArguments() != null ? getArguments().getInt("NUM_PAGES") : 1;
        url = getArguments().getString("url");
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        //Log.i("NovaScheduler", ("arg = " + argPosition));
        View rootView = inflater.inflate(R.layout.scheduleactivity, container, false);
        mImageView = (ImageView)rootView.findViewById(R.id.mImageView);

        loadBitmap();

        return rootView;
    }

    private void loadBitmap()
    {
        try {
            int week;

            if (argPosition % 5 == 0)
            {
                week = (argPosition / 5);
                Log.i(APP_NAME, "Apply bitmap, week: " + week);
            }
            else {
                week = (argPosition / 5) + 1;
                Log.i(APP_NAME, "Apply bitmap, week: " + week);
            }

            url = url.replace("week=", ("week=" + week));




            Log.i(APP_NAME, "weekURL: " + url);

            if (argPosition == NUM_PAGES+1)
            {

                //Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, 0, 95,52)).into(mImageView);
                url = url.replace("day", ("day=" + getDay(1)));
            }
            else if (argPosition == 0)
            {
                //Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, 4, 95,1)).into(mImageView);
                url = url.replace("day", ("day=" + getDay(5)));
            }
            else
            {
                //Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, ((argPosition-1) % 5), 95,week)).into(mImageView);
                url = url.replace("day=", ("day=" + getDay(argPosition)));
            }

            //Picasso.with(getActivity().getApplicationContext()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(argPosition,95)).into(mImageView);

            //Picasso.with(getActivity().getApplicationContext()).setIndicatorsEnabled(true);
            ImageLoader.getInstance().displayImage(url, mImageView);

        } catch (Exception e) {
            // TODO: handle exception
            //Picasso.with(getActivity().getApplicationContext()).load(R.drawable.noschedule);
            mImageView.setImageResource(R.drawable.noschedule);
        }
    }

    private int getDay(int position)
    {
        return (int)Math.pow(2, ((position - 1) % 5));
    }

    @Override
    public void update(String newUrl) {

            url = newUrl;
            loadBitmap();

    }
}


