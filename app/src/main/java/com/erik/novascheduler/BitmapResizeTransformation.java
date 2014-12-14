package com.erik.novascheduler;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Transformation;

public class BitmapResizeTransformation implements Transformation
{
	
	private int amount, position, week;
	private double heightPercent;
	
	public BitmapResizeTransformation(int amount, int position, double heightPercent, int week)
	{
		this.amount = amount;
		this.position = position;
		this.heightPercent = heightPercent;
        this.week = week;
	}
	
	@Override 
	public Bitmap transform(Bitmap bm)
	{
		Bitmap resizedBitmap = null;
		
		try {
				
	
		int width = (bm.getWidth() - 5);
	    int height = bm.getHeight();
	    
	    
	    
	    int startwidth = (width/5)*position;
	    
	    //Log.i("NovaScheduler", "Bitmap Start width: " + startwidth);
	    
	    //Log.i("NovaScheduler", ("Bitmap width: " + width));
	    //Log.i("NovaScheduler", ("Bitmap height: " + height));
	    
	    int scaleWidth = (int)(width/amount);
	    int scaleHeight = (int)((heightPercent / 100) * height);
	    
	    //Log.i("NovaScheduler", ("Scaled Bitmap width: " + scaleWidth));
	    //Log.i("NovaScheduler", ("Scaled Bitmap height: " + scaleHeight));

	    resizedBitmap = Bitmap.createBitmap(cropImage(bm, (width), (height)), startwidth, height-scaleHeight, scaleWidth, scaleHeight, null, false);
		}
		catch (NullPointerException e)
		{
			resizedBitmap = null;
		}
		if (resizedBitmap != bm)
		{
			bm.recycle();
		}
		return resizedBitmap;
	    
	}
	
	@Override
	public String key() { 
		return "bitMap" + (position*week);
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




