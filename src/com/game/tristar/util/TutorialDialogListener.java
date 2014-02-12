package com.game.tristar.util;

import android.app.Dialog;
import android.view.*;

public class TutorialDialogListener implements View.OnClickListener{
	
	
	private final Dialog dialog;
    public TutorialDialogListener(Dialog dialog) {
        this.dialog = dialog;
    }
    
    @Override
    public void onClick(View v) {

        // Do whatever you want here

        // If tou want to close the dialog, uncomment the line below
        //dialog.dismiss();
    }
}
