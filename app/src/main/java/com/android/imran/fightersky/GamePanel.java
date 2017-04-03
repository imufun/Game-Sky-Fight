package com.android.imran.fightersky;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.http.SslCertificate;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Imran on 12/2/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;

    private long SmokeTimer;
    private long SmokeElastedTime;


    private MainThread mThread;
    private Background bg;

    private Player mPlayer;
    private ArrayList<Smoke> smokes;


    public GamePanel(Context context) {

        super(context);
        getHolder().addCallback(this);
        mThread = new MainThread(getHolder(), this);
        setFocusable(true);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                mThread.setRunning(false);
                mThread.join();
                retry = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        mPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);

        smokes = new ArrayList<Smoke>();
        SmokeTimer = System.nanoTime();
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mPlayer.getPlaying()) {
                mPlayer.setPlaying(true);
            } else {
                mPlayer.setUp(true);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mPlayer.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {

        if (mPlayer.getPlaying()) {
            bg.update();
            mPlayer.update();

            //Smoke
            SmokeElastedTime = (System.nanoTime() - SmokeTimer) / 1000000;
            if (SmokeElastedTime > 120) {
                smokes.add(new Smoke(mPlayer.getX(), mPlayer.getY() + 10));
                SmokeTimer = System.nanoTime();
            }
            for (int i = 0; i < smokes.size(); i++) {
                smokes.get(i).UpdateRoundCircle();
                if (smokes.get(i).getX() < -10) {
                    smokes.remove(i);
                }
            }

        }

    }


    @Override
    public void draw(Canvas canvas) {

        final float ScalefactorX = getWidth() / (WIDTH * 1.f);
        final float ScalefactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int saveState = canvas.save();
            canvas.scale(ScalefactorX, ScalefactorY);
            bg.draw(canvas);
            mPlayer.draw(canvas);
            for (Smoke smoke : smokes) {
                smoke.draw(canvas);
            }
            canvas.restoreToCount(saveState);


            // Log.v("Pictures", "Width and height are " + WIDTH + "--" + HEIGHT);
        }

    }

}
