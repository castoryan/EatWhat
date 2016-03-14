package com.example.castoryan.eatwhat_beta3;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeOne extends Fragment{

    private SoundPool soundPool;


    DataBaseHandler dbhand;
    private Button stopButton;

    private long initTime = 0;
    private long lastTime = 0;
    private long curTime = 0;
    private long duration = 0;

    private float last_x = 0.0f;
    private float last_y = 0.0f;
    private float last_z = 0.0f;

    private float shake = 0.0f;
    private float totalShake = 0.0f;

    private static final int SOUND_COUNT = 6;
    private int[] mSoundIds = new int[SOUND_COUNT + 1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shake_one, container, false);
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initShake();
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //  InputStream is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.shakesound);
        soundPool= new SoundPool(41, audioManager.STREAM_MUSIC,0);
        mSoundIds[1] = soundPool.load(getActivity(),R.raw.b1,1);
        mSoundIds[2] = soundPool.load(getActivity(),R.raw.b2,1);
        mSoundIds[3] = soundPool.load(getActivity(),R.raw.b3,1);
        mSoundIds[4] = soundPool.load(getActivity(),R.raw.b4,1);
        soundPool.play(0, 1.0f, 1.0f, 0, 0, 1);

        //Events of the only Button
        stopButton = (Button) getActivity().findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShake();
                soundPool.play(mSoundIds[1],1f, 1, 0, 0, 1);
                stopButton.setText("now, just shake me");
            }
        });



        //initialize the sensor and set the listener
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener acceleromererListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                curTime = System.currentTimeMillis();
                if ((curTime - lastTime) > 100) {
                    duration = (curTime - lastTime);
                    if (last_x == 0.0f && last_y == 0.0f && last_z == 0.0f) {
                        initTime = System.currentTimeMillis();
                    } else {
                        shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math.abs(z - last_z)) / duration * 100;
                    }
                    totalShake += shake;
                    if (totalShake > 15 && totalShake / (curTime - initTime) * 1000 > 15) {
                        initShake();
                        dbhand = new DataBaseHandler(getActivity());
                        int k = dbhand.getResturantCount();
                        int a = 1 + (int)(Math.random()*k);
                        Toast.makeText(getActivity(),"The resturant id is "+a,Toast.LENGTH_SHORT).show();
                        soundPool.play(mSoundIds[2],1f, 1, 0, 0, 1);
                        stopButton.setText("press me to have a fun");
                        RandomOne(a);
                    }
                    //tx1.setText("The total shake is "+totalShake+ " the average shake is"+totalShake / (curTime - initTime) * 1000 );
                }
                last_x = x;
                last_y = y;
                last_z = z;
                lastTime = curTime;
            }
        };
        sm.registerListener(acceleromererListener, acceleromererSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onResume(){
        initShake();
        super.onResume();
    }


    //Method of initializing all variables in field
    public void initShake() {
        lastTime = 0;
        duration = 0;
        curTime = 0;
        initTime = 0;
        last_x = 0.0f;
        last_y = 0.0f;
        last_z = 0.0f;
        shake = 0.0f;
        totalShake = 0.0f;
    }


    //Choose a resturant form our database by a randomly Id,and switchs to the fragment
    public void RandomOne(int id){
        List<String> list= new ArrayList<String>();
        list = dbhand.getAllResturantsString();
        list.get(id);

        Resturant res_pass = dbhand.getResturantByName(list.get(id));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment newFra = new ItemDetail();

        //meassage transport
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", res_pass);
        newFra.setArguments(bundle);

        ft.replace(R.id.container, newFra);
        ft.addToBackStack(null);
        ft.commit();
    }
}