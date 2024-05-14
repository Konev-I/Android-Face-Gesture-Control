package com.example.application.objectDetection.faceDetection.negative;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;
import com.example.application.R;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

public class TestNegativeObjectDetectionFace {
    private static final String noFaceImg = "noface.jpg";

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения лица на изображении при его отсутствии
     * @throws IOException
     */
    @Test
    public void testNegativeObjectDetectionFace() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);
        testingDetector.writeFileToPrivateStorage(R.raw.noface, noFaceImg);
        assertNull(testingDetector.faceDetection(Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + noFaceImg, IMREAD_GRAYSCALE)));
    }
}
