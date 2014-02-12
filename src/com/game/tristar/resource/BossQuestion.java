package com.game.tristar.resource;
/**
 * @author Huy Vu
 * the question class
 */
import java.util.Random;


public class BossQuestion{
    
    private String title;
    private String[] opts;
    private int correct;
    private int pos;
    private Random gen = new Random();
    
    public BossQuestion(String questionTitle){
        title = questionTitle;
        opts = new String[4];
        pos = 0;
    }
    
    public void addOption(String opt){
        opts[pos] = opt;
        pos++;
    }
    
    public int getCorrectAnswer(){
        return correct;
    }
    
    public void shuffle(){
        for(int i=3; i>0; i--){
            int pos = gen.nextInt(i+1);
            String temp = opts[i];
            opts[i] = opts[pos];
            opts[pos] = temp;
        }
        for(int j=0; j<4; j++){
            if(opts[j].startsWith("###")){
                correct = j;
                opts[j]=opts[j].substring(3);
            }
        }
    }
    
    public void printQuestion(){
        System.out.println(title);
        for(String s:opts)
            System.out.println(s);
        System.out.println("cor "+correct);
    }
    
    public String getTitle(){
        return title;
    }
    
    public String[] getAllChoices(){
        return opts;
    }
}
