package com.game.tristar.resource;

import java.util.ArrayList;
import java.util.Collections;

public class PhotoStorage {
    
    private ArrayList<TripletPhoto> triplets = new ArrayList<TripletPhoto>();
    private ArrayList<PartitionPhoto> partitions = new ArrayList<PartitionPhoto>();
    private ArrayList<FlippingPhoto> flippings = new ArrayList<FlippingPhoto>();
    private ArrayList<SymmetryPhoto> symmetries = new ArrayList<SymmetryPhoto>();
    private ArrayList<BossPhoto> bosses = new ArrayList<BossPhoto>();
    
    public PhotoStorage(){
        
    }
    
    public void addTriplet(TripletPhoto tp){
        triplets.add(tp);
    }
    
    public void addSymmetry(SymmetryPhoto sp){
        symmetries.add(sp);
    }
    
    public void addPartition(PartitionPhoto pp){
        partitions.add(pp);
    }
    
    public void addFlipping(FlippingPhoto fp){
        flippings.add(fp);
    }
    
    public void addBoss(BossPhoto bp){
        bosses.add(bp);
    }

    public ArrayList<TripletPhoto> getTripletSet(){
        return triplets;
    }
    
    public ArrayList<SymmetryPhoto> getSymmetrySet(){
        return symmetries;
    }
    
    public ArrayList<PartitionPhoto> getPartitionSet(){
        return partitions;
    }
    
    public ArrayList<FlippingPhoto> getFlippingSet(){
        return flippings;
    }
    
    public ArrayList<BossPhoto> getFBossSet(){
        return bosses;
    }
    
    public void printSize(){
        System.out.println("Triplet " + triplets.size()
                + "\nPartition " + partitions.size()
                +"\nSymmetry " + symmetries.size()
                +"\nFlipping " + flippings.size()
                +"\nBoss " + bosses.size());
    }
    
    public void shuffle(){
        Collections.shuffle(triplets);
        Collections.shuffle(partitions);
        Collections.shuffle(flippings);
        Collections.shuffle(symmetries);
        Collections.shuffle(bosses);
    }
    
   
}
