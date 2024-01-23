package com.kingmang.Kconc;

import java.io.IOException;
import java.util.Scanner;

import static com.kingmang.Kconc.Render.load;
import static com.kingmang.Kconc.Render.render;
import static com.kingmang.Kconc.Screen.initScreen;

public class Main {
    public static void main(String[] args) throws IOException {
        String file = args[0];
        Matrix.cols = Integer.parseInt(args[1]);
        Matrix.rows = Integer.parseInt(args[2]);
        Matrix.screenBuffer = new char[2][Matrix.rows][Matrix.cols + 1];

        initScreen(Matrix.screenBuffer[0]);
        initScreen(Matrix.screenBuffer[1]);
        render(load(file));
    }
}
