package com.game.tristar.resource;

/*
* @author Huy Vu
*/

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.ImageView;

public abstract class Photo extends ImageView {

    protected Paint paint = new Paint();
    protected String imagefilename;
    protected int timeAllow;
    protected int scoregain;
    protected Context photoContext;
    protected String curPackage = "";
    
    public Photo(Context context) {
        super(context);
        photoContext = context;
        paint.setColor(Color.RED);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
    }
    
    public void setResourceOn(String curPackage){
        this.curPackage = curPackage;
    }
    
    public void setResource(String filename){
        imagefilename = filename;
    }
    
    public abstract void clearAll();
    
    public int getTimeAllow(){
        return timeAllow;
    }
    
    public int getScoreGain(){
        return scoregain;
    }
    
    public void updatePartition(int index, String curPackage){}
    
    public int getNextPart(){return -1;}
    
    public abstract ArrayList<String> getColorFiles();
    
    public abstract ArrayList<String> getBlackFiles();
    
    public void doneOne(){}
    
    public abstract int getCurrentPart();
    
    public abstract boolean isGoodTouch();
    
    public abstract void solveHint();
    
    public abstract boolean isDone();
    
    public abstract void updateView(MotionEvent event);
    
    public abstract void resetTouch();
    
    
    
}