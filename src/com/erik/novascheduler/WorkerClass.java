package com.erik.novascheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract.Constants;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;

public class WorkerClass {
	
	public static boolean isSchedule = false;
	public static boolean firstTime = true;
	public static boolean isConnected = false;
	public static Bitmap scheduleBitmap = null;
	public static SharedPreferences appPreferences = null;
	public static int week = 0;
	public static Context context = null;
	public static String urlString = null;
	public static int imageWidth, imageHeight;
	public static Spinner mSpinner;
	public static boolean spinnerItemSelected = false;
	public static int position;
	public static myPageChangeListener mPageChangeListener;
	
	public static void loadBitmap (int position, ImageView mImageView)
	{
		BitmapWorkerTask mWorker = new BitmapWorkerTask(mImageView);
		mWorker.execute(position);
	}
	
	public static boolean connectedToInternet(Context context)
	{
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	public static Bitmap cropImage (Bitmap imgBitmap, int x, int y)
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
	
	

}
