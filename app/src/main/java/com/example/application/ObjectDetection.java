package com.example.application;

import static android.content.ContentValues.TAG;
import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;
import static java.util.Collections.emptyList;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectDetection {
    private String fileNameFace = "haarcascade_frontalface_alt.xml";
    private String fileNameEyes = "haarcascade_eye_tree_eyeglasses.xml";
    private Context appContext;
    private CascadeClassifier detector;

    public static CascadeClassifier faceDetector;
    public static CascadeClassifier eyesDetector;

    public ObjectDetection(Context appContext) throws IOException {
        this.appContext = appContext;

        writeFileToPrivateStorage(R.raw.haarcascade_frontalface_alt, fileNameFace);
        writeFileToPrivateStorage(R.raw.haarcascade_eye_tree_eyeglasses, fileNameEyes);

        detector = new CascadeClassifier();
        if (!detector.load(appContext.getFilesDir().getPath() + "/" + fileNameFace)) {
            Log.e(TAG, "Face detector initialization failed!");
            throw new IOException("Face detector initialization failed!");
        }
        faceDetector = detector;

        detector = new CascadeClassifier();
        if (!detector.load(appContext.getFilesDir().getPath() + "/" + fileNameEyes)) {
            Log.e(TAG, "Eyes detector initialization failed!");
            throw new IOException("Eyes detector initialization failed!");
        }
        eyesDetector = detector;
    }

    public Rect faceDetection(Mat grayFrame) {
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale( grayFrame, faces, 1.1, 2, CASCADE_SCALE_IMAGE, new Size(30, 30));

        Rect[] facesArr = faces.toArray();
        if (facesArr.length != 0) {
            return facesArr[0];
        }
        else {
            return null;
        }
    }

    public List<Rect> eyesDetection(Rect face, Mat grayFrame) {
        List<Rect> eyesReturn;
        MatOfRect eyes = new MatOfRect();
        eyesDetector.detectMultiScale(grayFrame.submat(face), eyes);
        Rect[] eyesRect = eyes.toArray();

        if (eyesRect.length >= 2) {
            int minX = -1;
            int first = 0;
            int second = 0;

            for(int i = 0; i < eyesRect.length - 1; i++) {
                for(int j = i + 1; j < eyesRect.length; j++) {
                    if (minX == -1 || eyesRect[i].x - eyesRect[j].x < minX) {
                        minX = eyesRect[i].x - eyesRect[j].x;
                        first = i;
                        second = j;
                    }
                }
            }

            eyesReturn = eyesPos(eyesRect[first], eyesRect[second]);
            for (int i = 0; i < 2; i++)
            {
                eyesReturn.get(i).x += face.x;
                eyesReturn.get(i).y += face.y;
            }
        }
        else {
            eyesReturn = emptyList();
        }

        return eyesReturn;
    }

    public List<Rect> eyesPos(Rect eye0, Rect eye1) {
        List<Rect> eyes = new ArrayList<>(2);
        if (eye0.x > eye1.x) {
            eyes.add(eye0);
            eyes.add(eye1);
        }
        else {
            eyes.add(eye1);
            eyes.add(eye0);
        }
        return eyes;
    }

    public void writeFileToPrivateStorage(int fromFile, String toFile) throws IOException {
        InputStream is = appContext.getResources().openRawResource(fromFile);
        int bytes_read;
        byte[] buffer = new byte[4096];
        try
        {
            FileOutputStream fos = appContext.openFileOutput(toFile, Context.MODE_PRIVATE);

            while ((bytes_read = is.read(buffer)) != -1)
                fos.write(buffer, 0, bytes_read);
            fos.close();
            is.close();
        }
        catch (FileNotFoundException e)
        {
            throw new IOException("Cascade file not found!");
        }
        catch (IOException e)
        {
            throw new IOException("Failed to read cascade file!");
        }
    }
}
