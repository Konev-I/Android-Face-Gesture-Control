package com.example.application.pointer.setRadius.negative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestPointerSetNegativeRadius {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 200;
    private static final int DEFAULT_RADIUS = 50;
    private static final int CUSTOM_RADIUS = -100;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод установки отрицательного значения размера слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerSetNegativeRadius() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
        testingPointer.setRadius(CUSTOM_RADIUS);
        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
    }
}
