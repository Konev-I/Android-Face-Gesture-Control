package com.example.application.unitTests.pointer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;
import com.example.application.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

public class CameraMock implements CameraBridgeViewBase.CvCameraViewFrame {
    private static final String faceImg = "face.jpg";
    private static final String noFaceImg = "noface.jpg";
    public enum frameState {
        noFace,
        face
    }

    Mat frame;
    Mat grayFrame;
    public CameraMock(frameState state) throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = null;
        try {
            testingDetector = new ObjectDetection(appContext);
        }
        catch (IOException ignored) {}

        switch (state) {
            case face:
                testingDetector.writeFileToPrivateStorage(R.raw.dontexistpersonface, faceImg);
                frame = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + faceImg);
                grayFrame = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + faceImg, IMREAD_GRAYSCALE);
                break;
            case noFace:
                testingDetector.writeFileToPrivateStorage(R.raw.noface, noFaceImg);
                frame = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + noFaceImg);
                grayFrame = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + noFaceImg, IMREAD_GRAYSCALE);
                break;
        }

        Core.rotate(grayFrame, grayFrame, Core.ROTATE_90_CLOCKWISE);
        Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
    }


    @Override
    public Mat rgba() {
        return frame;
    }

    @Override
    public Mat gray() {
        return grayFrame;
    }
}
