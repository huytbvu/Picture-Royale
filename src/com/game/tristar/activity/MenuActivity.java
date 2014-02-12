package com.game.tristar.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        ((Button)findViewById(R.id.playButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent togame = new Intent(MenuActivity.this, PhotoActivity.class);
                startActivity(togame);
            }
        });
        
        ((Button)findViewById(R.id.ruleButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent toinstruction = new Intent(MenuActivity.this, InstuctionActivity.class);
                startActivity(toinstruction);
            }
        });
        
        ((Button)findViewById(R.id.exitButton)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });
    }    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
