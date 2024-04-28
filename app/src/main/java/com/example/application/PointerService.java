package com.example.application;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class PointerService extends Service implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static TapService tapService;
    private WindowManager windowManager;
    public Pointer pointer;
    private ConstraintLayout buttonsView;
    private JavaCameraView cameraView;

    public static int width;
    public static int height;
    private int statusBarOffSet;
    public CameraBridgeViewBase.CvCameraViewListener2 a;
    private CameraBridgeViewBase mOpenCvCameraView;
    WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        cameraView = new JavaCameraView(getApplicationContext(), 98);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        PointerService.width = displayMetrics.widthPixels;
        PointerService.height = displayMetrics.heightPixels;

        // Get the ID of status_bar_height resource
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            // Acquire the size value of the response according to the resource ID
            int dp = getResources().getDimensionPixelSize(resourceId);
            statusBarOffSet = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            Log.i(TAG, "OFFSET " + statusBarOffSet);
        }

        WindowManager.LayoutParams a = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSPARENT);
        a.height = 1;
        a.width = 1;

        windowManager.addView(cameraView, a);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        pointer = new Pointer(getApplicationContext(), width, height);
        windowManager.addView(pointer, params);

        buttonsView = new ConstraintLayout(getApplicationContext());
        WindowManager.LayoutParams paramsButtons = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        paramsButtons.gravity = Gravity.BOTTOM | Gravity.START;
        windowManager.addView(buttonsView, paramsButtons);

        Button buttonReset = new Button(getApplicationContext());
        buttonReset.setId(View.generateViewId());

        Button buttonTap = new Button(getApplicationContext());
        buttonTap.setId(View.generateViewId());

        Button buttonExit = new Button(getApplicationContext());
        buttonExit.setId(View.generateViewId());

        buttonReset.setOnClickListener(view -> pointer.isCenterSet = false);
        buttonReset.setText("Reset face");
        ConstraintLayout.LayoutParams params1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params1.bottomToBottom = ConstraintSet.PARENT_ID;
        params1.leftToLeft = ConstraintSet.PARENT_ID;
        buttonsView.addView(buttonReset, params1);

        buttonExit.setText("Exit");
        buttonExit.setOnClickListener(view ->
        {
            windowManager.removeViewImmediate(buttonsView);
            windowManager.removeViewImmediate(pointer);
            windowManager.removeViewImmediate(cameraView);
            this.onDestroy();
            this.stopSelf();
        });
        params1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params1.bottomToBottom = ConstraintSet.PARENT_ID;
        params1.leftToRight = buttonReset.getId();
        buttonsView.addView(buttonExit, params1);

        /*
        buttonTap.setText("Tap");
        buttonTap.setOnClickListener(view ->
        {
            int tapX = pointer.getPosX();
            int tapY = pointer.getPosY() + statusBarOffSet;
            tapService.makeTap(tapX, tapY);

            Log.i(TAG, "X tap " + tapX);
            Log.i(TAG, "Y tap " + tapY);
        });
        params1 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params1.bottomToBottom = ConstraintSet.PARENT_ID;
        params1.leftToRight = buttonReset.getId();
        buttonsView.addView(buttonTap, params1);
        */

        pointer.bringToFront();

        mOpenCvCameraView = cameraView;
        mOpenCvCameraView.setVisibility(View.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();
        onCameraPermissionGranted();
    }

    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Pointer service shutdown");
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        stopService(new Intent(getApplicationContext(), PointerService.class));

        windowManager.removeViewImmediate(buttonsView);
        windowManager.removeViewImmediate(pointer);
        windowManager.removeViewImmediate(cameraView);
        this.stopSelf();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        return pointer.faceDetection(inputFrame);

        pointer.faceDetection(inputFrame);
        return null;
    }
}
