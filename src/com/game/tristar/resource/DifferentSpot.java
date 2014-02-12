package com.game.tristar.resource;

/*
 * @author: Huy Vu
 */
public class DifferentSpot {

    private float Xratio, Yratio, Rratio;
    private float xpos,ypos;
    private boolean hit;
    
    public DifferentSpot(float xloc, float yloc, float rloc){
        this.Xratio = xloc;
        this.Yratio = yloc;
        this.Rratio = rloc*1.75f;
    }
    
    public boolean isHit(){
        return hit;
    }
    
    public void hit(){
        hit = true;
    }
    
    public void clear(){
        hit = false;
    }
    
    public void setPosition(int xdim,int ydim){
        xpos = Xratio*xdim;
        ypos = Yratio*ydim;
    }
    
    public boolean fit(float x, float y){
        return(Math.abs(Xratio-x)<Rratio && Math.abs(Yratio-y)<Rratio);
    }
    
    public float getXposition(){
        return xpos;
    }
    
    public float getYpositon(){
        return ypos;
    }
    
    public float getRadius(int dim){
        return Rratio*dim;
    }
    
    
}
