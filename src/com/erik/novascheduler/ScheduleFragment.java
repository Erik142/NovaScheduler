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
	private int position;
	
	public static ScheduleFragment newInstace(int position)
	{
		final ScheduleFragment fragment = new ScheduleFragment();
        final Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        
        return fragment;
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		position = getArguments().getInt("position");
		//position = WorkerClass.position;
		//position -= 1;
		Log.i("NovaScheduler", ("arg = " + position));
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.scheduleactivity, container, false);
        mImageView = (ImageView)rootView.findViewById(R.id.mImageView);
        
        return rootView;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		
			//WorkerClass.loadBitmap(position, mImageView);
			
			/*BitmapResizeTransformation transformation = new BitmapResizeTransformation();
			transformation.amount = 5;
			transformation.heightPercent = 95;
			transformation.position = ((position - 1) % 5);*/
		try {
			int week;
			String urlString;
			
			if (position % 5 == 0)
			{
				week = position / 5;
				Log.i("NovaScheduler", "Apply bitmap, week: " + week);
			}
			else {
				week = (position / 5) + 1;
				Log.i("NovaScheduler", "Apply bitmap, week: " + week);
			}
			
			urlString = WorkerClass.urlString;
			urlString = urlString.replace("week=", ("week=" + week));
			
			Picasso.with(WorkerClass.context).load(urlString).placeholder(R.drawable.noschedule).transform(new BitmapResizeTransformation(5, ((position-1) % 5), 95)).into(mImageView);
			WorkerClass.mSpinner.setSelection((week-1));
			//WorkerClass.mSpinner.setSelected(false);
			
		} catch (Exception e) {
			// TODO: handle exception
			Picasso.with(WorkerClass.context).load(R.drawable.noschedule);
		}
		
		
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//WorkerClass.loadBitmap(position, mImageView);
		
		/*BitmapResizeTransformation transformation = new BitmapResizeTransformation();
		transformation.amount = 5;
		transformation.heightPercent = 95;
		transformation.position = ((position - 1) % 5);*/
		
		int week;
		String urlString;
		
		if (position % 5 == 0)
		{
			week = position / 5;
		}
		else {
			week = (position / 5) + 1;
		}
		urlString = WorkerClass.urlString;
		urlString = urlString.replace("week=", ("week=" + week));
		
		
		Picasso.with(WorkerClass.context).load(urlString).transform(new BitmapResizeTransformation(5, ((position - 1) % 5), 95)).into(mImageView);
	}
	
	
	}


