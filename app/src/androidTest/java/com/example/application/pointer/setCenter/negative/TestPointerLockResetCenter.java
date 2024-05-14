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

public class TestPointerLockResetCenter {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point FIRST_POINT = new Point(200, 200);
    private static final Point SECOND_POINT = new Point(300, 300);
    private static final Size FIRST_CENTER_SIZE = new Size(50, 50);
    private static final Size SECOND_CENTER_SIZE = new Size(100, 100);
    private static final Point EXPECTED_FIRST_POINT = new Point(FIRST_POINT.x - FIRST_CENTER_SIZE.width / 2, FIRST_POINT.y - FIRST_CENTER_SIZE.height / 2);
    private static final double DELTA = 0;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет что без изменения флага отвечающего за блокировку изменения слепой зоны,
     * не получится изменить расположение слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerLockResetCenter() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(FIRST_POINT, FIRST_CENTER_SIZE);
        Rect faceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_FIRST_POINT.x, faceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_FIRST_POINT.y, faceCenter.y, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.width, faceCenter.width, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.height, faceCenter.height, DELTA)
        );

        testingPointer.setFaceCenter(SECOND_POINT, SECOND_CENTER_SIZE);
        assertAll(
                () -> assertEquals(EXPECTED_FIRST_POINT.x, faceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_FIRST_POINT.y, faceCenter.y, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.width, faceCenter.width, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.height, faceCenter.height, DELTA)
        );
    }
}
