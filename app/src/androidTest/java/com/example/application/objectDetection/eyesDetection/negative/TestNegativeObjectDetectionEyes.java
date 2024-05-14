package com.example.application.objectDetection.eyesDetection.negative;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;
import com.example.application.R;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.List;

public class TestNegativeObjectDetectionEyes {
    private static final String noFaceImg = "noface.jpg";

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения глаз на изображении при их отсутствии
     * @throws IOException
     */
    @Test
    public void testNegativeObjectDetectionEyes() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);
        testingDetector.writeFileToPrivateStorage(R.raw.dontexistpersonface, noFaceImg);

        Mat img = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + noFaceImg, IMREAD_GRAYSCALE);
        Rect fakeFace = new Rect(100, 100, 100, 100);

        List<Rect> eyes = testingDetector.eyesDetection(fakeFace, img);
        assertTrue(eyes.isEmpty());
    }
}