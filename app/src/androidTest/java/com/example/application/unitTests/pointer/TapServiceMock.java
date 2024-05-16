package com.example.application.unitTests.pointer;

import com.example.application.TapService;

public class TapServiceMock extends TapService {
    @Override
    public void makeTap(int x, int y) {}

    @Override
    public void makeSwipe(int x1, int y1, int x2, int y2) {}
}
