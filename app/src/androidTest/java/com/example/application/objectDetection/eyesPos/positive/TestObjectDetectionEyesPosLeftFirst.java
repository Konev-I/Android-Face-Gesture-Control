package com.example.application.objectDetection.eyesPos.positive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Rect;

import java.io.IOException;
import java.util.List;

public class TestObjectDetectionEyesPosLeftFirst {
    private static final Rect RIGHT_EYE = new Rect(100, 100, 100, 100);
    private static final Rect LEFT_EYE = new Rect(200, 100, 100, 100);

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения позиции глаз, при передаче первым правого глаза
     * @throws IOException
     */
    @Test
    public void testObjectDetectionEyesPosLeftFirst() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);

        List<Rect> eyes = testingDetector.eyesPos(LEFT_EYE, RIGHT_EYE);
        assertAll(
                () -> assertEquals(LEFT_EYE.x, eyes.get(0).x),
                () -> assertEquals(RIGHT_EYE.x, eyes.get(1).x)
        );
    }
}
