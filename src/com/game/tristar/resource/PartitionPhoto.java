package com.game.tristar.resource;

import java.util.*;

import com.game.tristar.activity.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PartitionPhoto extends Photo{
    
    private ArrayList<String> blackfiles = new ArrayList<String>();
    private ArrayList<String> colorfiles = new ArrayList<String>();
// private ImageView[] photoparts = new ImageView[GlobalResource.PARTITION_PIECE];
    private int doneCount = 0;
    private ArrayList<Integer> parts = new ArrayList<Integer>();
    private int currentPart = -1;

    public PartitionPhoto(Context context) {
        super(context);
        timeAllow = 25000;
        for(int i=0;i<GlobalResource.PARTITION_PIECE; i++)
            parts.add(i);
        Collections.shuffle(parts);
    }
    
    public void addSource(String filecode){
        for(int i=1; i<=9; i++){
            colorfiles.add(filecode+"_"+i);
            blackfiles.add(filecode+"_"+i+"_b");
        }
    }
    
    @Override
    public ArrayList<String> getColorFiles(){
        return colorfiles;
    }
    
    @Override
    public ArrayList<String> getBlackFiles(){
        return blackfiles;
    }
    
    
    @Override
    public void setResourceOn(String curPackage){
        super.setResourceOn(curPackage);
        for(int i=0; i<GlobalResource.PARTITION_PIECE; i++){
            ((ImageView)((Activity)photoContext).findViewById(GlobalResource.partviewID[i])).setBackgroundResource(getContext().getResources().getIdentifier(blackfiles.get(i), "drawable", curPackage));
        }
    }
    
    @Override
    public void updatePartition(int index, String curPackage){
        ((ImageView)((Activity)photoContext).findViewById(GlobalResource.partviewID[index])).setBackgroundResource(getContext().getResources().getIdentifier(colorfiles.get(index), "drawable", curPackage));
    }
    
    @Override
    public boolean isGoodTouch() {
        return false;
    }

    @Override
    public void solveHint() {
        ((ImageView)((Activity)photoContext).findViewById(R.id.currentPart))
        .setBackgroundResource(getContext().getResources().getIdentifier(colorfiles.get(currentPart), "drawable", curPackage));
        getNextPart();
    }
    
    @Override
    public void doneOne(){
        doneCount++;
    }

    @Override
    public boolean isDone() {
        return doneCount==9;
    }

    @Override
    public void updateView(MotionEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetTouch() {
        // TODO Auto-generated method stub
        for(int i=0;i<GlobalResource.PARTITION_PIECE; i++){
            parts.add(i);
            /*Drawable drawable = ((ImageView)((Activity)photoContext).findViewById(GlobalResource.partviewID[i])).getDrawable();
if (drawable instanceof BitmapDrawable) {
BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
Bitmap bitmap = bitmapDrawable.getBitmap();
bitmap.recycle();
}*/
        }
    
    }
    
    @Override
    public int getNextPart(){
        if(parts.size()>0){
            currentPart = parts.get(0);
            parts.remove(0);
            return currentPart;
        }
        return -1;
    }
    
    @Override
    public int getCurrentPart(){
        return currentPart;
    }

    @Override
    public void clearAll() {
        // TODO Auto-generated method stub
        
    }

}