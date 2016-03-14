package com.example.castoryan.eatwhat_beta3;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CrazyShake extends Fragment{

    private SoundPool soundPool;


    DataBaseHandler dbhand;
    private Button stopButton;


    private long initTime = 0;
    private long lastTime = 0;
    private long curTime = 0;
    private long goaltime = 0;
    private long duration = 0;

    private float last_x = 0.0f;
    private float last_y = 0.0f;
    private float last_z = 0.0f;


    private float result = 0.0f;
    private float shake = 0.0f;
    private float totalShake = 0.0f;

    private static final int SOUND_COUNT = 6;
    private int[] mSoundIds = new int[SOUND_COUNT + 1];

    boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crazy_shake, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        initShake();
        super.onResume();
    }
    TextView cur_shake;
    TextView total_shake;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        cur_shake = (TextView)getActivity().findViewById(R.id.text_cur);
        total_shake = (TextView)getActivity().findViewById(R.id.text_total);
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //  InputStream is = getActivity().getApplicationContext().getResources().openRawResource(R.raw.shakesound);
        soundPool= new SoundPool(41, audioManager.STREAM_MUSIC,0);
        mSoundIds[1] = soundPool.load(getActivity(),R.raw.b1,1);
        mSoundIds[2] = soundPool.load(getActivity(),R.raw.b2,1);
        mSoundIds[3] = soundPool.load(getActivity(),R.raw.b3,1);
        mSoundIds[4] = soundPool.load(getActivity(),R.raw.b4,1);
        soundPool.play(0, 1.0f, 1.0f, 0, 0, 1);


        stopButton = (Button) getActivity().findViewById(R.id.button_go);
        stopButton.setText("Start Crazy Shakeing");
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                initShake();
                goaltime = System.currentTimeMillis() + 10000;
                soundPool.play(mSoundIds[1],1f, 1, 0, 0, 1);
                stopButton.setText("Gaming Time");
            }
        });
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener acceleromererListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                if(flag ) {
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
                        //total_shake.setText(String.valueOf(shake));
                    }

                    long nowTime = System.currentTimeMillis();
                    if(8100>(goaltime - nowTime)&&(goaltime - nowTime)>7900){
                        soundPool.play(mSoundIds[4], 1f, 1, 0, 1, 0.7f);
                    }

                    if(5100>(goaltime - nowTime)&&(goaltime - nowTime)>5000){
                        //soundPool.play(mSoundIds[4], 1f, 1, 0, 1, 0.6f);
                    }
                    if(4100>(goaltime - nowTime)&&(goaltime - nowTime)>3900){
                        soundPool.play(mSoundIds[4], 1f, 1, 0, 1, 1f);
                    }
                    if(4100>(goaltime - nowTime)&&(goaltime - nowTime)>3900){
                        //soundPool.play(mSoundIds[4], 1f, 1, 0, 1, 1f);
                    }
                    if(2100>(goaltime - nowTime)&&(goaltime - nowTime)>1900){
                        soundPool.play(mSoundIds[4], 1f, 1, 0, 1, 1.2f);
                    }
                    if(1100>(goaltime - nowTime)&&(goaltime - nowTime)>900){
                        soundPool.play(mSoundIds[4], 1f, 1, 0, 2, 1.4f);
                    }

                    if (goaltime - nowTime > 0) {
                        result = totalShake;
                        cur_shake.setText(String.valueOf(result));
                        stopButton.setText(String.valueOf((goaltime - nowTime)/1000));
                    } else {
                        stopButton.setText("Game Finished");
                        cur_shake.setText(String.valueOf(result));
                        flag = false;
                        popResult();
                    }


                    last_x = x;
                    last_y = y;
                    last_z = z;
                    lastTime = curTime;
                }
            }
        };
        sm.registerListener(acceleromererListener, acceleromererSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void initShake() {
        lastTime = 0;
        duration = 0;
        curTime = 0;
        initTime = 0;
        last_x = 0.0f;
        last_y = 0.0f;
        last_z = 0.0f;
        shake = 0.0f;
        result = 0.0f;
        totalShake = 0.0f;
    }


    public void popResult(){
        soundPool.play(mSoundIds[3], 1f, 1, 0, 2, 1);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View myLoginView = layoutInflater.inflate(R.layout.pop_result, null);

        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle("Your Score is "+ result+" ! !").
                setIcon(R.drawable.ic_launcher).
                setView(myLoginView).
                setPositiveButton("Yeah!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopButton.setText("Start Crazy Shakeing");
                    }
                }).
                create();
        alertDialog.show();
    }

}  