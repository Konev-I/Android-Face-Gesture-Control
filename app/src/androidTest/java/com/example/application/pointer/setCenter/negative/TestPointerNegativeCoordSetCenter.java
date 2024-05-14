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

public class TestPointerNegativeCoordSetCenter {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point NEGATIVE_X_POINT = new Point(-100, 100);
    private static final Point NEGATIVE_Y_POINT = new Point(100, -100);
    private static final Point NEGATIVE_POINT = new Point(-100, -100);
    private static final Size CENTER_SIZE = new Size(50, 50);
    private static final Point EXPECTED_FIRST_POINT = new Point(-CENTER_SIZE.width / 2, NEGATIVE_X_POINT.y - CENTER_SIZE.height / 2);
    private static final Point EXPECTED_SECOND_POINT = new Point(NEGATIVE_Y_POINT.x - CENTER_SIZE.width / 2, -CENTER_SIZE.height / 2);
    private static final Point EXPECTED_THIRD_POINT = new Point(-CENTER_SIZE.width / 2, -CENTER_SIZE.height / 2);

    private static final double DELTA = 0;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет передачу отрицательного значаения координат
     * @throws IOException
     */
    @Test
    public void testPointerNegativeCoordSetCenter() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(NEGATIVE_X_POINT, CENTER_SIZE);
        Rect firstFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_FIRST_POINT.x, firstFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_FIRST_POINT.y, firstFaceCenter.y, DELTA),
                () -> assertEquals(CENTER_SIZE.width, firstFaceCenter.width, DELTA),
                () -> assertEquals(CENTER_SIZE.height, firstFaceCenter.height, DELTA)
        );

        testingPointer.isCenterSet = false;
        testingPointer.setFaceCenter(NEGATIVE_Y_POINT, CENTER_SIZE);
        Rect secondFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_SECOND_POINT.x, secondFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_SECOND_POINT.y, secondFaceCenter.y, DELTA),
                () -> assertEquals(CENTER_SIZE.width, secondFaceCenter.width, DELTA),
                () -> assertEquals(CENTER_SIZE.height, secondFaceCenter.height, DELTA)
        );

        testingPointer.isCenterSet = false;
        testingPointer.setFaceCenter(NEGATIVE_POINT, CENTER_SIZE);
        Rect thirdFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_THIRD_POINT.x, thirdFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_THIRD_POINT.y, thirdFaceCenter.y, DELTA),
                () -> assertEquals(CENTER_SIZE.width, thirdFaceCenter.width, DELTA),
                () -> assertEquals(CENTER_SIZE.height, thirdFaceCenter.height, DELTA)
        );
    }
}
