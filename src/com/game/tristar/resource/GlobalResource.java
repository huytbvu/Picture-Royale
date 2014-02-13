package com.game.tristar.resource;

import java.io.IOException;
import java.io.InputStream;

import com.game.tristar.activity.R;
import com.game.tristar.util.PhotoParser;

public class GlobalResource {

    
    public static final int NUMBER_OF_HINT = 4;
    public static final int NUMBER_OF_HEART = 5;
    public static final int TOTAL_TIME = 60000;
    public static final int HINT_ICON_RESID = R.drawable.hint_icon;
    public static final int HEART_ICON_RESID = R.drawable.heart_icon;
    public static final int PARTITION_PIECE = 9;
    public static final int TRIPET_CODE = 11;
    public static final int SYMMETRY_CODE = 12;
    
    public static final int FACE_DOWN_CARD = R.drawable.flipdown;
    
    public static final int[] partviewID = {
        R.id.part1,R.id.part2,R.id.part3,
        R.id.part4,R.id.part5,R.id.part6,
        R.id.part7,R.id.part8,R.id.part9
    };
    
    public static final int[] TRIP_TUT_IMG = {
        R.drawable.trip_tut_1,R.drawable.trip_tut_2,R.drawable.trip_tut_3,
        R.drawable.trip_tut_4,R.drawable.trip_tut_5,R.drawable.trip_tut_6,
        R.drawable.trip_tut_7,R.drawable.trip_tut_8,R.drawable.trip_tut_9,
        R.drawable.trip_tut_10
    };
    
    public static final int[] SYMM_TUT_IMG = {
        R.drawable.symm_tut_1,R.drawable.symm_tut_2,R.drawable.symm_tut_3,
        R.drawable.symm_tut_4,R.drawable.symm_tut_5,R.drawable.symm_tut_6,
        R.drawable.symm_tut_7,R.drawable.symm_tut_8,R.drawable.symm_tut_9,
        R.drawable.symm_tut_10,R.drawable.symm_tut_11
    };
    
    public static final int[] PART_TUT_IMG = {
        R.drawable.part_tut_1,R.drawable.part_tut_2,R.drawable.part_tut_3,
        R.drawable.part_tut_4
    };
    
    public static PhotoStorage AllPhotoDB;
}
