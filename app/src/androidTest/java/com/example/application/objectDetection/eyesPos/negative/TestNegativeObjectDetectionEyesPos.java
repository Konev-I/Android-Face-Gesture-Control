package com.example.application.objectDetection.eyesPos.negative;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Rect;

import java.io.IOException;

public class TestNegativeObjectDetectionEyesPos {
    private static final Rect RIGHT_EYE = new Rect(100, 100, 100, 100);
    private static final Rect LEFT_EYE = new Rect(200, 100, 100, 100);

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения позиции глаз, при передаче null объектов
     * @throws IOException
     */
    @Test
    public void testNegativeObjectDetectionEyesPos() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);

        assertTrue(testingDetector.eyesPos(null, RIGHT_EYE).isEmpty());
        assertTrue(testingDetector.eyesPos(LEFT_EYE, null).isEmpty());
        assertTrue(testingDetector.eyesPos(null, null).isEmpty());
    }
}
