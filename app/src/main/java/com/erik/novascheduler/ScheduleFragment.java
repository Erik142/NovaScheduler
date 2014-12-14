package com.erik.novascheduler;

import com.squareup.picasso.Picasso;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScheduleFragment extends Fragment implements UpdateableFragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
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
        //position = WorkerClass.mViewPager.getCurrentItem();
		//position = WorkerClass.position;
		//position -= 1;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.i("NovaScheduler", ("arg = " + argPosition));
        View rootView = inflater.inflate(R.layout.scheduleactivity, container, false);
        mImageView = (ImageView)rootView.findViewById(R.id.mImageView);

        loadBitmap();

        return rootView;
    }

    private void loadBitmap()
    {
        try {
            int week;

            String urlString;


			/*if (position >= 0 && position <=10)
			{
				week = (position / 5) + 1;
                Log.i("NovaScheduler", "Apply bitmap, week: " + week);
			}*/
            if (argPosition % 5 == 0)
            {
                week = (argPosition / 5);
                Log.i("NovaScheduler", "Apply bitmap, week: " + week);
            }
            else {
                week = (argPosition / 5) + 1;
                Log.i("NovaScheduler", "Apply bitmap, week: " + week);
            }

            url = url.replace("week=", ("week=" + week));
            Log.i("NovaScheduler", "weekURL: " + url);
            //Log.i("NovaScheduler", "ScheduleFragment, newPosition: " + newPosition);

            if (argPosition == NUM_PAGES)
            {

                Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, 0, 95,52)).into(mImageView);
            }
            else if (argPosition == 0)
            {
                Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, 4, 95,1)).into(mImageView);
            }
            else
            {
                Picasso.with(getActivity()).load(url).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, ((argPosition-1) % 5), 95,week)).into(mImageView);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Picasso.with(getActivity()).load(R.drawable.noschedule);
        }
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		loadBitmap();
    }
	
	
	}


