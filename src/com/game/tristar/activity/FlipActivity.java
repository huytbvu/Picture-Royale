package com.game.tristar.activity;


import com.game.tristar.resource.*;
import com.game.tristar.util.Delay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class FlipActivity extends Activity {

    private FlippingPhoto current;
    //private LinearLayout line1, line2, line3;
    private int faceup = 0;
    private int group_rule = 0;
    private int[] cur_group;
    private int flip_used = 0;
    private int raw_score = 0;
    private int time_left = -1;
    private CountDownTimer cdtime;
    private Handler delayHandler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);
        //current = (FlippingPhoto)getIntent().getSerializableExtra("flipset");
        current = GlobalResource.AllPhotoDB.getFlippingSet().get(0);
        current.shuffleCard();
        GlobalResource.AllPhotoDB.getFlippingSet().remove(0);
        group_rule = current.getGroupRule();
        cur_group = new int[group_rule];
        for(final FlippingCard fc:current.getAllCards()){
            fc.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    fc.turnFaceUp(getPackageName());
                    cur_group[faceup] = fc.getGroup();
                    faceup = (faceup+1)%group_rule;
                    if(faceup==0){
                        if(isMatching(cur_group)){
                            raw_score++;
                            ((TextView)findViewById(R.id.flipscore)).setText("FOUND: "+raw_score);
                            current.doneOne();
                            Delay.delayHandler(500, delayHandler, new Runnable(){

                                @Override
                                public void run() {
                                    invisiblize();
                                }
                                
                            });
                            if(current.isDone()){
                                end(true);
                            } 
                        }
                        else
                            Delay.delayHandler(500, delayHandler, new Runnable(){

                            @Override
                            public void run() {
                                facedownAll();
                            }
                            
                        });
                    }
                }
            });
        }
        for(int i=0; i<current.getAllCards().size(); i++){
            current.getAllCards().get(i).turnFaceDown();
            current.getAllCards().get(i).setVisibility(View.VISIBLE);
            if(i<current.getAllCards().size()/3)
                ((LinearLayout)findViewById(R.id.flipline1)).addView(current.getAllCards().get(i));
            else if(i<current.getAllCards().size()*2/3)
                ((LinearLayout)findViewById(R.id.flipline2)).addView(current.getAllCards().get(i));
            else
                ((LinearLayout)findViewById(R.id.flipline3)).addView(current.getAllCards().get(i));
        }
        instruction(current.getGroupRule(),current.getPattern());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flip, menu);
        return true;
    }
    
    private void facedownAll(){
        flip_used++;
        for(FlippingCard fc:current.getAllCards())
            if(fc.isFaceUp()) fc.turnFaceDown();
    }
    
    private void invisiblize(){
        for(FlippingCard fc:current.getAllCards())
            if(fc.isFaceUp()) fc.setVisibility(View.INVISIBLE);
    }
    
    /*private boolean isMatching(FlippingCard f1, FlippingCard f2){
        return f1.getGroup()==f2.getGroup();
    }*/
    
    private boolean isMatching(int[] result){
        for(int i=0; i<result.length-1; i++)
            for(int j=i+1; j<result.length; j++)
                if(result[i]!=result[j]) return false;
        return true;
    }
    
    private void handleTime(){
        cdtime = new CountDownTimer(60000,1000){

            @Override
            public void onFinish() {
                end(false);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                time_left = (int) millisUntilFinished/1000;
                ((TextView)findViewById(R.id.fliptimeview)).setText("Time Left:\n"+ time_left);
                
            }
            
        }.start();
    }
    
    private void instruction(int rule, String pattern){
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        congratDialog.setMessage("Find the matching " +pattern+ " in group of "+rule);
        congratDialog.setPositiveButton("Ready", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleTime();
            }
        });
        AlertDialog al = congratDialog.create();
        al.setCanceledOnTouchOutside(false);
        al.show();
    }
    

    private void end(final boolean finished){
        cdtime.cancel();
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        if(finished)
            congratDialog.setMessage("WELL-DONE!!! You have completed the level");
        else congratDialog.setMessage("TIME UP!!!");
        congratDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent scoregained = new Intent();
                scoregained.putExtra("scoregained", calculateScore(finished));
                setResult(Activity.RESULT_OK, scoregained);
                finish();
            }
        });
        AlertDialog al = congratDialog.create();
        al.setCanceledOnTouchOutside(false);
        al.show();
    }
    
    private int calculateScore(boolean finished){
        int score = (finished) ? 2:1;
        score += score*raw_score*100;
        score = (flip_used < current.getCount()) ? (score + score/2) : score;
        return score;
    }
    
}
