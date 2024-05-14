package com.example.application.objectDetection.eyesDetection.positive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import java.util.ArrayList;
import java.util.List;

public class TestObjectDetectionEyes {
    private static final String faceImg = "face.jpg";
    private static final List<Rect> EXPECTED_EYES = new ArrayList<>(2);
    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
        EXPECTED_EYES.add(new Rect(519, 459, 58, 58));
        EXPECTED_EYES.add(new Rect(419, 460, 56, 56));
    }

    /**
     * Тест проверяет метод определения глаз на изображении
     * @throws IOException
     */
    @Test
    public void testObjectDetectionEyes() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);
        testingDetector.writeFileToPrivateStorage(R.raw.dontexistpersonface, faceImg);

        Mat img = Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + faceImg, IMREAD_GRAYSCALE);
        Rect face = testingDetector.faceDetection(img);
        assertNotNull(face);

        List<Rect> eyes = testingDetector.eyesDetection(face, img);
        assertEquals(2, eyes.size());
        for(int i = 0; i < 2; i++) {
            Rect eye = eyes.get(i);
            Rect expectedEye = EXPECTED_EYES.get(i);
            assertNotNull(eye);
            assertAll(
                    () -> assertEquals(expectedEye.x, eye.x),
                    () -> assertEquals(expectedEye.y, eye.y),
                    () -> assertEquals(expectedEye.width, eye.width),
                    () -> assertEquals(expectedEye.height, eye.height)
            );
        }
    }
}
