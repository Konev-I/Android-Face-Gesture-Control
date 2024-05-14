package com.example.application.pointer.setCenter.negative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.io.IOException;

public class TestPointerNegativeSizeSetCenter {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point POINT = new Point(100, 100);
    private static final Size NEGATIVE_WIDTH_SIZE = new Size(-50, 50);
    private static final Size NEGATIVE_HEIGHT_SIZE = new Size(50, -50);
    private static final Size NEGATIVE_SIZE = new Size(-50, -50);
    private static final Size EXPECTED_FIRST_SIZE = new Size(0, 50);
    private static final Size EXPECTED_SECOND_SIZE = new Size(50, 0);
    private static final Size EXPECTED_THIRD_SIZE = new Size(0, 0);
    private static final double DELTA = 0;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет передачу отрицательного значаения размера слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerNegativeSizeSetCenter() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(POINT, NEGATIVE_WIDTH_SIZE);
        Rect firstFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_FIRST_SIZE.width, firstFaceCenter.width, DELTA),
                () -> assertEquals(EXPECTED_FIRST_SIZE.height, firstFaceCenter.height, DELTA)
        );

        testingPointer.isCenterSet = false;
        testingPointer.setFaceCenter(POINT, NEGATIVE_HEIGHT_SIZE);
        Rect secondFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_SECOND_SIZE.width, secondFaceCenter.width, DELTA),
                () -> assertEquals(EXPECTED_SECOND_SIZE.height, secondFaceCenter.height, DELTA)
        );

        testingPointer.isCenterSet = false;
        testingPointer.setFaceCenter(POINT, NEGATIVE_SIZE);
        Rect thirdFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_THIRD_SIZE.width, thirdFaceCenter.width, DELTA),
                () -> assertEquals(EXPECTED_THIRD_SIZE.height, thirdFaceCenter.height, DELTA)
        );
    }
}
