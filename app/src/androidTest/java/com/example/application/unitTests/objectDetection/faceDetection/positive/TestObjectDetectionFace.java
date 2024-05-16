package com.example.application.unitTests.objectDetection.faceDetection.positive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.application.ObjectDetection;
import com.example.application.R;

import org.junit.Before;
import org.junit.Test;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

public class TestObjectDetectionFace {
    private static final String faceImg = "face.jpg";
    private static final Rect EXPECTED_FACE = new Rect(349, 370, 295, 295);
    @Before
    public void setup() {
        if (! OpenCVLoader.initLocal()) {
            fail("Не смогли инициализировать библиотеку OpenCV");
        }
    }

    /**
     * Тест проверяет метод определения лица на изображении
     * @throws IOException
     */
    @Test
    public void testObjectDetectionFace() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ObjectDetection testingDetector = new ObjectDetection(appContext);
        testingDetector.writeFileToPrivateStorage(R.raw.dontexistpersonface, faceImg);

        Rect face = testingDetector.faceDetection(Imgcodecs.imread(appContext.getFilesDir().getPath() + "/" + faceImg, IMREAD_GRAYSCALE));
        assertNotNull(face);
        assertAll(
                () -> assertEquals(EXPECTED_FACE.x, face.x),
                () -> assertEquals(EXPECTED_FACE.y, face.y),
                () -> assertEquals(EXPECTED_FACE.width, face.width),
                () -> assertEquals(EXPECTED_FACE.height, face.height)
        );
    }
}
