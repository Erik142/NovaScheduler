package com.erik.novascheduler;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erikwahlberger on 2015-02-01.
 */
public class schemaDrawerItem {


    private String url;
    private static String baseURL = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=/sv-se&type=-1&id=&period=&week=&mode=2&printer=0&colors=32&head=0&clock=0&foot=0&day=&width=&height=";

    public static final String APP_NAME = "NovaScheduler";

    public schemaDrawerItem(String url)
    {
        this.url = url;
    }

    public String getSchool(Activity activity)
    {
        try {
            Pattern p = Pattern.compile("&schoolid=(.+?)/");
            Matcher m = p.matcher(url);

            if (m.find()) {
                Log.i(APP_NAME, "Found matching id");
                String school = m.group(1);
                //Log.i(APP_NAME, "school = " + school);
                Resources res = activity.getResources();
                String[] schools = res.getStringArray(R.array.schoolArrayValues);

                for (int i = 0; i < schools.length; i++) {
                    //Log.i(APP_NAME, "schools[" + i + "] = " + schools[i]);
                    if (school.equals(schools[i])) {

                        return res.getStringArray(R.array.schoolArray)[i];
                    }
                }
                return null;

            } else {
                Log.i(APP_NAME, "Did not find matching id");
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(APP_NAME, e.toString());
            return null;
        }
    }

    public String getURL()
    {
        return url;
    }

    public String getFreeTextBox()
    {
        Pattern p = Pattern.compile("&id=(.+?)&");
        Matcher m = p.matcher(url);

        if (m.find())
        {
            return m.group(1);
        }
        else
        {
            return null;
        }
    }

    public static String getDefaultURL(ArrayList<String> URLList)
    {
        String[] tempArray;

        for (int i = 0; i < URLList.size(); i++)
        {
            tempArray = URLList.get(i).split(";");
            Log.i(APP_NAME, "tempArray[" + 0 + "] = " + tempArray[0]);
            Log.i(APP_NAME, "tempArray[" + 1 + "] = " + tempArray[1]);
            if (tempArray[1].equals("default=true"))
            {
                return tempArray[0];
            }
        }

        return null;
    }

    public static String buildURL(Activity activity,String freeTextBox, String school)
    {
        String returnURL = baseURL;

        int width = 0;
        int height = 0;
        //schoolID = 0;
        //freeTextBox = null;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //width = size.x * 5;
        width = size.x;

        if (size.x >= 1080)
        {
            width = (width/3);
        }
        else
        {
            width = width/2;
        }


        Log.i(APP_NAME, "Screen width: " + Integer.toString(width));

        if (size.y >= 1920)
        {
            height = (int)((size.y/3)*(1-(1.8/10.6)));
        }
        else
        {
            height = (int)((size.y/2)*(1-(1.8/10.6)));
        }

        Resources res = activity.getResources();
        String[] schools = res.getStringArray(R.array.schoolArray);

        Log.i(APP_NAME, "schools.length = " + schools.length);

        Log.i(APP_NAME, "school = " + school);
        Log.i(APP_NAME, "freeTextBox = " + freeTextBox);

        for (int i=0; i < schools.length; i++)
        {
            Log.i(APP_NAME, "schools[" + i + "] = " + schools[i]);
            if (school.equals(schools[i]))
            {
                returnURL = returnURL.replace("&schoolid=", ("&schoolid=" + res.getStringArray(R.array.schoolArrayValues)[i]));
                returnURL = returnURL.replace("&id=", ("&id=" + freeTextBox));
                returnURL = returnURL.replace("&width=", ("&width=" + width));
                returnURL = returnURL.replace("&height=", "&height=" + height);
                returnURL = returnURL.replace("Å", "%C3%85");
                returnURL = returnURL.replace("Ä", "%C3%84");
                returnURL = returnURL.replace("Ö", "%C3%96");
                Log.i(APP_NAME, ("URL: " + returnURL));
                return returnURL;
            }
        }

        Log.i(APP_NAME, ("URL: " + returnURL));
        return null;
    }

    public static ArrayList<String> getDefiniteURLs(String[] inputURLs)
    {
        ArrayList<String> definiteURLs = new ArrayList<String>();

        for (int i=0; i < inputURLs.length; i++)
        {
            definiteURLs.add(inputURLs[i].split(";")[0]);
        }

        return definiteURLs;

    }
}
