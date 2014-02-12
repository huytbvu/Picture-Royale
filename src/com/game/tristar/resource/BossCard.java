package com.game.tristar.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.game.tristar.activity.R;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BossCard extends ImageView implements Serializable{
    
    public enum Status{INACTIVE,ACTIVE,RIGHT,WRONG}
    
    private String filenameRight;
    private String filenameActive;
    private Status state;
    private BossQuestion chosen;
    private ArrayList<BossQuestion> lists = new ArrayList<BossQuestion>();
    private Random gen = new Random();
    
    public BossCard(Context context){
        super(context);
        this.state = Status.INACTIVE;
        setBackgroundResource(R.drawable.flipdownboss);
        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);
        setLayoutParams(params);*/
    }
    
    public void addQuestion(BossQuestion q){
        lists.add(q);
    }
    
    public BossQuestion getQuestion(){
        chosen = lists.get(gen.nextInt(lists.size()));
        return chosen;
    }
    
    public int getCorrect(){
        return chosen.getCorrectAnswer();
    }
    
    public void saveResourceActive(String filename){
        this.filenameActive = filename;
    }
    
    public void saveResourceRight(String filename){
        this.filenameRight = filename;
    }
    
    public void unbind(){
        this.getBackground().setCallback(null);
    }
    
    public String turnUp(){
    //    setBackgroundResource(getContext().getResources().getIdentifier(filenameActive, "drawable", curPackage));
        state = Status.ACTIVE;
        return filenameActive;
    }
    
    public void turnWrong(){
        setBackgroundResource(R.drawable.nopicboss);
        state = Status.WRONG;
    }
    
    public void turnRight(String curPackage){
        setBackgroundResource(getContext().getResources().getIdentifier(filenameRight, "drawable", curPackage));
        state = Status.RIGHT;
    }
    
    public Status getStatus(){
        return state;
    }

    
    
}
