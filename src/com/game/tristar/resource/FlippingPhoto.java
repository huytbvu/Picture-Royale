package com.game.tristar.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class FlippingPhoto implements Serializable{

    private ArrayList<FlippingCard> cardsets;
    private int done = 0;
    private int numberOfGroups, rule;
    private String pattern;
    
    public FlippingPhoto(int numGr, int rule, String pattern){
        this.cardsets = new ArrayList<FlippingCard>();
        this.numberOfGroups = numGr;
        this.rule = rule;
        this.pattern = pattern;
    }
    
    public int getCount(){
        return cardsets.size();
    }
    
    public void addCard(FlippingCard card){
        cardsets.add(card);
    }
    
    public void shuffleCard(){
        Collections.shuffle(cardsets);
    }
    
    public ArrayList<FlippingCard> getAllCards(){
        return cardsets;
    }
    
    public boolean isDone(){
        return done==numberOfGroups; 
    }
    
    public int getGroupRule(){
        return rule;
    }
    
    public String getPattern(){
        return pattern;
    }
    
    public void doneOne(){
        done++;
    }
    
}
