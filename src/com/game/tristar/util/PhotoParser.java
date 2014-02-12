package com.game.tristar.util;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;

import com.game.tristar.resource.*;

public class PhotoParser{

    /*private static TripletPhoto trip;
    private static SymmetryPhoto symm;
    private static PartitionPhoto part;*/
    
    
    public static PhotoStorage parse(InputStream in, final Context c){

        final PhotoStorage store = new PhotoStorage();
        class PhotoHandler extends BasicHandler{
            
            TripletPhoto trip;
            SymmetryPhoto symm;
            PartitionPhoto part;
            FlippingPhoto flip;
            BossPhoto boss;
            BossQuestion bq;
            BossCard bc;
            String block = "";
            
            @Override
            public void startElement(String uri, String localName,String tagName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, tagName, attributes);
                if(tagName.equals("Triplet")){
                    block = "Triplet";
                    trip = new TripletPhoto(c);
                }
                else if(tagName.equals("Symmetry")){
                    block = "Symmetry";
                    symm = new SymmetryPhoto(c);
                }else if(tagName.equals("Partition")){
                    part = new PartitionPhoto(c);
                    part.addSource(attributes.getValue("filecode"));
                }
                else if(tagName.equals("Flipping")) 
                    flip = new FlippingPhoto(Integer.parseInt(attributes.getValue("group")),
                            Integer.parseInt(attributes.getValue("rule")), attributes.getValue("pattern"));
                else if(tagName.equals("Part")){
                    FlippingCard card = new FlippingCard(c);
                    card.saveResourceInfo(attributes.getValue("file"));
                    card.setGroup(Integer.parseInt(attributes.getValue("pair")));
                    flip.addCard(card);
                }
                else if(tagName.equals("Location")){
                    float x = Float.parseFloat(attributes.getValue("x"));
                    float y = Float.parseFloat(attributes.getValue("y"));
                    float r = Float.parseFloat(attributes.getValue("r"));
                    if(block.equals("Triplet"))trip.addPoint(new DifferentSpot(x, y, r));
                    else if (block.equals("Symmetry"))symm.addPoint(new DifferentSpot(x, y, r));
                }
                else if(tagName.equals("Boss")){
                    block = "Boss";
                    boss = new BossPhoto();
                    boss.addBackgroundPattern(attributes.getValue("prefix"));
                }
                else if(tagName.equals("Sub")){
                    block = "Sub";
                    bc = new BossCard(c);
                    bc.saveResourceActive(attributes.getValue("file"));
                }
                else if(tagName.equals("Question")){
                    bq = new BossQuestion(attributes.getValue("title"));
                }
                
            }
            @Override
            public void endElement(String uri, String localName, String tagName) throws SAXException {
                super.endElement(uri, localName, tagName);
                if(tagName.equals("Triplet")) store.addTriplet(trip);              
                else if(tagName.equals("Symmetry")) store.addSymmetry(symm);       
                else if(tagName.equals("Flipping")) store.addFlipping(flip);
                else if(tagName.equals("Partition")) store.addPartition(part);
                else if(tagName.equals("ID")){
                    if(block.equals("Triplet")) trip.setResource(lastString());
                    else if (block.equals("Symmetry"))symm.setResource(lastString());
                }
                else if(tagName.equals("Question")){
                    bq.shuffle();
                    if(block.equals("Sub")) bc.addQuestion(bq);
                    else if(block.equals("Boss")) boss.addQuest(bq);
                }
                else if(tagName.equals("Choice")) bq.addOption(lastString());
                else if(tagName.equals("Sub")) boss.addCard(bc);
                else if(tagName.equals("Boss")) store.addBoss(boss);
            }
        }
        
        try {       
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser(); 
            parser.parse(in, new PhotoHandler());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } 
        store.shuffle();
        return store;
    }
    
    
    
    
    
}
