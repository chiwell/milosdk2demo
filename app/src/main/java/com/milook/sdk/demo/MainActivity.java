package com.milook.sdk.demo;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.milook.sdk.MiloSDKListener;
import com.milook.sdk.lib.Test;
import com.milook.sdk.MiloSDK;

public class MainActivity extends AppCompatActivity implements MiloSDKListener {

    RelativeLayout flMain;
    ImageView ivMain;

    Button btCamera;
    ToggleButton btPoints;
    ToggleButton btFilter;
    ToggleButton btSlim;
    ToggleButton btBeautify;
    ToggleButton btMask;

    boolean decoPoints;
    String decoFilter;
    boolean decoSlim;
    boolean decoBeautify;
    String decoMask;

    int currentCameraID;
    int cameraNum;

    public void setDeco() {
        String st;
        st="{ ";
        if(decoPoints) st+="'points':true,";
        if(decoSlim) st+="'faceDeform':{'type':'slim'},";
        if(decoBeautify) st+="'beautify':true,";
        if(decoFilter.equals("grey")) st+="'postFilter':{'type':'grey'},";
        if(!decoMask.equals("")) st+="'mask':{'type':'"+decoMask+"'},";
        st=st.substring(0, st.length()-1)+"}";

        // MiloSDK.SetDecoration("{'points':true}");
        Log.d("JSON", st);
        MiloSDK.SetDecoration(st);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flMain=(RelativeLayout)findViewById(R.id.fl_main);
        ivMain=(ImageView) findViewById(R.id.iv_main);
        btCamera=(Button)  findViewById(R.id.bt_camera);
        btPoints=(ToggleButton)  findViewById(R.id.bt_points);
        btFilter=(ToggleButton)  findViewById(R.id.bt_filter);
        btSlim=(ToggleButton)  findViewById(R.id.bt_slim);
        btBeautify=(ToggleButton)  findViewById(R.id.bt_beautify);
        btMask=(ToggleButton)  findViewById(R.id.bt_mask);

        cameraNum= Camera.getNumberOfCameras();
        currentCameraID=0;
        decoPoints=false;
        decoSlim=false;
        decoBeautify=false;
        decoFilter="";
        decoMask="";

        MiloSDK.Init(this, "", "", this);
        MiloSDK.StartCamera(flMain, "{'previewSize':'640x480', 'cameraID':'"+currentCameraID+"'}");
        MiloSDK.StartPreview(flMain, ivMain);

        setDeco();

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCameraID=(currentCameraID+1)%cameraNum;
                MiloSDK.StopCamera();
                MiloSDK.StartCamera(flMain, "{'previewSize':'640x480', 'cameraID':'"+currentCameraID+"'}");
            }
        });

        btPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decoPoints=!decoPoints;
                setDeco();
            }
        });

        btSlim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decoSlim=!decoSlim;
                setDeco();
            }
        });

        btBeautify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decoBeautify=!decoBeautify;
                setDeco();
            }
        });

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(decoFilter.equals("")) decoFilter="grey";
                else decoFilter="";
                setDeco();
            }
        });

        btMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(decoMask.equals("")) decoMask="firemask2";
                else decoMask="";
                setDeco();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MiloSDK.StopCamera();
        MiloSDK.StopPreview();
        MiloSDK.Finish();
    }

    @Override
    public void onCameraPreviewFrame(byte[] data) {
        //Log.d("FBO", "Camera Callback!");
    }

    public void onFrameResult(int result, int trackPointsCount, int[] trackPoints){
        //Log.d("FBO", "FBO "+result+" "+trackPointsCount+" <"+trackPoints[0]+","+trackPoints[1]+">");
    }

    public void onProcessFrame(int width, int height, byte[] data){
        Log.d("FBO", "Process Frame!");
    }
}
