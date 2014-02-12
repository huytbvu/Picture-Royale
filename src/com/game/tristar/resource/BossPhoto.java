package com.game.tristar.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class BossPhoto implements Serializable{
    
    private ArrayList<BossQuestion> quests;
    private ArrayList<BossCard> cards;
    private int count;
    private String[] mainpic = new String[6];
    
    
    public BossPhoto(){
        quests = new ArrayList<BossQuestion>();
        cards = new ArrayList<BossCard>();
    }
    
    public void addCard(BossCard bc){
        cards.add(bc);
    }
    
    public void addBackgroundPattern(String prefix){
        for(int i=1; i<=6; i++){
            mainpic[i-1] = prefix + "_" +i+ "_r";
        }
    }
    
    public String[] getMainPicSub(){
        return mainpic;
    }
    
    public boolean doneSub(){
        return count==6;
    }
    
    public void doneOne(){
        count++;
    }
    
    public void addQuest(BossQuestion bq){
        quests.add(bq);
    }

    public ArrayList<BossCard> getCardSet(){
        return cards;
    }
    
    public ArrayList<BossQuestion> getQuestSet(){
        return quests;
    }
    
    public void shuffle(){
        Collections.shuffle(cards);
        Collections.shuffle(quests);
    }
    
    public BossQuestion getNextQuestion(){
        BossQuestion bq = quests.get(0);
        quests.remove(0);
        return bq;
    }
}
