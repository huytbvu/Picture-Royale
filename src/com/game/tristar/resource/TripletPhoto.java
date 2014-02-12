package com.game.tristar.resource;

import java.util.ArrayList;

import com.game.tristar.activity.PhotoActivity;
import com.game.tristar.activity.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TripletPhoto extends Photo {

    private ArrayList<DifferentSpot> points = new ArrayList<DifferentSpot>();
    private int count = 0;
    private boolean goodTouch = false;
    
    public TripletPhoto(Context context) {
        super(context);
        timeAllow = 45000;
        /*setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()!=MotionEvent.ACTION_DOWN) return false;
                updateView(event);
                ((PhotoActivity)((Activity)photoContext)).updateScore(TripletPhoto.this);
                if(!isGoodTouch()) ((PhotoActivity)((Activity)photoContext)).penalizeWrongTouch();
                if(isDone()){
                // System.gc();
                    
                    //getBackground().setCallback(null);
                    ((PhotoActivity)((Activity)photoContext)).doneOnePhoto();
                //    ((RelativeLayout)((Activity)photoContext).findViewById(R.id.topview)).removeView(TripletPhoto.this);
                    
                    
                }
                return true;
            }
        });*/
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
        scoregain = 0;
        resetTouch();
        float xratio = event.getX()/getWidth();
        float yratio = event.getY()/getHeight();
        for(DifferentSpot p:points){
            if(p.fit(xratio, yratio)){
                if(!p.isHit()){
                    p.hit();
                    p.setPosition(getWidth(), getHeight());
                    count++;
                    scoregain = 100;
                }
                goodTouch = true;
            }
        }
        if(xratio>0.5 && yratio>0.5) goodTouch = true;
        invalidate();
        return true;
    }
    
    
    @Override
    public void updateView(MotionEvent event){
        scoregain = 0;
        resetTouch();
        float xratio = event.getX()/getWidth();
        float yratio = event.getY()/getHeight();
        for(DifferentSpot p:points){
            if(p.fit(xratio, yratio)){
                if(!p.isHit()){
                    p.hit();
                    p.setPosition(getWidth(), getHeight());
                    count++;
                    scoregain = 100;
                }
                goodTouch = true;
            }
        }
        if(xratio>0.5 && yratio>0.5) goodTouch = true;
        invalidate();
    }
    
    
    @Override
    public void setResourceOn(String curpackage){
        super.setResourceOn(curpackage);
        setBackgroundResource(getContext().getResources().getIdentifier(imagefilename, "drawable", curPackage));       
    }
    
    @Override
    public boolean isGoodTouch(){
        return goodTouch;
    }
    
    @Override
    public void resetTouch(){
        goodTouch = false;
    }

    
    public void addPoint(DifferentSpot newDS){
        points.add(newDS);
    }
        
    public ArrayList<DifferentSpot> getPoints(){
        return points;
    }
    
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(DifferentSpot ds:points)
            if(ds.isHit()) canvas.drawCircle(ds.getXposition(), ds.getYpositon(), ds.getRadius(getHeight()), paint);
        
    }
    
    @Override
    public void solveHint(){
        for(int i=0; i<points.size(); i++){
            if(!points.get(i).isHit()){
                points.get(i).hit();
                count++;
                points.get(i).setPosition(getWidth(), getHeight());
                invalidate();
                return;
            }
        }
    }

    @Override
    public boolean isDone() {
        return (count==points.size());
    }
    
    @Override
    public int getScoreGain(){
        return scoregain;
    }
    
    @Override
    public void clearAll(){
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
        for(DifferentSpot ds:points) ds.clear();
        count = 0;
        invalidate();
    }

    @Override
    public ArrayList<String> getColorFiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<String> getBlackFiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCurrentPart() {
        // TODO Auto-generated method stub
        return 0;
    }

}