package com.erik.novascheduler;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mySpinnerAdapter extends BaseAdapter {

	private List<String> mList;
	private LayoutInflater mInflater;
	
	public mySpinnerAdapter(Context parentContext, List<String> stringList)
	{
		mList = stringList;
		mInflater = (LayoutInflater)parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View viewToConvert, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		view = mInflater.inflate(R.layout.spinnerdropdownitem, parent, false);
		TextView tView = (TextView)view.findViewById(R.id.dropDownItem);
		tView.setText(mList.get(position));
		
		return view;
		
		
		
	}

	

}
