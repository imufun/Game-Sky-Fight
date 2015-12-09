package com.android.imran.fightersky;

import android.graphics.Rect;

/**
 * Created by Imran on 12/9/2015.
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int WIDTH;
    protected int HEIGHT;


    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }


    public Rect getRectangle(){
        return new Rect(x,y,x+WIDTH,y+HEIGHT);
    }
}
