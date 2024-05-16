package com.example.application.unitTests.pointer.movePointer.positive;

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
import org.opencv.core.Size;

import java.io.IOException;

public class TestMovePointerUp {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point POINT = new Point(200, 200);
    private static final Size CENTER_SIZE = new Size(50, 50);
    private static final Point POINT_UP = new Point(200, 100);

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет перемещение указателя вверх
     * @throws IOException
     */
    @Test
    public void testMovePointerUp() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(POINT, CENTER_SIZE);
        assertAll(
                () -> assertEquals(SCREEN_WIDTH / 2, testingPointer.getPosX()),
                () -> assertEquals(SCREEN_HEIGHT / 2, testingPointer.getPosY())
        );

        testingPointer.movePointer(POINT_UP);
        assertAll(
                () -> assertEquals(SCREEN_WIDTH / 2, testingPointer.getPosX()),
                () -> assertEquals(SCREEN_HEIGHT / 2 - testingPointer.getSpeed(), testingPointer.getPosY())
        );
    }
}
