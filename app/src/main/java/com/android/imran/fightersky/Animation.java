package com.android.imran.fightersky;

import android.graphics.Bitmap;

public class Animation  {
    private Bitmap[]frames;
    private int currentFrams;
    private long starTime;
    private  long delay;
    private boolean playOnce;

    public void setFrames(Bitmap[]frames){
        this.frames=frames;
        currentFrams=0;
        starTime=System.nanoTime();
    }

    public void setDelay(long d){
        delay=d;
    }
    public void setFrame(int i){
        currentFrams=i;
    }
    public void update(){
        long elapsed=(System.nanoTime()-starTime)/1000000;
        if (elapsed>delay){
            currentFrams++;
            starTime=System.nanoTime();
        }
        if (currentFrams==frames.length){
            currentFrams=0;
            playOnce=true;
        }

    }
    public Bitmap getImage(){
        return frames[currentFrams];
    }

    public int getFrame(){
        return currentFrams;
    }
    public boolean playedOnce(){
        return playOnce;
    }

}
