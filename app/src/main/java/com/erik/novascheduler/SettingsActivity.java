package com.erik.novascheduler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class SettingsActivity extends ActionBarActivity {

    public final String APP_NAME = "NovaScheduler";

	private int schoolID;
	private String freeTextBox;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar)findViewById(R.id.ToolBar);
        toolbar.hideOverflowMenu();
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);



		getFragmentManager().beginTransaction()
        .replace(R.id.content_frame, new SettingsFragment())
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
            this.finish();
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
            Log.i(APP_NAME, "isSettingsEmpty, schoolID = " + schoolID);
            Log.i(APP_NAME, "isSettingsEmpty, freeTextBox = " + freeTextBox);
            if (freeTextBox != "" && schoolID != 0) {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return true;
        }
    }

}
