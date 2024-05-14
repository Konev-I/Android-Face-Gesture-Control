package com.example.application.pointer.movePointer.negative;

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

public class TestWrongMovePointerLeft {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Point POINT = new Point(200, 200);
    private static final Size CENTER_SIZE = new Size(50, 50);
    private static final Point POINT_MOVE_LEFT = new Point(500, 200);

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет что указатель не перемещается, если он находится на границе экрана слева
     * @throws IOException
     */
    @Test
    public void TestWrongMovePointerRight() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        testingPointer.setFaceCenter(POINT, CENTER_SIZE);
        testingPointer.setSpeed(500);
        testingPointer.movePointer(POINT_MOVE_LEFT);
        assertAll(
                () -> assertEquals(0, testingPointer.getPosX()),
                () -> assertEquals(SCREEN_HEIGHT / 2, testingPointer.getPosY())
        );

        testingPointer.movePointer(POINT_MOVE_LEFT);
        assertAll(
                () -> assertEquals(0, testingPointer.getPosX()),
                () -> assertEquals(SCREEN_HEIGHT / 2, testingPointer.getPosY())
        );
    }
}
