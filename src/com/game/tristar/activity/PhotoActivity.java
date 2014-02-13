package com.game.tristar.activity;

import java.io.IOException;
import java.io.InputStream;

import com.game.tristar.resource.*;
import com.game.tristar.util.PhotoParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.widget.*;

public class PhotoActivity extends Activity {
    
    private ImageView[] hintViews = new ImageView[GlobalResource.NUMBER_OF_HINT];
    private ImageView[] heartViews = new ImageView[GlobalResource.NUMBER_OF_HEART];
    private RelativeLayout photoView;
    private LinearLayout hintLayout, heartLayout;
    private Photo current; 
    private CountDownTimer timer;
    
    private int score = 0;
    private int stage = 1;
    private int lifeRemain = GlobalResource.NUMBER_OF_HEART;
    private int timeRemain = -1;
    private int bossSolved = 0;
    
    private final int FLIP_REQ_CODE = 10;
    private final int PART_REQ_CODE = 20;
    private final int BOSS_REQ_CODE = 30;
    
    private int curTutImg = 0;
	private AlertDialog.Builder tutorial;
	private AlertDialog tut;
	private ImageView curTut;
	
	private boolean tutTrip = false;
	private boolean tutSymm = false;
	private boolean tutPart = false;
	
    
    
    /*
* (non-Javadoc)
* main method to create
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photoView = (RelativeLayout)findViewById(R.id.topview);
        hintLayout = (LinearLayout)findViewById(R.id.hintLayout);
        heartLayout = (LinearLayout)findViewById(R.id.heartLayout);
        new PrepareSideBar().execute();        
    }
    
    
    @Override
    protected void onStart(){
        super.onStart();
        if(stage==1 && GlobalResource.AllPhotoDB==null){
        try {
            InputStream in = getAssets().open("PhotoInfo.xml");
            GlobalResource.AllPhotoDB = PhotoParser.parse(in, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        	handleNextLevel();
        }
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("resume");
    }

    
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////HANDLE PHOTO AND LEVEL////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    
    /*
    * handle next level, call appropriate game depend on level
    */
    public void handleNextLevel(){
        ((TextView)findViewById(R.id.stage)).setText("STAGE: " + stage);
        System.out.println(photoView.getChildCount());
        GlobalResource.AllPhotoDB.printSize();
        if(stage==51){
            if(bossSolved>=4){
                Intent toFinale = new Intent(PhotoActivity.this, FinaleActivity.class);
                startActivity(toFinale);
            }
            else{
                Intent toVictory = new Intent(PhotoActivity.this, VictoryActivity.class);
                toVictory.putExtra("finalscore", score);
                startActivity(toVictory);
            }
            finish();
        }
        switch(stage%10){
        case 0:
            handleOneBoss();
            break;
        case 4:
            hidePartition();
            if(stage==4 && !tutSymm){
        		showTutorial(GlobalResource.SYMM_TUT_IMG);
        		tutSymm = true;
        		return;
        	}
        case 9:
            ((LinearLayout)findViewById(R.id.sidebarLayout)).setVisibility(View.VISIBLE);
            handleOneSymmetry();
            break;
        case 5:
            handleOneFlipping();
            break;
        case 3:
        	if(stage==3 && !tutPart){
        		showTutorial(GlobalResource.PART_TUT_IMG);
        		tutPart = true;
        		return;
        	}
        case 7:
            ((LinearLayout)findViewById(R.id.sidebarLayout)).setVisibility(View.INVISIBLE);
            handleOnePartition();
            break;
        case 8:
            hidePartition();
        case 1:
        	if(stage==1 && !tutTrip){
        		showTutorial(GlobalResource.TRIP_TUT_IMG);
        		tutTrip = true;
        		return;
        	}
        case 2:
        case 6:
            ((LinearLayout)findViewById(R.id.sidebarLayout)).setVisibility(View.VISIBLE);
            handleOneTriplet();
            break;
        default:
            break;
        }
        
        stage++;
    }
    
    private void hidePartition(){
        ((ImageView)findViewById(R.id.currentPart)).setVisibility(View.INVISIBLE);
        for(int i=0; i<GlobalResource.PARTITION_PIECE; i++)
            ((ImageView)findViewById(GlobalResource.partviewID[i])).setVisibility(View.INVISIBLE);
    }
    
    /*
    * handle triplet photo
    */
    private void handleOneTriplet(){
        current = GlobalResource.AllPhotoDB.getTripletSet().get(0);
        current.setResourceOn(getPackageName());
        photoView.addView(current);
        handleTime(current.getTimeAllow());
        GlobalResource.AllPhotoDB.getTripletSet().remove(0);
        current.setOnTouchListener(new View.OnTouchListener() {
                    
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()!=MotionEvent.ACTION_DOWN) return false;
                current.updateView(event);
                score += current.getScoreGain();
                updateScore();
                if(!current.isGoodTouch()) penalizeWrongTouch();
                if(current.isDone()){
                    unbindImageDrawable(current);
                // System.gc();
                    doneOnePhoto();
                }
                return true;
            }
        });
        
    }
    
    private void handleOneSymmetry(){
        current = GlobalResource.AllPhotoDB.getSymmetrySet().get(0);
        current.setResourceOn(getPackageName());
        photoView.addView(current);
        handleTime(current.getTimeAllow());
        GlobalResource.AllPhotoDB.getSymmetrySet().remove(0);
        current.setOnTouchListener(new View.OnTouchListener() {
                    
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()!=MotionEvent.ACTION_DOWN) return false;
                current.updateView(event);
                score += current.getScoreGain()*2;
                updateScore();
                if(!current.isGoodTouch()) penalizeWrongTouch();
                if(current.isDone()){
                    unbindImageDrawable(current);
                // System.gc();
                    doneOnePhoto();
                }
                return true;
            }
        });
    }
    /*
    * handle one partition photo
    */
    public void handleOnePartition(){
        current = GlobalResource.AllPhotoDB.getPartitionSet().get(0);
        
        GlobalResource.AllPhotoDB.addPartition(GlobalResource.AllPhotoDB.getPartitionSet().get(0));
        GlobalResource.AllPhotoDB.getPartitionSet().remove(0);
    // current.setResourceOn(getPackageName());
        ((ImageView)findViewById(R.id.currentPart)).setVisibility(View.VISIBLE);
        for(int i=0; i<GlobalResource.PARTITION_PIECE; i++){
            final int index = i;
            ((ImageView)findViewById(GlobalResource.partviewID[i])).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(GlobalResource.partviewID[i])).setBackgroundResource(getResources().getIdentifier(current.getBlackFiles().get(i), "drawable", getPackageName()));
            ((ImageView)findViewById(GlobalResource.partviewID[i])).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(current.getCurrentPart()==index){
                    ((ImageView)findViewById(GlobalResource.partviewID[index])).setBackgroundResource(getResources().getIdentifier(current.getColorFiles().get(index), "drawable", getPackageName()));
                    current.doneOne();
                    score += 50;
                    updateScore();
                    int cur = current.getNextPart();
                    if(cur!=-1)
                        ((ImageView)findViewById(R.id.currentPart)).setBackgroundResource(getResources().getIdentifier(current.getColorFiles().get(cur), "drawable", getPackageName()));
                    
                }
            // else penalizeWrongTouch();
                if(current.isDone()){
                    current.resetTouch();
                    unbindPartition();
                    doneOnePhoto();
                }
                
            }
            });
        }
        int cur = current.getNextPart();
        ((ImageView)findViewById(R.id.currentPart)).setBackgroundResource(getResources().getIdentifier(current.getColorFiles().get(cur), "drawable", getPackageName()));
        
        
        handleTime(current.getTimeAllow());
    }
    
    public void handleOneFlipping(){
        Intent toflipping = new Intent(PhotoActivity.this, FlipActivity.class);
        startActivityForResult(toflipping, FLIP_REQ_CODE);
    }
    
    public void handleOneBoss(){
        Intent toBoss = new Intent(PhotoActivity.this,BossActivity.class);
        startActivityForResult(toBoss, BOSS_REQ_CODE);
    }
    

    ////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////LOGISTICS/////////////////////////////////////////////
    //////////////////////////////////LOGISTICS/////////////////////////////////////////////
    //////////////////////////////////LOGISTICS/////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    
    /*
     * tutorial popup
     */
    
    private void showTutorial(final int[] tutImgSrc){
    	curTutImg = 0;
    	tutorial = new AlertDialog.Builder(this);
    	final View tutLayout = getLayoutInflater().inflate(R.layout.tutorial_toast, (ViewGroup)findViewById(R.id.tut_layout_id));
    	curTut = (ImageView)tutLayout.findViewById(R.id.tutorial_image);
    	curTut.setImageResource(tutImgSrc[curTutImg]);
    	tutorial.setTitle("Tutorial");
    	tutorial.setCancelable(false);
    	
    	tutorial.setView(tutLayout);
    	tutorial.setPositiveButton("Next", null);
    	
    	tut = tutorial.create();
        tut.getWindow().setLayout(curTut.getWidth(), curTut.getHeight());
        tut.show();
        tut.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				curTutImg++;
				if(curTutImg<tutImgSrc.length){
                	curTut.setImageResource(tutImgSrc[curTutImg]);
                	tutLayout.invalidate();
            	}else{
            		tut.dismiss();
            		handleNextLevel();
            	}
			}
        	
        });
    }
    
    
    /*
     *  call when one photo is done
     */
    public void doneOnePhoto(){
        timer.cancel();
        AlertDialog.Builder congratDialog = new AlertDialog.Builder(this);
        congratDialog.setMessage("Great job, you have solved the photo");
        congratDialog.setPositiveButton("Next Stage", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                photoView.removeView(current);
                score += timeRemain*10;
                updateScore();
                handleNextLevel();
            }
        });
        AlertDialog al = congratDialog.create();
        al.show();
    }

    public void updateScore(){
        ((TextView)findViewById(R.id.score)).setText("SCORE: " + score);
    }
    
    private void handleTime(int timeToSet) {
        timer = new CountDownTimer(timeToSet, 1000) {
            
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemain = (int) (millisUntilFinished/1000);
                ((TextView)findViewById(R.id.timeLeft)).setText("Time Left: \n" + timeRemain);
            }
            
            @Override
            public void onFinish() {
                ((TextView)findViewById(R.id.timeLeft)).setText("Time Left: \n 0");
                toGameOver();
            }
        }.start();
    }
    
    private void toGameOver(){
        //TODO
        Intent toGameOver = new Intent(PhotoActivity.this, GameoverActivity.class);
        toGameOver.putExtra("score", score);
        toGameOver.putExtra("stage", stage);
        finish();
        startActivity(toGameOver);
    }
    
    public void penalizeWrongTouch(){
        //TODO
        lifeRemain--;
        updateHeartView();
        current.resetTouch();
    }
    
    private void updateHeartView(){
        for(int i=0; i<GlobalResource.NUMBER_OF_HEART; i++){
            if(i<lifeRemain) heartLayout.getChildAt(i).setVisibility(View.VISIBLE);
            else heartLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        if(lifeRemain == 0){
            timer.cancel();
            toGameOver();
        }
    }
    
    /*
	* prepare the side bar in the background
	*/
    class PrepareSideBar extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            
            for(int i=0; i<GlobalResource.NUMBER_OF_HINT; i++){
                ImageView hintIconView = new ImageView(hintLayout.getContext());
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                
                hintIconView.setBackgroundResource(GlobalResource.HINT_ICON_RESID);
                hintIconView.setLayoutParams(params);
                hintIconView.setPadding(2, 2, 2, 2);
                hintIconView.setClickable(true);
                hintViews[i] = hintIconView;
            }
            
            for(int i=0; i<GlobalResource.NUMBER_OF_HEART; i++){
                ImageView heartIconView = new ImageView(heartLayout.getContext());
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                
                heartIconView.setBackgroundResource(GlobalResource.HEART_ICON_RESID);
                heartIconView.setLayoutParams(params);
                heartViews[i] = heartIconView;
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void v){

            for(ImageView heart:heartViews){
                heart.setVisibility(View.INVISIBLE);
                heartLayout.addView(heart);
            }
            updateHeartView();
            for(final ImageView hint:hintViews){
                hint.setVisibility(View.VISIBLE);
                hint.setOnClickListener(new View.OnClickListener() {
                    
                    boolean available = true;
                    @Override
                    public void onClick(View v) {
                        if(available){
                            hint.setBackgroundResource(R.drawable.hint_icon_cross);
                            current.solveHint();
                            if(current.isDone()){
                                unbindImageDrawable(current);
                                System.gc();
                                doneOnePhoto();
                            }
                            available = false;
                        }
                    }
                });
                hintLayout.addView(hint);
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////OVERRIDING////////////////////////////////////////////
	//////////////////////////////////ACTIVITY//////////////////////////////////////////////
	//////////////////////////////////METHODS///////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////


    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == FLIP_REQ_CODE && resultCode == Activity.RESULT_OK)
            score += data.getIntExtra("scoregained", -1);
           
        else if(requestCode == BOSS_REQ_CODE && resultCode == Activity.RESULT_OK){
            score += data.getIntExtra("bonusgained", -1);
            if(data.getBooleanExtra("solved", false)) bossSolved++;
        }
        updateScore();
        handleNextLevel();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
        /*switch(currentRound){
        case TRIP_ROUND:
        case SYMM_ROUND:
            unbindImageDrawable(current); break;
        case PART_ROUND:
            unbindPartition(); break;
        default:
            break;
        }
        GlobalResource.AllPhotoDB = null;
        System.gc();*/
    }
    
    @Override
    public void onStop(){
        super.onDestroy();
        if(timer!=null)
        timer.cancel();
        /*timer.cancel();
    //    current.destroyDrawingCache();
        switch(currentRound){
        case TRIP_ROUND:
        case SYMM_ROUND:
            unbindImageDrawable(current); break;
        case PART_ROUND:
            unbindPartition(); break;
        default:
            break;
        }
        GlobalResource.AllPhotoDB = null;
        System.gc();*/
    }
    
    private void unbindPartition(){
        for(int i=0; i<GlobalResource.PARTITION_PIECE; i++){
            ((ImageView)findViewById(GlobalResource.partviewID[i])).setImageBitmap(null);
            unbindImageDrawable(findViewById(GlobalResource.partviewID[i]));
        }
    }
    
    private void unbindImageDrawable(View v){
        if(v.getBackground() != null) v.getBackground().setCallback(null);
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {

                unbindImageDrawable(((ViewGroup) v).getChildAt(i));
            }
            try {
                ((ViewGroup) v).removeAllViews();
            } catch (UnsupportedOperationException e) {

            }
        }
    }
    
    
}