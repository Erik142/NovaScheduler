package com.erik.novascheduler;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

	private int schoolID;
	private String freeTextBox;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			
			schoolID = Integer.parseInt(mPreferences.getString("school_key", null));
			freeTextBox = mPreferences.getString("freeTextBox_key", null);
			
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
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if (schoolID != Integer.parseInt(mPreferences.getString("school_key", null)) || freeTextBox != mPreferences.getString("freeTextBox_key", null))
		{
			if (MainActivity.class.isInstance(MainActivity.mainActivity))
			{
				((MainActivity)MainActivity.mainActivity).DownloadAndUpdate();
				((MainActivity)MainActivity.mainActivity).setActionBarTitle();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
		case R.id.accept_settings:
		
			this.finish();
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
