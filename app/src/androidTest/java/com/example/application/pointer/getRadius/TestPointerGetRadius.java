package com.example.application.pointer.getRadius;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestPointerGetRadius {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 200;
    private static final int DEFAULT_RADIUS = 50;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод получения радиуса слепой зоны
     * @throws IOException
     */
    @Test
    public void testPointerGetRadius() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);

        assertEquals(DEFAULT_RADIUS, testingPointer.getRadius());
    }
}