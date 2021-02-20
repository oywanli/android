package com.dspread.demoui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerListview extends ListView{
//	private ScrollView parentScrollView;
	public InnerListview(Context context) {
		super(context);
		
		// TODO Auto-generated constructor stub
	}
	

	public InnerListview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}



	public InnerListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	/*public void setParentScrollview(ScrollView parentScrollView){
		this.parentScrollView=parentScrollView;
	}*/
	
	//The method is to let the listview automatically adapt to the height of the scrollview. As the listview increases, the scrollview will become longer
	//the listview  needs to be wrapcontent
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	        MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec);
	    }
	
	 //Let the listview ensure a certain height, so that the listview can slide in a certain position, and the scrollview can also slide
	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setParentScrollAble(false);
			break;
		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	 private void setParentScrollAble(boolean flag) {
	     
		  parentScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
	 }*/
}
