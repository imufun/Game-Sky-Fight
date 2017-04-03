package com.android.imran.fightersky;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.http.SslCertificate;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Imran on 12/2/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;

    private long SmokeTimer;
    private long SmokeElastedTime;

    private long MissileStarTime;
    private long MissileElastedTime;


    private MainThread mThread;
    private Background bg;

    private Player mPlayer;
    private ArrayList<Smoke> smokes;
    private ArrayList<Missile> missiles;
    Random rand = new Random();


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
        missiles = new ArrayList<Missile>();
        SmokeTimer = System.nanoTime();
        MissileStarTime = System.nanoTime();
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


            // Misile
            MissileElastedTime = (System.nanoTime() - MissileStarTime) / 1000000;
            if (MissileElastedTime > (2000 - mPlayer.getScore() / 4)) {
                if (missiles.size() == 0) {

                    System.out.print("Miss" + Missile.class);
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),
                            R.drawable.missile), WIDTH + 10, HEIGHT / 2, 45, 15,
                            mPlayer.getScore(), 13));
                } else {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),
                            R.drawable.missile), WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT)),
                            45, 15, mPlayer.getScore(), 13));
                }
                MissileStarTime = System.nanoTime();
            }
            for (int i = 0; i < missiles.size(); i++) {
                missiles.get(i).update();
                if (collision(missiles.get(i), mPlayer)) {
                    missiles.remove(i);
                    mPlayer.setPlaying(false);
                    break;
                }
                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i);
                    break;
                }
            }

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

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
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
                break;
            }
            for (Missile missile : missiles) {
                missile.draw(canvas);
                break;
            }

            canvas.restoreToCount(saveState);


            // Log.v("Pictures", "Width and height are " + WIDTH + "--" + HEIGHT);
        }

    }

}
