package com.android.mystock.common.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.android.mystock.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
下流星雨效果
 */
public class MeteorView extends View {
	List<Object[]> meters = new ArrayList<Object[]>();
	float left;
	float top;
	int maxNum = 5;
	int mHeight;
	int mWidth;
	Random random;
	public MeteorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		random=new Random();
	}

	public MeteorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        intMeteor();
        for(Object[] temp:meters){ 
        	Paint vPaint = new Paint();  
            vPaint .setStyle( Paint.Style.STROKE );   //空心  
            vPaint .setAlpha(((int)temp[4]+10)*200/100);   //
            canvas.drawBitmap((Bitmap)temp[3], (float)temp[0], (float)temp[1], vPaint);
            
        }
        meteorRain();
	}
	/*
	 * 重绘任务
	 */
	public void postPath() {
		post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});
	}
	/*
	 * 下流星雨
	 */
	public void meteorRain(){
		pathPoint();
		postPath();
	}
	/*
	 * 随机初始化流行的生成点和大小以及透明度
	 */
	public void intMeteor(){
		
		if(meters.size() < maxNum){
			int createflag = random.nextInt(1000000);
			if(createflag > 490000 && createflag < 500000){
				int flag = random.nextInt(2);
				if(flag == 0){
					flag = -1;
				}else{
					flag = 1;
				}
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.meteor);
				float tempX = getWidth()/6 + random.nextInt((int) (getWidth()*1.2));
				float tempY = (float) -((Math.tan(Math.toRadians(30))*((float)tempX+200f)) - (Math.tan(Math.toRadians(30))*(float)tempX));
				int scale = 40 + random.nextInt(50);
				int speedScale = 100 + random.nextInt(80);
				Object[] object = {tempX+200f,tempY,(Math.tan(Math.toRadians(30))*(float)tempX),small(bmp,scale),scale,(tempX+200f)/speedScale};
				meters.add(object);
			}
		}
	}
	/*
	 * 计算轨迹上的点
	 */
	public void pathPoint(){
		
		for(int i=0;i<meters.size();i++){
			Object[] temp = meters.get(i);
			float tempX = (float) ((float)temp[0]- (float)temp[5]);
			float tempY = (float) -((Math.tan(Math.toRadians(30))*(float)tempX) - (double)temp[2]);
			Object[] newtemp = {tempX,tempY,temp[2],temp[3],temp[4],temp[5]};
			if(tempY > getHeight() || tempX < -300){
				((Bitmap)temp[3]).recycle();
				temp = null;
				meters.remove(i);
				i--;
			}else{
				meters.set(i, newtemp);
			}
			
        }
	}
	/*
	 * 缩放bitmap
	 */
	private  Bitmap small(Bitmap sourceImg,int scale) {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(scale/100f , scale/100f); //长和宽放大缩小的比例
		  sourceImg = Bitmap.createBitmap(sourceImg,0,0,sourceImg.getWidth(),sourceImg.getHeight(),matrix,true);
		  return sourceImg;
	}
}
