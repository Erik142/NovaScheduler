package com.erik.novascheduler;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class BitmapResizeTransformation implements Transformation
{
	
	//private final int amount, position, week;
    final int position;
	private final double heightPercent;
	
	public BitmapResizeTransformation(int position, double heightPercent)
	{
		//this.amount = amount;
		this.position = position;
		this.heightPercent = heightPercent;
        //this.week = week;
	}
	
	@Override 
	public Bitmap transform(Bitmap bm)
	{
		Bitmap resizedBitmap = null;
		
		try {

		int width = (bm.getWidth() - 5);
	    int height = bm.getHeight();

	    //int startwidth = (width/5)*position;
	    //int scaleWidth = (int)(width/amount);
        int startwidth = 0;
        int scaleWidth = width;
	    int scaleHeight = (int)((heightPercent / 100) * height);

	    //resizedBitmap = Bitmap.createBitmap(cropImage(bm, (width), (height)), startwidth, height-scaleHeight, scaleWidth, scaleHeight, null, false);
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
	/*public String key() {
		return "bitMap" + ((position + 1) * week);
		}*/
    public String key() {
        return "bitMap" + (position);
    }

    private static Bitmap cropImage (Bitmap imgBitmap, int x, int y)
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




