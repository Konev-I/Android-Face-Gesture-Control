package com.example.application.unitTests.objectDetection.faceDetection.negative;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestNegativeNullObjectDetectionFace {
    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения лица на изображении при передаче null параметра
     * @throws IOException
     */
    @Test
    public void testNegativeNullObjectDetectionFace() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);
        assertNull(testingDetector.faceDetection(null));
    }
}
