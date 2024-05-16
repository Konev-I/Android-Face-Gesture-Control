package com.example.application.unitTests.pointer.setCenter.positive;

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

public class TestPointerResetCenter {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point FIRST_POINT = new Point(200, 200);
    private static final Point SECOND_POINT = new Point(300, 300);
    private static final Size FIRST_CENTER_SIZE = new Size(50, 50);
    private static final Size SECOND_CENTER_SIZE = new Size(100, 100);
    private static final Point EXPECTED_FIRST_POINT = new Point(FIRST_POINT.x - FIRST_CENTER_SIZE.width / 2, FIRST_POINT.y - FIRST_CENTER_SIZE.height / 2);
    private static final Point EXPECTED_SECOND_POINT = new Point(SECOND_POINT.x - SECOND_CENTER_SIZE.width / 2, SECOND_POINT.y - SECOND_CENTER_SIZE.height / 2);
    private static final double DELTA = 0;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет что после изменения флага отвечающего за блокировку изменения слепой зоны,
     * получится изменить расположение слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerResetCenter() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(FIRST_POINT, FIRST_CENTER_SIZE);
        Rect firstFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_FIRST_POINT.x, firstFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_FIRST_POINT.y, firstFaceCenter.y, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.width, firstFaceCenter.width, DELTA),
                () -> assertEquals(FIRST_CENTER_SIZE.height, firstFaceCenter.height, DELTA)
        );

        testingPointer.isCenterSet = false;
        testingPointer.setFaceCenter(SECOND_POINT, SECOND_CENTER_SIZE);
        Rect secondFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_SECOND_POINT.x, secondFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_SECOND_POINT.y, secondFaceCenter.y, DELTA),
                () -> assertEquals(SECOND_CENTER_SIZE.width, secondFaceCenter.width, DELTA),
                () -> assertEquals(SECOND_CENTER_SIZE.height, secondFaceCenter.height, DELTA)
        );
    }
}
