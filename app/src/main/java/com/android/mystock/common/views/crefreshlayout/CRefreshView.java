package com.android.mystock.common.views.crefreshlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.android.mystock.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CRefreshView extends RelativeLayout{
	
    private static final long kloadingIndividualAnimationTiming = 1200;
    private static final float kbarDarkAlpha = 0.4f;
    private static final long kloadingTimingOffset = 100;
    private static final float kdisappearDuration = 0.8f;
    
    private int dropHeight = 100;
    private int lineColor = Color.WHITE;
    private float lineWidth = 3f;
    private float disappearProgress;
    private boolean reverseLoadingAnimation = false;
    private float internalAnimationFactor = 0.6f;
    private int horizontalRandomness = 150;
    public CRefreshLayoutState state = CRefreshLayoutState.CRefreshLayoutStateIdle;
    
    private List<BarItem> barItems;
    private Context mContext;
	private int screenwidth;

	public CRefreshView(Context context) {
		super(context);
		init(context);
	}
	
	public CRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	
	public void init(Context context){
		mContext = context;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenwidth = outMetrics.widthPixels;
		screenwidth = screenwidth/2;

		barItems = new ArrayList<BarItem>();
		List<Point> startPoints = new ArrayList<Point>();
		//加
		startPoints.add(new Point(-100+screenwidth, 100));
		startPoints.add(new Point(-70+screenwidth, 100));
		startPoints.add(new Point(-75+screenwidth, 123));
		startPoints.add(new Point(-85+screenwidth, 85));
		startPoints.add(new Point(-65+screenwidth, 100));
		startPoints.add(new Point(-65+screenwidth, 100));
		startPoints.add(new Point(-38+screenwidth, 100));
		startPoints.add(new Point(-65+screenwidth, 127));
		
		//载
		startPoints.add(new Point(-20+screenwidth, 90));
		startPoints.add(new Point(-27+screenwidth, 100));
		startPoints.add(new Point(-10+screenwidth, 83));
		startPoints.add(new Point(-25+screenwidth, 107));
		startPoints.add(new Point(-10+screenwidth, 100));
		startPoints.add(new Point(-25+screenwidth, 120));
		startPoints.add(new Point(-10+screenwidth, 110));
		startPoints.add(new Point(-25+screenwidth, 130));
		startPoints.add(new Point(5+screenwidth, 85));
		startPoints.add(new Point(17+screenwidth, 87));
		startPoints.add(new Point(23+screenwidth, 123));
		//中
		startPoints.add(new Point(35+screenwidth, 100));
		startPoints.add(new Point(35+screenwidth, 100));
		startPoints.add(new Point(85+screenwidth, 100));
		startPoints.add(new Point(40+screenwidth, 115));
		startPoints.add(new Point(60+screenwidth, 83));
		
		List<Point> endPoints = new ArrayList<Point>();
		endPoints.add(new Point(-70+screenwidth, 100));
		endPoints.add(new Point(-70+screenwidth, 130));
		endPoints.add(new Point(-70+screenwidth, 130));
		endPoints.add(new Point(-90+screenwidth, 130));
		endPoints.add(new Point(-65+screenwidth, 127));
		endPoints.add(new Point(-38+screenwidth, 100));
		endPoints.add(new Point(-38+screenwidth, 127));
		endPoints.add(new Point(-38+screenwidth, 127));
		//载
		endPoints.add(new Point(screenwidth, 90));
		endPoints.add(new Point(20+screenwidth, 100));
		endPoints.add(new Point(-10+screenwidth, 100));
		endPoints.add(new Point(screenwidth, 107));
		endPoints.add(new Point(-25+screenwidth, 120));
		endPoints.add(new Point(5+screenwidth, 118));
		endPoints.add(new Point(-1+screenwidth, 140));
		endPoints.add(new Point(5+screenwidth, 128));
		endPoints.add(new Point(20+screenwidth, 140));
		endPoints.add(new Point(23+screenwidth, 95));
		endPoints.add(new Point(5+screenwidth, 137));
		//中
		endPoints.add(new Point(40+screenwidth, 115));
		endPoints.add(new Point(85+screenwidth, 100));
		endPoints.add(new Point(80+screenwidth, 115));
		endPoints.add(new Point(80+screenwidth, 115));
		endPoints.add(new Point(60+screenwidth, 140));

		lineColor = ContextCompat.getColor(context, R.color.black_skin);

		for(int i = 0; i < startPoints.size(); i++){
			Point startP = startPoints.get(i);
			Point endP = endPoints.get(i);
			BarItem item = new BarItem(getContext(), startP, endP, lineColor, lineWidth);
			item.setTag(Integer.valueOf(i));
			item.setBackgroundColor(Color.TRANSPARENT);
			item.setAlpha(0f);
			barItems.add(item);
			this.addView(item);
			item.setHorizontalRandomness(this.horizontalRandomness, this.dropHeight);
		}
		
		for (BarItem barItem : this.barItems) {
			barItem.setupFrame();
	    }
	}
	
	public void updateBarItemsWithProgress(float progress){
		Log.v("CRefreshLayout", "updateBarItemsWithProgress dragPercent" + progress);
	    for (BarItem barItem : this.barItems) {
	        int index = this.barItems.indexOf(barItem);
	        float startPadding = (1 - this.internalAnimationFactor) / this.barItems.size() * index;
	        float endPadding = 1 - this.internalAnimationFactor - startPadding;
	        
	        barItem.resetMatrix();
	        if (progress == 1 || progress >= 1 - endPadding) {
	            barItem.setAlpha(kbarDarkAlpha);
	        }else if (progress == 0) {
	            barItem.setHorizontalRandomness(this.horizontalRandomness, this.dropHeight);	        
	        }else {
	            float realProgress;
	            if (progress <= startPadding)
	                realProgress = 0;
	            else
	                realProgress = Math.min(1, (progress - startPadding)/this.internalAnimationFactor);
	            barItem.preMatrixTranslate(barItem.translationX*(1-realProgress), -this.dropHeight*(1-realProgress));
	            barItem.preMatrixScale(1.0f*realProgress, 1.0f*realProgress);
	            barItem.preMatrixRotate(-(float)Math.PI*realProgress);
	            barItem.invalidate();
	            barItem.setAlpha(realProgress*kbarDarkAlpha);
	        }
	    }
	}

	public void startLoadingAnimation(){
	    if (this.reverseLoadingAnimation) {
	        int count = (int)this.barItems.size();
	        for (int i= count-1; i>=0; i--) {
	        	final BarItem barItem = this.barItems.get(i);
	            new Handler().postDelayed(new Runnable(){  
	                public void run() {  
	                	CRefreshView.this.barItemAnimation(barItem);
	                }  
	             }, (this.barItems.size()-i-1)*kloadingTimingOffset);
	        }
	    }else {
	        for (int i=0; i<this.barItems.size(); i++) {
	        	final BarItem barItem = this.barItems.get(i);
	            new Handler().postDelayed(new Runnable(){  
	                public void run() {  
	                	CRefreshView.this.barItemAnimation(barItem);
	                }  
	             }, i*kloadingTimingOffset);
	        }
	    }
	}

	private void barItemAnimation(BarItem barItem){
	    if (this.state == CRefreshLayoutState.CRefreshLayoutStateRefreshing){
	    	barItem.setAlpha(1f);
	        barItem.clearAnimation();
	        
	        Animation alphaA = new AlphaAnimation(1f, kbarDarkAlpha);
	        alphaA.setDuration(kloadingIndividualAnimationTiming);
	        alphaA.setFillAfter(true);
	        barItem.startAnimation(alphaA);
	        boolean isLastOne;
	        if (this.reverseLoadingAnimation)
	            isLastOne = (Integer)barItem.getTag() == 0;
	        else
	            isLastOne = (Integer)barItem.getTag() == this.barItems.size()-1;
	            
	        if (isLastOne && this.state == CRefreshLayoutState.CRefreshLayoutStateRefreshing) {
	            this.startLoadingAnimation();
	        }
	    }
	}

	public void updateDisappearAnimation(){
	    if (this.disappearProgress >= 0 && this.disappearProgress <= 1) {
	        this.disappearProgress -= 1/30.f/kdisappearDuration;
	        this.updateBarItemsWithProgress(this.disappearProgress);
	    }
	}
    
	public void finishingLoading(){
	    this.state = CRefreshLayoutState.CRefreshLayoutStateDisappearing;
        this.disappearProgress = 1;
	    for (BarItem barItem : this.barItems) {
	        barItem.clearAnimation();
	        barItem.setAlpha(kbarDarkAlpha);
	    }
	    
	    updateDisappearProgress();
	}
	
	public void updateDisappearProgress(){
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(disappearProgress <= 0){
					timer.cancel();
					timer.purge();
				}
				synchronized (this) {//must make sure thread is synchronize
					((Activity) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateDisappearAnimation();
						}
					});
				}
			}
		}, 0, 1000/30);
	}
	
    public enum CRefreshLayoutState{
    	CRefreshLayoutStateIdle,
    	CRefreshLayoutStateRefreshing,
    	CRefreshLayoutStateDisappearing
    }
    
    public void setDisappearProgress(float disappearProgress){
    	this.disappearProgress = disappearProgress;
    }
}
