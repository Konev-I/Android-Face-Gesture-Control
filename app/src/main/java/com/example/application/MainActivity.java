package com.example.application;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.constraintlayout.widget.ConstraintSet;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends CameraActivity implements CvCameraViewListener2 {

    private static final int OVERLAY_PERMISSION_CODE = 5463;
    private static boolean allPermissions;
    private CameraBridgeViewBase mOpenCvCameraView;
    private Pointer pointerView;

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        PointerService.width = displayMetrics.widthPixels;
        PointerService.height = displayMetrics.heightPixels;

        CameraBridgeViewBase.maxWidth = displayMetrics.widthPixels;
        CameraBridgeViewBase.maxHeight = displayMetrics.heightPixels;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarOffSet = 0;
        if (resourceId > 0) {
            int dp = getResources().getDimensionPixelSize(resourceId);
            statusBarOffSet = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
        Pointer.statusBarOffSet = statusBarOffSet;

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            startActivityForResult(intent, OVERLAY_PERMISSION_CODE);

            (Toast.makeText(this, "После выдачи всех прав, перезапустите приложение", Toast.LENGTH_LONG)).show();
            onDestroy();
        }

        if (PointerService.tapService == null) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            (Toast.makeText(this, "После выдачи всех прав, перезапустите приложение", Toast.LENGTH_LONG)).show();
            return;
        }

        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = findViewById(R.id.app_camera_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        try {
            pointerView = new Pointer(getApplicationContext(), PointerService.width, PointerService.height);
        } catch (IOException e) {
            (Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)).show();
            return;
        }

        this.runOnUiThread(() -> {
            Button buttonResetFace = new Button(getApplicationContext());
            buttonResetFace.setId(View.generateViewId());

            Button buttonSpeedUp = new Button(getApplicationContext());
            buttonSpeedUp.setId(View.generateViewId());

            Button buttonSpeedDown = new Button(getApplicationContext());
            buttonSpeedDown.setId(View.generateViewId());

            Button buttonRadiusUp = new Button(getApplicationContext());
            buttonRadiusUp.setId(View.generateViewId());

            Button buttonRadiusDown = new Button(getApplicationContext());
            buttonRadiusDown.setId(View.generateViewId());

            //Кнопка ресета
            buttonResetFace.setText("Reset face");
            ConstraintLayout layout = findViewById(R.id.buttons_layout);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomToBottom = ConstraintSet.PARENT_ID;
            params.leftToLeft = ConstraintSet.PARENT_ID;
            buttonResetFace.setOnClickListener(view -> pointerView.isCenterSet = false);
            layout.addView(buttonResetFace, params);

            //Кнопка увеличения скорости
            buttonSpeedUp.setText("S+");
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomToBottom = ConstraintSet.PARENT_ID;
            params.leftToRight = buttonResetFace.getId();
            params.rightToLeft = buttonSpeedDown.getId();
            buttonSpeedUp.setOnClickListener(view -> {
                pointerView.setSpeed(pointerView.getSpeed() + 1);
            });
            layout.addView(buttonSpeedUp, params);

            //Кнопка уменьшения скорости
            buttonSpeedDown.setText("S-");
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomToBottom = ConstraintSet.PARENT_ID;
            params.leftToRight = buttonSpeedUp.getId();
            buttonSpeedDown.setOnClickListener(view -> {
                pointerView.setSpeed(pointerView.getSpeed() - 1);
            });
            layout.addView(buttonSpeedDown, params);

            //Кнопка увеличения радиуса
            buttonRadiusUp.setText("R+");
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomToTop = buttonSpeedUp.getId();
            buttonRadiusUp.setOnClickListener(view -> {
                pointerView.setRadius(pointerView.getRadius() + 1);
            });
            layout.addView(buttonRadiusUp, params);

            //Кнопка уменьшения радиуса
            buttonRadiusDown.setText("R-");
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftToRight = buttonRadiusUp.getId();
            params.bottomToTop = buttonSpeedDown.getId();
            buttonRadiusDown.setOnClickListener(view -> {
                pointerView.setRadius(pointerView.getRadius() - 1);
            });
            layout.addView(buttonRadiusDown, params);

            //Указатель
            FrameLayout frameLayout = findViewById(R.id.frame_layout);
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            frameLayout.addView(pointerView, frameParams);
        });
        allPermissions = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

        if (allPermissions) {
            startService(new Intent(getApplicationContext(), PointerService.class));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.enableView();

        stopService(new Intent(getApplicationContext(), PointerService.class));
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        stopService(new Intent(getApplicationContext(), PointerService.class));
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return pointerView.actionByFrame(inputFrame);
    }
}