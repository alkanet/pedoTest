package com.ebs.test;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PedoAct extends Activity {


    private static final String TAG = "PedoAct";
    private TextView mStepValueView;

    Button bindBtn;
    Button startBtn;
    private SharedPreferences mSettings;
    private PedoSettings mPedoSettings;

    private boolean mIsRunning;
    private boolean mQuitting = false; // Set when user selected Quit from menu, can be used by onPause, onStop, onDestroy
    private float mDesiredPaceOrSpeed;
    private int mMaintain;

    private int mStepValue;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "[ACTIVITY] onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStepValue = 0;


/*
        bindBtn = (Button)findViewById(R.id.bindBtn);
        startBtn = (Button)findViewById(R.id.startBtn);
        bindBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bindService(new Intent(PedoAct.this,PedoService.class), mConnection, BIND_AUTO_CREATE);
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(PedoAct.this,PedoService.class));
            }
        });*/


    }

    protected void onStart() {
        Log.i(TAG, "[ACTIVITY] onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedoSettings = new PedoSettings(mSettings);
        mIsRunning = mPedoSettings.isServiceRunning();
        if (!mIsRunning && mPedoSettings.isNewStart()) {
            startStepService();
            bindStepService();
        }
        else if (mIsRunning) {
            bindStepService();
        }


    }

    protected void onRestart() {
        Log.i(TAG, "[ACTIVITY] onRestart");
        super.onRestart();
    }
    @Override
    protected void onPause() {
        Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedoSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        }
        else {
            mPedoSettings.saveServiceRunningWithTimestamp(mIsRunning);
        }

        super.onPause();
        savePaceSetting();
    }
    private PedoService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            Log.i(TAG, "[Service] onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className){
            Log.i(TAG, "[Service] onServiceConnected");
        }
    };



    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy unbindService");
        unbindService(mConnection);
        super.onDestroy();
    };
    private void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(PedoAct.this,
                    PedoService.class));
        }
    }
    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(this,
                PedoService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }
    private void savePaceSetting() {
        mPedoSettings.savePaceOrSpeedSetting(mMaintain, mDesiredPaceOrSpeed);
    }
}
