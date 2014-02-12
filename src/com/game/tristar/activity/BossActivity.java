package com.game.tristar.activity;

/*
 * @author Huy Vu
 */

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

public class BossActivity extends Activity {
    
    private Handler timeHandler = new Handler();
    private BossPhoto current;
    private int subOpened = 0;
    private int bonus = 0;
    private int questRight = 0;
    private boolean doneSub = false;
    private int userClick = 0;
    private int time_left = 90;
    private CountDownTimer timer = null;
    private BossQuestion currentQuestion;
    private int currentSub = -1;
    private int[] buttons = {R.id.choice1,R.id.choice2,R.id.choice3,R.id.choice4};
    private int[] subids = {R.id.sub1,R.id.sub2,R.id.sub3,R.id.sub4,R.id.sub5,R.id.sub6};
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);
        ((TextView)findViewById(R.id.bosstimeview)).setText("    Time Left: "+ time_left);
        current = GlobalResource.AllPhotoDB.getFBossSet().get(0);
        GlobalResource.AllPhotoDB.getFBossSet().remove(0);
        current.shuffle();
        

        for(int i=0;i<6; i++)
            ((ImageView)findViewById(subids[i])).setBackgroundResource(R.drawable.flipdownboss);
        
        
        ((Button)findViewById(buttons[0])).setOnClickListener(new View.OnClickListener() 
        {@Override public void onClick(View v) {userClick = 0;checkAnswer();}});
        
        ((Button)findViewById(buttons[1])).setOnClickListener(new View.OnClickListener() 
        {@Override public void onClick(View v) {userClick = 1;checkAnswer();}});
        
        ((Button)findViewById(buttons[2])).setOnClickListener(new View.OnClickListener() 
        {@Override public void onClick(View v) {userClick = 2;checkAnswer();}});
        
        ((Button)findViewById(buttons[3])).setOnClickListener(new View.OnClickListener() 
        {@Override public void onClick(View v) {userClick = 3;checkAnswer();}});
        
        instruction();
        
    }
    

    private void buttonControl(boolean state){
        for(int j=0; j<4;j++)
            ((Button)findViewById(buttons[j])).setEnabled(state);
    }
    
    private void checkAnswer(){
        if(userClick==currentQuestion.getCorrectAnswer()){
            if(doneSub){
                bonus += 30*time_left;
                questRight++;
            }
            else{
                ((Button)findViewById(buttons[userClick])).setBackgroundColor(getResources().getColor(R.color.Green));
                ((ImageView)findViewById(subids[currentSub])).setBackgroundResource(getApplicationContext().getResources().getIdentifier(current.getMainPicSub()[currentSub], "drawable", getPackageName()));
                subOpened++;
            }
            Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT/2).show();
        }else{
            if(!doneSub){
                ((Button)findViewById(buttons[userClick])).setBackgroundColor(getResources().getColor(R.color.Red));
                ((Button)findViewById(buttons[currentQuestion.getCorrectAnswer()])).setBackgroundColor(getResources().getColor(R.color.Green));
                ((ImageView)findViewById(subids[currentSub])).setBackgroundResource(R.drawable.nopicboss);
            }
            Toast.makeText(getApplicationContext(), "WRONG", Toast.LENGTH_SHORT/2).show();
        }
        Delay.delayHandler(1000, timeHandler, new Runnable(){

            @Override
            public void run() {
                handleNext();
            }
        });
    }
    
    private void handleNext(){
        updateStatus();
        reset();
        if(currentSub<5) handleOneSub();
        else{
            if(!doneSub){
                doneSub = true;enterMain();
            }
            else if(current.getQuestSet().size()>0)
                handleMain();
            else end(true);
        }
    }
    
    private void reset(){
        ((TextView)findViewById(R.id.questionText)).setText("");
        for(int i=0; i<4; i++){
            ((Button)findViewById(buttons[i])).setText("");
            ((Button)findViewById(buttons[i])).setBackgroundColor(getResources().getColor(R.color.Gray));
        }
    }
    
    private void updateStatus() {
        ((TextView)findViewById(R.id.boss_score)).setText("Solved: " + subOpened + " Bonus: "+bonus);
    }

    private void handleOneSub(){
        currentSub++;
        if(timer!=null)timer.cancel();
        buttonControl(false);
        ((ImageView)findViewById(subids[currentSub])).setBackgroundResource(R.drawable.flipupboss);
        BossCard currentCard = current.getCardSet().get(currentSub);
        currentQuestion = currentCard.getQuestion();
        ((ImageView)findViewById(R.id.subimage)).setBackgroundResource(getApplicationContext().getResources().getIdentifier(currentCard.turnUp(), "drawable", getPackageName()));
        
        Delay.delayHandler(7000, timeHandler, new Runnable(){

            @Override
            public void run() {
                ((ImageView)findViewById(R.id.subimage)).setBackgroundResource(R.drawable.flipdownbossbig);
                displayQuestion(currentQuestion);
                handleTime(time_left*1000);
            }
            
        });
    }
    
    private void handleMain(){
        currentQuestion = current.getNextQuestion();
        displayQuestion(currentQuestion);
    }
    
    private void displayQuestion(BossQuestion q){
        buttonControl(true);
        ((TextView)findViewById(R.id.questionText)).setText(q.getTitle());
        for(int i=0; i<4; i++){
            ((Button)findViewById(buttons[i])).setText(q.getAllChoices()[i]);
        }
    }
    
    
    private void handleTime(int begin){
        timer = new CountDownTimer(begin,1000){

            @Override
            public void onFinish() {
            	((TextView)findViewById(R.id.bosstimeview)).setText("    TIME UP!!!");
                end(false);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                time_left = (int) millisUntilFinished/1000;
                ((TextView)findViewById(R.id.bosstimeview)).setText("    Time Left: "+ time_left);
            }
            
        }.start();
    }
    
    private int calculateScore(boolean finished){
        if(subOpened==6) bonus += 3000;
        else bonus += subOpened*400;
        if(finished) bonus += questRight*time_left*10;
        return bonus;
    }
    
    private void enterMain(){
        timer.cancel();
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        congratDialog.setMessage("You have opened " + subOpened + " parts. Now try to solve the big picture");
        congratDialog.setPositiveButton("Ready", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleMain();
                handleTime(time_left*1000);
            }
        });
        AlertDialog al = congratDialog.create();
        al.setCanceledOnTouchOutside(false);
        al.show();
    }
    
    private void instruction(){
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        congratDialog.setMessage(getResources().getString(R.string.boss_instruction));
        congratDialog.setPositiveButton("GO", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleNext();
            }
        });
        AlertDialog al = congratDialog.create();
        al.setCanceledOnTouchOutside(false);
        al.show();
    }
    
    private void end(final boolean finished){
        timer.cancel();
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        if(finished)
            congratDialog.setMessage("WELL-DONE!!! You have completed the level");
        else congratDialog.setMessage("TIME UP!!!");
        congratDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent scoregained = new Intent();
                scoregained.putExtra("bonusgained", calculateScore(finished));
                scoregained.putExtra("solved", finished);
                setResult(Activity.RESULT_OK, scoregained);
                finish();
            }
        });
        AlertDialog al = congratDialog.create();
        al.setCanceledOnTouchOutside(false);
        al.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.boss, menu);
        return true;
    }

}
