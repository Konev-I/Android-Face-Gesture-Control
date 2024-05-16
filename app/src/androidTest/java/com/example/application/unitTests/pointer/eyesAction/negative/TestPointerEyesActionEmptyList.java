package com.example.application.unitTests.pointer.eyesAction.negative;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static java.util.Collections.emptyList;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestPointerEyesActionEmptyList {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет работу метода eyesAction при передаче пустого List
     * @throws IOException
     */
    @Test
    public void testPointerEyesActionEmptyList() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);
        assertDoesNotThrow(() -> testingPointer.eyesAction(emptyList()));
    }
}
