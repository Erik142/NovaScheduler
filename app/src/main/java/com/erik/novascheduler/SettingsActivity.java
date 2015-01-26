package com.erik.novascheduler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
		
		if (!isSettingsEmpty())
		{
			if (MainActivity.class.isInstance(MainActivity.mainActivity))
			{
                if (!((MainActivity)MainActivity.mainActivity).getStatus()) {
                    ((MainActivity) MainActivity.mainActivity).initDayWeek();
                    ((MainActivity) MainActivity.mainActivity).initVars();
                    ((MainActivity) MainActivity.mainActivity).prepareForDownload();
                    ((MainActivity) MainActivity.mainActivity).DownloadTodaysSchedule();
                    ((MainActivity) MainActivity.mainActivity).setActionBarTitle();
                }
                else
                {
                    ((MainActivity)MainActivity.mainActivity).updateURL();
                    ((MainActivity) MainActivity.mainActivity).setActionBarTitle();
                }
			}
            else
            {
                startActivity(new Intent(this, MainActivity.class));
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

            if (!isSettingsEmpty()) {
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Det saknas fortfarande nödvändiga inställningar för att visa schemat!", Toast.LENGTH_SHORT).show();
            }
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onBackPressed() {
        if (!isSettingsEmpty())
        {
            finish();
        }
        else
        {
            Toast.makeText(this, "Det saknas fortfarande nödvändiga inställningar för att visa schemat!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSettingsEmpty()
    {
        try
        {
            schoolID = Integer.parseInt(mPreferences.getString("school_key", null));
            freeTextBox = mPreferences.getString("freeTextBox_key", null);
            return false;
        }
        catch (Exception e)
        {
            return true;
        }
    }

}
