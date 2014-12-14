package com.erik.novascheduler;

import android.app.Application;
import android.content.Context;

public class NovaScheduler extends Application {

	private static Context context;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
	}
	
	public Context getAppContext()
	{
		return NovaScheduler.context;
	}
	
}
