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

/**
 * Проверяем работу метода Pointer.setFaceCenter(Point p, Size size)
 */
public class TestPointerSetCenter {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point POINT = new Point(200, 200);
    private static final Size CENTER_SIZE = new Size(50, 50);
    private static final Point EXPECTED_POINT = new Point(POINT.x - CENTER_SIZE.width / 2, POINT.y - CENTER_SIZE.height / 2);
    private static final double DELTA = 0;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }


    /**
     * Тест проверяет установку центра слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerSetCenter() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(POINT, CENTER_SIZE);
        Rect firstFaceCenter = testingPointer.rectCenter;
        assertAll(
                () -> assertEquals(EXPECTED_POINT.x, firstFaceCenter.x, DELTA),
                () -> assertEquals(EXPECTED_POINT.y, firstFaceCenter.y, DELTA),
                () -> assertEquals(CENTER_SIZE.width, firstFaceCenter.width, DELTA),
                () -> assertEquals(CENTER_SIZE.height, firstFaceCenter.height, DELTA)
        );
    }
}