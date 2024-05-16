package com.example.application.unitTests.pointer.setRadius.negative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestPointerSetBigRadius {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 200;
    private static final int DEFAULT_RADIUS = 50;
    private static final int CUSTOM_RADIUS_HEIGHT = SCREEN_HEIGHT * 2;
    private static final int CUSTOM_RADIUS_WIDTH = SCREEN_WIDTH * 2;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод установки значения размера слепой зоны больше чем экран
     * @throws IOException
     */
    @Test
    public void testPointerSetBigRadius() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
        testingPointer.setRadius(CUSTOM_RADIUS_HEIGHT);
        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
        testingPointer.setRadius(CUSTOM_RADIUS_WIDTH);
        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
    }
}
