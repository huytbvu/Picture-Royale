package com.game.tristar.resource;

import java.io.Serializable;

import com.game.tristar.activity.R;

import android.content.Context;
import android.view.View;
import android.widget.*;

public class FlippingCard extends ImageView implements Serializable{

    private String filename;
    private int group;
    private boolean faceUp = false;
    private boolean active = true;
    
    public FlippingCard(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);
        setLayoutParams(params);
    }
    
    
    public void saveResourceInfo(String filename){
        this.filename = filename;
    }
    
    public void setGroup(int gr){
        this.group = gr;
    }
    
    public int getGroup(){
        return group;
    }
    
    public void turnFaceDown(){
        setBackgroundResource(R.drawable.flipdown);
        faceUp = false;
    }
    
    public void turnFaceUp(String curPackage){
        setBackgroundResource(getContext().getResources().getIdentifier(filename, "drawable", curPackage));       
        faceUp = true;
    }
    
    public void unbind(){
        this.getBackground().setCallback(null);
    }
    
    public boolean isFaceUp(){
        return faceUp;
    }
    
    public void reset(){
        setVisibility(View.VISIBLE);
        turnFaceDown();
        active = true;
    }
    
    public void inactivate(){
        active = false;
    }
    
    public boolean isActive(){
        return active;
    }

}
