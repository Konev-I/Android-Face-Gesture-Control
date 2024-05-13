package com.example.application;

import static org.opencv.imgproc.Imgproc.ellipse;
import static org.opencv.imgproc.Imgproc.rectangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Pointer extends View {
    private int speed = 15;
    private int radius = 10;
    private static int radiusFaceRect = 50;
    private int posX;
    private int posY;
    private int swipePosX = -100;
    private int swipePosY = -100;
    public static int statusBarOffSet;

    public static boolean wasTap;
    public static boolean wasSwipe;

    public boolean isCenterSet;
    public Rect rectCenter;

    private final Paint RED = new Paint();
    private final Paint GREEN = new Paint();
    private final Paint WHITE = new Paint();
    private ObjectDetection detector;

    public Pointer(Context context, int width, int height) throws IOException {
        super(context);
        RED.setColor(Color.RED);
        GREEN.setColor(Color.GREEN);
        WHITE.setColor(Color.WHITE);
        posX = width / 2;
        posY = height / 2;
        detector = new ObjectDetection(context);
    }

    public void setFaceCenter(Point p, Size size) {
        if (!isCenterSet) {
            Point p1 = new Point(p.x - size.width / 2, p.y - size.height / 2);
            Point p2 = new Point(p.x + size.width / 2, p.y + size.height / 2);
            rectCenter = new Rect(p1, p2);
            isCenterSet = true;
        }
    }

    public Mat actionByFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = new Mat();
        Mat grayFrame = inputFrame.gray();
        Core.rotate(grayFrame, grayFrame, Core.ROTATE_90_COUNTERCLOCKWISE);
        Core.rotate(inputFrame.rgba(), frame, Core.ROTATE_90_COUNTERCLOCKWISE);

        Rect face = detector.faceDetection(grayFrame);

        if (face != null) {
            List<Rect> eyes = detector.eyesDetection(face, grayFrame);

            Point center = new Point(face.x + (double) face.width / 2, face.y + (double) face.height / 2);
            ellipse(frame, center, new Size((double) face.width / 2, (double) face.height / 2), 0, 0, 360, new Scalar(255, 0, 255), 4, 8, 0);
            ellipse(frame, center, new Size(1, 1), 0, 0, 360, new Scalar(255, 255, 255), 3, 8, 0);
            this.setFaceCenter(center, new Size(radiusFaceRect, radiusFaceRect));
            rectangle(frame, this.rectCenter, new Scalar(255, 255, 255));
            this.movePointer(center);

            if (!eyes.isEmpty()) {
                eyesAction(eyes);
                for(Rect eye: eyes) {
                    rectangle(frame, eye, new Scalar(255, 0, 0));
                }
            }
        }

        Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
        Core.flip(frame, frame, 0);
        return frame;
    }

    public void movePointer(Point point) {
        if (!rectCenter.contains(point)) {
            // При повороте головы влево, по координатам OpenCV X увеличивается
            if (point.x > rectCenter.x + rectCenter.width) {
                if (!(posX <= 0)) {
                    posX -= speed;
                }
            }

            if (point.x < rectCenter.x) {
                if (!(posX >= getWidth())) {
                    posX += speed;
                }
            }

            if (point.y > rectCenter.y + rectCenter.height) {
                if (!(posY >= getHeight())) {
                    posY += speed;
                }
            }

            if (point.y < rectCenter.y) {
                if (! (posY <= -statusBarOffSet)) {
                    posY -= speed;
                }
            }
        }

        this.post(this::invalidate);
    }

    public void eyesAction(List<Rect> eyes) {
        Rect eyeL = eyes.get(0);
        Rect eyeR = eyes.get(1);

        Point centerL = new Point(eyeL.x + (double) eyeL.width / 2, eyeL.y + (double) eyeL.height / 2);
        Point centerR = new Point(eyeR.x + (double) eyeR.width / 2, eyeR.y + (double) eyeR.height / 2);

        if (!wasTap && (centerL.y - centerR.y > (double) (eyeL.height / 3))) {
            tap();
        }
        else if (!wasSwipe && (centerR.y - centerL.y > (double) (eyeR.height / 3))){
            if (swipePosX == -100) {
                swipePosX = getPosX();
                swipePosY = getPosY();
            }
            else {
                swipe();
            }
        }
    }

    public void tap() {
        if (PointerService.tapService != null) {
            wasTap = true;
            PointerService.tapService.makeTap(getPosX(), getPosY());
            new Timer().schedule(new TimerTask() {
                public void run() {
                    wasTap = false;
                }
            }, 2500);
        }
        else {
            (Toast.makeText(getContext(), "Нет доступа к специальным возможностям", Toast.LENGTH_LONG)).show();
        }
    }

    public void swipe() {
        if (PointerService.tapService != null) {
            wasSwipe = true;
            PointerService.tapService.makeSwipe(swipePosX, swipePosY, getPosX(), getPosY());
            swipePosX = -100;
            swipePosY = -100;
            new Timer().schedule(new TimerTask() {
                public void run() {
                    wasSwipe = false;
                }
            }, 2500);
        }
        else {
            (Toast.makeText(getContext(), "Нет доступа к специальным возможностям", Toast.LENGTH_LONG)).show();
        }

    }

    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawCircle(posX, posY, radius, RED);

        if (swipePosX != -100) {
            int drawPosY = Math.max(swipePosY - statusBarOffSet, 0);
            canvas.drawCircle(swipePosX, drawPosY, radius, GREEN);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (!(speed == 0)) {
            this.speed = speed;
        }
    }

    public int getRadius() {
        return radiusFaceRect;
    }

    public void setRadius(int radius) {
        if (!(radius <= 0 ) && !((radius >= getWidth()) || (radius >= getHeight())) ) {
            this.radiusFaceRect = radius;
            isCenterSet = false;
        }
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY + statusBarOffSet;
    }
}
