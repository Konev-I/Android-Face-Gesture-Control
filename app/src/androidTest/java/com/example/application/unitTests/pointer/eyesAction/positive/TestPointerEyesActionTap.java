package com.example.application.unitTests.pointer.eyesAction.positive;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static java.util.Collections.emptyList;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;
import com.example.application.PointerService;
import com.example.application.TapService;
import com.example.application.unitTests.pointer.TapServiceMock;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Rect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestPointerEyesActionTap {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final Rect LEFT_EYE = new Rect(100, 1000, 10, 10);
    private static final Rect RIGHT_EYE = new Rect(100, 100, 10, 10);

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет работу метода eyesAction при передаче параметров для тапа
     * @throws IOException
     */
    @Test
    public void testPointerEyesActionTap() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);
        List<Rect> eyes = new ArrayList<>();
        eyes.add(LEFT_EYE);
        eyes.add(RIGHT_EYE);
        PointerService.tapService = new TapServiceMock();
        assertDoesNotThrow(() -> testingPointer.eyesAction(eyes));
    }
}
