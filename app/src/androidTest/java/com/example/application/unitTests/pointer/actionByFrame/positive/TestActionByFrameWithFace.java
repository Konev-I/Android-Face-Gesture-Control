package com.example.application.unitTests.pointer.actionByFrame.positive;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.Pointer;
import com.example.application.unitTests.pointer.CameraMock;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class TestActionByFrameWithFace {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет работу метода actionByFrame при передаче кадра с лицом
     * @throws IOException
     */
    @Test
    public void testActionByFrameWithFace() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);
        CameraMock cameraMock = new CameraMock(CameraMock.frameState.face);
        assertDoesNotThrow(() -> testingPointer.actionByFrame(cameraMock));
    }
}