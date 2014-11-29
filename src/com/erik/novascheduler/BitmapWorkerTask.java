package com.erik.novascheduler;

import java.lang.ref.WeakReference;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

	//private Map<Integer, WeakReference<ImageView>> mImageViewReferenceList;
	private final WeakReference<ImageView> mImageViewReference;
	private int index;
	
	public BitmapWorkerTask (ImageView imageView)
	{
		//mImageViewReferenceList = new HashMap<Integer, WeakReference<ImageView>>();
		mImageViewReference = new WeakReference<ImageView>(imageView);
		//mImageViewReferenceList.put(week, mImageViewReference);
	}
	
	@Override
	protected Bitmap doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		//Log.i("NovaScheduler", ("Params[0] = " + params[0]));
		index = (params[0]);
		/*if (params[0] == 0)
		{
			return loadBitmap(5);
		}
		else {
			return loadBitmap(params[0]);
		}*/
		return null;
		
	}
	
	@Override
	protected void onPostExecute(Bitmap image)
	{
		if (image != null && mImageViewReference != null)
		{
			final ImageView mImageView = mImageViewReference.get();
			//mImageView.setImageBitmap(image);
			
			mImageView.setScaleType(ScaleType.FIT_XY);
			Log.i("NovaScheduler", ("Successfully applied bitmap " + (index + 1)));
		}
		else if (image == null)
		{
			final ImageView mImageView = mImageViewReference.get();
			mImageView.setImageResource(R.drawable.noschedule);
			mImageView.setScaleType(ScaleType.FIT_XY);
		}
		else
		{
			
		Log.i("NovaScheduler", "Something is null...");
		}
	}
	
	/*private Bitmap loadBitmap (int arg)
	{
		Bitmap resizedBitmap = null;
        	
        if (index >= 1)
        {
        	
        try {
			if (WorkerClass.scheduleBitmap != null)
			{
				resizedBitmap = getResizedBitmap(WorkerClass.scheduleBitmap, 95, 5, ((index-1) % 5));
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
        	Log.i("NovaScheduler", ("Resize bitmap: " + e.toString()));
					}
        
        }
        
        return resizedBitmap;
	}*/
	
public Bitmap getResizedBitmap(Bitmap bm, double heightPercent, int amount, int position) {
	    
		Bitmap resizedBitmap = null;
		
		try {
				
	
		int width = bm.getWidth();
	    int height = bm.getHeight();
	    
	    int startwidth = (width/5)*position;
	    
	    //Log.i("NovaScheduler", "Bitmap Start width: " + startwidth);
	    
	    //Log.i("NovaScheduler", ("Bitmap width: " + width));
	    //Log.i("NovaScheduler", ("Bitmap height: " + height));
	    
	    int scaleWidth = (int)(width/amount);
	    int scaleHeight = (int)((heightPercent / 100) * height);
	    
	    //Log.i("NovaScheduler", ("Scaled Bitmap width: " + scaleWidth));
	    //Log.i("NovaScheduler", ("Scaled Bitmap height: " + scaleHeight));

	    resizedBitmap = Bitmap.createBitmap(bm, startwidth, height-scaleHeight, scaleWidth, scaleHeight, null, false);
		}
		catch (NullPointerException e)
		{
			resizedBitmap = null;
		}
		return resizedBitmap;
	    
		}

}