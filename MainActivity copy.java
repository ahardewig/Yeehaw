package com.example.nazl.hackathonproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Class variables
    //This is a mess because of Alex
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double timeSinceLastChange = 0;
    private double x;
    private double y;
    private double z;
    private double lastx;
    private double lasty;
    final private double pauseTime = 500;


    private SensorManager mSensorManager;
    Thread shakeListenerThread = null;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    public boolean cowboy = false;
    public boolean whip = false;
    public boolean vroom = false;

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Android gibberish
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundboard_home);

        //Sensor managers
        //Or whatever
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    //This method will send you back to the soundboard screen when you click the button on the whip screen
    public void sendMessageWhipBackButton(View view) {
        setContentView(R.layout.soundboard_home);
        this.whip = false;
        shakeListenerThread.interrupt();
    }

    //This method will send you to the whip screen
    public void sendMessageGotoWhipScreen(View view) {
        setContentView(R.layout.whip_main);
        this.whip = true;

        //Start listening on a different thread for
        //a shake
        shakeListenerThread = new Thread() {
            public void run() {
                while (Thread.currentThread().isInterrupted() == false) {
                    if (mAccel > 12)
                        displayNoise(false, false, true);
                }
            }
        };

        shakeListenerThread.start();
    }

    //This method will send you back to the soundboard screen when you click the button on the cowboy screen
    public void sendMessageCowboyBackButton(View view) {
        setContentView(R.layout.soundboard_home);
        this.cowboy = false;
        shakeListenerThread.interrupt();
    }

    //This method will send you to the whip screen
    public void sendMessageGotoCowboyScreen(View view) {
        setContentView(R.layout.cowboy_main);
        this.cowboy = true;

        //Start listening on a different thread for
        //a shake

        shakeListenerThread = new Thread() {
            public void run() {
                while (Thread.currentThread().isInterrupted() == false) {
                    if (mAccel > 12)
                        displayNoise(true, false, false);
                }
            }
        };

        shakeListenerThread.start();
    }

    //This method will send you back to the soundboard screen when you click the button on the vroom screen
    public void sendMessageVroomBackButton(View view) {
        setContentView(R.layout.soundboard_home);
        this.vroom = false;
        shakeListenerThread.interrupt();
    }

    //This method will send you to the vroom screen
    public void sendMessageGotoVroomScreen(View view) {
        setContentView(R.layout.vroom_main);
        this.vroom = true;
        //Start listening on a different thread for
        //a shake
        shakeListenerThread = new Thread() {
            public void run() {
                while (Thread.currentThread().isInterrupted() == false) {
                    if (mAccel > 12)
                        displayNoise(false, true, false);
                }
            }
        };

        shakeListenerThread.start();
    }

    @Override
    //Evaluates if the sensor has been shaken
    public void onSensorChanged(SensorEvent event) {
    }


    //Displays the noise
    public void displayNoise(boolean cowboy, boolean vroom, boolean whip){

        //Media player variables CONSTANTS
        Random r = new Random();
        MediaPlayer playerYeehaw = null;
        MediaPlayer playerWhip = null;
        MediaPlayer playerVroom = null;

        int random = r.nextInt(3);
        if (random == 0) {
            playerYeehaw = MediaPlayer.create(this, R.raw.yeehaw);
            playerWhip = MediaPlayer.create(this, R.raw.wachowgood);
            playerVroom = MediaPlayer.create(this, R.raw.driving);
        } else if (random == 1) {
            playerYeehaw = MediaPlayer.create(this, R.raw.yeehaw);
            playerWhip = MediaPlayer.create(this, R.raw.wachow);
            playerVroom = MediaPlayer.create(this, R.raw.ambulance);
        } else {
            playerYeehaw = MediaPlayer.create(this, R.raw.yeehaw);
            playerWhip = MediaPlayer.create(this, R.raw.wachow2);
            playerVroom = MediaPlayer.create(this, R.raw.vroom1);
        }


        if (cowboy){

            playerYeehaw.start();

            return;
        }

        if (vroom){

            playerVroom.start();
            return;
        }

        if (whip){

            playerWhip.start();
            return;
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
