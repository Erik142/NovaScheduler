package com.erik.novascheduler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;

public class DownloadTask extends AsyncTask<String, Void, Void> {
	
	public static boolean isActive = false;
	
	private Context context;
	private int imageWidth, imageHeight;
	private ProgressDialog mDialog;
	private int schoolID;
	private String freeTextBox;
	private TabsPagerAdapter mTabsPagerAdapter;
	private int week, width, height;
	private Activity mainActivity;
	
	public DownloadTask (Context c, Activity mainActivity, TabsPagerAdapter tpa, int week)
	{

		mTabsPagerAdapter = tpa;
		context = c;
		mDialog = new ProgressDialog(context);
		this.week = week;
		this.mainActivity = mainActivity;
	}
	
	protected void onPreExecute() {
		this.mDialog.setCanceledOnTouchOutside(false);
        this.mDialog.setMessage("Laddar scheman...");
        this.mDialog.show();
    }
	
	protected Void doInBackground (String... params)
	{
		if (WorkerClass.connectedToInternet(context))
		{
			
		Log.i("NovaScheduler", "WorkerClass.week: " + WorkerClass.week);
		isActive = true;
		Display display = mainActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x * 5;
		
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
		
		
		Bitmap scheduleBitmap = null;
		
		String stringUrl = params[0];
		
		imageWidth = width;
		imageHeight = height;
		
		WorkerClass.imageWidth = imageWidth;
		WorkerClass.imageHeight = imageHeight;
		
		
		
		try {
			
			schoolID = Integer.parseInt(WorkerClass.appPreferences.getString("school_key", null).trim());
			freeTextBox = WorkerClass.appPreferences.getString("freeTextBox_key", null);
			
			
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
		
		stringUrl = stringUrl.replace("&schoolid=", ("&schoolid=" + schoolID));
		stringUrl = stringUrl.replace("&id=", ("&id=" + freeTextBox));
		stringUrl = stringUrl.replace("&width=", ("&width=" + imageWidth));
		stringUrl = stringUrl.replace("&height=", "&height=" + imageHeight);
		//stringUrl = stringUrl.replace("week=", ("week=" + week));
		stringUrl = stringUrl.replace("Å", "%C3%85");
		stringUrl = stringUrl.replace("Ä", "%C3%84");
		stringUrl = stringUrl.replace("Ö", "%C3%96");
		
		WorkerClass.urlString = stringUrl;
		
		//Log.i("school_key", mPreferences.getString("school_key", null));
		//Log.i("freeTextBox_key", mPreferences.getString("freeTextBox_key", null));
		Log.i("NovaScheduler", ("URL: " + stringUrl));
		
		Log.i("NovaScheduler", "freeTextBox: " + freeTextBox);
		Log.i("NovaScheduler", "New GregorianCalendarWeek: " + new GregorianCalendar().get(Calendar.WEEK_OF_YEAR));
		
		
		InputStream io;
		
		
		
		try {
			io = new URL(stringUrl).openStream();
			scheduleBitmap = BitmapFactory.decodeStream(new BufferedInputStream(io));
			io.close();
			WorkerClass.scheduleBitmap = WorkerClass.cropImage(scheduleBitmap, (imageWidth-5), (imageHeight));
			WorkerClass.firstTime = false;
			WorkerClass.isSchedule = true;
			
		} catch (IOException e) {
			// TODO: handle exception
			Log.e("NovaScheduler", ("Save image: " + e.toString()));
			WorkerClass.isConnected = false;
		}
		}
		
		else 
		{
			WorkerClass.isConnected = false;
		}
		
	return null;
}

		
	
	
	@Override
    protected void onPostExecute(Void v) {
		
		mTabsPagerAdapter.notifyDataSetChanged();
		
		if (this.mDialog.isShowing())
		{
			this.mDialog.dismiss();
		}
		/*if (!WorkerClass.isConnected)
		{
			Toast.makeText(context, "Du är inte ansluten till internet...", Toast.LENGTH_SHORT).show();
		}*/
		isActive = false;
    	
    }
	
	
	

}
