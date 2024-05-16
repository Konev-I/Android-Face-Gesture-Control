package com.example.application.integrationTests.actionByFrame;

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

public class TestIntegrationByFrameWithNoFace {
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;

    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет взаимодействие Pointer и ObjectDetection при обработке кадра без лица
     * @throws IOException
     */
    @Test
    public void testIntegrationByFrameWithNoFace() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Pointer testingPointer = new Pointer(appContext, SCREEN_WIDTH, SCREEN_HEIGHT);
        CameraMock cameraMock = new CameraMock(CameraMock.frameState.noFace);
        assertDoesNotThrow(() -> testingPointer.actionByFrame(cameraMock));
    }
}
